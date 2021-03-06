package cn.johnnyzen.word;

import cn.johnnyzen.authority.AuthorityService;
import cn.johnnyzen.newWord.NewWordService;
import cn.johnnyzen.user.User;
import cn.johnnyzen.user.UserService;
import cn.johnnyzen.util.reuslt.Result;
import cn.johnnyzen.util.reuslt.ResultCode;
import cn.johnnyzen.util.reuslt.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

/**
 * @IDE: Created by IntelliJ IDEA.
 * @Author: 千千寰宇
 * @Date: 2018/11/5  22:08:37
 * @Description: ...
 */
@Controller("wordController")
public class WordController {
    private static final Logger logger = Logger.getLogger(WordController.class.getName());

    //日志前缀字符串,方便通过日志定位程序
    private static String logPrefix = null;

    @Autowired
    private WordService wordService;

    /**
     * 新增用户自定义的单词/词组
     *  场景：由于用户的特别需要，想增加一些用户容易忘记的词组和意思等
     *  区别：
     *      与saveNewWord相比，saveNewWord新增生词的单词来源：
     *          数据库已存储的单词翻译记录或者第三方翻译接口的翻译
     *      与saveNewWord相比，saveWordOfUser：
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
    @RequestMapping(value = "/saveWordOfUser/api")
    @ResponseBody
    public Result saveWordOfUser(HttpServletRequest request,
                                    @RequestParam(value = "englishWord",required = true)String englishWord,
                                    @RequestParam(value = "chineseTranslate",required = true)String chineseTranslate,
                                    @RequestParam(value = "authorityCode",required = true)String authorityCode,
                                    @RequestParam(value = "token",required = true) String token
    ){
        logPrefix = "[WordController.saveWordOfUser] ";
        int handle = wordService.saveWordOfUser(request, englishWord.trim(), chineseTranslate.trim(), authorityCode.trim());
        logger.info(logPrefix + "start exetuting...");
        if(handle == 0){
            return ResultUtil.error(ResultCode.FAIL, "无此权限，操作失败。");
        } else if(handle == 1){
            return ResultUtil.success("修改单词成功。");
        }  else if(handle == 2){
            return ResultUtil.success("新建单词成功。");
        }  else {
            return ResultUtil.error(ResultCode.INTERNAL_SERVER_ERROR, "未知错误。");
        }
    }

}
