package com.ming.m_blog;

import com.ming.m_blog.dto.UserListDTO;
import com.ming.m_blog.mapper.UserAuthMapper;
import com.ming.m_blog.service.UserInfoService;
import com.ming.m_blog.utils.HashUtils;
import com.ming.m_blog.utils.JwtUtil;
import com.ming.m_blog.vo.UserSimpInfoVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserTest {

    @Autowired
    UserAuthMapper userAuthMapper;

    @Autowired
    UserInfoService userInfoService;

    // 创建JWT
    @Test
    public void testCreateJWT(){
        System.out.println(JwtUtil.createJWT(Integer.toString(1)));
    }

    // 测试通过id查询用户权限
    @Test
    public void testSelectPowerById(){
        List<String> list = userAuthMapper.selectPowerByUserId("1");
        list.forEach(System.out::println);
    }

    // 测试通过用户id查询用户角色
    @Test
    public void testSelectRoleByUserId(){
        List<String> roles = userInfoService.getRoles("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJjOWE0MjYwMjc5MTg0NjdjODY3YWVjOTJmZGZjZmM2ZCIsInN1YiI6IjEiLCJpc3MiOiJzZyIsImlhdCI6MTY1OTMyMDA5NywiZXhwIjoxNjU5MzMwODk3fQ.9smFy1qXKBiqkQ68leCb-1AfrTNF5Ohk_TYPEq-UxAU");
        roles.forEach(System.out::println);
    }

    // 测试通过token查询用户简单信息
    @Test
    public void testSelectSimpUserInfo(){
        UserSimpInfoVO userSimpInfo = userInfoService.getUserSimpInfo("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJjMDQxYjBmMWU3YTk0YmU3OWM2NzkzNDFkZGMxM2VkMSIsInN1YiI6IjEiLCJpc3MiOiJzZyIsImlhdCI6MTY1OTM2OTkxMywiZXhwIjoxNjU5MzgwNzEzfQ.SbymE_Rr47IRQgtuXph1RgZxayfiMQfQRC9En_A8e44");
        System.out.println(userSimpInfo);
    }

    // 测试后台分页查询用户信息
    @Test
    public void testSelectUserList(){
        List<UserListDTO> userList = userInfoService.getUserList(1, 5, null);
        userList.forEach(System.out::println);
    }

    // 测试查询用户默认头像
    @Test
    public void getUserHandler(){
        String qqEmail = "1940307627@qq.com";
        String s = HashUtils.hashKeyForDisk(qqEmail);
        System.out.println(s);
    }

}
