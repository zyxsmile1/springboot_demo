package com.example.xdesk.service.impl;


import com.example.xdesk.config.WeChatConfig;
import com.example.xdesk.domain.User;
import com.example.xdesk.service.UserService;
import com.example.xdesk.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private WeChatConfig weChatConfig;

    @Override
    public User saveWeChatUser(String code) {

        /**获取访问token的Url*/
        String accessTokenUrl = String.format(weChatConfig.getOpenAccessTokenUrl(), weChatConfig.getOpenAppid(), weChatConfig.getAppsecret(), code);

        //获取access_token
        Map<String ,Object> baseMap =  HttpUtils.doGet(accessTokenUrl);
        if(baseMap==null&& baseMap.isEmpty()){return null}
        String accessToken = (String)baseMap.get("access_token");
        String openId  = (String) baseMap.get("openid");

        //获取用户信息
        String userInfoUrl = String.format(weChatConfig.getOpenUserInfoUrl(), accessToken, openId);

        Map<String, Object> userMap = HttpUtils.doGet(userInfoUrl);
        if(userMap==null&&userMap.isEmpty()){return null;}

        String nickname = (String)baseUserMap.get("nickname");

        Double sexTemp  = (Double) baseUserMap.get("sex");
        int sex = sexTemp.intValue();
        String province = (String)baseUserMap.get("province");
        String city = (String)baseUserMap.get("city");
        String country = (String)baseUserMap.get("country");
        String headimgurl = (String)baseUserMap.get("headimgurl");
        StringBuilder sb = new StringBuilder(country).append("||").append(province).append("||").append(city);
        String finalAddress = sb.toString();

        try {
            nickname = new String(nickname.getBytes("ISO-8859-1"), "UTF-8");
            finalAddress=new String(finalAddress.getBytes("ISO-8859-1"),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        User user = new User();
        user.setName(nickname);
        user.setHeadImg(headimgurl);
        user.setCity(finalAddress);
        user.setOpenid(openId);
        user.setSex(sex);
        user.setCreateTime(new Date());
        return user;

    }
}
