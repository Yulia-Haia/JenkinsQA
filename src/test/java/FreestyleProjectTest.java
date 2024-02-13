import org.openqa.selenium.By;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import model.*;
import model.errors.RenameErrorPage;
import model.jobs.configs.FreestyleProjectConfigurePage;
import model.jobs.details.FolderDetailsPage;
import model.jobs.details.FreestyleProjectDetailsPage;
import runner.BaseTest;
import runner.TestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.*;

public class FreestyleProjectTest extends BaseTest {

    private static final String PROJECT_NAME = "NewFreestyleProject";
    private static final String NEW_PROJECT_NAME = "NewFreestyleProjectName";
    private static final String PROJECT_DESCRIPTION = "Freestyle project description";
    private final static String NEW_PROJECT_DESCRIPTION = "New freestyle project description";
    private final static String PARAMETER_NAME = "Ņame";
    private final static String PARAMETER_DESCRIPTION = "Description";



    @Test
    public void testCreateFreestyleProjectWithValidName() {
        String homePage = new HomePage(getDriver())
                .clickNewItem()
                .createFreestyleProject(PROJECT_NAME)
                .clickSaveButton()
                .goHomePage()
                .getJobList()
                .toString();

        assertTrue(homePage.contains(PROJECT_NAME));
    }

    @Test
    public void testDeleteFreestyleProjectSideMenu() {
        boolean projectExist = new HomePage(getDriver())
                .clickNewItem()
                .createFreestyleProject("other" + PROJECT_NAME)
                .clickSaveButton()
                .goHomePage()
                .clickJobByName("other" + PROJECT_NAME, new FreestyleProjectDetailsPage(getDriver()))
                .deleteProject()
                .isProjectExist("other" + PROJECT_NAME);

        assertFalse(projectExist);
    }

    @Test(dependsOnMethods = "testCreateFreestyleProjectWithValidName")
    public void testRenameProject() {
        final HomePage homePage = new HomePage(getDriver())
                .goHomePage()
                .clickJobByName(PROJECT_NAME, new FreestyleProjectDetailsPage(getDriver()))
                .clickRename()
                .clearInputField()
                .enterName(NEW_PROJECT_NAME)
                .clickRenameButton()
                .goHomePage();

        assertTrue(homePage.isProjectExist(NEW_PROJECT_NAME));
    }

    @Test
    public void testErrorMessageWhenNewNameFieldEmpty() {
        final String expectedErrorMessage = "No name is specified";

        String actualErrorMessage = new HomePage(getDriver())
                .clickNewItem()
                .createFreestyleProject(PROJECT_NAME)
                .clickSaveButton()
                .clickRename()
                .clearInputField()
                .getErrorMessage();

        assertEquals(actualErrorMessage, expectedErrorMessage);
    }

    @Test
    public void testAddDescriptionFreestyleProject() {
        String actualDescription = new HomePage(getDriver())
                .clickNewItem()
                .createFreestyleProject(PROJECT_NAME)
                .inputDescription(PROJECT_DESCRIPTION)
                .clickSaveButton()
                .getDescriptionText();

        assertEquals(actualDescription, PROJECT_DESCRIPTION);
    }

    @Test
    public void testAddDescriptionFromStatusPage() {
        String actualDescription = new HomePage(getDriver())
                .clickNewItem()
                .createFreestyleProject(PROJECT_NAME)
                .goHomePage()
                .clickJobByName(PROJECT_NAME, new FreestyleProjectDetailsPage(getDriver()))
                .clickAddOrEditDescription()
                .insertDescriptionText(PROJECT_DESCRIPTION)
                .clickSaveDescriptionButton()
                .getDescriptionText();

        assertEquals(actualDescription, PROJECT_DESCRIPTION);
    }

    @Test(dependsOnMethods = "testAddDescriptionFreestyleProject")
    public void testEditDescriptionDetailsPage() {
        String actualNewDescriptionText = new HomePage(getDriver())
                .clickJobByName(PROJECT_NAME, new FreestyleProjectDetailsPage(getDriver()))
                .clickAddOrEditDescription()
                .deleteDescriptionText()
                .insertDescriptionText(NEW_PROJECT_DESCRIPTION)
                .clickSaveDescriptionButton()
                .getDescriptionText();

        assertEquals(actualNewDescriptionText, NEW_PROJECT_DESCRIPTION);
    }

    @Test(dependsOnMethods = "testAddDescriptionFreestyleProject")
    public void testDeleteTheExistingDescription() {
        String actualEmptyDescriptionInputField = new HomePage(getDriver())
                .clickJobByName(PROJECT_NAME, new FreestyleProjectDetailsPage(getDriver()))
                .clickAddOrEditDescription()
                .deleteDescriptionText()
                .clickSaveDescriptionButton()
                .getDescriptionText();

        assertEquals(actualEmptyDescriptionInputField, "");
    }

    @Test
    public void testTooltipDiscardOldBuildsIsVisible() {
        boolean tooltipIsVisible = new HomePage(getDriver())
                .clickNewItem()
                .createFreestyleProject(PROJECT_NAME)
                .tooltipDiscardOldBuildsIsVisible();

        assertTrue(tooltipIsVisible, "The tooltip is not displayed.");
    }

    @Test
    public void testDisableProjectFromDetailsPage() {
        boolean isEnabled = new HomePage(getDriver())
                .clickNewItem()
                .createFreestyleProject(PROJECT_NAME)
                .clickSaveButton()
                .clickEnableButton()
                .isProjectEnabled();

        assertFalse(isEnabled);
    }

    @Test
    public void testDisableProjectFromConfigurePage() {
        boolean isEnabled = new HomePage(getDriver())
                .clickNewItem()
                .createFreestyleProject(PROJECT_NAME)
                .goHomePage()
                .clickJobByName(PROJECT_NAME, new FreestyleProjectDetailsPage(getDriver()))
                .clickConfigure()
                .clickDisableEnableToggle()
                .clickSaveButton()
                .isProjectEnabled();

        assertFalse(isEnabled);
    }

    @Test
    public void testDisableProjectWhenCreating() {
        boolean isEnabled = new HomePage(getDriver())
                .clickNewItem()
                .createFreestyleProject(PROJECT_NAME)
                .clickDisableEnableToggle()
                .clickSaveButton()
                .isProjectEnabled();

        assertFalse(isEnabled);
    }

    @Test
    public void testEnableProjectFromStatusPage() {
        boolean isEnabled = new HomePage(getDriver())
                .clickNewItem()
                .createFreestyleProject(PROJECT_NAME)
                .clickDisableEnableToggle()
                .clickSaveButton()
                .clickEnableButton()
                .isProjectEnabled();

        assertTrue(isEnabled);
    }

    @Test
    public void testEnableProjectFromConfigurePage() {
        boolean isEnabled = new HomePage(getDriver())
                .clickNewItem()
                .createFreestyleProject(PROJECT_NAME)
                .clickDisableEnableToggle()
                .clickSaveButton()
                .clickConfigure()
                .clickDisableEnableToggle()
                .clickSaveButton()
                .isProjectEnabled();

        assertTrue(isEnabled);
    }

    @Test
    public void testWarningMessageOnStatusPageWhenDisabled() {
        final String expectedWarningMessage = "This project is currently disabled";

        String actualWarningMessage = new HomePage(getDriver())
                .clickNewItem()
                .createFreestyleProject(PROJECT_NAME)
                .clickDisableEnableToggle()
                .clickSaveButton()
                .getDisabledMessageText();

        assertEquals(actualWarningMessage, expectedWarningMessage);
    }

    @Test
    public void testEnableButtonOnStatusPageWhenDisabled() {
        final String expectedButtonName = "Enable";

        String actualButtonName = new HomePage(getDriver())
                .clickNewItem()
                .createFreestyleProject(PROJECT_NAME)
                .clickDisableEnableToggle()
                .clickSaveButton()
                .getDisableEnableButtonText();

        assertEquals(actualButtonName, expectedButtonName);
    }

    @Test
    public void testStatusDisabledOnDashboardWhenDisabled() {
        final String expectedProjectStatus = "Disabled";

        String actualProjectStatus = new HomePage(getDriver())
                .clickNewItem()
                .createFreestyleProject(PROJECT_NAME)
                .clickDisableEnableToggle()
                .clickSaveButton()
                .goHomePage()
                .getProjectBuildStatusByName(PROJECT_NAME);

        assertEquals(actualProjectStatus, expectedProjectStatus);
    }

    @Test
    public void testScheduleBuildButtonNotVisibleWhenProjectDisabled() {
        boolean isScheduleABuildButtonNotDisplayed = new HomePage(getDriver())
                .clickNewItem()
                .createFreestyleProject(PROJECT_NAME)
                .goHomePage()
                .clickJobByName(PROJECT_NAME, new FreestyleProjectDetailsPage(getDriver()))
                .clickDisableButton()
                .goHomePage()
                .isScheduleABuildButtonNotDisplayed(PROJECT_NAME);

        assertTrue(isScheduleABuildButtonNotDisplayed);
    }

    @Test
    public void testRedirectionToStatusPageAfterRenaming() {
        boolean isStatusPageSelected = new HomePage(getDriver())
                .clickNewItem()
                .createFreestyleProject(PROJECT_NAME)
                .clickSaveButton()
                .clickRename()
                .clearInputField()
                .enterName(NEW_PROJECT_NAME)
                .clickRenameButton()
                .isStatusPageSelected();

        assertTrue(isStatusPageSelected);
    }

    @DataProvider(name = "ValidName")
    public String[][] validCredentials() {
        return new String[][]{
                {"Акико"}, {"Ак,ко"}, {"Акико"}, {"Akiko"}, {"12345`67890"}
        };
    }

    @Test(description = "Creating new Freestyle project using valid data", dataProvider = "ValidName")
    public void testFreestyleProjectWithValidData(String name) {
        TestUtils.createFreestyleProject(this, name, true);

        String result = getDriver().findElement(By.xpath("//*[@id ='job_" + name + "']/td[3]/a/span")).getText();

        assertEquals(result, name);
    }

    @DataProvider(name = "InvalidName")
    public Object[][] invalidCredentials() {
        return new Object[][]{
                {"!"}, {"@"}, {"#"}, {"$"}, {"%"}, {"^"}, {"&"}, {"*"}, {"?"}, {"|"}, {"/"},
                {"["}
        };
    }

    @Test(description = "Creating new Freestyle project using invalid data", dataProvider = "InvalidName")
    public void testCreateWithInvalidData(String name) {
        String errorMessage = new HomePage(getDriver())
                .clickNewItem()
                .typeItemName(name)
                .selectFreestyleProject()
                .getInvalidNameErrorMessage();

        assertEquals(errorMessage, "» ‘" + name + "’ is an unsafe character");
    }

    @Test(description = "Creating new Freestyle project using invalid data", dataProvider = "InvalidName")
    public void testDisabledOkButtonCreateWithInvalidName(String name) {
        boolean enabledOkButton = new HomePage(getDriver())
                .clickNewItem()
                .typeItemName(name)
                .selectFreestyleProject()
                .isOkButtonEnabled();

        assertFalse(enabledOkButton);
    }

    @Test(description = "Creating Freestyle project using an empty name")
    public void testCreateWithEmptyName() {
        NewItemPage newItemPage = new HomePage(getDriver())
                .clickNewItem()
                .clickOkWithError(new NewItemPage(getDriver()));

        assertEquals(newItemPage.getRequiredNameErrorMessage(), "» This field cannot be empty, please enter a valid name");
        assertFalse(newItemPage.isOkButtonEnabled());
    }

    @Test
    public void testCreateWithDuplicateName() {
        RenameErrorPage errorPage = new HomePage(getDriver())
                .clickNewItem()
                .createFreestyleProject(PROJECT_NAME)
                .clickSaveButton()
                .clickRename()
                .clickRenameWithError();

        assertEquals(errorPage.getErrorText(), "The new name is the same as the current name.");
    }

    @Test
    public void testRenameToEmptyNameAndGoErrorPage() {
        String errorText = new HomePage(getDriver())
                .clickNewItem()
                .createFreestyleProject(PROJECT_NAME)
                .goHomePage()
                .clickJobByName(PROJECT_NAME, new FreestyleProjectDetailsPage(getDriver()))
                .clickRename()
                .clickRenameButtonEmptyName()
                .getErrorText();

        assertEquals(errorText, "No name is specified");
    }

    @Test(dependsOnMethods = "testRenameProject")
    public void testDisable() {
        FreestyleProjectDetailsPage detailsPage = new HomePage(getDriver())
                .clickJobByName(NEW_PROJECT_NAME, new FreestyleProjectDetailsPage(getDriver()))
                .clickDisableButton();

        assertTrue(detailsPage.isProjectDisabled());
    }

    @Test(dependsOnMethods = {"testDisable"})
    public void testEnable() {
        FreestyleProjectDetailsPage detailsPage = new HomePage(getDriver())
                .clickJobByName(NEW_PROJECT_NAME, new FreestyleProjectDetailsPage(getDriver()))
                .clickEnableButton();

        assertTrue(detailsPage.isProjectEnabled());
    }

    @Test(dependsOnMethods = "testTooltipDiscardOldBuildsIsVisible")
    public void testHelpDescriptionOfDiscardOldBuildsIsVisible() {
        String actualResult = new HomePage(getDriver())
                .clickJobByName(PROJECT_NAME, new FreestyleProjectDetailsPage(getDriver()))
                .clickConfigure()
                .clickHelpDescriptionOfDiscardOldBuilds()
                .getAttributeOfHelpDescriptionDiscardOldBuilds();

        assertEquals(actualResult, "display: block;");
    }

    @Test(dependsOnMethods = "testTooltipDiscardOldBuildsIsVisible")
    public void testHelpDescriptionOfDiscardOldBuildsIsClosed() {
        String actualResult = new HomePage(getDriver())
                .clickJobByName(PROJECT_NAME, new FreestyleProjectDetailsPage(getDriver()))
                .clickConfigure()
                .clickHelpDescriptionOfDiscardOldBuilds()
                .clickHelpDescriptionOfDiscardOldBuilds()
                .getAttributeOfHelpDescriptionDiscardOldBuilds();


        assertEquals(actualResult, "display: none;");
    }

    @Test
    public void testSelectThisProjectIsParameterizedCheckbox() {
        boolean isAddParameterButtonDisplayed = new HomePage(getDriver())
                .clickNewItem()
                .createFreestyleProject(PROJECT_NAME)
                .goHomePage()
                .clickJobByName(PROJECT_NAME, new FreestyleProjectDetailsPage(getDriver()))
                .clickConfigure()
                .clickThisProjectIsParameterizedCheckbox()
                .isAddParameterButtonDisplayed();

        assertTrue(isAddParameterButtonDisplayed);
    }

    @Test
    public void testFreestyleProjectConfigureIsParameterizedCheckboxSelected() {
        boolean isParametrizedProjectCheckboxSelected = new HomePage(getDriver())
                .clickNewItem()
                .createFreestyleProject(PROJECT_NAME)
                .clickSaveButton()
                .goHomePage()
                .clickJobByName(PROJECT_NAME, new FreestyleProjectDetailsPage(getDriver()))
                .clickConfigure()
                .clickThisProjectIsParameterizedCheckbox()
                .isParametrizedProjectCheckboxSelected();

        assertTrue(isParametrizedProjectCheckboxSelected);
    }

    @Test
    public void testOldBuildsAreDiscarded() {
        final int numOfBuildNowClicks = 2;

        TestUtils.createFreestyleProject(this, PROJECT_NAME, false);

        List<String> buildsList = new FreestyleProjectDetailsPage(getDriver())

                .clickConfigure()
                .clickDiscardOldBuildsCheckBox()
                .inputMaxNumberOfBuildsToKeep(String.valueOf(numOfBuildNowClicks))
                .clickSaveButton()
                .clickBuildNowSeveralTimes(numOfBuildNowClicks + 1)
                .refreshPage()
                .getBuildsInBuildHistoryList();

        assertEquals(buildsList.get(buildsList.size() - 1), "#2");
    }

    @Test(dependsOnMethods = "testRenameProject")
    public void testEditDescriptionConfigurePage() {
        String editDescription = new HomePage(getDriver())
                .clickJobByName(NEW_PROJECT_NAME, new FreestyleProjectDetailsPage(getDriver()))
                .clickConfigure()
                .inputProjectDescription(PROJECT_DESCRIPTION)
                .clickSaveButton()
                .clickConfigure()
                .inputProjectDescription(NEW_PROJECT_DESCRIPTION)
                .clickSaveButton()
                .getDescriptionText();

        assertEquals(editDescription, NEW_PROJECT_DESCRIPTION);
    }

    @Ignore("no such element: Unable to locate element: {\"method\":\"xpath\",\"selector\":\"//td/a[@href='job/NewFreestyleProject/']\"}, change dependencies for testRename")
    @Test(dependsOnMethods = "testCreateFreestyleProjectWithValidName")
    public void testFreestyleProjectAdvancedSettingVisibilityOfHelpDescriptionQuietPeriod() {
        boolean helpMessageDisplay = new HomePage(getDriver())
                .clickJobByName(PROJECT_NAME, new FreestyleProjectDetailsPage(getDriver()))
                .clickConfigure()
                .clickAdvancedButton()
                .clickQuietPeriodHelpIcon()
                .isQuietPeriodHelpTextDisplayed();

        assertTrue(helpMessageDisplay);
    }

    @Test(dependsOnMethods = "testRenameProject")
    public void testStatusPageUrlCheck() {
        String currentUrl = new HomePage(getDriver())
                .clickJobByName(NEW_PROJECT_NAME, new FreestyleProjectDetailsPage(getDriver()))
                .getCurrentUrl();

        assertTrue(currentUrl.contains("/job/" + NEW_PROJECT_NAME.replace(" ", "%20")));
    }

    @Ignore
    @Test
    public void testDisableFreestyleProjectFromFreestyleProjectDetailPage() {
        String warningMessage = new HomePage(getDriver())
                .clickNewItem()
                .createFreestyleProject(PROJECT_NAME)
                .goHomePage()
                .clickJobByName(PROJECT_NAME, new FreestyleProjectDetailsPage(getDriver()))
                .clickDisableButton()
                .getDisabledMessageText();

        assertEquals(warningMessage, "This project is currently disabled");
    }

    @Test(dependsOnMethods = "testOldBuildsAreDiscarded")
    public void testSetUpstreamProject() {
        final String upstreamProjectName = "Upstream Test";

        TestUtils.createFreestyleProject(this, upstreamProjectName, true);

        List<String> upstreamProjectsList = new HomePage(getDriver())
                .clickJobByName(PROJECT_NAME, new FreestyleProjectDetailsPage(getDriver()))
                .clickConfigure()
                .clickBuildAfterOtherProjectsAreBuilt()
                .inputUpstreamProject(upstreamProjectName)
                .clickAlwaysTrigger()
                .clickSaveButton()
                .waitAndRefresh()
                .getUpstreamProjectsList();

        assertEquals(upstreamProjectsList, List.of(upstreamProjectName));
    }

    @Test
    public void testSettingsOfDiscardOldBuildsIsDisplayed() {
        boolean isDiscardOldBuildsSettingsFieldDisplayed = new HomePage(getDriver())
                .clickNewItem()
                .createFreestyleProject(PROJECT_NAME)
                .clickDiscardOldBuildsCheckBox()
                .isDiscardOldBuildsSettingsFieldDisplayed();

        assertTrue(isDiscardOldBuildsSettingsFieldDisplayed);
    }

    @Test(dependsOnMethods = "testSettingsOfDiscardOldBuildsIsDisplayed")
    public void testDaysToKeepBuildsErrorMessageIsDisplayed() {

        String errorMessage = new HomePage(getDriver())
                .clickJobByName(PROJECT_NAME, new FreestyleProjectDetailsPage(getDriver()))
                .clickConfigure()
                .clickDiscardOldBuildsCheckBox()
                .inputDaysToKeepBuilds("-2")
                .clickApply()
                .getErrorMessageText();

        assertEquals(errorMessage, "Not a positive integer");
    }

    @Test
    public void testGitRadioButtonSettingsIsDisplayed() {
        boolean areSettingsDisplayed = new HomePage(getDriver())
                .clickNewItem()
                .createFreestyleProject(PROJECT_NAME)
                .clickGitRadioButtonWithScroll()
                .isGitRadioButtonSettingsFormDisplayed();

        assertTrue(areSettingsDisplayed);
    }

    @Ignore
    @Test
    public void testVerifyValueOfInsertedGitSourceLink() {
        final String inputText = "123";

        String repositoryUrlText = new HomePage(getDriver())
                .clickNewItem()
                .createFreestyleProject(PROJECT_NAME)
                .clickGitRadioButtonWithScroll()
                .inputGitHubRepositoryURLWithScroll(inputText)
                .clickApply()
                .refreshPage()
                .clickGitRadioButtonWithScroll()
                .getValueGitHubRepositoryURL();

        assertEquals(repositoryUrlText, inputText);
    }

    @Test
    public void testAddBuildStep() {
        final String buildStepTitle = "buildStep";

        String shellScript = new HomePage(getDriver())
                .clickNewItem()
                .createFreestyleProject(PROJECT_NAME)
                .clickAddBuildStepsDropdown()
                .clickExecuteShellOption()
                .inputShellScript(buildStepTitle)
                .clickSaveButton()
                .clickConfigure()
                .getShellScriptText();

        assertEquals(shellScript, buildStepTitle);
    }

    @Test
    public void testGitHubEditedLabelAppears() {
        boolean isLabelAppears = new HomePage(getDriver())
                .clickNewItem()
                .createFreestyleProject(PROJECT_NAME)
                .clickCheckboxGitHubProject()
                .clickAdvancedDropdownGitHubProjectWithScroll()
                .inputDisplayNameGitHubProject("GitHubURL")
                .isEditedLabelInGitHubProjectDisplayed();

        assertTrue(isLabelAppears);
    }
    @Ignore("expected [Freestyle project description] but found []")
    @Test
    public void testDescriptionPreviewAppears() {
        String previewText = new HomePage(getDriver())
                .clickNewItem()
                .createFreestyleProject(PROJECT_NAME)
                .inputDescription(PROJECT_DESCRIPTION)
                .clickApply()
                .clickPreviewDescription()
                .getPreviewDescriptionText();

        assertEquals(previewText, PROJECT_DESCRIPTION);
    }
    @Ignore
    @Test(dependsOnMethods = "testDescriptionPreviewAppears")
    public void testDescriptionPreviewHides() {
        boolean isTextDisplayed = new HomePage(getDriver())
                .clickJobByName(PROJECT_NAME, new FreestyleProjectDetailsPage(getDriver()))
                .clickConfigure()
                .clickPreviewDescription()
                .clickHidePreviewDescription()
                .isPreviewDescriptionTextDisplayed();

        assertFalse(isTextDisplayed);
    }

    @Test
    public void testSetNumberDaysToKeepBuildsIsSaved() {
        final String daysToKeepBuilds = "2";

        String daysToKeepBuildsFieldValue = new HomePage(getDriver())
                .clickNewItem()
                .createFreestyleProject(PROJECT_NAME)
                .clickDiscardOldBuildsCheckBox()
                .inputDaysToKeepBuilds(daysToKeepBuilds)
                .clickSaveButton()
                .clickConfigure()
                .getInputDaysToKeepBuildsFieldValue();

        assertEquals(daysToKeepBuildsFieldValue, daysToKeepBuilds);
    }

    @Test(dependsOnMethods = "testThisProjectIsParameterizedCheckboxAddBooleanParameter")
    public void testSavedNotificationIsDisplayed() {

        String notificationMessage = new HomePage(getDriver())
                .clickJobByName(PROJECT_NAME, new FreestyleProjectDetailsPage(getDriver()))
                .clickConfigure()
                .clickApply()
                .getSavedNotificationMessage();

        assertEquals(notificationMessage, "Saved");
    }

    // @Ignore
    @Test
    public void testRenameProjectFromDashboard() {
        new HomePage(getDriver())
                .clickNewItem()
                .createFreestyleProject(PROJECT_NAME)
                .clickSaveButton()
                .goHomePage()
                .clickJobNameDropdown(PROJECT_NAME)
                .clickRenameInDropdownMenu(new FreestyleProjectDetailsPage(getDriver()))
                .enterName(NEW_PROJECT_NAME)
                .clickRenameButton()
                .goHomePage();

        assertTrue(new HomePage(getDriver())
                .isProjectExist(NEW_PROJECT_NAME));
    }

    @Test
    public void testConfigureBuildEnvironmentSettingsAddTimestamp() {

        TestUtils.createFreestyleProject(this, PROJECT_NAME, true);

        List<String> timestampsList = new HomePage(getDriver())
                .clickJobByName(PROJECT_NAME, new FreestyleProjectDetailsPage(getDriver()))
                .clickConfigure()
                .clickAddTimestampsToConsoleOutput()
                .clickSaveButton()
                .clickBuildNow()
                .waitAndRefresh()
                .clickBuildIconInBuildHistory().getTimestampsList();

        assertNotEquals(timestampsList.size(), 0);
        for (String timestamp : timestampsList) {
            assertTrue(timestamp.trim().matches("[0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}"));
        }
    }

    @Test
    public void testMoveFreestyleProjectToFolder() {
        final String folderName = "FolderWrapper";

        TestUtils.createFolder(this, folderName, true);
        TestUtils.createFreestyleProject(this, PROJECT_NAME, false);

        boolean isJobInJobsListInDestinationFolder = new FreestyleProjectDetailsPage(getDriver())
                .clickMove()
                .clickArrowDropDownMenu()
                .clickFolderByName(folderName)
                .clickMoveFromMovePage()
                .goHomePage()
                .clickJobByName(folderName, new FolderDetailsPage(getDriver()))
                .isJobInJobsList(PROJECT_NAME);

        assertTrue(isJobInJobsListInDestinationFolder);
    }

    @Test
    public void testThisProjectIsParameterizedCheckboxAddBooleanParameter() {
        TestUtils.createFreestyleProject(this, PROJECT_NAME, true);

        FreestyleProjectConfigurePage configurePage = new HomePage(getDriver())
                .clickJobByName(PROJECT_NAME, new FreestyleProjectDetailsPage(getDriver()))
                .clickConfigure()
                .clickThisProjectIsParameterizedCheckbox()
                .clickAddParameter()
                .clickBooleanParameterOption()
                .inputParameterName(PARAMETER_NAME)
                .inputParameterDescription(PARAMETER_DESCRIPTION)
                .clickSaveButton()
                .goHomePage()
                .clickJobByName(PROJECT_NAME, new FreestyleProjectDetailsPage(getDriver()))
                .clickConfigure();

        assertTrue(configurePage.getParameterName().equals(PARAMETER_NAME) &&
                configurePage.getParameterDescription().equals(PARAMETER_DESCRIPTION));
    }

    @Test(dependsOnMethods = "testRenameProject")
    public void testAddBooleanParameterDropdownIsSortedAlphabetically() {
        List<String> expectedResult = List.of(
                "Boolean Parameter",
                "Choice Parameter",
                "Credentials Parameter",
                "File Parameter",
                "Multi-line String Parameter",
                "Password Parameter",
                "Run Parameter",
                "String Parameter");

        FreestyleProjectConfigurePage freestyleProjectConfigurePage = new HomePage(getDriver())
                .clickJobByName(NEW_PROJECT_NAME, new FreestyleProjectDetailsPage(getDriver()))
                .clickConfigure()
                .clickThisProjectIsParameterizedCheckbox()
                .clickAddParameter();

        assertEquals(freestyleProjectConfigurePage.getAddParameterDropdownText()
                , expectedResult);
    }

    @Test(dependsOnMethods = "testTooltipDiscardOldBuildsIsVisible")
    public void testPermalinksListOnStatusPage() {
        final String[] buildSuccessfulPermalinks = {
                "Last build",
                "Last stable build",
                "Last successful build",
                "Last completed build"
        };

        String permaLinks = new HomePage(getDriver())
                .clickJobByName(PROJECT_NAME, new FreestyleProjectDetailsPage(getDriver()))
                .clickBuildNow()
                .refreshPage()
                .getPermalinksText();

        for (String x : buildSuccessfulPermalinks) {
            assertTrue(permaLinks.contains(x));
        }
    }

    @Test
    public void testDiscardOldBuildsDaysAndMaxNumberSaved() {
        String inputDaysToKeepBuilds = new HomePage(getDriver())
                .clickNewItem()
                .createFreestyleProject(PROJECT_NAME)
                .clickDiscardOldBuildsCheckBox()
                .scrollPage(0, 300)
                .inputMaxNumberOfBuildsToKeep("2")
                .inputDaysToKeepBuilds("3")
                .clickSaveButton()
                .clickConfigure()
                .scrollPage(0, 300)
                .getInputDaysToKeepBuildsFieldValue();

        String inputMaxNumberOfBuildsToKeep = new FreestyleProjectConfigurePage(getDriver())
                .getInputMaxNumberOfBuildsToKeepFieldValue();

        assertEquals(inputDaysToKeepBuilds, "3");
        assertEquals(inputMaxNumberOfBuildsToKeep, "2");
    }

    @Test
    public void testThrottleBuildsNumberAndPeriodSaved() {
        String numberOfBuilds = new HomePage(getDriver())
                .clickNewItem()
                .createFreestyleProject(PROJECT_NAME)
                .scrollPage(0, 400)
                .clickThrottleBuildsCheckBox()
                .inputNumberOfBuilds("4")
                .selectTimePeriod("day")
                .clickSaveButton()
                .clickConfigure()
                .scrollPage(0, 600)
                .getNumberOfBuildsFieldValue();

        String timePeriod = new FreestyleProjectConfigurePage(getDriver())
                .getTimePeriodFieldValue();

        assertEquals(numberOfBuilds, "4");
        assertEquals(timePeriod, "day");
    }

    @Test
    public void testSelectExecuteConcurrentBuilds() {

        TestUtils.createFreestyleProject(this, PROJECT_NAME, true);

        String checkBoxDisplayStyle = new HomePage(getDriver())
                .clickJobByName(PROJECT_NAME, new FreestyleProjectDetailsPage(getDriver()))
                .clickConfigure()
                .scrollPage(0, 300)
                .clickExecuteConcurrentBuildsIfNecessaryCheckBox()
                .clickSaveButton()
                .clickConfigure()
                .scrollPage(0, 300)
                .getExecuteConcurrentBuildsIfNecessaryCheckBoxValue("display");

        assertNotEquals(checkBoxDisplayStyle, "none");
    }

    @Test
    public void testIsWorkspaceCreated() {

        TestUtils.createFreestyleProject(this, PROJECT_NAME, true);

        String titleBeforeWorkspaceCreating = new HomePage(getDriver())
                .clickJobByName(PROJECT_NAME, new FreestyleProjectDetailsPage(getDriver()))
                .goToWorkspaceFromSideMenu()
                .getHeadLineText();

        assertEquals(titleBeforeWorkspaceCreating, "Error: no workspace");

        String titleAfterWorkspaceCreating = new HomePage(getDriver())
                .goHomePage()
                .clickBuildByGreenArrow(PROJECT_NAME)
                .clickJobByName(PROJECT_NAME, new FreestyleProjectDetailsPage(getDriver()))
                .goToWorkspaceFromSideMenu()
                .getHeadLineText();

        assertEquals(titleAfterWorkspaceCreating, "Workspace of " + PROJECT_NAME + " on Built-In Node");
    }

    @Test
    public void testDisableProjectMessage() {
        boolean isMessageVisible = new HomePage(getDriver())
                .clickNewItem()
                .createFreestyleProject(PROJECT_NAME)
                .goHomePage()
                .clickJobByName(PROJECT_NAME, new FreestyleProjectDetailsPage(getDriver()))
                .clickDisableButton()
                .isProjectDisabled();

        assertTrue(isMessageVisible, "The warning message is not visible.");
    }

    @Ignore
    @Test(dependsOnMethods = "testTooltipDiscardOldBuildsIsVisible")
    public void testDeletePermalinksOnProjectsStatusPage() {
        final List<String> removedPermalinks = List.of(
                "Last build",
                "Last stable build",
                "Last successful build",
                "Last completed build");

        String permaLinks = new HomePage(getDriver())
                .clickJobByName(PROJECT_NAME, new FreestyleProjectDetailsPage(getDriver()))
                .clickBuildNow()
                .refreshPage()
                .clickPermalinkLastBuild()
                .clickDeleteBuildSidePanel()
                .clickButtonDeleteBuild(new FreestyleProjectDetailsPage(getDriver()))
                .getPermalinksText();

        for (String x : removedPermalinks) {
            assertFalse(permaLinks.contains(x));
        }
    }
    @Ignore("stale element reference: stale element not found(..)")
    @Test(dependsOnMethods = "testDeletePermalinksOnProjectsStatusPage")
    public void testRenameUnsafeCharacters() {
        final List<String> unsafeCharacters = List.of("%", "<", ">", "[", "]", "&", "#", "|", "/", "^");

        RenamePage<FreestyleProjectDetailsPage> renamePage = new HomePage(getDriver())
                .clickJobByName(PROJECT_NAME, new FreestyleProjectDetailsPage(getDriver()))
                .clickRename();

        for (String x : unsafeCharacters) {
            renamePage.enterName(x);

            assertEquals(renamePage.getErrorMessage(), "‘" + x + "’ is an unsafe character");
        }
    }

    @Test
    public void testParameterizedBuildWithChoices() {
        List<String> choices = List.of("Chrome", "Firefox", "Edge", "Safari");
        final String choiceName = "browsers";

        TestUtils.createFreestyleProject(this, PROJECT_NAME, false);

        List<String> actualChoiceList = new FreestyleProjectDetailsPage(getDriver())
                .clickConfigure()
                .clickThisProjectIsParameterizedCheckbox()
                .clickAddParameter()
                .selectParameterType("Choice Parameter")
                .inputParameterName(choiceName)
                .setParameterChoices(choices)
                .clickSaveButton()
                .clickBuildWithParameters()
                .getChoiceParameterOptions();

        assertEquals(actualChoiceList, choices);
    }

    @Test
    public void testVerify7ItemsSidePanelDetailsPage() {
        final List<String> itemsExpected = new ArrayList<>(Arrays.asList("Status", "Changes", "Workspace", "Build Now", "Configure", "Delete Project", "Rename"));

        TestUtils.createFreestyleProject(this, PROJECT_NAME, true);

        List<String> itemsActual = new HomePage(getDriver())
                .clickJobByName(PROJECT_NAME, new FreestyleProjectDetailsPage(getDriver()))
                .getTextItemsSidePanel();

        assertEquals(itemsActual, itemsExpected);
    }
}