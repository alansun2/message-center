package vip.tuoyang.push.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.tuoyang.base.constants.BaseConstants;
import vip.tuoyang.push.bean.entity.WechatAppletAccount;
import vip.tuoyang.push.bean.request.RegisterWechatAppletDto;
import vip.tuoyang.push.repo.WechatAppletAccountRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author AlanSun
 * @date 2022/1/19 11:33
 */
@Slf4j
@Service
public class PushService {
    @Autowired
    private WechatAppletAccountRepository wechatAppletAccountRepository;

    /**
     * 注册小程序
     *
     * @param request {@link RegisterWechatAppletDto}
     */
    @Transactional(rollbackFor = Exception.class)
    public void registerWechatApplet(List<RegisterWechatAppletDto> request) {
        log.info("用户注册，param: [{}]", request);
        final LocalDateTime now = LocalDateTime.now();
        // 删除相同账号，在不同的微信账号之间登录
        final List<String> systemAccounts = request.stream().map(RegisterWechatAppletDto::getSystemAccount).collect(Collectors.toList());
        final List<WechatAppletAccount> existAccounts = wechatAppletAccountRepository.findBySystemAccountInAndIsDelete(systemAccounts, BaseConstants.IS_DELETE_0);
        final Map<String, String> accountOpenMapRe = request.stream().collect(Collectors.toMap(o -> o.getSystemAccount() + ":" + o.getAccountType(), RegisterWechatAppletDto::getOpenId));
        final List<Integer> delete2 = existAccounts.stream().filter(wechatAppletAccount -> {
            final String openId = accountOpenMapRe.get(wechatAppletAccount.getSystemAccount() + ":" + wechatAppletAccount.getAccountType());
            if (openId != null) {
                // 判断账号是否相等，如果不相等则删除
                return !wechatAppletAccount.getOpenId().equals(openId);
            }
            return false;
        }).map(WechatAppletAccount::getId).collect(Collectors.toList());
        if (!delete2.isEmpty()) {
            wechatAppletAccountRepository.updateIsDelete(delete2, "被挤掉");
        }

        final Set<String> key = existAccounts.stream().map(wechatAppletAccount -> wechatAppletAccount.getSystemAccount() + ":" + wechatAppletAccount.getAccountType() + ":" + wechatAppletAccount.getOpenId()).collect(Collectors.toSet());
        request.removeIf(registerWechatAppletDto -> key.contains(registerWechatAppletDto.getSystemAccount() + ":" + registerWechatAppletDto.getAccountType() + ":" + registerWechatAppletDto.getOpenId()));

        if (!request.isEmpty()) {
            final List<WechatAppletAccount> wechatAppletAccounts = request.stream().map(registerWechatAppletDto -> {
                WechatAppletAccount wechatAppletAccount = new WechatAppletAccount();
                wechatAppletAccount.setSystemAccount(registerWechatAppletDto.getSystemAccount());
                wechatAppletAccount.setAccountType(registerWechatAppletDto.getAccountType());
                wechatAppletAccount.setAvatar(registerWechatAppletDto.getAvatar());
                wechatAppletAccount.setNickName(registerWechatAppletDto.getNickName());
                wechatAppletAccount.setUnionId(registerWechatAppletDto.getUnionId());
                wechatAppletAccount.setOpenId(registerWechatAppletDto.getOpenId());
                wechatAppletAccount.setIsDelete(BaseConstants.IS_DELETE_0);
                wechatAppletAccount.setCreateTime(now);
                wechatAppletAccount.setCreateBy(BaseConstants.SYSTEM);
                return wechatAppletAccount;
            }).collect(Collectors.toList());
            wechatAppletAccountRepository.saveAll(wechatAppletAccounts);
        }
    }
}
