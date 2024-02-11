package model;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class FreestyleProjectPage extends BasePage {

    @FindBy(xpath = "//h1")
    private WebElement headline;

    @FindBy(linkText = "Configure")
    private WebElement sideMenuConfigure;

    public FreestyleProjectPage(WebDriver driver) {
        super(driver);
    }

    public FreestyleConfigSideMenuPage clickSideMenuConfigure() {
        sideMenuConfigure.click();

        return new FreestyleConfigSideMenuPage(getDriver());
    }

    public String getHeadlineText() {
        return headline.getText();
    }
}
