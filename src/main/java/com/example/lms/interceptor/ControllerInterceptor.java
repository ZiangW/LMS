package com.example.lms.interceptor;

import com.example.lms.vo.ResponseVo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.alibaba.fastjson.JSON;


import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.lms.constants.GlobalConstants.IP_SEPARATOR;
import static com.example.lms.constants.GlobalConstants.SESSION_TOKEN_ID;
import static com.example.lms.enums.RespEnum.PARAM_ERROR;
import static com.example.lms.enums.RespEnum.SERVICE_ERROR;

/**
 * @author 王子昂
 * @date 5/21/21
 * @description The type Controller interceptor.
 */
@Aspect
@Component
@Slf4j
public class ControllerInterceptor {

    @Autowired
    private Validator validator;
    /**
     * ip后缀
     */
    private String ipSuffix;

    @Around(value = "execution(public * com.example.lms.controller.*.*(..))")
    public Object aroundMethod(ProceedingJoinPoint proceedingJoinPoint) {
        Object res;
        MDC.put(SESSION_TOKEN_ID, ipSuffix + UUID.randomUUID().toString().replace("-", "").toUpperCase());
        long start = System.currentTimeMillis();
        String url = this.getRequestUrl();
        log.info("请求url为{}，请求参数为{}", url, JSON.toJSON(proceedingJoinPoint.getArgs()));
        try {
            String error = validateParam(proceedingJoinPoint.getArgs()).toString();
            return error.isEmpty() ? proceedingJoinPoint.proceed(): ResponseVo.getErrorResp(PARAM_ERROR.getErrno(), error);
        } catch (Throwable throwable) {
            res = this.warpException(throwable);
        } finally {
            log.info("请求耗时总共为：{}ms", System.currentTimeMillis() - start);
        }
        log.info("请求结果为:{}", res);
        MDC.remove(SESSION_TOKEN_ID);

        return res;
    }

    /**
     * 获取请求的接口地址
     * @return 请求url
     */
    private String getRequestUrl(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            return request.getRequestURI();
        }

        return "/default";
    }

    /**
     * 对参数进行基本校验
     * 比如是否为空、参数大小值范围
     * @param  args 参数数组
     * @return
     */
    private Object validateParam(Object[] args){
        StringBuilder errorBuilder = new StringBuilder();

        for (Object arg : args) {
            Set<ConstraintViolation<Object>> tempSet = validator.validate(arg);
            errorBuilder.append(tempSet.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(",")));
        }

        return errorBuilder;
    }

    /**
     * 接口处理过程中的异常包装
     * @param throwable 异常
     * @return
     */
    private Object warpException(Throwable throwable){
        log.error("请求过程发生异常：", throwable);

        int errno = SERVICE_ERROR.getErrno();

        return ResponseVo.getErrorResp(errno, throwable.getMessage());
    }

    @PostConstruct
    public void init(){
        String  host;
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.error("ip获取失败，异常为：", e);
            host = "0000.0000.0000.0000";
        }

        //增加服务器ip最后一段，便于定位节点，加快排查问题
        ipSuffix = host.substring(host.lastIndexOf(IP_SEPARATOR) + 1);
    }

}
