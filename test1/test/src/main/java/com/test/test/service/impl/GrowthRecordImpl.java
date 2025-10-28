package com.test.test.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.test.test.context.BaseContext;
import com.test.test.dto.AddGrowthRecordCommentDTO;
import com.test.test.entiy.GrowthRecordComment;
import com.test.test.enumerate.MessageSourceType;
import com.test.test.enumerate.MessageType;
import com.test.test.mapper.GrowthRecordMapper;
import com.test.test.mongoDB.Message;
import com.test.test.service.GrowthRecordService;
import com.test.test.special.SendUserData;
import com.test.test.special.messageData.reply.impl.GrowthRecordReplyMessageData;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;


@Service
@RequiredArgsConstructor
public class GrowthRecordImpl implements GrowthRecordService {

    private final GrowthRecordMapper growthRecordMapper;

    private final RabbitTemplate rabbitTemplate;

    private final MongoTemplate mongoTemplate;
    @Override
    public Long addGrowthRecordComment(AddGrowthRecordCommentDTO addGrowthRecordCommentDTO) throws IOException {
        GrowthRecordComment growthRecordComment=new GrowthRecordComment();
        growthRecordComment.setId(IdWorker.getId());
        growthRecordComment.setGrowthRecordId(addGrowthRecordCommentDTO.getGrowthRecordId());
        growthRecordComment.setComment(addGrowthRecordCommentDTO.getComment());
        growthRecordComment.setCreateUser(BaseContext.getCurrentUserId());
        growthRecordMapper.insert(growthRecordComment);
        Long targetUserId = growthRecordMapper.selectUserIdByGrowthRecordId(addGrowthRecordCommentDTO.getGrowthRecordId());
        Message<GrowthRecordReplyMessageData> message = new Message<>();
        message.setMessageData(GrowthRecordReplyMessageData.builder()
                .commentId(growthRecordComment.getId())
                .commentMessage(addGrowthRecordCommentDTO.getComment())
                .growthRecordId(addGrowthRecordCommentDTO.getGrowthRecordId())
                .build());
        message.setSendTime(new Date());
        message.setSendUserData(SendUserData.builder()
                .sendUserId(BaseContext.getCurrentUserId())
                .build());
        message.setTargetUserId(targetUserId);
        rabbitTemplate.convertAndSend("message.direct", "GrowthRecordReply", JSON.toJSONString(message));
        return growthRecordComment.getId();
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "GrowthRecordReply.message"),
            exchange = @Exchange(value = "message.direct"),
            key = {"GrowthRecordReply"}
    ))
    public void GrowthRecordReplyMessageListener(String msg) {
        Message<GrowthRecordReplyMessageData> message = JSON.parseObject
                (msg, new TypeReference<Message<GrowthRecordReplyMessageData>>() {
        });
        message.setMessageSourceType(MessageSourceType.USER);
        message.setMessageType(MessageType.LIKES_RECEIVED);
        rabbitTemplate.convertAndSend("message.direct", "online", mongoTemplate.insert(message));
    }
}
