package model.nodes;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import model.base.BasePage;

public class NodeCreatePage extends BasePage<NodeCreatePage> {

    @FindBy(id = "name")
    private WebElement inputName;

    @FindBy(name = "from")
    private WebElement inputExistingName;

    @FindBy(xpath = "//label[@for='hudson.slaves.DumbSlave']")
    private WebElement permanentAgentRadioButton;

    @FindBy(xpath = "//label[@for='copy']")
    private WebElement copyExistingNodeRadioButton;

    @FindBy(name = "Submit")
    private WebElement createButton;

    @FindBy(css = ".error")
    private WebElement errorMessage;

    public NodeCreatePage(WebDriver driver) {
        super(driver);
    }

    public NodeCreatePage sendNodeName(String nodeName) {
        inputName.sendKeys(nodeName);
        return this;
    }

    public NodeCreatePage sendExistingNodeName(String nodeName) {
        inputExistingName.sendKeys(nodeName);
        return this;
    }

    public NodeCreatePage SelectPermanentAgentRadioButton() {
        permanentAgentRadioButton.click();
        return this;
    }

    public NodeCreatePage SelectCopyExistingNodeRadioButton() {
        copyExistingNodeRadioButton.click();
        return this;
    }

    public NodeCofigurationPage clickCreateButton() {
        createButton.click();
        return new NodeCofigurationPage(getDriver());
    }

    public <T> T clickCreateButton(T page) {
        createButton.click();
        return page;
    }

    public String getErrorMessage() {
        return getWait2().until(ExpectedConditions.visibilityOf(errorMessage)).getText();
    }

    public boolean enabledCreateButton() {

        return createButton.isEnabled();
    }
}
