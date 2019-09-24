package br.com.commandline;

import br.com.commandline.domain.Question;
import br.com.commandline.domain.Topic;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

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

    static List<Topic> topics;
    static Topic topic;

    static List<Question> listQuestionsTemp = new ArrayList<>();
    static List<Question> listQuestionsError = new ArrayList<>();

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
            String temp = input;
            String answer = question.getAnswers().stream().filter(p -> temp.equalsIgnoreCase(p.trim())).findAny().orElse("");
            if (!"".equals(answer)) break;
            System.out.println(ANSI_RED + "Error -> Retry " + count + " of " + qtdeRetry);
            input = scanner.nextLine().trim();
            close(input);
            count++;
            if (count == qtdeRetry) {
                System.out.println(ANSI_RED + "Error -> Retry " + count + " of " + qtdeRetry);
                System.out.println();
                System.out.println(ANSI_RED + "Answer: " + question.getAnswers());
                listQuestionsError.add(question);
            }
        }
        command();
    }

    static void menuTopics()  {
        listQuestionsTemp.clear();
        finalTopic = false;
        if (!listQuestionsError.isEmpty()) {
            Topic topic = new Topic("RETRY TO THE WRONG QUESTIONS!", new ArrayList<>(listQuestionsError));
            topics.add(topic);
            listQuestionsError.clear();
        }
        System.out.println(ANSI_RED + "Selecione o numero de um Topico!");
        for (int i = 0; i < topics.size(); i++) {
            System.out.println(ANSI_GREEN + "[" + i + "] " + topics.get(i).getTopic() + " - TOTAL QUESTIONS: " + topics.get(i).getList().size());
        }

        String input = new Scanner(System.in).nextLine();
        close(input);
        topic = topics.get(Integer.valueOf(input));

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
        Type listType = new TypeToken<ArrayList<Topic>>(){}.getType(); //Usado para fazer o parser da lista
        topics = gson.fromJson(br, listType);
        menuTopics();
    }

	public static void main(String[] args) throws Exception {
        init();
    }

}
