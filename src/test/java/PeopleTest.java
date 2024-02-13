import org.testng.Assert;
import org.testng.annotations.Test;
import model.HomePage;
import runner.BaseTest;

public class PeopleTest extends BaseTest {
    @Test
    public void largeIconSizeTable() {
        final int[] largeIconExpectedResult = {64, 48};

        int[] largeIconActualResult = new HomePage(getDriver())
                .clickPeople()
                .clickLargeIcon()
                .iconActualResult();

        Assert.assertEquals(largeIconExpectedResult,largeIconActualResult);
    }

    @Test
    public void mediumIconSizeTable() {
        final int[] mediumIconExpectedResult = {64, 40};

        int[] mediumIconActualResult = new HomePage(getDriver())
                .clickPeople()
                .clickMediumIcon()
                .iconActualResult();

        Assert.assertEquals(mediumIconExpectedResult,mediumIconActualResult);
    }
    @Test
    public void smallIconSizeTable() {
        final int[] smallIconExpectedResult = {64, 34};

        int[] smallIconActualResult = new HomePage(getDriver())
                .clickPeople()
                .clickSmallIcon()
                .iconActualResult();

        Assert.assertEquals(smallIconExpectedResult,smallIconActualResult);
    }
}
