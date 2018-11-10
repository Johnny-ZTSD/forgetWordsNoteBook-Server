package cn.johnnyzen.authority;

import cn.johnnyzen.newWord.NewWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

/**
 * @IDE: Created by IntelliJ IDEA.
 * @Author: 千千寰宇
 * @Date: 2018/11/5  18:03:19
 * @Description: ...
 */
@Service("authorityService")
public class AuthorityService {
    private static final Logger logger = Logger.getLogger(AuthorityService.class.getName());

    //日志前缀字符串,方便通过日志定位程序
    private static String logPrefix = null;

    @Autowired
    private AuthorityRepository authorityRepository;

    /**
     * 指定用户是否有操作某些某数Action的权限
     *  0.不是对全部action的判断，是对一些需要授权特权才能执行的action
     *      目前仅有一个这样的action:[WordController]:"/saveWordOfUser/api"
     *  1.依据用户UserId和操作权限码authorityCode
     *  2.Controller层的数据接口操作名命名中不得含有字符'-'，否则一定会计算错误
     * @author johnny
     * @param userId
     * @param authorityCode 操作权限码
     * @param action Controller层的数据接口的URL映射路径，形如"/saveNewWordOfUser/api"
     */
    public boolean hasOperationAuthorityOfUser(Integer userId, String authorityCode, String action){
        logPrefix = "[AuthorityService.hasOperationAuthorityOfUser] ";
        String dbAuthrityType = null;//数据库中的权限类型字段
        String []authorities = null; //所有的操作权限
        dbAuthrityType = authorityRepository.findAuthorityTypeOfUserByUserIdAndAndAuthorityCode(userId,authorityCode).trim();
        if(dbAuthrityType == null){//authorities is no record
            logger.info(logPrefix + "User(" + userId + ")'s authorityType is empty in database,and return null;the reason maybe be that this user's authorityCode is error or that this user is hasn't any authorities.");
            return false;
        } else {
            if(dbAuthrityType.equals("ALL")){//all authorities
                logger.info(logPrefix + "User(" + userId + ")'s authorityType is ALL.");
                return true;
            } else if(dbAuthrityType.equalsIgnoreCase("NULL")){//authorities is NULL
                logger.info(logPrefix + "User(" + userId + ")'s authorityType is NULL.");
                return false;
            } else {//some authorities
                authorities = dbAuthrityType.split("-");
                for(int i=0;i<authorities.length;i++){
                    if(action.equals(authorities[i])){
                        return true;
                    }
                }
                return false;
            }
        }
        }
}
