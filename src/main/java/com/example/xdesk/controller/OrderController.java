package com.example.xdesk.controller;

import com.example.xdesk.dto.VideoOrderDto;
import com.example.xdesk.service.VideoOrderService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 订单接口
 */
@RestController
@RequestMapping("/user/api/v1/order")
public class OrderController {

    @Autowired
    private VideoOrderService videoOrderService;

    @GetMapping("add")
    public void saveOrder(@RequestParam(value = "video_id",required = true) int videoId, HttpServletRequest request,
                              HttpServletResponse response) throws Exception {
        //String ipAddr = IpUtils.getIpAddr(request);
        //int userId = request.getAttribute("user_id");

        int userId = 1;    //临时写死的配置
        String ip = "120.25.1.43";
        VideoOrderDto videoOrderDto = new VideoOrderDto();
        videoOrderDto.setUserId(userId);
        videoOrderDto.setVideoId(videoId);
        videoOrderDto.setIp(ip);

        String codeUrl = videoOrderService.save(videoOrderDto);
        if(codeUrl == null) {
            throw new  NullPointerException();
        }
        //生成二维码配置
        Map<EncodeHintType,Object> hints=new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        hints.put(EncodeHintType.CHARACTER_SET,"UTF-8");


        BitMatrix bitMatrix = new MultiFormatWriter().encode(codeUrl, BarcodeFormat.QR_CODE, 400, 400, hints);
        OutputStream out = response.getOutputStream();

        MatrixToImageWriter.writeToStream(bitMatrix,"png",out);
    }
}
