package Base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import java.util.HashMap;

public class base {

    public WebDriver driver;
    public WebDriverWait wait;

    @BeforeSuite
    public void initializeDriver() {
        WebDriverManager.chromedriver().setup();
        

        ChromeOptions options = new ChromeOptions();
        options.setBinary("C:\\Users\\hp\\chromedriver\\chrome-win64\\chrome-win64\\chrome.exe");

        // ✅ No need for setBinary unless Chrome is not in default path
        HashMap<String, Object> prefs = new HashMap<>();
        prefs.put("profile.password_manager_leak_detection", false);
        options.setExperimentalOption("prefs", prefs);

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @AfterSuite
    public void tearDown() {
        System.out.println("Closing Browser...");
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
