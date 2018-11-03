package cn.johnnyzen.newWord;

import cn.johnnyzen.user.UserService;
import cn.johnnyzen.util.reuslt.Result;
import cn.johnnyzen.util.reuslt.ResultCode;
import cn.johnnyzen.util.reuslt.ResultUtil;
import cn.johnnyzen.word.Word;
import cn.johnnyzen.word.WordService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

/**
 * @IDE: Created by IntelliJ IDEA.
 * @Author: 千千寰宇
 * @Date: 2018/11/1  22:27:14
 * @Description: ...
 */
@Controller
public class NewWordController {
    private static final Logger logger = Logger.getLogger(NewWordController.class.getName());

    @Autowired
    private NewWordService newWordService;

    @Autowired
    private UserService userService;

    /*
     * 查词
     * @author johnny
     * @param search
     * @param token
     */
    @RequestMapping(value = "/searchWords/api")
    @ResponseBody
    public Result searchWords(HttpServletRequest request,
                              @RequestParam(value = "search",required = true) String search,
                              @RequestParam(value = "token",required = true) String token){
        String logPrefix = "[NewWordController.searchWords()] ";
        Collection<Word> thirdWords = null; //第三方单词
        Collection<Word> userNewWords = null; //用户生词

        Map<String, Word> words = null; //已英文单词未主键，使用map对两集合的英文单词去重

        try {
            thirdWords = newWordService.translate(search);
            userNewWords = newWordService.findAllWordsOfUserByFuzzySearch(request, search);

            if(thirdWords != null){ //不判断时，会导致运行时错误
                userNewWords.addAll(thirdWords); //全部集中到userNewWords集合中处理
            }
            if(newWordService == null){ //如果整个结果集都为空
                logger.info(logPrefix + "result of search word(" + search + ") :success but null.");
                return ResultUtil.success("查词成功，但无结果。");
            }

            words = new HashMap<String, Word>();//此时words必不为空
            Iterator<Word> iter = userNewWords.iterator();
            while(iter.hasNext()){
                Word word = iter.next();
                /*
                    如果map中已含有该单词:
                        查看map中的该英文单词或者当前的单词是否id不为0;
                        如果其中任意有一个不为0，则该单词的id变更为该非0id
                        即：相当于更新当前英文单词的id
                 */
                if(words.containsKey(word.getEnglishWord())){
                    Word mapWord = words.get(word.getEnglishWord());
                    Integer id = mapWord.getId() != 0 ? mapWord.getId() : word.getId();
                    mapWord.setId(id);
                    words.put(mapWord.getEnglishWord(), mapWord);
                } else { //map中不含当前单词
                    words.put(word.getEnglishWord(), word);
                }
            }
        } catch (IOException e) {//第三方API翻译异常
            logger.warning(logPrefix + " thrid-api failed to tranlate : "+ search);
            e.printStackTrace();
        }

        //[notice] must use jackson version:com.fasterxml.jackson,else it will pose error.
        logger.warning(logPrefix + "result of search word(" + search + ") :success.");
        return ResultUtil.success("查词成功！", words.values());
    }

    /*
     * 查看某生词
     *  1.精确查找
     *  2.必须为用户的生词
     * @author johnny
     * @param request
     * @param englishWord
     * @param token
     */
    @RequestMapping(value = "/viewWord/api")
    @ResponseBody
    public Result viewWord(HttpServletRequest request,
                              @RequestParam(value = "englishWord",required = true) String englishWord,
                              @RequestParam(value = "token",required = true) String token){
        String logPrefix = "[NewWordController.viewWord()] ";
        NewWord newWord = null;
        newWord = newWordService.findOneOfUserByExactSearch(request, englishWord);
        if(newWord == null) {
            logger.warning(logPrefix + "result of search word(" + englishWord + ") :success but null.");
            return ResultUtil.success("查词成功，但无匹配结果。");
        } else {
            logger.warning(logPrefix + "result of search word(" + englishWord + ") :success.");

            //newWord部分字段由于其他方法的原因需要被jackson屏蔽
            //此处设置视图Bean，根据用户所需进行字段可视化调整
            ViewWord viewWord = new ViewWord(newWord);
            return ResultUtil.success("查词成功!", viewWord);
        }
    }

    /*
     * 新增生词
     * @author johnny
     * @param request
     * @param englishWord
     */
    @RequestMapping(value = "/saveNewWord/api")
    @ResponseBody
    public Result saveNewWord(HttpServletRequest request,
                           @RequestParam(value = "englishWord",required = true) String englishWord,
//                           @RequestParam(value = "chineseTranslate",required = true) String chineseTranslate,
                           @RequestParam(value = "token",required = true) String token){
        String logPrefix = "[NewWordController.saveNewWord] ";
        int handle = newWordService.saveNewWord(request, englishWord);
        if(handle == 3){
            logger.info(logPrefix + "数据库中未存在该生词关系，且插入数据成功！");
            return ResultUtil.success("添加生词成功!");
        } else if(handle == 2){
            logger.info(logPrefix + "数据库中已存在该生词关系！");
            return ResultUtil.success("添加的生词已存在!");
        } else {//1
            logger.warning(logPrefix + "translate word(" + englishWord + ") is null or error");
            return ResultUtil.error(ResultCode.FAIL, "因服务器内部原因，添加生词失败！");
        }
    }

    @RequestMapping(value = "/tagStoredWord/api")
    @ResponseBody
    public Result tagStoredWord(HttpServletRequest request,
                              @RequestParam(value = "id",required = true) Integer id,
                              @RequestParam(value = "token",required = true) String token){
        return ResultUtil.error(ResultCode.FAIL, "[NewWordController.tagStoredWord] 接口暂未开发");
    }

    @RequestMapping(value = "/tagForgetWord/api")
    @ResponseBody
    public Result tagForgetWord(HttpServletRequest request,
                                @RequestParam(value = "id",required = true) Integer id,
                                @RequestParam(value = "token",required = true) String token){
        return ResultUtil.error(ResultCode.FAIL, "[NewWordController.tagForgetWord] 接口暂未开发");
    }

    @RequestMapping(value = "/viewEverydayNewWords/api")
    @ResponseBody
    public Result viewEverydayNewWords(HttpServletRequest request,
                                       @RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                                       @RequestParam(value = "token",required = true) String token){
        return ResultUtil.error(ResultCode.FAIL, "[NewWordController.viewEverydayNewWords] 接口暂未开发");
    }

    @RequestMapping(value = "/viewOftenForgotWords/api")
    @ResponseBody
    public Result viewOftenForgotWords(HttpServletRequest request,
                                @RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                                @RequestParam(value = "token",required = true) String token){
        return ResultUtil.error(ResultCode.FAIL, "[NewWordController.viewOftenForgotWords] 接口暂未开发");
    }

    @RequestMapping(value = "/viewDisorderdWords/api")
    @ResponseBody
    public Result viewDisorderdWords(HttpServletRequest request,
                                     @RequestParam(value = "token",required = true) String token){
        return ResultUtil.error(ResultCode.FAIL, "[NewWordController.viewDisorderdWords] 接口暂未开发");
    }

    @RequestMapping(value = "/viewForgetWords/api")
    @ResponseBody
    public Result viewForgetWords(HttpServletRequest request,
                                  @RequestParam(value = "searchType",required = true) String searchType,
                                  @RequestParam(value = "sortType",required = true) String sortType,
                                  @RequestParam(value = "token",required = true) String token){
        return ResultUtil.error(ResultCode.FAIL, "[NewWordController.viewForgetWords] 接口暂未开发");
    }

    @RequestMapping(value = "/deleteNewWords/api")
    @ResponseBody
    public Result deleteNewWords(HttpServletRequest request,
                                  @RequestParam(value = "id",required = true) String id,
                                  @RequestParam(value = "token",required = true) String token){
        return ResultUtil.error(ResultCode.FAIL, "[NewWordController.deleteNewWords] 接口暂未开发");
    }
}
