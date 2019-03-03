package com.example.xdesk.service.impl;


import com.example.xdesk.config.WeChatConfig;
import com.example.xdesk.domain.User;
import com.example.xdesk.domain.Video;
import com.example.xdesk.domain.VideoOrder;
import com.example.xdesk.dto.VideoOrderDto;
import com.example.xdesk.mapper.UserMapper;
import com.example.xdesk.mapper.VideoMapper;
import com.example.xdesk.mapper.VideoOrderMapper;
import com.example.xdesk.service.VideoOrderService;
import com.example.xdesk.utils.CommonUtils;
import com.example.xdesk.utils.HttpUtils;
import com.example.xdesk.utils.WXPayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@Service
public class VideoOrderServiceImpl implements VideoOrderService {

    @Autowired
    private WeChatConfig weChatConfig;

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private VideoOrderMapper videoOrderMapper;

    @Autowired
    private UserMapper userMapper;


    @Transactional(propagation = Propagation.REQUIRED)
    public String save(VideoOrderDto videoOrderDto) throws Exception {

        //获取视频
        Video video = videoMapper.findById(videoOrderDto.getVideoId());
        //获取用户信息
        User user = userMapper.findByid(videoOrderDto.getUserId());

        //生t成订单
        VideoOrder videoOrder=new VideoOrder();
        videoOrder.setTotalFee(video.getPrice());
        videoOrder.setVideoImg(video.getCoverImg());
        videoOrder.setVideoTitle(video.getTitle());
        videoOrder.setCreateTime(new Date());
        videoOrder.setVideoId(video.getId());
        videoOrder.setState(0);
        videoOrder.setUserId(user.getId());
        videoOrder.setHeadImg(user.getHeadImg());
        videoOrder.setNickname(user.getName());

        videoOrder.setDel(0);
        videoOrder.setIp(videoOrderDto.getIp());
        videoOrderMapper.insert(videoOrder);

        String codeUrl = unifiedOrder(videoOrder);

        return codeUrl;
    }

    /**
     *统一下单
     * @param videoOrder
     * @return
     * @throws Exception
     */
    private String unifiedOrder(VideoOrder videoOrder) throws Exception {

        //生成签名
        SortedMap<String,String> params = new TreeMap<>();
        params.put("appid",weChatConfig.getAppId());
        params.put("mch_id", weChatConfig.getMchId());
        params.put("nonce_str", CommonUtils.generateUUID());
        params.put("body",videoOrder.getVideoTitle());
        params.put("out_trade_no",videoOrder.getOutTradeNo());
        params.put("total_fee",videoOrder.getTotalFee().toString());
        params.put("spbill_create_ip",videoOrder.getIp());
        params.put("notify_url",weChatConfig.getPayCallbackUrl());
        params.put("trade_type","NATIVE");

        //sign签名
        String sign = WXPayUtils.createSign(params, weChatConfig.getKey());
        params.put("sign",sign);

        //map转xml
        String payXml = WXPayUtils.mapToXml(params);

        System.out.println(payXml);
        //统一下单
        String orderStr = HttpUtils.doPost(WeChatConfig.getUnifiedOrderUrl(),payXml,4000);
        if(null == orderStr) {
            return null;
        }

        Map<String, String> unifiedOrderMap =  WXPayUtils.xmlToMap(orderStr);
        System.out.println(unifiedOrderMap.toString());
        if(unifiedOrderMap != null) {
            return unifiedOrderMap.get("code_url");
        }

        return null;
    }

    public VideoOrder findByOutTradeNo(String outTradeNo) {
       return videoOrderMapper.findByOutTradeNO(outTradeNo);
    }

    public int updateVideoOrderByOutTradeNo(VideoOrder videoOrder){
        return videoOrderMapper.updateVideoOrderByOutTradeNo(videoOrder);

    }


}
