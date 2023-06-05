package com.ming.m_blog.strategy.impl;

import com.alibaba.fastjson.JSON;
import com.ming.m_blog.config.QQConfigProperties;
import com.ming.m_blog.constant.SocialLoginConst;
import com.ming.m_blog.dto.oauth.QQTokenDTO;
import com.ming.m_blog.dto.oauth.QQUserInfoDTO;
import com.ming.m_blog.dto.oauth.SocialTokenDTO;
import com.ming.m_blog.dto.oauth.SocialUserInfoDTO;
import com.ming.m_blog.enums.LoginTypeEnum;
import com.ming.m_blog.enums.StatusCodeEnum;
import com.ming.m_blog.exception.ReRuntimeException;
import com.ming.m_blog.utils.CommonUtils;
import com.ming.m_blog.vo.QQLoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service("qqLoginStrategyImpl")
public class QQLoginStrategyImpl extends AbstractLoginStrategyImpl{

    @Autowired
    private QQConfigProperties qqConfigProperties;
    @Autowired
    private RestTemplate restTemplate;

    // 获取token信息
    @Override
    public SocialTokenDTO getSocialTokenDTO(String data) {
        QQLoginVO qqLoginVO = JSON.parseObject(data, QQLoginVO.class);
        checkToken(qqLoginVO);
        return SocialTokenDTO.builder()
                .openId(qqLoginVO.getOpenId())
                .accessToken(qqLoginVO.getAccessToken())
                .loginType(LoginTypeEnum.QQ.getType())
                .build();
    }

    // 获取用户信息
    @Override
    public SocialUserInfoDTO socialUserInfoDTO(SocialTokenDTO socialTokenDTO) {
        Map<String,String> paramMap = new HashMap<>(3);
        paramMap.put(SocialLoginConst.OAUTH_CONSUMER_KEY,qqConfigProperties.getAppId());
        paramMap.put(SocialLoginConst.ACCESS_TOKEN,socialTokenDTO.getAccessToken());
        paramMap.put(SocialLoginConst.QQ_OPEN_ID,socialTokenDTO.getOpenId());
        String responseData = restTemplate.getForObject(qqConfigProperties.getUserInfoUrl(), String.class, paramMap);
        QQUserInfoDTO qqUserInfoDTO = JSON.parseObject(responseData, QQUserInfoDTO.class);
        return SocialUserInfoDTO.builder()
                .avatar(Objects.requireNonNull(qqUserInfoDTO).getFigureurl_qq_1())
                .nickname(qqUserInfoDTO.getNickname())
                .build();
    }

    /**
     * 校验QQ登录信息
     * @param qqLoginVO QQ登录信息
     */
    private void checkToken(QQLoginVO qqLoginVO){
        Map<String,String> data = new HashMap<>(1);
        data.put(SocialLoginConst.ACCESS_TOKEN,qqLoginVO.getAccessToken());
        try {
            String responseDate = restTemplate.getForObject(qqConfigProperties.getCheckTokenUrl(), String.class, data);
            // System.out.println("QQLoginStrategyImpl.checkToken检测到的返回数据-->"+responseDate);
            QQTokenDTO qqTokenDTO = JSON.parseObject(CommonUtils.getBracketsContent(Objects.requireNonNull(responseDate)), QQTokenDTO.class);
            if (!qqLoginVO.getOpenId().equals(qqTokenDTO.getOpenid())){
                throw new ReRuntimeException(StatusCodeEnum.QQ_LOGIN_ERROR.getDesc());
            }
        } catch (Exception e) {
            throw new ReRuntimeException(StatusCodeEnum.QQ_LOGIN_ERROR.getDesc());
        }
    }
}
