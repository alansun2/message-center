package vip.tuoyang.push.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import vip.tuoyang.push.bean.entity.WechatPublicInfo;

import java.util.List;

public interface WechatPublicInfoRepository extends JpaRepository<WechatPublicInfo, Integer> {

    List<WechatPublicInfo> findByIsDelete(Byte isDelete);
}