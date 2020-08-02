package org.takeaway.stepdefs;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.takeaway.core.helper.TestHelper;
import org.takeaway.core.util.Environment;
import org.takeaway.core.util.PropertyUtils;
import org.takeaway.server.client.ListServer;
import org.takeaway.server.entity.model.List;
import org.takeaway.server.entity.response.ListResponse;
import org.testng.Assert;

public class ListStepDefinitions {

    private ListServer listServer;
    private ListResponse createdList;

    @Given("list service is running")
    public void listServiceIsRunning() {
        listServer = new ListServer(PropertyUtils.get(Environment.API_PROTOCOL),
                PropertyUtils.get(Environment.API_URL),
                PropertyUtils.getInt(Environment.API_VERSION));
    }

    @When("I create a list with {string} name")
    public void iCreateAListWithName(String listName) {
        var list = new List();
        list.setName(listName.strip());
        list.setIso_639_1("en");

        createdList = listServer.createList(TestHelper.serializeToJson(list));

        Assert.assertEquals(createdList.getStatus_message(), "The item/record was created successfully.",
                String.format("List creation with name: %s failed", list.getName()));
    }

    @Then("List should get created {string} result")
    public void listShouldGetCreatedSuccessfully(boolean status) {
        if(status){
        Assert.assertTrue(listServer.doesListExists(createdList.getId()));
        } else {
            Assert.assertFalse(listServer.doesListExists(createdList.getId()));
        }
    }

    @After
    public void tearDown(){
        var response = listServer.deleteList(createdList.getId());
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK);

        Assert.assertFalse(listServer.doesListExists(createdList.getId()));
    }
}