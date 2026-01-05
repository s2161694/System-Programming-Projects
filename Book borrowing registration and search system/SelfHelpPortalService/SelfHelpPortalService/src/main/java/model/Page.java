package model;
public class Page {
    private String title;
    private String content;
    private boolean isPrivate;

    public Page(String inputTitle, String inputContent, boolean isPrivate) {
        this.title = inputTitle;
        this.content = inputContent; 
        this.isPrivate = isPrivate;
    }

    public String getTitle() {
        return this.title;
    }

    public String getContent() {
        return this.content;
    }

    public boolean getIsPrivate() {
        return this.isPrivate;
    }

}