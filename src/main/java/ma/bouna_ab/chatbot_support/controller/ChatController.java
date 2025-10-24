package ma.bouna_ab.chatbot_support.controller;

import lombok.AllArgsConstructor;
import ma.bouna_ab.chatbot_support.records.ChatRequest;
import ma.bouna_ab.chatbot_support.records.ChatResponse;
import ma.bouna_ab.chatbot_support.services.OllamaChatService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/ai")
public class ChatController {

    private final OllamaChatService chatService;

    public ChatController(OllamaChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody ChatRequest chatRequest) {
        String conversationId = (chatRequest.conversationId() == null || chatRequest.conversationId().isBlank())
                ? UUID.randomUUID().toString()
                : chatRequest.conversationId();

        String responseMessage = chatService.generateResponse(new ChatRequest(conversationId, chatRequest.message()));

        return new ChatResponse(conversationId, responseMessage);
    }
}
