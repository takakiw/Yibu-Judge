package com.yibu.yibuJudge.interceptor;

import com.yibu.yibuJudge.constant.UserConstant;
import com.yibu.yibuJudge.utils.BaseContext;
import com.yibu.yibuJudge.utils.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

public class GlobalInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")){
            String token = authorization.substring(7);
            try{
                Map<String, Object> map = JWTUtil.parseJwt(token);
                request.setAttribute(UserConstant.ROLE, map.get(UserConstant.ROLE));
                BaseContext.setCurrentId((Long) map.get(UserConstant.USER_ID));
            }catch (Exception e){
                return true;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        BaseContext.removeCurrentId();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
