package com.example.xdesk.mapper;

import com.example.xdesk.domain.VideoOrder;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface VideoOrderMapper {


    /**
     * 保存订单
     * @param videoOrder
     * @return
     */
    @Insert("INSERT INTO `video_order` (`openid`, `out_trade_no`, `state`, `create_time`," +
           " `notify_time`, `total_fee`, `nickname`, `head_img`, `video_id`, `video_title`,"  +
            " `video_img`, `user_id`, `ip`, `del`)"  +
           "VALUES" +
            "(#{openid},#{outTradeNo},#{state},#{createTime},#{notifyTime},#{totalFee},"  +
            "#{nickname},#{headImg},#{videoId},#{videoTitle},#{videoImg},#{userId},#{ip},#{del});")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    int insert(VideoOrder videoOrder);

    /**
     * 根据主键id查找订单
     * @param id
     * @return
     */
    @Select("select * from video_order where id=#{order_id} and del=0")
    VideoOrder findById(@Param("order_id") int id);


    /**
     * 根据交易订单号查询订单对象
     * @param outTradeNO
     * @return
     */
    @Select("select * from video_order where out_trade_no=#{out_trade_no} and del=0")
    VideoOrder findByOutTradeNO(@Param("out_trade_no") String outTradeNo);

    /**
     * 逻辑删除订单
     * @param id
     * @param userId
     * @return
     */
    @Update("update video_order ser del=0 where id=#{id} adn user_id=#{userId}")
    int del(@Param("id") int id,@Param("userId") int userId);

    /**
     * 查找我的全部订单
     * @param userId
     * @return
     */
    @Select("select * from video_order where user_id=#{userId}")
    List<VideoOrder> findMyOrderList(int userId);

    /**
     * 根据订单流水号更新
     * @param videoOrder
     * @return
     */
    @Update("update video_order set state=#{state},notify_time=#{notifyTime},openid=#{openid}"+
           "where out_trade_no=#{outTradeNo} and state=0 and del=0")
    int updateVideoOrderByOutTradeNo(VideoOrder videoOrder);

}
