import static org.junit.Assert.*;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.junit.Test;
import org.openqa.selenium.interactions.Actions;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.io.IOException;
import java.net.MalformedURLException;


public class Mythology_test {
    @Test
    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "src/chromedriver");
        WebDriver driver = new ChromeDriver();
        String webPage = "https://en.wikipedia.org/wiki/Metis_(mythology)";
        driver.get(webPage);
        String anchorUrl = "";
        HttpURLConnection conn = null;
        int code = 200;
        ArrayList<String> HeadingsArrayLst = new ArrayList<>();

        //Test Case 1:
        //Identify headings under 'Contents' box
        List<WebElement> c = driver.findElements(By.xpath("//span[@class='toctext']"));
        // iterate all nodes under 'toctext' class
        for (WebElement i : c) {
            HeadingsArrayLst.add(i.getText());
        }
        //Identify Page headings and validate against under 'Contents' box headings
        List<WebElement> headings = driver.findElements(By.xpath("//span[@class='mw-headline']"));
        int iter = 0;
        for (WebElement headingsElement : headings) {
            String Element =HeadingsArrayLst.get(iter);
            if (headingsElement.getText().equals(Element)) {
                System.out.println("Test Case 1: PASS - Heading in Contents matches the Page heading for: "+headingsElement.getText());
            }
            else {
                System.out.println("Test Case 1: FAIL - Heading in Contents does not match the Page heading for: "+headingsElement.getText());
            }
            iter++;
        }

        //Test Case 2: Identify Contents box and all anchor tags under it
        WebElement contents = driver.findElement(By.xpath("//div[@class='toc']"));
        List<WebElement> links = contents.findElements(By.tagName("a"));
        Iterator<WebElement> linksElements = links.iterator();
        while(linksElements.hasNext()) {
            anchorUrl = linksElements.next().getAttribute("href");
            try {
                conn = (HttpURLConnection) (new URL(anchorUrl).openConnection());
                conn.setRequestMethod("HEAD");
                conn.connect();
                code = conn.getResponseCode();
                if (code >= 400) {
                    System.out.println("Test Case 2: FAIL - "+ anchorUrl + " is not a working link");
                } else {
                    System.out.println("Test Case 2: PASS - "+ anchorUrl + " is a functioning link");
                }
            }
            catch(MalformedURLException e){
                    e.printStackTrace();
                } catch(IOException e){
                    e.printStackTrace();
                }
            }

        // Test Case 3: Identify mouse hover text and validate its text
        String expectedText = "In ancient Greek civilization, Nike was a goddess who personified victory. Her Roman equivalent was Victoria.";
        // Using actions class for mouse hover
        WebElement toolTipElement = driver.findElement(By.linkText("Nike"));
        Actions action = new Actions(driver);
        action.moveToElement(toolTipElement);
        action.clickAndHold(toolTipElement).perform();
        Thread.sleep(2000);
        WebElement popupText =toolTipElement.findElement(By.xpath("//div[contains(@class, 'mwe-popups')]"));
        String actualText = popupText.getText();
        Assert.assertEquals(expectedText, actualText);
        if(expectedText.equals(actualText))
            System.out.println("Test Case 3: PASS - MouseHover Popup text matches actual text: "+actualText );
        else
            System.out.println("Test Case 3: FAIL - MouseHover Popup text does not match actual text: "+actualText );

        //Test Case 4: Validate 'Family Tree' text in a navigated page
        WebElement nikePge = driver.findElement(By.linkText("Nike"));
        nikePge.click();
        if (driver.getPageSource().contains("Family tree")) {
            System.out.println("Test Case 4: PASS - Family Tree text present in Nike navigated page");
        } else {
            System.out.println("Test Case 4: FAIL - Family Tree text not present in Nike navigated page");
        }

        driver.close();
    }
}


