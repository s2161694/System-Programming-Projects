package model;

import java.util.Collection;

public class FAQItem {
    private String question;
    private String answer;

    public FAQItem(String question,String answer){
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
    public FAQItem getItem(Collection<FAQItem> items) {
        return items.stream().findFirst().orElse(null);
    }
}