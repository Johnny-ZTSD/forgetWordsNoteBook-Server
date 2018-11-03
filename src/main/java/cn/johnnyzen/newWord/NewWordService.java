package cn.johnnyzen.newWord;

import cn.johnnyzen.user.User;
import cn.johnnyzen.user.UserRepository;
import cn.johnnyzen.user.UserService;
import cn.johnnyzen.util.request.RequestUtil;
import cn.johnnyzen.word.ThirdWordResult;
import cn.johnnyzen.word.Word;
import cn.johnnyzen.word.WordRepository;
import cn.johnnyzen.word.WordService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

/**
 * @IDE: Created by IntelliJ IDEA.
 * @Author: 千千寰宇
 * @Date: 2018/11/1  18:08:44
 * @Description: ...
 */
@Service("newWordService")
public class NewWordService {
    private static final Logger logger = Logger.getLogger(NewWordService.class.getName());

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

    public Collection<NewWord> findAllByUserId(Integer id){
        return newWordRepository.findAllByUserId(id);
    }

    /*
     * 查找用户的所有生词。
     *  1.通过token，查找用户信息
     *  2.通过用户id查询用户的所有生词。
     *
     * @return Collection<Word>
     */
    public Collection<Word> findAllWordsOfUserByRequest(HttpServletRequest request){
        String logPrefix = "[NewWordService.findAllWordsOfUserByRequest] ";
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

    /*
     * 模糊查找用户的所有生词。[主要用于查词时，查某些类似单词]
     *  1.通过token，查找用户信息
     *  2.通过用户id和检索的单词模糊查询用户的所有生词。
     *
     * @param search
     * @param request [need:token]
     * @return Collection<Word>
     */
    public Collection<Word> findAllWordsOfUserByFuzzySearch(HttpServletRequest request,String englishWord){
        String logPrefix = "[NewWordService.findAllWordsOfUserByFuzzySearch] ";
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

    /*
     * 精确查找用户的所有生词。[主要用于查某一词时]
     *  1.通过token，查找用户信息
     *  2.通过用户id和检索的单词精确查询用户的某个生词。
     *
     * @param search
     * @param request [need:token]
     * @return Collection<Word>
     */
    public NewWord findOneOfUserByExactSearch(HttpServletRequest request,String englishWord){
        String logPrefix = "[NewWordService.findAllWordsOfUserByFuzzySearch] ";
        User user = null;
        user = userService.findOneByLoginUsersMap(request);
        //user登陆校验已通过过滤器实现，无需再校验。

        NewWord newWords = null;
        newWords = newWordRepository.findDistinctFirstByUserIdAndEnglishWord(user.getId(), englishWord);
        return newWords; // null or entity object
    }


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
}
