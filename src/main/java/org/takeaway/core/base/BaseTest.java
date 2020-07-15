package org.takeaway.core.base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.apache.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;

public class BaseTest {

    protected final Logger log = Logger.getLogger(getClass());

    protected ExtentSparkReporter spark;
    protected ExtentReports extent;
    protected ExtentTest test;

    @BeforeClass(alwaysRun = true)
    public void baseTestBeforeClass() {
        log.info("Starting the Before class of 'Base Test'");
    }

    @BeforeTest
    public void baseBeforeTest() {
        log.info("Test started");

        // initialize the ExtentSparkReporter
        spark = new ExtentSparkReporter(System.getProperty("user.dir") + "/test-output/testReport.html");
        spark.loadConfig("src/test/resources/html-config.xml");

        //initialize ExtentReports and attach the ExtentSparkReporter
        extent = new ExtentReports();
        extent.attachReporter(spark);

        //To add system or environment info by using the setSystemInfo method.
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("User ", System.getProperty("user.name"));
        extent.setSystemInfo("Java Version", String.valueOf(Runtime.version()));

    }

    @AfterTest
    public void tearDown() {
        log.info("Test finished");
        //to write or update test information to reporter
        extent.flush();
    }

    @AfterMethod
    public void getResult(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            test.log(Status.FAIL, MarkupHelper.createLabel(result.getName() + " FAILED ", ExtentColor.RED));
            test.fail(result.getThrowable());

        } else if (result.getStatus() == ITestResult.SUCCESS) {
            test.log(Status.PASS, MarkupHelper.createLabel(result.getName() + " PASSED ", ExtentColor.GREEN));
        } else {
            test.log(Status.SKIP, MarkupHelper.createLabel(result.getName() + " SKIPPED ", ExtentColor.ORANGE));
            test.skip(result.getThrowable());
        }
    }

    @AfterClass(alwaysRun = true)
    public void baseTestAfterClass() {
        log.info("Starting the After class of 'Base Test'");
    }

    @BeforeMethod(alwaysRun = true)
    public void logStartMethod(Method testMethod) {
        log.info(" =============================================== Starting test method [" + testMethod.getName() + "] " +
                "===================================");
        test = extent.createTest(testMethod.getName(), "Extent test");
    }

    @AfterMethod(alwaysRun = true)
    public void logEndMethod(Method testMethod) {
        log.info("=============================================== Ending test method [" + testMethod.getName() + "] " +
                "===================================");
    }
}
