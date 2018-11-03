package cn.johnnyzen.newWord;

import cn.johnnyzen.user.UserService;
import cn.johnnyzen.util.reuslt.Result;
import cn.johnnyzen.util.reuslt.ResultCode;
import cn.johnnyzen.util.reuslt.ResultUtil;
import cn.johnnyzen.word.Word;
import cn.johnnyzen.word.WordService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
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
     * @author johnny
     * @param search
     * @param token
     *
     */
    @RequestMapping(value = "/searchWords/api")
    @ResponseBody
    public Result searchWords(HttpServletRequest request,
                              @RequestParam(value = "search",required = true) String search,
                              @RequestParam(value = "token",required = true) String token){
        String logPrefix = "[UserService.searchWords()] ";
        Collection<Word> thirdWords = null; //第三方单词
        Collection<Word> userNewWords = null; //用户生词

        try {
            thirdWords = newWordService.translate(search);
            userNewWords = newWordService.findAllWordsOfUserByRequest(request);
            userNewWords.addAll(thirdWords);
        } catch (IOException e) {//第三方API翻译异常
            logger.warning(logPrefix + " thrid-api failed to tranlate : "+ search);
            e.printStackTrace();
        }

        System.out.println(logPrefix + "userNewWords:"); //test
        for(Word item : userNewWords){//test
            System.out.println(item.toString());
        }
        if(userNewWords == null){
            return ResultUtil.error(ResultCode.FAIL, "查词无结果。");
        }

        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
           json = mapper.writeValueAsString(userNewWords.toArray());
           System.out.println(logPrefix + "json转换成功！");
        } catch (JsonProcessingException e) {
            System.out.println(logPrefix + "json转换失败！");
            e.printStackTrace();
        }

        return ResultUtil.success("查词成功！", userNewWords);
//        return ResultUtil.error(ResultCode.FAIL, "[NewWordController.searchWords] 接口暂未开发");
    }

    @RequestMapping(value = "/viewWord/api")
    @ResponseBody
    public Result viewWord(HttpServletRequest request,
                              @RequestParam(value = "englishWord",required = true) String englishWord,
                              @RequestParam(value = "token",required = true) String token){
        return ResultUtil.error(ResultCode.FAIL, "[NewWordController.viewWord] 接口暂未开发");
    }

    @RequestMapping(value = "/saveNewWord/api")
    @ResponseBody
    public Result saveNewWord(HttpServletRequest request,
                           @RequestParam(value = "englishWord",required = true) String englishWord,
                           @RequestParam(value = "chineseTranslate",required = true) String chineseTranslate,
                           @RequestParam(value = "token",required = true) String token){
        return ResultUtil.error(ResultCode.FAIL, "[NewWordController.saveNewWord] 接口暂未开发");
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
