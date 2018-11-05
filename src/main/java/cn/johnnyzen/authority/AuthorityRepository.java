package cn.johnnyzen.authority;

import cn.johnnyzen.newWord.NewWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuthorityRepository extends JpaRepository<Authority, Integer> {
//    @Query(value="select authority.authrityType from Authority authority where authority.user.id = :userId and authority.authorityCode = :authorityCode")
    @Query(value =
            "SELECT tb_authority.authority_type FROM tb_authority " +
            " WHERE tb_authority.fk_user_id = :userId and tb_authority.authority_code = :authorityCode",
            nativeQuery = true)
    public String findAuthorityTypeOfUserByUserIdAndAndAuthorityCode(@Param("userId") Integer userId,@Param("authorityCode") String authorityCode);
}
