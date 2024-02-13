import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import model.builds.BuildHistoryPage;
import model.jobs.configs.FreestyleProjectConfigurePage;
import model.jobs.details.FreestyleProjectDetailsPage;
import model.HomePage;
import runner.BaseTest;
import runner.TestUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BuildHistoryTest extends BaseTest {

    private static final String NAME_FREESTYLE_PROJECT = "FreestyleProject";

    @Test
    public void testViewBuildHistory() {
        BuildHistoryPage buildHistoryPage = new HomePage(getDriver())
                .clickBuildHistoryButton();

        Assert.assertTrue(buildHistoryPage.getMainPanel().isDisplayed());
    }

    @Test
    public void testViewBuildHistoryClickableIconLegend() {
        BuildHistoryPage buildHistoryPage = new HomePage(getDriver())
                .clickBuildHistoryButton()
                .clickIconLegendButton();

        Assert.assertEquals(buildHistoryPage.getIconLegendHeaders().size(), 2);
    }

    @Test
    public void testCheckDateAndMonthBuildHistory() {
        Date systemDate = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("MMM d", Locale.ENGLISH);
        String dateNow = formatForDateNow.format(systemDate);

        FreestyleProjectDetailsPage buildHistoryPage = new HomePage(getDriver())
                .clickNewItem()
                .typeItemName("Test")
                .selectFreestyleProject()
                .clickOk(new FreestyleProjectConfigurePage(getDriver()))
                .clickSaveButton()
                .clickBuildNow();

        if (dateNow.length() == 5) {
            Assert.assertEquals(getWait10().until(ExpectedConditions
                            .presenceOfElementLocated(By.xpath("//a[@class='model-link inside build-link']"))).getText()
                    .substring(0, 5), dateNow);
        } else {
            Assert.assertEquals(getWait10().until(ExpectedConditions
                            .presenceOfElementLocated(By.xpath("//a[@class='model-link inside build-link']"))).getText()
                    .substring(0, 6), dateNow);
        }
    }

    @Test
    public void testTooltipIsVisibleInTheTimeSinceSection() {
        String tooltipIsVisible = new HomePage(getDriver())
                .clickNewItem()
                .typeItemName(NAME_FREESTYLE_PROJECT)
                .selectFreestyleProject()
                .clickOk(new FreestyleProjectConfigurePage(getDriver()))
                .clickSaveButton()
                .clickBuildNow()
                .goHomePage()
                .clickBuildHistoryButton()
                .getTextLastTimeSinceInLineBuild();

        Assert.assertEquals(tooltipIsVisible, "Click to center timeline on event");
    }

    @Ignore
    @Test(dependsOnMethods = "testTooltipIsVisibleInTheTimeSinceSection")
    public void testReturnBuildPoint() {
        Point startPosition = new HomePage(getDriver())
                .clickBuildByGreenArrow(NAME_FREESTYLE_PROJECT)
                .clickBuildHistoryButton()
                .getPointLocation();

        Point actualPosition = new BuildHistoryPage(getDriver())
                .scrollTimelineBuildHistory()
                .clickLastTimeSinceInLineBuild()
                .getPointLocation();

        Assert.assertEquals(actualPosition, startPosition);
    }

    @Test
    public void testNavigationToPreviousBuild() {
        TestUtils.createFreestyleProject(this, NAME_FREESTYLE_PROJECT, true);

        List<String> builds = new HomePage(getDriver())
                .clickJobByName(NAME_FREESTYLE_PROJECT, new FreestyleProjectDetailsPage(getDriver()))
                .clickBuildNowSeveralTimesAndWait(2, NAME_FREESTYLE_PROJECT)
                .getBuildsInBuildHistoryList();

        String previousBuildTitle = new FreestyleProjectDetailsPage(getDriver())
                .clickLastBuild()
                .clickPreviousBuild()
                .getBuildTitle();

        Assert.assertTrue(previousBuildTitle.contains(builds.get(1)));
    }
}