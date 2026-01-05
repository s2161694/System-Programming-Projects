package view;

import model.*;

import java.util.Collection;
import java.util.Locale;
import java.util.Scanner;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;


public class TextUserInterface implements View {
    private Scanner scanner = new Scanner(System.in);


    public String getInput(String prompt) {
        displayInfo(prompt);
        return scanner.nextLine();
    }
    public boolean getYesNoInput(String prompt) {
        displayInfo(prompt);
        String userInput = scanner.nextLine();
        userInput = userInput.toLowerCase(Locale.ROOT);
        if (userInput.equals("y") || userInput.equals("ye") || userInput.equals("yes") || userInput.equals("1"))
        {
            return true;
        }
        else if (userInput.equals("no") || userInput.equals("n") || userInput.equals("2"))
        {
            return false;
        }
        else
        {
            displayError("Please enter a valid input[y/n]");
            return getYesNoInput(prompt);
        }
    }
    public void displayInfo(String content)
    {
        System.out.print(content);
    }
    public void displaySuccess(String message)
    {
        displayInfo(message);
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            displayException("System encountered an error" + e.getMessage());
        }
    }
    public void displayWarning(String message)
    {
        displayInfo(message);
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            displayException("System encountered an error" + e.getMessage());
        }
    }
    public void displayError(String errorMessage)
    {
        try
        {
            String os = System.getProperty("os.name");
            if(os.contains("Windows"))
            {
                Runtime.getRuntime().exec("cls");
            }
            else
            {
                Runtime.getRuntime().exec("clear");
            }
        }
        catch(Exception e)
        {
            displayException("System encountered an error" + e.getMessage());
        }
        System.out.print(errorMessage);
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            displayException("System encountered an error" + e.getMessage());
        }
    }
    public void displayException(String exceptionMessage)
    {
        System.err.println(exceptionMessage);
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            displayException("System encountered an error" + e.getMessage());
        }
    }
    public void displayDivider() {
        System.out.print("\n--------------------------------------\n");
    }
    public void displayFAQ(FAQ faqList, boolean condition) {
        FAQSection headSection = faqList.getfaqSection();
        Collection<FAQSection> headSectionSubSections = headSection.getSubsections();
        Iterator<FAQSection> faqSectionIter = headSectionSubSections.iterator();
        int count_int = 0;
        String temp_string;
        while (faqSectionIter.hasNext()) {
            FAQSection section = faqSectionIter.next();
            count_int++;
            temp_string = String.format("%d.", count_int);
            displayInfo(temp_string);
            displayInfo(section.getTopic() + "\n");
        }
    }

    public void displayFAQSection(FAQSection faqListSection, boolean condition) {
        System.out.println(faqListSection.getTopic());
        Iterator<FAQSection> iterSubSections = faqListSection.getSubsections().iterator();
        int count_int = 0;
        String temp_string;
        while (iterSubSections.hasNext()) {
            FAQSection subSection = iterSubSections.next();
            count_int++;
            temp_string = String.format("    %d.", count_int);
            displayInfo(temp_string);
            displayInfo(subSection.getTopic()+"\n");
        }
        Iterator<FAQItem> iterItems = faqListSection.getFaqItems().iterator();
        while (iterItems.hasNext()) {
            FAQItem items = iterItems.next();
            System.out.println( "Question: " + items.getQuestion());
            System.out.println( "Answer: " + items.getAnswer());                    
        }
    }

    public void displayInquiry(Inquiry inquiry) {
        System.out.println(inquiry.getCreatedAt());
        System.out.println(inquiry.getInquirerEmail());
        System.out.println(inquiry.getSubject());
        System.out.println(inquiry.getContent());
    }
    
    public void displaySearchResults(Collection<PageSearchResult> searchResult) {
        Iterator<PageSearchResult> iteratorOverSearchResult = searchResult.iterator();
        int temp_count = 0;
        while(iteratorOverSearchResult.hasNext()) {
            PageSearchResult pageResult = iteratorOverSearchResult.next();
            displayInfo(pageResult.getFormattedContent());
            temp_count++;
            if(temp_count == 4)
                break;
        }
        if(temp_count == 1)
            displayInfo("This is the most relevant result");
        else
            displayInfo("These are the" +String.format(" %d ", temp_count) + "most relevant results");
    }
}


