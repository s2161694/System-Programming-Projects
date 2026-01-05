package model;
import java.util.ArrayList;
import java.util.Collection;

public class FAQSection {
    private String topic;
    private Collection<FAQSection> subSections;
    private Collection<FAQItem> faqItems;
    private FAQSection parent;


    public FAQSection(String topic) 
    {
        this.topic = topic;
        this.subSections = new ArrayList<FAQSection>();
        this.faqItems = new ArrayList<FAQItem>();
    }



    public void setParent(FAQSection parentSection) 
    {
        this.parent = parentSection;
    }
    public String getTopic() 
    {
        return this.topic;
    }
    public Collection<FAQSection> getSubsections() 
    {
        return this.subSections;
    }
    public Collection<FAQItem> getFaqItems() 
    {
        return this.faqItems;
    }
    public FAQSection getparent() 
    {
        return this.parent;
    }

    public void addItem(String question,String answer)
    {
        FAQItem item = new FAQItem(question,answer);
        faqItems.add(item);
    }

    public void addSubsection(FAQSection subSection) 
    {
        subSections.add(subSection);
        subSection.setParent(this);
    }

}