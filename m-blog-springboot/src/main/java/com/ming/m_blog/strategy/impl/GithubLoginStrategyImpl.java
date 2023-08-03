package com.ming.m_blog.strategy.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.ming.m_blog.config.GithubConfigProperties;
import com.ming.m_blog.dto.oauth.*;
import com.ming.m_blog.enums.LoginTypeEnum;
import com.ming.m_blog.exception.ReRuntimeException;
import com.ming.m_blog.vo.GithubLoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 第三方GitHub登录
 */
@Service("githubLoginStrategyImpl")
public class GithubLoginStrategyImpl extends AbstractLoginStrategyImpl{

    @Autowired
    private GithubConfigProperties githubConfigProperties;


    /**
     * 获取token信息
     * @param data
     * @return
     */
    @Override
    public SocialTokenDTO getSocialTokenDTO(String data) {
        GithubLoginVO githubLoginVO = JSONUtil.toBean(data, GithubLoginVO.class);
        GithubGetTokenRequest githubGetTokenRequest = GithubGetTokenRequest.builder()
                .client_id(githubConfigProperties.getClientId())
                .client_secret(githubConfigProperties.getClientSecret())
                .code(githubLoginVO.getCode())
                .build();
        HttpResponse response = HttpRequest.post(githubConfigProperties.getGetTokenUrl())
                .header("Accept", "application/json")
                .body(JSONUtil.toJsonStr(githubGetTokenRequest))
                // .setHttpProxy("127.0.0.1", 7890)
                .execute();
        GithubGetTokenResponse tokenResponse = JSONUtil.toBean(response.body(), GithubGetTokenResponse.class);
        if (StringUtils.isEmpty(tokenResponse.getAccess_token())){
            throw new ReRuntimeException("请求token失败");
        }
        return SocialTokenDTO.builder()
                .accessToken(tokenResponse.getAccess_token())
                .loginType(LoginTypeEnum.GIT_HUB.getType())
                .build();
    }

    @Override
    public SocialUserInfoDTO socialUserInfoDTO(SocialTokenDTO socialTokenDTO) {
        HttpResponse response = HttpRequest.get(githubConfigProperties.getGetUserUrl())
                .header("Authorization", "Bearer " + socialTokenDTO.getAccessToken())
                // .setHttpProxy("127.0.0.1", 7890)
                .execute();
        GithubUserResponse githubUserResponse = JSONUtil.toBean(response.body(), GithubUserResponse.class);
        return SocialUserInfoDTO.builder()
                .id(githubUserResponse.getId())
                .nickname(githubUserResponse.getLogin())
                .avatar(githubUserResponse.getAvatar_url())
                .build();
    }
}
