	package demotest;
	
	import org.openqa.selenium.By;
	import org.openqa.selenium.WebDriver;
	import org.openqa.selenium.WebElement;
	import org.openqa.selenium.chrome.ChromeDriver;
	import org.openqa.selenium.chrome.ChromeOptions;
	import org.openqa.selenium.JavascriptExecutor;
	import org.openqa.selenium.support.ui.ExpectedConditions;
	import org.openqa.selenium.support.ui.WebDriverWait;
	import org.testng.Assert;
	import org.testng.annotations.AfterClass;
	import org.testng.annotations.BeforeClass;
	import org.testng.annotations.Test;
	
	import Base.base;
	import io.github.bonigarcia.wdm.WebDriverManager;
	
	import java.time.Duration;
	
	public class mainlogin extends base {
		
		   
	
	        @Test(priority = 1)
		    public void loginTest() throws InterruptedException {
		        // Enter username and password
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
		}