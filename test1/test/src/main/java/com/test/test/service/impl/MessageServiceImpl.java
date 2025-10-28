package com.test.test.service.impl;


import com.test.test.mongoDB.Message;
import com.test.test.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @Author: Illya
 * @Date: 2024/9/3 下午5:39
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MessageServiceImpl implements MessageService {

    private final MongoTemplate mongoTemplate;

    /**
     * @param id 用户ID
     * @return 目标用户的所有消息
     */
    @Override
    public List<Message> getMessage(Long id) {
        Query selectQuery = new Query(Criteria.where("targetUserId").is(id));
        List<Message> messages = mongoTemplate.find(selectQuery, Message.class);
        List<String> messageIds = new ArrayList<>();
        for (Message message : messages) {
            messageIds.add(message.getId());
        }
        Query updateQuery = new Query(Criteria.where("_id").in(messageIds).and("lastStatus").is(true));
        Update update = new Update().set("lastStatus", false);
        mongoTemplate.updateMulti(updateQuery, update, Message.class);
        return messages;
    }

    /**
     * @param ids 需要删除的消息ID列表
     * @return 删除数量
     */
    @Override
    public Object deleteMessage(List<String> ids) {
        Query query = new Query(Criteria.where("_id").in(ids));
        return mongoTemplate.remove(query, Message.class);
    }

    /**
     * @param ids 需要修改已读状态的所有消息ID列表
     * @return 返回修改数量
     */
    @Override
    public Object readMessage(List<String> ids) {
        Query updateQuery = new Query(Criteria.where("_id").in(ids).and("readStatus").is(false));
        Update update = new Update().set("readStatus", true);
        return mongoTemplate.updateMulti(updateQuery, update, Message.class);
    }

    /**
     * @param ids 需要修改是否为新消息状态的消息ID
     * @return 修改数量
     */
    @Override
    public Object lastMessage(List<String> ids) {
        Query updateQuery = new Query(Criteria.where("_id").in(ids));
        Update update = new Update().set("lastStatus", false);
        return mongoTemplate.updateMulti(updateQuery, update, Message.class);
    }

    /**
     * 每天00:00清除三十天前的消息
     */
    @Scheduled(cron = "0 0 0 * * *")
    private void clear() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, -30);
        Query clearQuery = new Query(Criteria.where("sendTime").lt(calendar.getTime()));
        mongoTemplate.remove(clearQuery, Message.class);
    }
}
