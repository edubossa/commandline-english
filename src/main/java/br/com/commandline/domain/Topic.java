package br.com.commandline.domain;

import java.util.List;

/**
 * Created by wallace on 06/04/17.
 */
public class Topic {

    private String topic;
    private List<Question> list;

    public Topic() {
    }

    public Topic(String topic, List<Question> list) {
        this.topic = topic;
        this.list = list;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public List<Question> getList() {
        return list;
    }

    public void setList(List<Question> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "Topic{" +
                "topic='" + topic + '\'' +
                ", list=" + list +
                '}';
    }
}
