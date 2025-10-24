package ma.bouna_ab.chatbot_support.services;

import org.springframework.ai.chat.messages.Message;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatHistoryService {

    private final Map<String, List<Message>> chatHistory = new ConcurrentHashMap<>();

    public void addChat(String conversationId, Message  message){
        chatHistory.computeIfAbsent(conversationId, k -> new ArrayList<>()).add(message);
    }

    public List<Message> getChatHistory(String conversationId){
        return chatHistory.getOrDefault(conversationId, List.of());
    }
}
