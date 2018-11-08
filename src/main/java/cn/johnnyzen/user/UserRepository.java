package cn.johnnyzen.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @IDE: Created by IntelliJ IDEA.
 * @Author: 千千寰宇
 * @Date: 2018/10/6  23:40:59
 * @Description: ...
 */

public interface UserRepository extends JpaRepository<User, Integer>{
    public User findOneByUsernameAndPassword(String username, String password);

    @Query("select u from User u where u.activateCode = :activateCode")
    public User findDistinctByActivateCode(@Param("activateCode") String activateCode);

    public User findOneByEmailAndPassword(String email, String password);

    public List<User> findByEmailOrUsername(String email, String username);

    public User findOneByEmail(String email);

    public User findOneByUsername(String username);

    /**
     * 是否存在该用户名
     * 如果存在，必大于1
     * 不存在，等于0
     */
    @Query(value = "select count(*) from tb_user WHERE tb_user.username = :username",nativeQuery = true)
    public int isExistsThisUsername(@Param("username")String username);

    /**
     * 是否存在该邮箱
     * 如果存在，必大于1
     * 不存在，等于0
     */
    @Query(value = "select count(*) from tb_user WHERE tb_user.email = :email",nativeQuery = true)
    public int isExistsThisEmail(@Param("email")String email);
}
