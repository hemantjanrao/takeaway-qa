package org.takeaway.cucumber;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        plugin = {"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"},
        monochrome = true,
        glue = {"org/takeaway/stepdefs"},
        tags="@qa-task and @positive and @negative",
        features = {"src/test/resources/features"}
)
public class RunBDDTest extends AbstractTestNGCucumberTests {
}