package model;


public class FAQ {

    public FAQSection headSection = new FAQSection(null);

    public void addSection(FAQSection fAQSection) {
        headSection.addSubsection(fAQSection);
    }

    public FAQSection getfaqSection() {
        return this.headSection;
    }

}