package com.ming.m_blog.aspect;

import com.alibaba.fastjson.JSON;
import com.ming.m_blog.annotation.OptLog;
import com.ming.m_blog.dto.UserDetailDTO;
import com.ming.m_blog.mapper.OperationLogMapper;
import com.ming.m_blog.pojo.OperationLog;
import com.ming.m_blog.utils.IpUtils;
import com.ming.m_blog.utils.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

/**
 * AOP日志切面处理
 */
@Aspect
@Component
public class OptLogAspect {

    /**
     * 切入点声明,指定注解，在标记了注解的位置进行切入
     */
    @Pointcut("@annotation(com.ming.m_blog.annotation.OptLog)")
    public void optLogPointCut(){}

    @Autowired
    private OperationLogMapper operationLogMapper;

    /**
     * 方法执行完之后执行当前方法
     * @param joinPoint JoinPoint 对象封装了 Spring Aop中切面方法的信息,在切面方法中添加JoinPoint参数
     * @param result    切入方法的返回结果
     */
    @AfterReturning(value = "optLogPointCut()",returning = "result")
    public void saveOptLog(JoinPoint joinPoint, Object result){
        // 获取请求request信息
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String ipAddress = IpUtils.getIpAddress(request);
        String ipSource = IpUtils.getIpSource(ipAddress);
        // 获取封装了署名信息的对象,在该对象中可以获取到原始方法的各种属性
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取切点方法
        Method method = signature.getMethod();
        // 获取切点方法对应的注解
        OptLog optLogAnnotation = method.getAnnotation(OptLog.class);
        // Class declaringType = signature.getDeclaringType(); // 获取切点方法所在类的Class对象
        Api apiAnnotation = (Api) signature.getDeclaringType().getAnnotation(Api.class); // 获取类所在对象的注解
        ApiOperation apiOperationAnnotation = method.getAnnotation(ApiOperation.class);

        // 获取登录用户
        UserDetailDTO loginUser = UserUtils.getLoginUser();

        // 获取类名
        String className = joinPoint.getTarget().getClass().getName();
        // 方法名称
        String methodName = className + "." + method.getName();

        OperationLog operationLog = new OperationLog();
        operationLog.setOptModule(apiAnnotation.tags()[0]);     // 操作模块
        operationLog.setOptType(optLogAnnotation.optType());    // 操作类型
        operationLog.setOptUrl(request.getRequestURI()); // 请求uri
        operationLog.setOptMethod(methodName);    // 方法名称
        operationLog.setOptDesc(apiOperationAnnotation.value());  // 操作描述
        operationLog.setRequestParam(JSON.toJSONString(joinPoint.getArgs())); // 请求参数
        operationLog.setRequestMethod(request.getMethod()); // 请求方式
        operationLog.setResponseData(JSON.toJSONString(result));    // 响应结果
        operationLog.setUserId(loginUser.getUserId());  // 用户id
        operationLog.setNickname(loginUser.getNickname());  // 用户昵称
        operationLog.setIpAddress(ipAddress);
        operationLog.setIpSource(ipSource);

        operationLogMapper.insert(operationLog);
    }

}
