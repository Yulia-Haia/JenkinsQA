package model.jobs.configs;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import model.base.BaseConfigurationPage;
import model.jobs.details.FolderDetailsPage;

public class FolderConfigurationPage extends BaseConfigurationPage<FolderDetailsPage, FolderConfigurationPage> {

    @FindBy(name = "_.description")
    private WebElement descriptionTextField;

    @FindBy(className = "textarea-show-preview")
    private WebElement previewSwitch;

    @FindBy(xpath = "//div[@class = 'textarea-preview']")
    private WebElement descriptionPreview;

    @FindBy(xpath = "//button[@data-section-id='health-metrics']")
    private WebElement healthMetricsSideMenuOption;

    @FindBy(xpath = "//button[@class='jenkins-button advanced-button advancedButton']")
    private WebElement healthMetricsButton;

    @FindBy(id = "yui-gen1-button")
    private WebElement addMetricButton;

    @FindBy(linkText = "Child item with worst health")
    private WebElement childHealthMetric;

    @FindBy(xpath = "//div[@class='repeated-chunk__header' and contains(text(), 'Child item with worst health')]")
    private WebElement childHealthMetricSection;

    @FindBy(css = "a[tooltip='Help']")
    private WebElement helpButtonRecursive;

    @FindBy(xpath = "//div[@class='help']/div[1]")
    private  WebElement helpBlock;

    @FindBy(xpath = "//input[@name='_.displayNameOrNull']")
    private WebElement inputDisplayName;

    public FolderConfigurationPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected FolderDetailsPage createProjectPage() {
        return new FolderDetailsPage(getDriver());
    }

    public FolderConfigurationPage typeDescription(String description) {
        descriptionTextField.sendKeys(description);

        return this;
    }

    public FolderConfigurationPage clickPreviewDescription() {
        previewSwitch.click();

        return this;
    }

    public String getFolderDescription() {
        return descriptionPreview.getText();
    }

    public FolderConfigurationPage clickHealthMetricsInSideMenu() {
        healthMetricsSideMenuOption.click();

        return this;
    }

    public FolderConfigurationPage clickHealthMetrics() {
        healthMetricsButton.click();

        return this;
    }

    public FolderConfigurationPage clickAddHealthMetric() {
        getWait5().until(ExpectedConditions.elementToBeClickable(addMetricButton)).click();

        return this;
    }

    public FolderConfigurationPage selectChildHealthMetric() {
        getWait5().until(ExpectedConditions.elementToBeClickable(childHealthMetric)).click();

        return this;
    }

    public boolean  isChildHealthMetricDisplayed () {
        return getWait5().until(ExpectedConditions.visibilityOf(childHealthMetricSection)).isDisplayed();
    }

    public FolderConfigurationPage clickHelpButtonRecursive() {
        helpButtonRecursive.click();

        return this;
    }

    public String getHelpBlockText() {

        return helpBlock.getText();
    }

    public FolderConfigurationPage typeDisplayName(String displayName) {
        inputDisplayName.sendKeys(displayName);

        return this;
    }
}