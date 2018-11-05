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
import org.springframework.data.domain.Page;
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

    /**
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
        String message = "";
        Page<Word> words = null;
        words = newWordService.searchWords(request,search.trim());
        if(words.getSize() == 0){
            message = "查词成功，但无结果。";
            logger.info(logPrefix + message);
            return ResultUtil.success(message,words);
        } else {
            //[notice] must use jackson version:com.fasterxml.jackson,else it will pose error.
            logger.warning(logPrefix + "result of search word(" + search + ") :success.");
            return ResultUtil.success("查词成功！", words);
        }
    }

    /**
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
        newWord = newWordService.findOneOfUserByExactSearch(request, englishWord.trim());
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

    /**
     * 新增用户自定义的生词/词组
     *  场景：由于用户的特别需要，想增加一些用户容易忘记的词组和意思等
     *  区别：
     *      与saveNewWord相比，saveNewWord新增生词的单词来源：
     *          数据库已存储的单词翻译记录或者第三方翻译接口的翻译
     *      与saveNewWord相比，saveNewWordOfUser：
     *          0.如果该单词数据库中已存在，本接口HTTP请求所提供的单词翻译将覆盖数据的翻译
     *          1.新增生词的单词来源：只来源于用户的翻译，相当于提供一个用户编辑单词来提升/优化翻译效果的渠道；
     *          2.本接口不面向普通用户，仅用于管理员或者特殊用户[待定义]
     * @author johnny
     * @param request
     * @param englishWord
     * @param chineseTranslate
     * @param authorityCode
     * @param token
     */
    @RequestMapping(value = "/saveNewWordOfUser/api")
    @ResponseBody
    public Result saveNewWordOfUser(HttpServletRequest request,
                                    @RequestParam(value = "englishWord",required = true)String englishWord,
                                    @RequestParam(value = "chineseTranslate",required = true)String chineseTranslate,
                                    @RequestParam(value = "authorityCode",required = true)String authorityCode,
                                    @RequestParam(value = "token",required = true) String token
                                    ){

        return  null;
    }

    /**
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
        int handle = newWordService.saveNewWord(request, englishWord.trim());
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

    /**
     * 标记仍记得词汇
     * @author johnny
     * @param request HTTP请求
     * @param id 生词ID
     * @param token 用户登陆口令
     */
    @RequestMapping(value = "/tagStoredWord/api")
    @ResponseBody
    public Result tagStoredWord(HttpServletRequest request,
                              @RequestParam(value = "id",required = true) Integer id,
                              @RequestParam(value = "token",required = true) String token){
        String logPrefix = "[NewWordController.tagStoredWord] ";
        int handle = newWordService.tagMemoryResultOfNewWord(request, id, "Stored");
        if(handle == 3){
            logger.info(logPrefix + "标记(ID:" + id + ")成功。");
            return ResultUtil.success("标记成功。");
        } else if(handle == 2){
            logger.info(logPrefix + "标记(ID:" + id + ")失败，用户操作违法。");
            return ResultUtil.error(ResultCode.FAIL, "标记失败，用户操作违法。");
        } else if(handle == 1){
            logger.info(logPrefix + "标记(ID:" + id + ")失败，不存在该生词。");
            return ResultUtil.error(ResultCode.FAIL, "标记失败，不存在该生词。");
        } else {
            logger.info(logPrefix + "标记(ID:" + id + ")失败，服务器错误未知。");
            return ResultUtil.error(ResultCode.INTERNAL_SERVER_ERROR, "标记失败，服务器错误未知。");
        }
    }

    /**
     * 标记已遗忘词汇
     * @author johnny
     * @param request HTTP请求
     * @param id 生词ID
     * @param token 用户登陆口令
     */
    @RequestMapping(value = "/tagForgetWord/api")
    @ResponseBody
    public Result tagForgetWord(HttpServletRequest request,
                                @RequestParam(value = "id",required = true) Integer id,
                                @RequestParam(value = "token",required = true) String token){
        String logPrefix = "[NewWordController.tagForgetWord] ";
        int handle = newWordService.tagMemoryResultOfNewWord(request, id, "Forgeted");
        if(handle == 3){
            logger.info(logPrefix + "标记(ID:" + id + ")成功。");
            return ResultUtil.success("标记成功。");
        } else if(handle == 2){
            logger.info(logPrefix + "标记(ID:" + id + ")失败，用户操作违法。");
            return ResultUtil.error(ResultCode.FAIL, "标记失败，用户操作违法。");
        } else if(handle == 1){
            logger.info(logPrefix + "标记(ID:" + id + ")失败，不存在该生词。");
            return ResultUtil.error(ResultCode.FAIL, "标记失败，不存在该生词。");
        } else {
            logger.info(logPrefix + "标记(ID:" + id + ")失败，服务器错误未知。");
            return ResultUtil.error(ResultCode.INTERNAL_SERVER_ERROR, "标记失败，服务器错误未知。");
        }
    }

    /**
     * 查看每日生词
     * @author johnny
     * @param request HTTP请求
     * @param page 生词ID
     * @param token 用户登陆口令
     */
    @RequestMapping(value = "/viewEverydayNewWords/api")
    @ResponseBody
    public Result viewEverydayNewWords(HttpServletRequest request,
                                       @RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                                       @RequestParam(value = "token",required = true) String token){
        String logPrefix = "[NewWordController.viewEverydayNewWords] ";
        Page<ViewWord> viewWords = null;
        viewWords = newWordService.viewEverydayNewWords(request,page);
        if(viewWords == null){
            logger.info(logPrefix + "该用户无任何生词。");
            return ResultUtil.success("该用户无任何生词。");
        } else {
            logger.info(logPrefix + "获取用户每日生词成功!");
            return ResultUtil.success("获取用户每日生词成功!", viewWords);
        }
    }

    /**
     * 查看高频遗忘生词
     * @author johnny
     * @param request
     * @param token
     * @param page
     */
    @RequestMapping(value = "/viewOftenForgotWords/api")
    @ResponseBody
    public Result viewOftenForgotWords(HttpServletRequest request,
                                @RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                                @RequestParam(value = "token",required = true) String token){
        String logPrefix = "[NewWordController.viewOftenForgotWords] ";
        Page<ViewWord> viewWords = null;
        viewWords = newWordService.viewOftenForgotWords(request, page);
        if(viewWords == null){
            logger.info(logPrefix + "该用户无高频遗忘词。");
            return ResultUtil.success("该用户无高频遗忘词。");
        } else {
            logger.info(logPrefix + "获取用户高频遗忘词成功!");
            return ResultUtil.success("获取用户高频遗忘词成功!", viewWords);
        }
    }

    /**
     * 查看乱序词汇
     * @author johnny
     * @param request
     * @param token
     */
    @RequestMapping(value = "/viewDisorderdWords/api")
    @ResponseBody
    public Result viewDisorderdWords(HttpServletRequest request,
                                     @RequestParam(value = "token",required = true) String token) {
        String logPrefix = "[NewWordController.viewDisorderdWords] ";
        Page<ViewWord> viewWords = null;
        viewWords = newWordService.viewDisorderdWords(request);
        if(viewWords == null){
            logger.info(logPrefix + "该用户无任何生词。");
            return ResultUtil.success("该用户无任何生词。");
        } else {
            logger.info(logPrefix + "获取用户乱序版生词成功!");
            return ResultUtil.success("获取用户乱序版生词成功!", viewWords);
        }
    }

    /**
     * 查看遗忘词汇
     *     【查询策略】根据用户的排序规则，查询生词
     * @author johnny
     * @param request
     * @param searchType 查询类型 [ForgetCount遗忘次数/ForgetDatetime(最近)遗忘时间/StoredDatetime(最近)记忆时间]
     * @param sortType 排序规则 [DESC 升序 / ASC升序]
     * @param page 页数 [default value: 1]
     * @param token 用户登录口令
     */
    @RequestMapping(value = "/viewForgetWords/api")
    @ResponseBody
    public Result viewForgetWords(HttpServletRequest request,
                                  @RequestParam(value = "searchType",required = true) String searchType,
                                  @RequestParam(value = "sortType",required = true) String sortType,
                                  @RequestParam(value = "page",required = false, defaultValue = "1") Integer page,
                                  @RequestParam(value = "token",required = true) String token){
        Page<ViewWord> viewWords = null;
        viewWords = newWordService.viewForgetWords(request,searchType.trim(),sortType.trim(), page);
        if(viewWords == null){
            return ResultUtil.success("未查询到用户的遗忘生词，可能是您请求的参数[区分大小写]不在规定的取值中。");
        } else {
            return ResultUtil.success("获取用户的遗忘生词成功~", viewWords);
        }
    }


    @RequestMapping(value = "/deleteNewWords/api")
    @ResponseBody
    public Result deleteNewWords(HttpServletRequest request,
                                  @RequestParam(value = "id",required = true) String id,
                                  @RequestParam(value = "token",required = true) String token){
        return ResultUtil.error(ResultCode.FAIL, "[NewWordController.deleteNewWords] 接口暂未开发");
    }
}
