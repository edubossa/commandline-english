package br.com.commandline.domain;

import java.util.List;

/**
 * Created by wallace on 06/04/17.
 */
public class TopicList {

    private List<Topic> topics;

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    @Override
    public String toString() {
        return "TopicList{" +
                "topics=" + topics +
                '}';
    }
}
