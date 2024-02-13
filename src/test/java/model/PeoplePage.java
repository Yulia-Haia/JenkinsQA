package model;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import model.base.BasePage;
import model.users.CreatedUserPage;
import model.users.UserPage;

public class PeoplePage extends BasePage<PeoplePage> {

    @FindBy(xpath = "//li[@tooltip= 'Large' and @class='jenkins-icon-size__items-item' and @title='Large']")
    private WebElement largeButton;

    @FindBy(xpath = "//a[@title='Medium']")
    private WebElement mediumButton;

    @FindBy(xpath = "//a[@title='Small']")
    private WebElement smallButton;

    @FindBy(xpath = "//td[@class = 'jenkins-table__cell--tight jenkins-table__icon']")
    private WebElement iconFieldLarge;

    @FindBy(xpath = "//a[contains(@href,'/user/')]")
    private WebElement currentUserName;

    public PeoplePage(WebDriver driver) {
        super(driver);
    }

    public PeoplePage clickLargeIcon() {
        largeButton.click();
        return this;
    }

    public WebElement iconField() {
        return iconFieldLarge;
    }

    public int[] iconActualResult() {
        return new int[]{iconField().getSize().getWidth(), iconField().getSize().getHeight()};
    }

    public PeoplePage clickMediumIcon() {
        mediumButton.click();
        return this;
    }

    public PeoplePage clickSmallIcon() {
        smallButton.click();
        return this;
    }

    public CreatedUserPage clickOnTheCreatedUserID(String userName) {
        getDriver().findElement(
                By.xpath("//tr[@id = 'person-" + userName + "']/td/a")).click();

        return new CreatedUserPage(getDriver());
    }

    public UserPage clickCurrentUserName() {
        currentUserName.click();
        return new UserPage(getDriver());
    }
}
