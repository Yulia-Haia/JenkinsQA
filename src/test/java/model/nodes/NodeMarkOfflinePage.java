package model.nodes;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import model.base.BasePage;

public class NodeMarkOfflinePage extends BasePage<NodeMarkOfflinePage> {

    @FindBy(xpath = "//textarea[@name = 'offlineMessage']")
    private WebElement messageInputField;

    @FindBy(name = "Submit")
    private WebElement markThisNodeTemporarilyOfflineButton;

    public NodeMarkOfflinePage(WebDriver driver) {
        super(driver);
    }

    public NodeDetailsPage takingNewNodeOffline(String reasonMessage) {
        messageInputField.sendKeys(reasonMessage);
        markThisNodeTemporarilyOfflineButton.click();

        return new NodeDetailsPage(getDriver());
    }
}
