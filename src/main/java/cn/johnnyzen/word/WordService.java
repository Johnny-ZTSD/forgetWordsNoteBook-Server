package cn.johnnyzen.word;

import cn.johnnyzen.authority.AuthorityService;
import cn.johnnyzen.user.User;
import cn.johnnyzen.user.UserRepository;
import cn.johnnyzen.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

/**
 * @IDE: Created by IntelliJ IDEA.
 * @Author: 千千寰宇
 * @Date: 2018/11/1  18:07:47
 * @Description: ...
 */
@Service("wordService")
public class WordService {
    private static final Logger logger = Logger.getLogger(WordService.class.getName());

    //日志前缀字符串,方便通过日志定位程序
    private static String logPrefix = null;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private WordRepository wordRepository;

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
     *  算法
     *      0.判断该用户是否有权限进行该操作
     *          如果无权限，返回 0
     *      1.依据englishWord查询数据库该单词
     *          如果有该单词，则修改其释义
     *              返回 1
     *          如果无该单词，则新建word记录
     *              返回 2
     * @author johnny
     * @param request
     * @param englishWord
     * @param chineseTranslate
     * @param authorityCode
     */
    public int saveWordOfUser(HttpServletRequest request,
                                 String englishWord,
                                 String chineseTranslate,
                                 String authorityCode){
        logPrefix = "[WordService.saveWordOfUser] ";
        User user = null;
        Word word = null;

        user = userService.findOneByLoginUsersMap(request);
        boolean authority = false;
        String action = "/saveWordOfUser/api"; //操作URL的路径
        authority = authorityService.hasOperationAuthorityOfUser(user.getId(), authorityCode, action);
        if(!authority){// no authority of action
            logger.info(logPrefix + " this user" + user.toStringJustUsernameAndEmail() + " is no authority of operation<" + action + ">.");
            return 0;
        }
        word = wordRepository.findFirstByEnglishWord(englishWord.trim());
        if(word != null){
            word.setChineseTranslate(chineseTranslate.trim());
            wordRepository.save(word);
            logger.info(logPrefix + "update word<" + englishWord + "> in database success.");
            return 1;
        } else {
            word = new Word();
            word.setEnglishWord(englishWord);
            word.setChineseTranslate(chineseTranslate);
            wordRepository.save(word);
            logger.info(logPrefix + "add new word<" + englishWord + "> to database success.");
            return 2;
        }
    }
}
