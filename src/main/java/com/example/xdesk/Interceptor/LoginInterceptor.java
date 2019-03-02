package com.example.xdesk.Interceptor;

import com.example.xdesk.domain.JsonData;
import com.example.xdesk.utils.JwtUtils;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public class LoginInterceptor implements HandlerInterceptor{

    /**
     * 登录前进行拦截
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        if(token==null){
            token=request.getParameter("token");
        }

        if(token!=null){
            Claims claims = JwtUtils.checkJWT(token);
            if(claims!=null){
                Integer userId=(Integer)claims.get("id");
                String name=(String)claims.get("name");
                request.setAttribute("user_id",userId);
                request.setAttribute("name",name);
                return true;
            }
        }
        sendJsonMessage(response, JsonData.buildError("请登录"));
        return false;
    }

    public static  void sendJsonMessage(HttpServletResponse response,Object ojb) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer=response.getWriter();
        writer.print(new Gson().toJson(ojb));
        writer.close();
        response.flushBuffer();
    }
}
