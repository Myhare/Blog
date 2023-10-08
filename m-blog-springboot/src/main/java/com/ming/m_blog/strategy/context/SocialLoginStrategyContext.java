package com.ming.m_blog.strategy.context;

import com.ming.m_blog.dto.user.UserInfoDTO;
import com.ming.m_blog.enums.LoginTypeEnum;
import com.ming.m_blog.strategy.LoginStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 * 第三方登录策略上下文
 *
 * @author ming
 * @date 2021/07/28
 */
@Service
public class SocialLoginStrategyContext {

    @Autowired
    private Map<String, LoginStrategy> loginStrategyMap;

    /**
     * 执行第三方登录策略
     *executeLoginStrategy
     * @param data          数据
     * @param loginTypeEnum 登录枚举类型
     * @return {@link UserInfoDTO} 用户信息
     */
    public UserInfoDTO executeLoginStrategy(String data, LoginTypeEnum loginTypeEnum) {
        return loginStrategyMap.get(loginTypeEnum.getStrategy()).login(data);
    }

}
