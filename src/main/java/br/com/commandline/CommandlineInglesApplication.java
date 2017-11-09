package br.com.commandline;

import br.com.commandline.domain.Question;
import br.com.commandline.domain.Topic;
import br.com.commandline.domain.TopicList;
import com.google.gson.Gson;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Ref GSON:
 * https://github.com/google/gson/blob/master/UserGuide.md
 */
public class CommandlineInglesApplication  {

    public static final String ANSI_RESET = "\u001B[0m";
    //public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    static String[] ANSIS = {ANSI_GREEN, ANSI_YELLOW, ANSI_PURPLE , ANSI_CYAN, ANSI_WHITE  };

    static int qtdeRetry = 3;

    static boolean finalTopic = false;

    static TopicList topicList;
    static Topic topic;

    static List<Question> listQuestionsTemp = new ArrayList<>();

    static Question getQuestion() {
        finalTopic = false;
        Question question = topic.getList().get(new Random().nextInt(topic.getList().size()));
        if (listQuestionsTemp.contains(question)) {
            if (listQuestionsTemp.size() == topic.getList().size()) {
                finalTopic = true;
                return question;
            }
            return getQuestion();
        }
        listQuestionsTemp.add(question);
        return question;
    }

    static void close(String next) {
        if (finalTopic) menuTopics();
        if (next.equals("close")) System.exit(0);
    }

    static void command() throws UnsupportedEncodingException {
        Question question = getQuestion();
        if (finalTopic) {
            close("");
            return;
        }
        System.out.println(ANSI_RESET + "##############################################################");
        System.out.println(ANSIS[new Random().nextInt(ANSIS.length)] + "Question: " + question.getQuestion());

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().trim();
        close(input);
        int count = 1;
        while (count < qtdeRetry) {
            if (input.equalsIgnoreCase(question.getAnswer().trim())) break;
            System.out.println(ANSI_RED + "Error -> Retry " + count + " of " + qtdeRetry);
            input = scanner.nextLine().trim();
            close(input);
            count++;
            if (count == qtdeRetry) {
                System.out.println(ANSI_RED + "Error -> Retry " + count + " of " + qtdeRetry);
                System.out.println();
                System.out.println(ANSI_RED + "Answer: " + question.getAnswer());
            }
        }
        command();
    }

    static void menuTopics()  {
        listQuestionsTemp.clear();
        finalTopic = false;
        System.out.println(ANSI_RED + "Selecione o numero de um Topico!");
        for (int i = 0; i < topicList.getTopics().size(); i++) {
            System.out.println(ANSI_GREEN + "[" + i + "] " + topicList.getTopics().get(i).getTopic());
        }

        String input = new Scanner(System.in).nextLine();
        close(input);
        topic = topicList.getTopics().get(Integer.valueOf(input));

        System.out.println("Topico Selecionado -> " + topic.getTopic());
        System.out.println(ANSI_RED + "##############################################################");
        System.out.println(ANSI_GREEN + "Vamos comecar!");
        System.out.println(ANSI_RED + "##############################################################");
        System.out.println();

        try {
            command();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    static void init () throws IOException, URISyntaxException {
        Gson gson = new Gson();
        InputStream is = CommandlineInglesApplication.class.getResourceAsStream("/question.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        topicList = gson.fromJson(br, TopicList.class);
        menuTopics();
    }

	public static void main(String[] args) throws Exception {
        init();
    }

}
