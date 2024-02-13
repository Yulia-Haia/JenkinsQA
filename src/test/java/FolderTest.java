import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import model.*;
import model.errors.ErrorPage;
import model.jobs.details.FolderDetailsPage;
import model.jobs.details.PipelineDetailsPage;
import runner.BaseTest;

import java.util.List;

public class FolderTest extends BaseTest {
    private static final String FOLDER_NAME = "FolderName";
    private static final String NAME_FOR_BOUNDARY_VALUES = "A";
    private static final String RENAMED_FOLDER = "RenamedFolder";
    private static final String NESTED_FOLDER = "NestedFolder";
    private static final String JOB_NAME = "New Job";
    private static final String PIPELINE_PROJECT_NAME = "New pipeline project";
    private static final String DESCRIPTION_NAME = "Description Name";

    @DataProvider
    public Object[][] provideUnsafeCharacters() {

        return new Object[][]{
                {"#"}, {"&"}, {"?"}, {"!"}, {"@"}, {"$"}, {"%"}, {"^"}, {"*"}, {"|"}, {"/"}, {"\\"}, {"<"}, {">"},
                {"["}, {"]"}, {":"}, {";"}
        };
    }

    @Test
    public void testCreate() {
        HomePage homePage = new HomePage(getDriver())
                .clickNewItem()
                .createFolder(FOLDER_NAME)
                .clickSaveButton()
                .goHomePage();

        Assert.assertTrue(homePage.getJobList().contains(FOLDER_NAME));
    }

    @Test(dependsOnMethods = "testCreate")
    public void testRename() {
        HomePage homePage = new HomePage(getDriver())
                .clickJobByName(FOLDER_NAME, new FolderDetailsPage(getDriver()))
                .clickRename()
                .enterName(RENAMED_FOLDER)
                .clickRenameButton()
                .goHomePage();

        Assert.assertTrue(homePage.getJobList().contains(RENAMED_FOLDER));
    }

    @Test(dependsOnMethods = "testRename")
    public void testCreateNewJob() {
        boolean isJobCreated = new HomePage(getDriver())
                .clickJobByName(RENAMED_FOLDER, new FolderDetailsPage(getDriver()))
                .clickCreateJob()
                .createFreestyleProject(JOB_NAME)
                .clickSaveButton()
                .isJobExist();

        Assert.assertTrue(isJobCreated);
    }

    @Test(dependsOnMethods = "testCreateNewJob")
    public void testMoveFolderToFolder() {
        FolderDetailsPage folderDetailsPage = new model.HomePage(getDriver())
                .clickNewItem()
                .createFolder(NESTED_FOLDER)
                .goHomePage()
                .clickJobByName(NESTED_FOLDER, new FolderDetailsPage(getDriver()))
                .clickMove()
                .clickArrowDropDownMenu()
                .clickFolderByName(RENAMED_FOLDER)
                .clickMoveFromMovePage()
                .goHomePage()
                .clickJobByName(RENAMED_FOLDER, new FolderDetailsPage(getDriver()));

        Assert.assertTrue(folderDetailsPage.getJobListInsideFolder().contains(NESTED_FOLDER));
    }

    @Test(dependsOnMethods = {"testCreate", "testRename"})
    public void testAddDisplayName() {
        final String expectedFolderDisplayName = "Best folder";

        List<String> jobList = new model.HomePage(getDriver())
                .clickJobByName(RENAMED_FOLDER, new FolderDetailsPage(getDriver()))
                .clickConfigure()
                .typeDisplayName(expectedFolderDisplayName)
                .clickSaveButton()
                .goHomePage()
                .getJobList();

        Assert.assertTrue(jobList.contains(expectedFolderDisplayName));
    }

    @Test
    public void testErrorMessageIsDisplayedWithoutFolderName() {
        String expectedErrorMessage = "» This field cannot be empty, please enter a valid name";

        String actualErrorMessage = new model.HomePage(getDriver())
                .clickNewItem()
                .selectItemFolder()
                .getRequiredNameErrorMessage();

        Assert.assertEquals(actualErrorMessage, expectedErrorMessage);
    }

    @Test
    public void testOKButtonIsNotClickableWithoutFolderName() {
        boolean isOkButtonDisabled = new model.HomePage(getDriver())
                .clickNewItem()
                .selectItemFolder()
                .isOkButtonEnabled();

        Assert.assertFalse(isOkButtonDisabled, "OK button is clickable when it shouldn't be!");
    }

    @Test
    public void testCreatedPipelineWasBuiltSuccessfullyInCreatedFolder() {
        String actualTooltipValue = new model.HomePage(getDriver())
                .clickNewItem()
                .createFolder(FOLDER_NAME)
                .clickSaveButton()
                .clickNewItemButton()
                .createPipeline(JOB_NAME)
                .clickSaveButton()
                .clickBuildNowButton()
                .getTooltipAttributeValue();

        Assert.assertEquals(actualTooltipValue, "Success > Console Output");
    }

    @Ignore
    @Test(dependsOnMethods = "testCreatedPipelineWasBuiltSuccessfullyInCreatedFolder")
    public void testDeletePipelineInsideOfFolder() {
        int sizeOfEmptyJobListInsideOfFolderAfterJobDeletion = new model.HomePage(getDriver())
                .clickJobByName(FOLDER_NAME, new FolderDetailsPage(getDriver()))
                .clickJobByName(JOB_NAME, new PipelineDetailsPage(getDriver()))
                .deletePipelineJobInsideOfFolder()
                .getJobListInsideFolder().size();

        Assert.assertEquals(sizeOfEmptyJobListInsideOfFolderAfterJobDeletion, 0);
    }

    @Test(dataProvider = "provideUnsafeCharacters")
    public void testCreateNameSpecialCharactersGetMessage(String unsafeChar) {
        String errorMessage = new model.HomePage(getDriver())
                .clickNewItem()
                .typeItemName(unsafeChar)
                .selectItemFolder()
                .getInvalidNameErrorMessage();

        Assert.assertEquals(errorMessage, "» ‘" + unsafeChar + "’ is an unsafe character");
    }

    @Test(dataProvider = "provideUnsafeCharacters")
    public void testDisabledOkButtonCreateWithInvalidName(String unsafeChar) {
        boolean enabledOkButton = new HomePage(getDriver())
                .clickNewItem()
                .typeItemName(unsafeChar)
                .selectItemFolder()
                .isOkButtonEnabled();

        Assert.assertFalse(enabledOkButton);
    }

    @Test
    public void testPositiveBoundaryValuesName() {
        String listJob = new HomePage(getDriver())
                .clickNewItem()
                .createFolder(NAME_FOR_BOUNDARY_VALUES)
                .goHomePage()
                .clickNewItem()
                .createFolder(NAME_FOR_BOUNDARY_VALUES.repeat(255))
                .goHomePage()
                .getJobList()
                .toString();

        Assert.assertTrue(listJob.contains(NAME_FOR_BOUNDARY_VALUES));
        Assert.assertTrue(listJob.contains(NAME_FOR_BOUNDARY_VALUES.repeat(255)));
    }

    @Test
    public void testNegativeBoundaryValuesNameGetErrorMessage() {
        String errorMessage = new HomePage(getDriver())
                .clickNewItem()
                .typeItemName(NAME_FOR_BOUNDARY_VALUES.repeat(256))
                .selectItemFolder()
                .clickOkWithError(new ErrorPage(getDriver()))
                .getErrorMessageFromOopsPage();

        Assert.assertEquals(errorMessage, "A problem occurred while processing the request.");
    }

    @Test
    public void testNegativeBoundaryValuesNameAbsenceOnHomePage() {
        String listJob = new HomePage(getDriver())
                .clickNewItem()
                .typeItemName(NAME_FOR_BOUNDARY_VALUES.repeat(256))
                .selectItemFolder()
                .clickOkWithError(new ErrorPage(getDriver()))
                .goHomePage()
                .getJobList()
                .toString();

        Assert.assertFalse(listJob.contains(NAME_FOR_BOUNDARY_VALUES.repeat(256)));
    }

    @Test(dependsOnMethods = "testRename")
    public void testAddDescriptionToFolder() {
        String actualDescription = new HomePage(getDriver())
                .clickJobByName(RENAMED_FOLDER,new FolderDetailsPage(getDriver()))
                .clickAddOrEditDescription()
                .inputDescription(DESCRIPTION_NAME)
                .clickSaveDescriptionButton()
                .getDescriptionText();

        Assert.assertEquals(actualDescription, DESCRIPTION_NAME);
    }

    @Test(dependsOnMethods = "testMoveFolderToFolder")
    public void testClickPreview() {
        String previewDescription = new HomePage(getDriver())
                .clickJobByName(RENAMED_FOLDER, new FolderDetailsPage(getDriver()))
                .clickAddOrEditDescription()
                .inputDescription(DESCRIPTION_NAME)
                .clickDescriptionPreview()
                .getDescriptionPreviewText();

        Assert.assertEquals(previewDescription, DESCRIPTION_NAME);
    }

    @Test(dependsOnMethods = "testClickPreview")
    public void testHidePreview() {
        String previewDescription = new HomePage(getDriver())
                .clickJobByName(RENAMED_FOLDER, new FolderDetailsPage(getDriver()))
                .clickAddOrEditDescription()
                .clickDescriptionPreview()
                .clickHideDescriptionPreview()
                .getDescriptionPreviewText();

        Assert.assertTrue(previewDescription.isEmpty());
    }

    @Test(dependsOnMethods = {"testAddDescriptionToFolder", "testRename", "testCreate"})
    public void testEditDescriptionOfFolder() {
        final String newDescriptionText = "This is new Folder's description";

        String actualUpdatedDescription = new HomePage(getDriver())
                .clickJobByName(RENAMED_FOLDER, new FolderDetailsPage(getDriver()))
                .clickAddOrEditDescription()
                .inputDescription(newDescriptionText)
                .clickSaveDescriptionButton()
                .getDescriptionText();

        Assert.assertEquals(actualUpdatedDescription, newDescriptionText);
    }

    @Test(dependsOnMethods = {"testAddDescriptionToFolder"})
    public void testDeleteDescriptionOfFolder() {
        FolderDetailsPage folderDescription = new HomePage(getDriver())
                .clickJobByName(RENAMED_FOLDER, new FolderDetailsPage(getDriver()))
                .clickAddOrEditDescription()
                .clearDescriptionTextArea()
                .clickSaveDescriptionButton();

        Assert.assertTrue(folderDescription.getDescriptionText().isEmpty());
        Assert.assertEquals(folderDescription.getAddDescriptionButtonText(), "Add description");
    }

    @Test(dependsOnMethods = {"testCreate", "testRename"})
    public void testRenameWithEndingPeriod() {
        final String point = ".";

        String errorMessage = new HomePage(getDriver())
                .clickJobByName(RENAMED_FOLDER, new FolderDetailsPage(getDriver()))
                .clickRename()
                .enterName(point)
                .clickRenameWithError()
                .getErrorText();

        Assert.assertEquals(errorMessage, "“.” is not an allowed name");
    }

    @Test(dependsOnMethods = {"testCreate", "testRenameWithEndingPeriod"})
    public void testRenameFolderThroughLeftPanelWithEmptyName() {
        String errorMessage = new HomePage(getDriver())
                .clickJobByName(RENAMED_FOLDER, new FolderDetailsPage(getDriver()))
                .clickRename()
                .enterName("")
                .clickRenameWithError()
                .getErrorText();

        Assert.assertEquals(errorMessage, "No name is specified");
    }
    @Ignore("expected [Description Name] but found []")
    @Test(dependsOnMethods = {"testCreate", "testRename", "testRenameWithEndingPeriod", "testRenameFolderThroughLeftPanelWithEmptyName"})
    public void testFolderDescriptionPreviewWorksCorrectly() {

        String previewText = new HomePage(getDriver())
                .clickJobByName(RENAMED_FOLDER, new FolderDetailsPage(getDriver()))
                .clickConfigure()
                .typeDescription(DESCRIPTION_NAME)
                .clickPreviewDescription()
                .getFolderDescription();

        Assert.assertEquals(previewText, DESCRIPTION_NAME);
    }

    @Ignore
    @Test
    public void testAddChildHealthMetric()  {

        boolean isChildHealthMetricDisplayed = new HomePage(getDriver())
                .clickNewItem()
                .createFolder(FOLDER_NAME)
                .clickHealthMetricsInSideMenu()
                .clickHealthMetrics()
                .clickAddHealthMetric()
                .selectChildHealthMetric()
                .clickSaveButton()
                .clickConfigure()
                .clickHealthMetrics()
                .isChildHealthMetricDisplayed();

        Assert.assertTrue(isChildHealthMetricDisplayed);
    }

    @Ignore
    @Test(dependsOnMethods = "testAddChildHealthMetric")
    public void testDisplayingHelpTextButtonRecursive() {
        final String expectedText = "Controls whether items within sub-folders will be considered as contributing to the health of this folder.";

        String helpText = new HomePage(getDriver())
                .clickJobByName(FOLDER_NAME, new FolderDetailsPage(getDriver()))
                .clickConfigure()
                .clickHealthMetrics()
                .clickHelpButtonRecursive()
                .getHelpBlockText();

        Assert.assertEquals(helpText, expectedText);
    }

    @Ignore
    @Test (dependsOnMethods = "testCreateNewJob")
    public void testCreatePipelineProjectInsideFolder() {

        boolean isNewCreatedProjectDisplayed = new HomePage(getDriver())
                .clickFolderName(RENAMED_FOLDER)
                .clickCreateAJob()
                .createPipeline(PIPELINE_PROJECT_NAME)
                .clickSaveButton()
                .clickFolderBreadCrumbs()
                .isNewCreatedProjectDisplayed();

        Assert.assertTrue(isNewCreatedProjectDisplayed);
    }
}