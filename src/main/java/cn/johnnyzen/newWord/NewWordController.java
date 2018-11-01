package cn.johnnyzen.newWord;

import cn.johnnyzen.util.reuslt.Result;
import cn.johnnyzen.util.reuslt.ResultCode;
import cn.johnnyzen.util.reuslt.ResultUtil;
import cn.johnnyzen.word.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
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
    private WordService wordService;

    @RequestMapping(value = "/searchWords/api")
    @ResponseBody
    public Result searchWords(HttpServletRequest request,
                              @RequestParam(value = "search",required = true) String search,
                              @RequestParam(value = "token",required = true) String token){
        return ResultUtil.error(ResultCode.FAIL, "[UserController.searchWords] 接口暂未开发");
    }

    @RequestMapping(value = "/viewWord/api")
    @ResponseBody
    public Result viewWord(HttpServletRequest request,
                              @RequestParam(value = "englishWord",required = true) String englishWord,
                              @RequestParam(value = "token",required = true) String token){
        return ResultUtil.error(ResultCode.FAIL, "[UserController.viewWord] 接口暂未开发");
    }

    @RequestMapping(value = "/saveNewWord/api")
    @ResponseBody
    public Result saveNewWord(HttpServletRequest request,
                           @RequestParam(value = "englishWord",required = true) String englishWord,
                           @RequestParam(value = "chineseTranslate",required = true) String chineseTranslate,
                           @RequestParam(value = "token",required = true) String token){
        return ResultUtil.error(ResultCode.FAIL, "[UserController.saveNewWord] 接口暂未开发");
    }

    @RequestMapping(value = "/tagStoredWord/api")
    @ResponseBody
    public Result tagStoredWord(HttpServletRequest request,
                              @RequestParam(value = "id",required = true) Integer id,
                              @RequestParam(value = "token",required = true) String token){
        return ResultUtil.error(ResultCode.FAIL, "[UserController.tagStoredWord] 接口暂未开发");
    }

    @RequestMapping(value = "/tagForgetWord/api")
    @ResponseBody
    public Result tagForgetWord(HttpServletRequest request,
                                @RequestParam(value = "id",required = true) Integer id,
                                @RequestParam(value = "token",required = true) String token){
        return ResultUtil.error(ResultCode.FAIL, "[UserController.tagForgetWord] 接口暂未开发");
    }

    @RequestMapping(value = "/viewEverydayNewWords/api")
    @ResponseBody
    public Result viewEverydayNewWords(HttpServletRequest request,
                                       @RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                                       @RequestParam(value = "token",required = true) String token){
        return ResultUtil.error(ResultCode.FAIL, "[UserController.viewEverydayNewWords] 接口暂未开发");
    }

    @RequestMapping(value = "/viewOftenForgotWords/api")
    @ResponseBody
    public Result viewOftenForgotWords(HttpServletRequest request,
                                @RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                                @RequestParam(value = "token",required = true) String token){
        return ResultUtil.error(ResultCode.FAIL, "[UserController.viewOftenForgotWords] 接口暂未开发");
    }

    @RequestMapping(value = "/viewDisorderdWords/api")
    @ResponseBody
    public Result viewDisorderdWords(HttpServletRequest request,
                                     @RequestParam(value = "token",required = true) String token){
        return ResultUtil.error(ResultCode.FAIL, "[UserController.viewDisorderdWords] 接口暂未开发");
    }

    @RequestMapping(value = "/viewForgetWords/api")
    @ResponseBody
    public Result viewForgetWords(HttpServletRequest request,
                                  @RequestParam(value = "searchType",required = true) String searchType,
                                  @RequestParam(value = "sortType",required = true) String sortType,
                                  @RequestParam(value = "token",required = true) String token){
        return ResultUtil.error(ResultCode.FAIL, "[UserController.viewForgetWords] 接口暂未开发");
    }

    @RequestMapping(value = "/deleteNewWords/api")
    @ResponseBody
    public Result deleteNewWords(HttpServletRequest request,
                                  @RequestParam(value = "id",required = true) String id,
                                  @RequestParam(value = "token",required = true) String token){
        return ResultUtil.error(ResultCode.FAIL, "[UserController.deleteNewWords] 接口暂未开发");
    }
}
