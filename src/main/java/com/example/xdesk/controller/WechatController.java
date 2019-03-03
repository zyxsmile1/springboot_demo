package com.example.xdesk.controller;

import com.example.xdesk.config.WeChatConfig;
import com.example.xdesk.domain.JsonData;
import com.example.xdesk.domain.User;
import com.example.xdesk.domain.VideoOrder;
import com.example.xdesk.service.UserService;
import com.example.xdesk.service.VideoOrderService;
import com.example.xdesk.utils.JwtUtils;
import com.example.xdesk.utils.WXPayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;

@Controller
@RequestMapping("/api/v1/wechat")
public class WechatController {

    @Autowired
    private WeChatConfig weChatConfig;

    @Autowired
    private VideoOrderService videoOrderService;

    @Autowired
    private UserService userService;

    /**
     * 微信扫一扫登录的url
     * @param accessPage
     * @return
     * @throws UnsupportedEncodingException
     */
    @GetMapping("login_url")
    @ResponseBody
    public JsonData loginUrl(@RequestParam(value = "access_page",required = true) String accessPage) throws UnsupportedEncodingException {

        String redirectUrl = weChatConfig.getOpenRedirectUrl();
        String callbackUrl= URLEncoder.encode(redirectUrl,"GBK");
        String qrcodeUrl = String.format(weChatConfig.getOpenQrcodeUrl(), weChatConfig.getOpenAppid(), callbackUrl, accessPage);

        return JsonData.buildSuccess(qrcodeUrl);
    }


    /**
     * 微信扫描登录,回调地址
     * @param code
     * @param state
     * @param response
     * @throws IOException
     */
    @GetMapping("user/callback")
    public void wechatUserCallback(@RequestParam(value = "code",required = true) String code, String state, HttpServletResponse response) throws IOException {

        User user = userService.saveWeChatUser(code);
        if(user!=null){
            //生成Jwt
            String token= JwtUtils.geneJsonWebToken(user);
            // state 当前用户的页面地址，需要拼接 http://  这样才不会站内跳转
            response.sendRedirect(state+"?token="+token+"&head_img="+user.getHeadImg()+"&name="+ URLEncoder.encode(user.getName(),"UTF-8"));
        }
    }


    /**
     * 微信支付回调
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/order/callback")
    public void  orderCallback(HttpServletRequest request,HttpServletResponse response) throws Exception {

        InputStream inputStream = request.getInputStream();

        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

        StringBuffer sb=new StringBuffer();
        String line;
        while ((line=in.readLine())!=null){
             sb.append(line);
        }
        in.close();
        inputStream.close();

        Map<String, String> callbackMap = WXPayUtils.xmlToMap(sb.toString());

        SortedMap<String, String> sortedMap = WXPayUtils.getSortedMap(callbackMap);
        if(WXPayUtils.isCorrectSign(sortedMap,weChatConfig.getKey())){
            if("SUCCESS".equals(sortedMap.get("result_code"))){
                String outTradeNo = sortedMap.get("out_trade_no");
                VideoOrder dbVideoOrder = videoOrderService.findByOutTradeNo(outTradeNo);

                if(dbVideoOrder!=null&&dbVideoOrder.getState()==0){
                    VideoOrder videoOrder=new VideoOrder();

                    videoOrder.setOpenid(sortedMap.get("openid"));
                    videoOrder.setOutTradeNo(outTradeNo);
                    videoOrder.setNotifyTime(new Date());
                    videoOrder.setState(1);
                    int rows = videoOrderService.updateVideoOrderByOutTradeNo(videoOrder);
                    if(rows==1){
                        response.setContentType("text/xml");
                        response.getWriter().println("success");
                        return;
                    }
                }
            }
        }

        //处理失败
        response.setContentType("text/xml");
        response.getWriter().println("fail");
    }




}
