package com.amjs.generate.model;

import com.amjs.generate.Const;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;


@Getter
@Setter
public class ChatMessage {
    private String algorithm;
    private Const.MessageType type;
    private String content;
    private int length;
    private List<int[]> generateArray;
    private Set<Integer> sequenceAuto;
    Map<Integer, Set<Integer>> sequenceGenerate;


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
