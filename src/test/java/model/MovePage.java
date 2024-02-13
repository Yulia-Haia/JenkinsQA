package model;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import model.base.BaseDetailsPage;
import model.base.BasePage;

public class MovePage<DetailsPage extends BaseDetailsPage<?, ?>> extends BasePage<DetailsPage> {

    @FindBy(name = "Submit")
    private WebElement moveButton;

    @FindBy(name = "destination")
    private WebElement destinationDropdown;

    public MovePage(WebDriver driver) {
        super(driver);
    }

    public MovePage<DetailsPage> clickArrowDropDownMenu() {
        destinationDropdown.click();

        return this;
    }

    public MovePage<DetailsPage> clickFolderByName(String folderName) {
        getDriver().findElement(By.xpath("//*[contains(@value,'" + folderName + "')]")).click();

        return this;
    }

    public MovePage<DetailsPage> clickMoveFromMovePage() {
        moveButton.click();

        return new MovePage<>(getDriver());
    }
}
