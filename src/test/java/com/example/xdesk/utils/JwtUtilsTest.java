package com.example.xdesk.utils;

import com.example.xdesk.domain.User;
import io.jsonwebtoken.Claims;
import org.junit.Test;

public class JwtUtilsTest {
    @Test
    public void geneJsonWebToken() throws Exception {
        User user=new User();
        user.setId(1);
        user.setName("onehee");
        user.setHeadImg("www.xwr.com");
        String token = JwtUtils.geneJsonWebToken(user);
        System.out.println(token);

    }

    @Test
    public void checkJWT() throws Exception {

        String token="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJvbmVoZWUiLCJpZCI6MSwibmFtZSI6Im9uZWhlZSIsImltZyI6Ind3dy54d3IuY29tIiwiaWF0IjoxNTUxMTQ1MDA3LCJleHAiOjE1NTE3NDk4MDd9.Pi_9v2ZQ4MdakuAq2sHUTkGXQyO6B-7LUROCR6n3n8Y";
        Claims claims = JwtUtils.checkJWT(token);
        Integer id = (Integer) claims.get("id");
        String name = (String)claims.get("name");
        String  img =(String) claims.get("img");
        System.out.println(id);
        System.out.println(name);
        System.out.println(img);


    }

}