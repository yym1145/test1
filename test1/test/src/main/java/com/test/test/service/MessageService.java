package com.test.test.service;



import com.test.test.mongoDB.Message;

import java.util.List;

/**
 * @Author: Illya
 * @Date: 2024/9/3 下午5:38
 */
public interface MessageService {
    List<Message> getMessage(Long currentId);

    Object deleteMessage(List<String> ids);

    Object readMessage(List<String> ids);

    Object lastMessage(List<String> ids);
}
