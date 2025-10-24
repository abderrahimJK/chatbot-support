package ma.bouna_ab.chatbot_support.services;

import lombok.AllArgsConstructor;
import ma.bouna_ab.chatbot_support.records.ChatRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OllamaChatService {


    private final ChatClient chatClient;
    private final ChatHistoryService chatHistoryService;
    String defaultSystemPrompt = """
                Your are a useful AI assistant, your responsibility is provide users questions
                about a variety of topics.
                When answering a question, always greet first and state your name as JavaChat
                When unsure about the answer, simply state that you don´t know.
                Always answer in English.
                """;

    public OllamaChatService(ChatClient.Builder chatClientBuilder, ChatHistoryService chatHistoryService){
        this.chatClient = chatClientBuilder.defaultSystem(defaultSystemPrompt).build();
        this.chatHistoryService = chatHistoryService;
    }

    public String  generateResponse(ChatRequest chatRequest){

        List<Message> history = new ArrayList<>(chatHistoryService.getChatHistory(chatRequest.conversationId()));

        UserMessage userMessage = new UserMessage(chatRequest.message());
        history.add(userMessage);

        chatHistoryService.addChat(chatRequest.conversationId(), userMessage);

        String aiMessageContent = chatClient.prompt()
                .messages(history)
                .call()
                .content();

        AssistantMessage assistantMessage = new AssistantMessage(aiMessageContent);
        chatHistoryService.addChat(chatRequest.conversationId(), assistantMessage);

        return aiMessageContent;
    }
}
