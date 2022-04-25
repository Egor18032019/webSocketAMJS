package com.amjs.generate.controller;

import com.amjs.generate.Const;
import com.amjs.generate.model.ChatMessage;
import com.amjs.generate.utils.Generation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.*;

@Controller
public class ChatController {
    @Autowired
    Generation generation;
    @Autowired
    SimpMessagingTemplate template;

    // Из ТЗ
// Массив генерируется разово и не изменяется до следующего запроса генерации массива из WEB UI
    private List<int[]> generateArray;

    public void setGenerateArray(List<int[]> generateArray) {
        this.generateArray = generateArray;
    }

    public List<int[]> getGenerateArray() {
        return generateArray;
    }

    @MessageMapping("/chat.addUser")
    //сообщение, направленное по адресу/app/chat.addUser будет перенаправлено в метод addUser().
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        System.out.println(" !!!!!   ChatController addUser  " + chatMessage.getLength());
        int lengthArray = chatMessage.getLength();
        List<int[]> generateArray = generation.generate(lengthArray);
        chatMessage.setGenerateArray(generateArray);
        setGenerateArray(generateArray);
        // Add header in web socket session
        headerAccessor.getSessionAttributes().put("length", chatMessage.getLength());
        return chatMessage;
    }

    @MessageMapping("/chat.sendTypeAndLength")  //аннотация гарантирует, что если сообщение отправляется на
    @SendTo("/topic/public") // куда будет послано итоговое сообщение
    public ChatMessage sendTypeAndLength(@Payload ChatMessage chatMessage) throws InterruptedException {
        if (chatMessage.getAlgorithm().equals(Const.GENERATE)) {
//            При нажатии на кнопку «СГЕНЕРИРОВАТЬ»
//            выдается 5 последовательностей чисел
//            по 6 случайных чисел из каждого массива.
//            Последовательность чисел должна исключать дубли.
            Map<Integer, Set<Integer>> sequenceGenerate = generation.creatGenerateSequence(generateArray);
            chatMessage.setSequenceGenerate(sequenceGenerate);
            chatMessage.setContent(sequenceGenerate.toString());
            return chatMessage;
        } else {
//            - При нажатии на кнопку «АВТО»
//            последовательность генерируется каждые 10 секунд и передается в UI.
//            5 раз же ?? или Сколько ?
            for (int i = 0; i < Const.COUNTSEQUENCE; i++) {
                Set<Integer> sequenceAuto = generation.creatAutoSequence(generateArray);
                chatMessage.setSequenceAuto(sequenceAuto);
                template.convertAndSend("/topic/public", chatMessage);
                Thread.sleep(10000);
            }
            return null;
        }

    }

    @MessageMapping("/chat.sendMessage")  //аннотация гарантирует, что если сообщение отправляется на
    // /app + /chat.sendMessage то  будет вызван метод sendMessage
    @SendTo("/topic/public") // куда будет послано итоговое сообщение
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        chatMessage.setContent("Задача сгенерировать 5 последовательностей простых чисел длинной " + chatMessage.getLength());
        return chatMessage;
    }
}