package demotest;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import Base.base;

public class AppWall extends base {

    // Global variable so create/edit/delete use the same value
    String appreciationword;

    // Helper: show quick JS alert and close it
    private void showPopup(String message) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("alert(arguments[0]);", message);
            Thread.sleep(1200);
            driver.switchTo().alert().accept();
        } catch (Exception e) {
            System.out.println("Popup could not be shown: " + e.getMessage());
        }
    }

    // Try multiple candidate xpaths to locate the list item that contains the text
    private By detectItemLocator(String text, int timeoutSeconds) {
        String[] candidateXPaths = new String[] {
                "//*[@id='appreciation_list']//li[contains(normalize-space(.),\"" + text + "\")]",
                "//*[@id='appreciation_wall']//li[contains(normalize-space(.),\"" + text + "\")]",
                "//li[contains(@class,'list-group-item') and contains(normalize-space(.),\"" + text + "\")]",
                "//div[contains(@class,'appreciation-item') and contains(normalize-space(.),\"" + text + "\")]",
                "//div[contains(@class,'media') and contains(normalize-space(.),\"" + text + "\")]",
                "//*[contains(normalize-space(.),\"" + text + "\") and (contains(@class,'appreciation') or contains(@id,'appreciation') or name() = 'li')]"
        };

        long end = System.currentTimeMillis() + timeoutSeconds * 1000L;
        while (System.currentTimeMillis() < end) {
            for (String xp : candidateXPaths) {
                List<WebElement> found = driver.findElements(By.xpath(xp));
                if (found != null && !found.isEmpty()) {
                    return By.xpath(xp);
                }
            }
            try { Thread.sleep(400); } catch (InterruptedException ignored) {}
        }
        return null;
    }

    @Test(priority = 1)
    public void navigateToAppreciationWall() throws InterruptedException {
        Thread.sleep(2500); // small buffer after login
        WebElement appreciationWall = wait.until(ExpectedConditions
                .elementToBeClickable(By.xpath("//ul[@id='menu']//a[i[@title='Appreciation Wall']]")));
        appreciationWall.click();

        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("appreciation"),
                ExpectedConditions.presenceOfElementLocated(By.xpath("//textarea[contains(@placeholder,'Type')]"))
        ));

        String currentUrl = driver.getCurrentUrl();
        System.out.println("Navigated to (maybe): " + currentUrl);
        Assert.assertTrue(driver.findElements(By.xpath("//textarea[contains(@placeholder,'Type')]")).size() > 0,
                "Appreciation wall page not loaded (textarea not found).");
    }

    @Test(priority = 2)
    public void createAppreciation() throws InterruptedException {
        System.out.println("Creating appreciation...");
        Thread.sleep(1000);

        // unique message
        appreciationword = "Test appreciation " + (System.currentTimeMillis() % 100000);

        By textareaBy = By.xpath("//textarea[@placeholder='Type Something...']");
        if (driver.findElements(textareaBy).isEmpty()) {
            textareaBy = By.xpath("//textarea[contains(@placeholder,'Type')]");
        }
        WebElement reqInput = wait.until(ExpectedConditions.visibilityOfElementLocated(textareaBy));
        reqInput.clear();
        reqInput.sendKeys(appreciationword);

        By sendBtnBy = By.xpath("//button[normalize-space()='Send' or contains(translate(., 'SEND', 'send'), 'send') or contains(.,'SEND') or contains(.,'Send')]");
        if (driver.findElements(sendBtnBy).isEmpty()) {
            sendBtnBy = By.xpath("//button[contains(.,'SEND') or contains(.,'Send') or contains(.,'send')]");
        }
        WebElement sendBtn = wait.until(ExpectedConditions.elementToBeClickable(sendBtnBy));
        sendBtn.click();

        Thread.sleep(1200);

        By itemLocator = detectItemLocator(appreciationword, 8);
        Assert.assertNotNull(itemLocator, "Could not detect list container or item. Inspect DOM and update candidate xpaths.");

        WebElement created = wait.until(ExpectedConditions.visibilityOfElementLocated(itemLocator));
        Assert.assertTrue(created.getText().trim().contains(appreciationword),
                "Created item found but text mismatch. Found text: " + created.getText());

        System.out.println("Appreciation created successfully: " + appreciationword);
        showPopup("Appreciation Created Successfully: " + appreciationword);
    }

    @Test(priority = 3)
    public void editAppreciationWord() {
        System.out.println("Editing Appreciation...");

        // Pick first card
        WebElement appreciationCard = wait.until(ExpectedConditions
                .elementToBeClickable(By.xpath("(//div[@class='content']//h4[@class='title'])[1]")));

        String oldAppreciation = appreciationCard.getText().trim();
        appreciationCard.click();
        System.out.println("Clicked appreciation card: " + oldAppreciation);

        // Wait for modal/container to appear
        WebElement editContainer = wait.until(ExpectedConditions
                .visibilityOfElementLocated(By.xpath("//div[contains(@class,'modal') or contains(@class,'edit')]")));

        // Find textarea inside the modal
        WebElement textarea = editContainer.findElement(By.xpath(".//textarea"));
        textarea.click();
        textarea.clear();

        appreciationword = "Updated Appreciation " + (System.currentTimeMillis() % 100000);
        textarea.sendKeys(appreciationword);
        System.out.println("Typed updated appreciation: " + appreciationword);

        // Click UPDATE inside modal
        WebElement updateBtn = editContainer.findElement(By.xpath(".//button[normalize-space(text())='UPDATE']"));
        updateBtn.click();
        System.out.println("Clicked UPDATE button...");

        // Optional: validate toast
        try {
            WebElement toast = wait.until(ExpectedConditions
                    .visibilityOfElementLocated(By.xpath("//*[contains(text(),'successfully')]")));
            Assert.assertTrue(toast.isDisplayed(), "❌ Toast message not displayed!");
            System.out.println("✅ Toast message displayed: " + toast.getText());
        } catch (Exception e) {
            System.out.println("⚠ No toast message found. Skipping toast validation.");
        }
    }

    @Test(priority = 4)
    public void deleteAppreciationWord() {
        System.out.println("Deleting appreciation...");

        try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }

        // Use updated global variable for deletion
        WebElement deleteIcon = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
                "//h4[@class='title' and normalize-space(text())='" + appreciationword
                        + "']/ancestor::div[contains(@class,'row')]//button")));
        deleteIcon.click();
        System.out.println("Delete icon clicked for: " + appreciationword);

        WebElement confirmYes = wait
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Yes']")));
        confirmYes.click();
        System.out.println("Confirmed deletion with YES");

        try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }

        // Validate deletion
        boolean isDeleted = wait.until(driver -> driver.findElements(By.xpath(
                "//h4[@class='title' and normalize-space(text())='" + appreciationword + "']")).isEmpty());

        Assert.assertTrue(isDeleted, "Appreciation was not deleted!");
        System.out.println("✅ Appreciation deleted successfully: " + appreciationword);
    }

}
