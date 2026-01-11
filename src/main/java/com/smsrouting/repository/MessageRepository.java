package com.smsrouting.repository;

import com.smsrouting.model.Message;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class MessageRepository {

    /*
        - concurrent hashmap will store messages against their id with built-in thread-safety
        - I'm not explicitly using a hash set as it's not thread safe - hence `newKeySet()`
    */
    private final Map<String, Message> messages = new ConcurrentHashMap<>();
    private final Set<String> optedOutNumbers = ConcurrentHashMap.newKeySet();

    public void save(Message message) {
        messages.put(message.getId(), message);
    }

    public Optional<Message> findMessageById(String id) {
        return Optional.ofNullable(messages.get(id));
    }

    public void optOut(String phoneNumber) {
        optedOutNumbers.add(phoneNumber);
    }

    public boolean isOptedOut(String phoneNumber) {
        return optedOutNumbers.contains(phoneNumber);
    }
}
