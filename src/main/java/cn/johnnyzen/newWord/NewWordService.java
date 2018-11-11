package cn.johnnyzen.newWord;

import cn.johnnyzen.authority.AuthorityService;
import cn.johnnyzen.user.User;
import cn.johnnyzen.user.UserRepository;
import cn.johnnyzen.user.UserService;
import cn.johnnyzen.util.collection.CollectionUtil;
import cn.johnnyzen.util.datetime.DatetimeUtil;
import cn.johnnyzen.util.request.RequestUtil;
import cn.johnnyzen.util.reuslt.ResultUtil;
import cn.johnnyzen.word.ThirdWordResult;
import cn.johnnyzen.word.Word;
import cn.johnnyzen.word.WordRepository;
import cn.johnnyzen.word.WordService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.logging.Logger;

import static java.lang.System.currentTimeMillis;

/**
 * @IDE: Created by IntelliJ IDEA.
 * @Author: 千千寰宇
 * @Date: 2018/11/1  18:08:44
 * @Description: ...
 */
@Service("newWordService")
public class NewWordService {
    private static final Logger logger = Logger.getLogger(NewWordService.class.getName());

    //日志前缀字符串,方便通过日志定位程序
    private static String logPrefix = null;

    @Autowired
    private NewWordRepository newWordRepository;

    @Autowired
    private WordRepository wordRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RequestUtil requestUtil;

    /**
     * 利用entityManager来定义原生sql
     * 注入的是实体管理器,执行持久化操作(仅仅用于实现动态SQL需求的viewForgetWords方法)
     * @reference
     *      [1] springboot根据表名和字段查询和更新实现动态sql(jpa,mybatis)
     *              https://blog.csdn.net/hzs33/article/details/81017827
     */
    @PersistenceContext
    EntityManager entityManager;

    /**
     * 查词[searchWords]
     *  优先从数据库中查找，且数据库中的释义优先。
     * @param request
     * @param search 搜索的英文单词
     */
    public Page<Word> searchWords(HttpServletRequest request,String search){
        logPrefix = "[NewWordService.searchWords] ";
        Collection<Word> thirdWords = null; //第三方单词
        Collection<Word> wordsOfNotUserFromDB = null; //来自于数据库的非用户的生词
        List<Word> userNewWords = null; //用户生词
        Page<Word> wordsPage = null; //分页器
        Pageable pageable = new PageRequest(0, 50); //分页器
        Map<String, Word> wordsMap = null; //已英文单词未主键，使用map对两集合的英文单词去重

        User user = null;
        user = userService.findOneByLoginUsersMap(request);
        try {
            thirdWords = this.translate(search.trim());//第三方单词
            //查询数据库中非用户的单词（即 不存在生词的单词记录）
            wordsOfNotUserFromDB = wordRepository.findAllNewWordsOfNotUserByUserIdAndFuzzyEnglishWord(user.getId(), search.trim());

            userNewWords = (List<Word>) this.findAllWordsOfUserByFuzzySearch(request, search);
            if(thirdWords != null){ //不判断时，会导致运行时错误
                userNewWords.addAll(thirdWords); //全部集中到userNewWords集合中处理
            }
            if(wordsOfNotUserFromDB !=null){//防止异常
                userNewWords.addAll(wordsOfNotUserFromDB);
            }
            if(userNewWords == null){ //如果整个结果集都为空
                logger.info(logPrefix + "result of search word(" + search + ") :success but null.");
                wordsPage = new PageImpl<Word>(null, pageable,0);//"查词成功，但无结果。"
                return wordsPage;
            }
            wordsMap = new HashMap<String, Word>();//此时words必不为空
            Iterator<Word> iter = userNewWords.iterator();
            while(iter.hasNext()){
                Word word = iter.next();
                /*
                    如果map中已含有该单词:
                        查看map中的该英文单词或者当前的单词是否id不为0;
                            单词的id为0，即：该单词属于第三方翻译的单词或者数据库中存在但不是该用户的单词。
                        如果其中任意有一个不为0，则该单词的id变更为该非0id
                        即：相当于更新当前英文单词的id
                 */
                if(wordsMap.containsKey(word.getEnglishWord())){//通过englishWord去重
                    Word mapWord = wordsMap.get(word.getEnglishWord());
                    Integer id = mapWord.getId();
                    if(id == 0){//已存储的单词是第三方翻译，用当前单词覆盖掉,以优先当前单词<可能是来源于数据库，也可能来源于第三方>的翻译
                        mapWord.setId(word.getId());
                        wordsMap.put(mapWord.getEnglishWord(), word);
                    } else {//第三方非翻译，不做任何处理
                    }
                } else { //map中不含当前单词
                    wordsMap.put(word.getEnglishWord(), word);
                }
            }
        } catch (IOException e) {//第三方API翻译异常
            logger.warning(logPrefix + " thrid-api failed to tranlate : "+ search);
            e.printStackTrace();
        }
        //[notice] must use jackson version:com.fasterxml.jackson,else it will pose error.
        logger.info(logPrefix + "result of search word(" + search + ") :success.");
        wordsPage = new PageImpl<Word>(new ArrayList<Word>(wordsMap.values()), pageable, userNewWords.size());
        return wordsPage;
    }


    /**
     * 通过用户ID，获取用户所有生词
     * @param userId
     */
    public Collection<NewWord> findAllByUserId(Integer userId){
        return newWordRepository.findAllByUserId(userId);
    }

    /**
     * 查找用户的所有生词。
     *  1.通过token，查找用户信息
     *  2.通过用户id查询用户的所有生词。
     *
     * @return Collection<Word>
     * @param request
     */
    public Collection<Word> findAllWordsOfUserByRequest(HttpServletRequest request){
        logPrefix = "[NewWordService.findAllWordsOfUserByRequest] ";
        User user = null;
        user = userService.findOneByLoginUsersMap(request);
        //user登陆校验已通过过滤器实现，无需再校验。

        Collection<NewWord> newWords = null;
        newWords = newWordRepository.findAllByUserId(user.getId());
        if(newWords == null){
            logger.info(logPrefix + "该用户(" + user.getEmail() + ")暂无任何生词！(数据库无记录)");
            return null;
        }

        Collection<Word> words = new ArrayList<Word>();
        for(NewWord item:newWords){
            words.add(item.getWord());
        }

        logger.info(logPrefix + "该用户(" + user.getEmail() + ")有生词！(数据库有记录)");
        return words;
    }

    /**
     * 模糊查找用户的所有生词。[主要用于查词时，查某些类似单词]
     *  1.通过token，查找用户信息
     *  2.通过用户id和检索的单词模糊查询用户的所有生词。
     *
     * @param englishWord
     * @param request [need:token]
     * @return Collection<Word>
     */
    public Collection<Word> findAllWordsOfUserByFuzzySearch(HttpServletRequest request,String englishWord){
        logPrefix = "[NewWordService.findAllWordsOfUserByFuzzySearch] ";
        User user = null;
        user = userService.findOneByLoginUsersMap(request);
        //user登陆校验已通过过滤器实现，无需再校验。

        Collection<NewWord> newWords = null;
        newWords = newWordRepository.findNewWordsLikeByUserIdAndEnglishWord(user.getId(), englishWord);
        if(newWords == null){
            logger.info(logPrefix + "该用户(" + user.getEmail() + ")暂无任何生词！(数据库无记录)");
            return null;
        }

        Collection<Word> words = new ArrayList<Word>();
        for(NewWord item:newWords){
            words.add(item.getWord());
        }

        logger.info(logPrefix + "该用户(" + user.getEmail() + ")有生词！(数据库有记录)");
        return words;
    }

    /**
     * 精确查找用户的所有生词。[主要用于查某一词时]
     *  1.通过token，查找用户信息
     *  2.通过用户id和检索的单词精确查询用户的某个生词。
     *
     * @param englishWord
     * @param request [need:token]
     * @return Collection<Word>
     */
    public NewWord findOneOfUserByExactSearch(HttpServletRequest request,String englishWord){
        logPrefix = "[NewWordService.findAllWordsOfUserByFuzzySearch] ";
        User user = null;
        user = userService.findOneByLoginUsersMap(request);
        //user登陆校验已通过过滤器实现，无需再校验。

        NewWord newWords = null;
        newWords = newWordRepository.findDistinctFirstByUserIdAndEnglishWord(user.getId(), englishWord);
        return newWords; // null or entity object
    }

    /**
     * @method 增添生词记录
     * @author johnny
     *  1.通过englishWord，查询数据库word表中是否存在该单词记录
     *      如果无，新增该单词记录到word表中
     *      2.通过englishWord，调用第三方翻译接口，获取翻译结果，封装为word结果集
     *          如果第三方翻译或者接口调用失败，返回1
     *  3.查询用户是否已有该生词记录
     *          如果已有该记录：
     *              插入生词记录失败，但添加生词请求属于成功，返回2
     *          如果无：
     *              通过request[token]获取用户信息，向NewWord(表：r_user_focus_word)中插入生词关系记录。
     *              返回3
     *  @param englishWord
     *  @param request [use: token]
     */
    public int saveNewWord(HttpServletRequest request,String englishWord){
        logPrefix = "[NewWordService.saveNewWord] ";
        Collection<Word> dbWords = null;
        dbWords = wordRepository.findDistinctFirstByEnglishWord(englishWord);
        if(dbWords.size() < 1){//数据库无记录
            List<Word> transWords = null;
            try {
                transWords = this.translate(englishWord);//翻译该英文词汇
            } catch (IOException e) {
                logger.warning(logPrefix + "translate word(" + englishWord + ") failed.<IOException>");
                e.printStackTrace();
            }
            if(transWords == null){//translate is null or error
                logger.warning(logPrefix + "translate word(" + englishWord + ") is null or error");
                return 1;
            }
            wordRepository.save(transWords.get(0)); //插入word数据到word表中
        }

        User user = null;
        user = userService.findOneByLoginUsersMap(request);
        Word insertingWord = null;
        insertingWord = wordRepository.findDistinctFirstByEnglishWord(englishWord).iterator().next(); //通过上面步骤，数据库word表中已必然存在该单词；从数据库word表中查询该单词，以便获取单词的id,保证生词关系插入成功
        NewWord dbWordOfUser = null;
        dbWordOfUser = newWordRepository.findDistinctFirstByUserIdAndEnglishWord(user.getId(), englishWord);
        if(dbWordOfUser != null){//数据库中已存在该生词关系
            logger.info(logPrefix + "数据库中已存在该生词关系！");
            return 2;
        } else {
            NewWord newWord = new NewWord();

            Calendar now = Calendar.getInstance();
            Timestamp nowTs = DatetimeUtil.calendarToTimestamp(now);

            //设置初始化数据
            newWord.setUser(user);
            newWord.setWord(insertingWord);

            newWord.setCreateNewWordDatetime(nowTs);
            newWord.setLastForgotDatetime(nowTs);
            newWord.setLastStoredDatetime(nowTs);
            newWord.setForgetCount((byte) 1);
            newWord.setForgetRate(calculateForgetRate(1,1, 1));

            newWordRepository.save(newWord);
            logger.info(logPrefix + "数据库中未存在该生词关系，且插入数据成功！");
            return 3;
        }
    }

    /**
     * 删除生词
     * @param request
     * @param englishWord
     */
    public int deleteWords(HttpServletRequest request,String englishWord){
        logPrefix = "[NewWordService.deleteWords] ";
        User user=userService.findOneByLoginUsersMap(request);
        NewWord newWord=null;
        newWord=newWordRepository.findDistinctFirstByUserIdAndEnglishWord(user.getId(),englishWord);
        if(newWord!=null){
            newWordRepository.delete(newWord);
            return 1;//删除生词成功
        }
        return -1;//不存在此生词关系
    }

    /**
     * 标记生词记忆结果
     *      即 标记仍记得/已遗忘词汇
     *      注 标记仍记得词汇的前提：必然是生词；更新生词记录的记忆时间、遗忘权重指数
     *     1.根据id，查询到该词newWord
     *          如果查询不到该生词，即 非用户生词记录，则：
     *              标记失败，不存在该生词，返回 1
     *     2.通过token获取用户user.id，并与newWord.user.id对比
     *          如果非同一id，则：
     *              标记失败，用户操作违法，返回 2
     *     3.更新生词的记忆时间、遗忘权重指数。
     *          标记成功，返回 3
     * @param request [use:token]
     * @param newWordId 生词ID
     * @param tagType ["Forgeted", "Stored"] 标记类型[已遗忘或者仍记得],区分大小写
     */
    public int tagMemoryResultOfNewWord(HttpServletRequest request, Integer newWordId, String tagType){
        logPrefix = "[NewWordService.tagMemoryResultOfNewWord] ";
        NewWord newWord = null;
        newWord = newWordRepository.findNewWordById(newWordId);
        if(newWord == null){
            logger.info(logPrefix + "标记失败，不存在该生词。");
            return 1;
        }
        User user = null;
        user = userService.findOneByLoginUsersMap(request);
        if(!newWord.getUser().getId().equals(user.getId())){
            logger.info(logPrefix + "标记失败，用户操作违法。");
            return 2;
        }
        //更新生词的记忆时间、遗忘权重指数
        Calendar now = Calendar.getInstance();
        Timestamp ts = DatetimeUtil.calendarToTimestamp(now);
        if(tagType.equals("Forgeted")){
            newWord.setLastForgotDatetime(ts);
            newWord.setForgetCount((byte) (newWord.getForgetCount() + 1));
        } else if(tagType.equals("Stored")){
            newWord.setLastStoredDatetime(ts);
        } else {
            logger.info(logPrefix + "tagType非指定值，导致：更新仍记得时间或遗忘时间失败。");
        }
        double forgetRate = 0;
        forgetRate = calculateForgetRate(newWord.getLastForgotDatetime(),newWord.getLastStoredDatetime(),newWord.getForgetCount());
        newWord.setForgetRate(forgetRate);
        newWordRepository.save(newWord);
        logger.info(logPrefix + "标记成功。");
        return 3;
    }

    /**
     * 查看每日生词
     *      注：包括每日是否已被成功记忆的生词;
     *      查询近三天用户添加的生词
     * @param request
     * @param page
     */
    public Page<ViewWord> viewEverydayNewWords(HttpServletRequest request, Integer page){
        User user = null;
        user = userService.findOneByLoginUsersMap(request);
        Page<NewWord> newWords= newWordRepository.findNewWordsOfLastDaysOfUser(user.getId(),3,new PageRequest(page<1?0:page-1,50));
        //将获取的生词NewWord转换为ViewWord
        Page<ViewWord> viewWords = new PageImpl<ViewWord>(ViewWord.newWordsToViewWords(newWords.getContent()));
        return viewWords;
    }

    /**
     * 获取用户的高频忘词[forgetRate Top 300]
     * @param request
     * @param page
     */
    public Page<ViewWord> viewOftenForgotWords(HttpServletRequest request, Integer page){
        User user = null;
        user = userService.findOneByLoginUsersMap(request);
        Page<NewWord> newWords= null;
        Page<ViewWord> viewWords = null;
        if(page>0 && page < 7) { //page in [1,6]页时查询成功
            newWords = newWordRepository.findNewWordsOfForgetRateTopByUserId(user.getId(), new PageRequest(page < 1 ? 0 : page - 1, 50));
            //将获取的生词NewWord转换为ViewWord
            viewWords = new PageImpl<ViewWord>(ViewWord.newWordsToViewWords(newWords.getContent()));
        } else {//page值不合法
            logger.info(logPrefix + "page<" + page + "> is illegal,just in [1,6]!");
        }

        return viewWords;//null or 0 record or some records
    }

    /**
     * 查询某单词是否是高频遗忘词汇
     *    算法：
     *      查找用户信息，获取用户id；
     *      查询数据库该单词是否在遗忘指数top300以内
     *          如果  在，返回 1；[是高频遗忘词汇]
     *          如果不在，返回 0；[不是高频遗忘词汇]
     * @param request
     * @param englishWord
     */
    public int isOftenForgotWords(HttpServletRequest request, String englishWord){
        logPrefix = "[NewWordService.IsOftenForgotWords] ";
        int result = 0;
        User user = null;
        user = userService.findOneByLoginUsersMap(request); // get user
        result = newWordRepository.isNewWordsOfForgetRateTopByUserId(user.getId(), 300, englishWord.trim());
        logger.info(logPrefix + "englishWord<" + result + "> is OftenForgotWord?result:" + result); // log
        return result>0?1:0; // 1[yes] or 0[no]
    }

    /**
     * 查看乱序生词 [用于"随便看看"功能]
     * @param request
     */
    public Page<ViewWord> viewDisorderdWords(HttpServletRequest request){
        User user = null;
        user = userService.findOneByLoginUsersMap(request);
        Page<NewWord> newWords= null;
        newWords = newWordRepository.findDisorderedNewWordsByUserId(user.getId(), new PageRequest(0, 50));
        //将获取的生词NewWord转换为ViewWord
        Page<ViewWord> viewWords = new PageImpl<ViewWord>(ViewWord.newWordsToViewWords(newWords.getContent()));
        return viewWords;
    }

    /**
     * 查看遗忘词汇
     *     【查询策略】根据用户的排序规则，查询生词
     * @author johnny
     * @param request
     * @param searchType 查询类型 [ForgetCount遗忘次数/ForgetDatetime(最近)遗忘时间/StoredDatetime(最近)记忆时间]
     * @param sortType 排序规则 [DESC 升序 / ASC升序]
     * @param page 页数 [default value: 1]
     */
    public Page<ViewWord> viewForgetWords(HttpServletRequest request, String searchType, String sortType, Integer page){
        String logPrefix = "[NewWordService.viewForgetWords] ";
        Map<String,String> searchTypeMapOfOToF = new HashMap<>(); //搜索类型与数据库字段相匹配
        //检验参数的格式
        searchTypeMapOfOToF.put("ForgetCount","forget_count");
        searchTypeMapOfOToF.put("ForgetDatetime","last_forgot_datetime");
        searchTypeMapOfOToF.put("StoredDatetime","last_stored_datetime");
        if(!CollectionUtil.isItemInList(Arrays.asList(searchTypeMapOfOToF.keySet().toArray()), searchType)){//如果searchType不在上述值中[字母大小写严格匹配]
            logger.info(logPrefix + "searchType参数[非指定值]异常。");
            return null;
        }
        if((!sortType.equals("DESC")) && (!sortType.equals("ASC"))){
            logger.info(logPrefix + "sortType参数[非指定值]异常。");
            return null;
        }
        //依据用户指定的排序规则，获取数据库中用户的所有生词
        User user = null;
        user = userService.findOneByLoginUsersMap(request);
        Page<ViewWord> viewWordsPage = null;
        Pageable pageable = new PageRequest(0,50);
        List<ViewWord> viewWords = null;
        //暂时未能解决数据库表级别动态SQL的暴力解决法↓(JPA/Hibernate不支持;Mybits支持)
        if(searchType.equals("ForgetCount") && sortType.equals("DESC")){
            viewWords = ViewWord.newWordsToViewWords(newWordRepository.findAllByUserIdOrderByForgetCountDesc(user.getId(), pageable).getContent());
            for(int i=0;i<viewWords.size();i++){
                int j=viewWords.get(i).getForgetCount();
                viewWords.get(i).setTmpData(j);
                System.out.println(viewWords.get(i));
            }
            viewWordsPage = new PageImpl<ViewWord>(viewWords, pageable, viewWords.size());
            logger.info(logPrefix + "<user:" + user.toStringJustUsernameAndEmail() + " | ForgetCount & DESC | count:" + viewWords.size()  + ">");
            System.out.println(logPrefix + "[viewForgetWords:ForgetCount+DESC] viewWords:");

            return viewWordsPage;
        } else if(searchType.equals("ForgetCount") && sortType.equals("ASC")){
            viewWords = ViewWord.newWordsToViewWords(newWordRepository.findAllByUserIdOrderByForgetCountAsc(user.getId(), pageable).getContent());
            for(int i=0;i<viewWords.size();i++){
                int j=viewWords.get(i).getForgetCount();
                viewWords.get(i).setTmpData(j);
                System.out.println(viewWords.get(i));
            }
            viewWordsPage = new PageImpl<ViewWord>(viewWords, pageable, viewWords.size());
            logger.info(logPrefix + "<user:" + user.toStringJustUsernameAndEmail() + " | ForgetCount & ASC | count:" + viewWords.size()  + ">");
            return viewWordsPage;
        } else if(searchType.equals("ForgetDatetime") && sortType.equals("DESC")){
            viewWords = ViewWord.newWordsToViewWords(newWordRepository.findAllByUserIdOrderByLastForgotDatetimeDesc(user.getId(), pageable).getContent());
            for(int i=0;i<viewWords.size();i++){
                if(viewWords.get(i).getLastForgotDatetime()==null){
                    System.out.println(logPrefix + "[viewForgetWords:ForgetCount+DESC] viewWords:"+"暂无数据");
                    viewWords.get(i).setTmpData(-1);
                }else {
                    long l=viewWords.get(i).getLastForgotDatetime().getTime();
                    String dateForLast=DatetimeUtil.millisecondToDateString(Math.abs(System.currentTimeMillis()-l));
                    System.out.println(dateForLast);
                    String day=dateForLast.substring(0,dateForLast.indexOf("天"));
                    int dayInt=Integer.parseInt(day);
                    String hour=dateForLast.substring(dateForLast.indexOf("天")+1,dateForLast.indexOf("时"));
                    String minute=dateForLast.substring(dateForLast.indexOf("时")+1,dateForLast.indexOf("分"));
                    String second=dateForLast.substring(dateForLast.indexOf("分")+1,dateForLast.indexOf("秒"));
                    if(dayInt==0){
                        if(Integer.parseInt(hour)>0||Integer.parseInt(minute)>0||Integer.parseInt(second)>0){
                            dayInt=1;
                        }else{
                            dayInt=0;
                        }
                    }
                    else if(dayInt>0){
                        if (Integer.parseInt(hour)>0||Integer.parseInt(minute)>0||Integer.parseInt(second)>0){
                            dayInt=dayInt+1;
                        }else {
                            dayInt = dayInt;
                        }
                    }
                    viewWords.get(i).setTmpData(dayInt);
                    System.out.println(viewWords.get(i));
                }
            }
            viewWordsPage = new PageImpl<ViewWord>(viewWords, pageable, viewWords.size());
            logger.info(logPrefix + "<user:" + user.toStringJustUsernameAndEmail() + " | ForgetDatetime & DESC | count:" + viewWords.size()  + ">");
            return viewWordsPage;
        } else if(searchType.equals("ForgetDatetime") && sortType.equals("ASC")){
            viewWords = ViewWord.newWordsToViewWords(newWordRepository.findAllByUserIdOrderByLastForgotDatetimeAsc(user.getId(), pageable).getContent());
            for(int i=0;i<viewWords.size();i++){
                if(viewWords.get(i).getLastForgotDatetime()==null){
                    System.out.println(logPrefix + "[viewForgetWords:ForgetCount+DESC] viewWords:"+"暂无数据");
                    viewWords.get(i).setTmpData(-1);
                }else {
                    long l=viewWords.get(i).getLastForgotDatetime().getTime();
                    System.out.println(l);//代码重复防止提醒
                    String dateForLast=DatetimeUtil.millisecondToDateString(Math.abs(System.currentTimeMillis()-l));
                    String day=dateForLast.substring(0,dateForLast.indexOf("天"));
                    int dayInt=Integer.parseInt(day);
                    String hour=dateForLast.substring(dateForLast.indexOf("天")+1,dateForLast.indexOf("时"));
                    String minute=dateForLast.substring(dateForLast.indexOf("时")+1,dateForLast.indexOf("分"));
                    String second=dateForLast.substring(dateForLast.indexOf("分")+1,dateForLast.indexOf("秒"));
                    if(dayInt==0){
                        if(Integer.parseInt(hour)>0||Integer.parseInt(minute)>0||Integer.parseInt(second)>0){
                            dayInt=1;
                        }else{
                            dayInt=0;
                        }
                    }
                    else if(dayInt>0){
                        if (Integer.parseInt(hour)>0||Integer.parseInt(minute)>0||Integer.parseInt(second)>0){
                            dayInt=dayInt+1;
                        }else {
                            dayInt = dayInt;
                        }
                    }
                    viewWords.get(i).setTmpData(dayInt);
                    System.out.println(viewWords.get(i));
                }
            }
            viewWordsPage = new PageImpl<ViewWord>(viewWords, pageable, viewWords.size());
            logger.info(logPrefix + "<user:" + user.toStringJustUsernameAndEmail() + " | ForgetDatetime & ASC | count:" + viewWords.size()  + ">");
            return viewWordsPage;
        } else if(searchType.equals("StoredDatetime") && sortType.equals("DESC")){
            viewWords = ViewWord.newWordsToViewWords(newWordRepository.findAllByUserIdOrderByLastStoredDatetimeDesc(user.getId(), pageable).getContent());
            for(int i=0;i<viewWords.size();i++){
                if(viewWords.get(i).getLastStoredDatetime()==null){
                    System.out.println(logPrefix + "[viewForgetWords:ForgetCount+DESC] viewWords:"+"暂无数据");
                    viewWords.get(i).setTmpData(-1);
                }else {
                    long l = viewWords.get(i).getLastStoredDatetime().getTime();
                    String dateForLast = DatetimeUtil.millisecondToDateString(Math.abs(System.currentTimeMillis() - l));
                    String day = dateForLast.substring(0, dateForLast.indexOf("天"));
                    int dayInt = Integer.parseInt(day);
                    String hour = dateForLast.substring(dateForLast.indexOf("天") + 1, dateForLast.indexOf("时"));
                    String minute = dateForLast.substring(dateForLast.indexOf("时") + 1, dateForLast.indexOf("分"));
                    String second = dateForLast.substring(dateForLast.indexOf("分") + 1, dateForLast.indexOf("秒"));
                    if (dayInt == 0) {
                        if (Integer.parseInt(hour) > 0 || Integer.parseInt(minute) > 0 || Integer.parseInt(second) > 0) {
                            dayInt = 1;
                        } else {
                            dayInt = 0;
                        }
                    } else if (dayInt > 0) {
                        if (Integer.parseInt(hour) > 0 || Integer.parseInt(minute) > 0 || Integer.parseInt(second) > 0) {
                            dayInt = dayInt + 1;
                        } else {
                            dayInt = dayInt;
                        }
                    }
                    viewWords.get(i).setTmpData(dayInt);
                    System.out.println(viewWords.get(i));
                }
            }
            viewWordsPage = new PageImpl<ViewWord>(viewWords, pageable, viewWords.size());
            logger.info(logPrefix + "<user:" + user.toStringJustUsernameAndEmail() + " | StoredDatetime & DESC | count:" + viewWords.size()  + ">");
            return viewWordsPage;
        } else if(searchType.equals("StoredDatetime") && sortType.equals("ASC")){
            viewWords = ViewWord.newWordsToViewWords(newWordRepository.findAllByUserIdOrderByLastStoredDatetimeAsc(user.getId(), pageable).getContent());
            for(int i=0;i<viewWords.size();i++){
                if(viewWords.get(i).getLastStoredDatetime()==null){
                    System.out.println(logPrefix + "[viewForgetWords:ForgetCount+DESC] viewWords:"+"暂无数据");
                    viewWords.get(i).setTmpData(-1);
                }else {
                    long l = viewWords.get(i).getLastStoredDatetime().getTime();
                    System.out.println(l);//代码重复防止提醒
                    String dateForLast = DatetimeUtil.millisecondToDateString(Math.abs(System.currentTimeMillis() - l));
                    String day = dateForLast.substring(0, dateForLast.indexOf("天"));
                    int dayInt = Integer.parseInt(day);
                    String hour = dateForLast.substring(dateForLast.indexOf("天") + 1, dateForLast.indexOf("时"));
                    String minute = dateForLast.substring(dateForLast.indexOf("时") + 1, dateForLast.indexOf("分"));
                    String second = dateForLast.substring(dateForLast.indexOf("分") + 1, dateForLast.indexOf("秒"));
                    if (dayInt == 0) {
                        if (Integer.parseInt(hour) > 0 || Integer.parseInt(minute) > 0 || Integer.parseInt(second) > 0) {
                            dayInt = 1;
                        } else {
                            dayInt = 0;
                        }
                    } else if (dayInt > 0) {
                        if (Integer.parseInt(hour) > 0 || Integer.parseInt(minute) > 0 || Integer.parseInt(second) > 0) {
                            dayInt = dayInt + 1;
                        } else {
                            dayInt = dayInt;
                        }
                    }
                    viewWords.get(i).setTmpData(dayInt);
                    System.out.println(viewWords.get(i));
                }
            }
            viewWordsPage = new PageImpl<ViewWord>(viewWords, pageable, viewWords.size());
            logger.info(logPrefix + "<user:" + user.toStringJustUsernameAndEmail() + " | StoredDatetime & ASC | count:" + viewWords.size()  + ">");
            return viewWordsPage;
        } else {//异常情况
            logger.info(logPrefix + "<user:" + user.toStringJustUsernameAndEmail() + " is error.>");
            return viewWordsPage; // null
        }
    }

    /**
     * (第三方接口的)翻译
     * @return words [第三方翻译单词结果集]
     * @param englishWord
     */
    public List<Word> translate(String englishWord) throws IOException {
        String logPrefix = "[NewWordService.translate] ";
        StringBuilder url = new StringBuilder("http://fanyi.youdao.com/translate?&doctype=json&type=AUTO&i=");
        url.append(englishWord);
        Document doc = requestUtil.getDocument(url.toString());

        //json to third-tanslate-result obejct
        ObjectMapper mapper = new ObjectMapper();
        ThirdWordResult thirdWordResult = null;
        thirdWordResult = mapper.readValue(doc.text(), ThirdWordResult.class);
        //System.out.println(thirdWordResult.toString());//test
        //System.out.println(doc.text());//test

        //third-tanslate-result to word
        if(thirdWordResult != null){//获取翻译成功
            List<Word> words = new ArrayList<Word>();
            if(thirdWordResult.getErrorCode() == 0 && thirdWordResult.getType().equals("EN2ZH_CN")){//如果是英文翻译为中文类型，且接口翻译无异常
                Word word = new Word();
                word.setId(0); //
                word.setEnglishWord(thirdWordResult.getTranslateResult().get(0).get(0).getSrc());
                word.setChineseTranslate(thirdWordResult.getTranslateResult().get(0).get(0).getTgt());
                words.add(word);
                logger.info(logPrefix + " translate word(" + englishWord + ") success!");
                return words;
            } else {
                logger.info(logPrefix + " translate word(" + englishWord + ") failed!");
                return null;
            }
        } else{
            return null;
        }
    }

    /**
     * 计算遗忘权值
     *  <对double calculateForgetRate(Calendar,Calendar,Calendar)的重载>
     * @param lastForgetDate (距最近遗忘的天数) as nDate
     * @param lastStoredDate (距最近记忆的天数) as mDate
     * @param forgetCount int as s
     */
    public double calculateForgetRate(Timestamp lastForgetDate,Timestamp lastStoredDate,int forgetCount){
        Calendar today = Calendar.getInstance();
        int lastForgetDays = (int) ((today.getTimeInMillis() - DatetimeUtil.timestampToCalendar(lastForgetDate).getTimeInMillis()) / (60*1000*60*24)) + 1;//unit:day 60*1000[minute] 60*1000*60[hour]
        int lastStoredDays = (int) ((today.getTimeInMillis() - DatetimeUtil.timestampToCalendar(lastStoredDate).getTimeInMillis()) / (60*1000*60*24)) + 1;
        return calculateForgetRate(lastForgetDays, lastStoredDays, forgetCount);
    }

    /**
     * 计算遗忘权值
     *  <对double calculateForgetRate(int,int,int)的重载>
     * @param lastForgetDate (距最近遗忘的天数) as nDate
     * @param lastStoredDate (距最近记忆的天数) as mDate
     * @param forgetCount int as s
     */
    public double calculateForgetRate(Calendar lastForgetDate,Calendar lastStoredDate,int forgetCount){
        Calendar today = Calendar.getInstance();
        int lastForgetDays = (int) ((today.getTimeInMillis() - lastForgetDate.getTimeInMillis()) / (60*1000*60*24)) + 1;//unit:day 60*1000[minute] 60*1000*60[hour]
        int lastStoredDays = (int) ((today.getTimeInMillis() - lastStoredDate.getTimeInMillis()) / (60*1000*60*24)) + 1;
        return calculateForgetRate(lastForgetDays, lastStoredDays, forgetCount);
    }

    /**
     * 计算遗忘权值
     *      单位：天
     *      r = 10/n + 7*s -15/m
     *
     * @param lastForgetDays (距最近遗忘的天数,not 0) int as n
     * @param lastStoredDays (距最近记忆的天数, not 0) int as m
     * @param forgetCount int as s
     */
    public double calculateForgetRate(int lastForgetDays, int lastStoredDays, int forgetCount){
        if(lastForgetDays == 0){
            lastForgetDays = 1;
        }
        if(lastStoredDays == 0){
            lastStoredDays = 1;
        }
        return 10/lastForgetDays + 7*forgetCount - 15/lastStoredDays;
    }
}
