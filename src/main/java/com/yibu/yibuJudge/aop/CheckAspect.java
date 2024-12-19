package com.yibu.yibuJudge.aop;

import com.yibu.yibuJudge.constant.UserConstant;
import com.yibu.yibuJudge.exceptions.BaseException;
import com.yibu.yibuJudge.utils.BaseContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class CheckAspect {

    public final HttpServletRequest request;

    public CheckAspect(HttpServletRequest request) {
        this.request = request;
    }

    @Pointcut("@annotation(com.yibu.yibuJudge.annotation.CheckAuth)")
    public void cut(){}

    @Before("cut()")
    public void before(){
        Integer role =(Integer) request.getAttribute(UserConstant.ROLE);
        if(role == null || role.compareTo(UserConstant.ROLE_ADMIN) != 0){
            log.info("userId:{},role:{}", BaseContext.getCurrentId(), role);
            throw new BaseException("权限不足");
        }
    }
}
