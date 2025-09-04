package demotest;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.annotations.*;

import Base.base;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;

public class LeadSourceTest extends base{
	
	    @Test(priority = 2)
	    public void navigateToLeadSettings()   {
	    	System.out.println("2");
	        // Click on menu bar
	        try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	        WebElement menuBar = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"main-wrapper\"]//div[contains(@class,'hamburger')]")));
	        	
	        menuBar.click();

	        // Click on Admin dropdown
	        WebElement adminMenu = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[normalize-space(text())='Admin']")));
	        adminMenu.click();

	        // Click on Lead Settings
	        WebElement leadSettings = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[normalize-space(text())='Lead settings']")));
	        leadSettings.click();
	    	System.out.println("2 end");
	        System.out.println(driver.getCurrentUrl());
	        Assert.assertTrue(driver.getCurrentUrl().contains("lead/config"), "Not navigated to Lead Settings");
	    
	        
	     // ✅ Click on Lead Requirement
	        WebElement leadRequirement = wait.until(ExpectedConditions.elementToBeClickable(
	                By.xpath("//a[normalize-space(text())='Lead Requirement']")));
	        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", leadRequirement);

	        System.out.println("Clicked Lead Requirement");

	        // ✅ Verify navigation
	        System.out.println(driver.getCurrentUrl());
	        Assert.assertTrue(driver.getCurrentUrl().contains("lead/config"), "Not navigated to Lead Requirement page");


	    }

	    @Test(priority = 3)
	    public void createLeadRequirement() {
	        System.out.println("Creating Lead Requirement...");

	        try {
	            Thread.sleep(2000);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }

	        // ✅ Click Create button for Lead Requirement
	        WebElement createBtn = wait.until(ExpectedConditions.elementToBeClickable(
	                By.xpath("//*[@id='lead_requirement']/div/div/div[1]/span/button")
	        ));
	        createBtn.click();

	        // ✅ Enter Lead Requirement name
	        String requirementName = "Test Requirement " + System.currentTimeMillis() % 100000;
	        WebElement reqInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
	                By.name("name")   // <-- change if the input field has a different 'name' attribute
	        ));
	        reqInput.sendKeys(requirementName);

	        // ✅ Click Save and Close
	        WebElement saveBtn = wait.until(ExpectedConditions.elementToBeClickable(
	                By.xpath("//button[contains(text(),'Save and Close')]")
	        ));
	        saveBtn.click();

	        try {
	            Thread.sleep(3000);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }

	        // ✅ Validate creation (change xpath if needed)
	        WebElement createdRequirement = wait.until(ExpectedConditions.visibilityOfElementLocated(
	                By.xpath("//*[@id='lead_requirement']//li[contains(@class,'list-group-item') and normalize-space(text())='" + requirementName + "']")
	        ));

	        Assert.assertTrue(createdRequirement.isDisplayed(), "Lead Requirement not created!");
	        System.out.println("Lead Requirement created successfully: " + requirementName);
	    }


	    @Test(priority = 4)
	    public void editLeadRequirement() {
	        System.out.println("Editing Lead Requirement...");

	        try {
	            Thread.sleep(3000);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }

	        // ✅ Click edit icon for the first lead requirement (pencil icon)
	        WebElement editIcon = wait.until(ExpectedConditions.elementToBeClickable(
	                By.xpath("(//div[@id='lead_requirement']//li[1]//span[1]//*[name()='svg'])[1]")
	        ));
	        editIcon.click();
	        System.out.println("Edit icon clicked...");

	        // ✅ Edit name input field (relative xpath for input inside lead_requirement)
	        WebElement requirementInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
	                By.xpath("//div[@id='lead_requirement']//input[@type='text']")
	        ));
	        requirementInput.clear();

	        String updatedRequirement = "Updated Requirement " + System.currentTimeMillis() % 100000;
	        requirementInput.sendKeys(updatedRequirement);

	        // ✅ Save changes
	        WebElement saveBtn = driver.findElement(By.xpath("//button[normalize-space(text())='Save']"));
	        saveBtn.click();
	        System.out.println("Requirement updated, validating...");

	        // ✅ Validate update
	        WebElement updatedRequirementElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
	                By.xpath("//li[normalize-space(text())='" + updatedRequirement + "']")
	        ));

	        Assert.assertTrue(updatedRequirementElement.isDisplayed(), "Lead Requirement not updated!");
	        System.out.println("Lead Requirement updated successfully: " + updatedRequirement);
	    }

	    @Test(priority = 5)
	    public void deleteLeadRequirement() {
	        System.out.println("Deleting Lead Requirement...");

	        try {
	            Thread.sleep(2000); // allow UI to settle
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }

	        // ✅ Click delete icon for first lead requirement
	        WebElement deleteIcon = wait.until(ExpectedConditions.elementToBeClickable(
	                By.xpath("//div[@id='lead_requirement']//ul/li[1]//span[2]//svg")
	        ));
	        deleteIcon.click();
	        System.out.println("Delete icon clicked...");

	        // ✅ Confirm delete in popup
	        WebElement deleteConfirm = wait.until(ExpectedConditions.elementToBeClickable(
	                By.xpath("//button[contains(@class,'swal-button') and contains(@class,'swal-button--confirm') and contains(@class,'swal-button--danger')]")
	        ));
	        deleteConfirm.click();
	        System.out.println("Delete confirmed...");

	        try {
	            Thread.sleep(2000); // wait for confirmation alert
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }

	        // ✅ Validate deletion success alert
	        WebElement deleteSuccessAlert = wait.until(ExpectedConditions.visibilityOfElementLocated(
	                By.xpath("//div[@class='swal-title' and normalize-space(text())='Deleted!']")
	        ));
	        boolean isDeleted = deleteSuccessAlert.isDisplayed();
	        System.out.println("Deleted popup shown: " + isDeleted);

	        // ✅ Click OK button to close alert
	        WebElement okButton = wait.until(ExpectedConditions.elementToBeClickable(
	                By.xpath("//button[contains(@class,'swal-button--confirm')]")
	        ));
	        okButton.click();

	        // ✅ Final assertion
	        Assert.assertTrue(isDeleted, "Lead Requirement deletion failed!");
	        System.out.println("Lead Requirement deleted successfully.");
	    }

	}