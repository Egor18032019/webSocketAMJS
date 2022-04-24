package com.amjs.generate.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;


@Getter
@Setter
//@Component
public class ChatMessage {
    private String algorithm;
    private MessageType type;
    private String content;
    private int length;
    private List<int[]> generateArray;
    private Set<Integer> sequenceAuto ;
    Map<Integer, Set<Integer>> sequenceGenerate;
    public enum MessageType {
        JOIN,
        GENERATE,
        AUTO
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "algorithm='" + algorithm + '\'' +
                ", type=" + type +
                ", content='" + content + '\'' +
                ", length=" + length +
                ", generateArray=" + generateArray +
                ", sequenceAuto=" + sequenceAuto +
                ", sequenceGenerate=" + sequenceGenerate +
                '}';
    }
}
