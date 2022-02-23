package vip.tuoyang.push.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vip.tuoyang.push.bean.entity.WechatAppletAccount;

import java.util.List;

@Repository
public interface WechatAppletAccountRepository extends JpaRepository<WechatAppletAccount, Integer> {

    List<WechatAppletAccount> findBySystemAccountInAndIsDelete(List<String> systemAccounts, Byte isDelete);

    @Modifying
    @Query(nativeQuery = true, value = "UPDATE wechat_applet_account SET is_delete = 1, update_time = NOW(), update_by = ?2 WHERE id IN (?1) AND is_delete = 0")
    int updateIsDelete(List<Integer> ids, String updateBy);
}