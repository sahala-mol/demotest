package demotest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import Base.base;

public class mainlogin extends base {

    @Test(priority = 1)
    public void loginTest() throws InterruptedException {
        driver.get("https://demo.scaleup-business-builder.xyz");

        driver.findElement(By.name("username")).sendKeys("super-admin");
        driver.findElement(By.name("password")).sendKeys("00000");

        // Handle checkbox
        WebElement checkbox = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//label[contains(text(),'Remember my preference')]/preceding-sibling::input")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);

        // Wait for splash
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("splash-screen")));

        // Click login
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(text(),'Sign Me In')]")
        ));
        loginButton.click();

        Thread.sleep(2000);

        // Verify login
        String pageTitle = driver.getTitle();
        System.out.println("Page Title: " + pageTitle);
        Assert.assertTrue(pageTitle.contains("CRM"), "Login failed!");
    }
    @Test(priority = 2)
    public void navigateToMenu()   {
        // Click on menu bar
        try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        WebElement menuBar = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"main-wrapper\"]//div[contains(@class,'hamburger')]")));
        menuBar.click();    
}
}
