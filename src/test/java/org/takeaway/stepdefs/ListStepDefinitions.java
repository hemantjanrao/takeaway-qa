package org.takeaway.stepdefs;

import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.http.HttpStatus;
import org.takeaway.core.helper.TestHelper;
import org.takeaway.core.util.Environment;
import org.takeaway.core.util.PropertyUtils;
import org.takeaway.server.client.ListServer;
import org.takeaway.server.entity.model.List;
import org.takeaway.server.entity.model.UpdateList;
import org.takeaway.server.entity.response.ListResponse;
import org.testng.Assert;

public class ListStepDefinitions {

    private ListServer listServer;
    private ListResponse createdList;

    @Given("list service is running")
    public void list_service_is_running() {
        listServer = new ListServer(PropertyUtils.get(Environment.API_PROTOCOL),
                PropertyUtils.get(Environment.API_URL),
                PropertyUtils.getInt(Environment.API_VERSION));
    }

    @When("I create a list with {string} name")
    public void i_create_a_list_with_name(String listName) {
        var list = new List();
        list.setName(listName);
        list.setIso_639_1("en");

        createdList = listServer.createList(TestHelper.serializeToJson(list));

        Assert.assertEquals(createdList.getStatus_message(), "The item/record was created successfully.",
                String.format("List creation with name: %s failed", list.getName()));
    }

    @Then("Should be with {string} result")
    public void listShouldGetCreatedSuccessfully(String status) {
        if(status.equalsIgnoreCase("pass")){
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

    @And("I update list name to {string}")
    public void iUpdateListNameToUpdated_name(String updateName) {

        var updateList = new UpdateList();
        updateList.setDescription("Updated description");
        updateList.setName(updateName);

        var updatedList = listServer.updateList(createdList.getId(), TestHelper.serializeToJson(updateList));
        Assert.assertEquals(updatedList.statusCode(), HttpStatus.SC_CREATED);
    }
}
