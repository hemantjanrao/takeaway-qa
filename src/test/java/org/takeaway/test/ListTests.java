package org.takeaway.test;

import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.takeaway.core.base.BaseTest;
import org.takeaway.core.helper.TestHelper;
import org.takeaway.core.util.Environment;
import org.takeaway.core.util.PropertyUtils;
import org.takeaway.server.client.ListServer;
import org.takeaway.server.entity.model.ItemsModel;
import org.takeaway.server.entity.model.ListModel;
import org.takeaway.server.entity.model.UpdateListModel;
import org.takeaway.server.entity.response.AddItemResponse;
import org.takeaway.server.entity.response.GetListResponse;
import org.takeaway.server.entity.response.ListResponse;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ListTests extends BaseTest {

    private ListServer listServer;
    private ListResponse createdList;

    @BeforeClass
    public void beforeListClass() {

        listServer = new ListServer(PropertyUtils.get(Environment.API_PROTOCOL),
                PropertyUtils.get(Environment.API_URL),
                PropertyUtils.getInt(Environment.API_VERSION));
    }

    @BeforeMethod(description = "Create list")
    public void beforeMethod() {

        ListModel list = new ListModel();

        list.setName(RandomStringUtils.randomAlphabetic(10));
        list.setIso_639_1("en");

        Response createdList = listServer.createList(TestHelper.serializeToJson(list));
        Assert.assertEquals(createdList.statusCode(), HttpStatus.SC_CREATED);
        test.info(String.format("Created list with %s successfully ", TestHelper.serializeToJson(list)));

        this.createdList = TestHelper.deserializeJson(createdList, ListResponse.class);

        Assert.assertEquals(this.createdList.getStatus_message(), "The item/record was created successfully.",
                String.format("List creation with name: %s failed", list.getName()));
    }

    @AfterMethod(description = "Delete list")
    public void deleteList() {

        Response response = listServer.deleteList(createdList.getId());
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK);

        Response getList = listServer.getList(createdList.getId());
        Assert.assertEquals(getList.statusCode(), HttpStatus.SC_NOT_FOUND);
        test.info(String.format("List with id %s deleted successfully ", createdList.getId()));
    }

    @Test(description = "To verify list creation")
    public void createList() {
        Assert.assertEquals(createdList.getStatus_message(), "The item/record was created successfully.");

    }

    @Test(description = "To verify list update")
    public void updateList() {

        UpdateListModel updateList = new UpdateListModel();
        updateList.setDescription("Updated description");
        updateList.setName("Updated name");

        Response updatedList = listServer.updateList(createdList.getId(), TestHelper.serializeToJson(updateList));
        Assert.assertEquals(updatedList.statusCode(), HttpStatus.SC_CREATED);

        Response list1 = listServer.getList(createdList.getId());
        GetListResponse listResponse1 = TestHelper.deserializeJson(list1, GetListResponse.class);

        Assert.assertEquals(listResponse1.getDescription(), "Updated description");
        Assert.assertEquals(listResponse1.getName(), "Updated name");

        test.info(String.format("Updated list with %s successfully ", TestHelper.serializeToJson(updateList)));
    }

    @Test(description = "To verify adding item to the created list")
    public void addItemsToList() {

        ItemsModel items = new ItemsModel();
        items.setMedia(1668, "tv", null);
        items.setMedia(597, "movie", null);

        Response response = listServer.addItemToList(createdList.getId(), TestHelper.serializeToJson(items));
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK);

        AddItemResponse addItemResponse = TestHelper.deserializeJson(response, AddItemResponse.class);

        Assert.assertEquals(addItemResponse.getResults().get(0).getMedia_id(), 1668);
        Assert.assertEquals(addItemResponse.getResults().get(1).getMedia_id(), 597);

        test.info(String.format("Added items %s to the list successfully ", TestHelper.serializeToJson(items)));
    }

    @Test(description = "To verify updating items from the created list")
    public void updateItemsFromList() {

        ItemsModel items = new ItemsModel();
        items.setMedia(1668, "tv", null);
        items.setMedia(597, "movie", null);

        Response response1 = listServer.addItemToList(createdList.getId(), TestHelper.serializeToJson(items));
        Assert.assertEquals(response1.statusCode(), HttpStatus.SC_OK);

        test.info(String.format("Added items %s to the list successfully ", TestHelper.serializeToJson(items)));

        ItemsModel itemsToBeUpdated = new ItemsModel();
        itemsToBeUpdated.setMedia(238, "movie", "NO comment");
        itemsToBeUpdated.setMedia(490132, "movie", "NO comment");

        Response response = listServer.updateItemFromList(createdList.getId(), TestHelper.serializeToJson(itemsToBeUpdated));
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK);

        AddItemResponse addItemResponse = TestHelper.deserializeJson(response, AddItemResponse.class);

        Assert.assertEquals(addItemResponse.getResults().get(0).getMedia_id(), 238);
        Assert.assertEquals(addItemResponse.getResults().get(1).getMedia_id(), 490132);

        test.info(String.format("Updated items %s to the list successfully ", TestHelper.serializeToJson(itemsToBeUpdated)));
    }

    @Test(description = "To verify items deletion from the created list")
    public void deleteItemsFromList() {

        ItemsModel items = new ItemsModel();
        items.setMedia(1668, "tv", null);
        items.setMedia(597, "movie", null);

        Response response1 = listServer.addItemToList(createdList.getId(), TestHelper.serializeToJson(items));
        Assert.assertEquals(response1.statusCode(), HttpStatus.SC_OK);

        test.info(String.format("Added items %s to the list successfully ", TestHelper.serializeToJson(items)));

        ItemsModel itemsToBeDeleted = new ItemsModel();
        itemsToBeDeleted.setMedia(597, "movie", null);

        Response response = listServer.deleteItemFromList(createdList.getId(), TestHelper.serializeToJson(itemsToBeDeleted));
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK);

        Response list1 = listServer.getList(createdList.getId());
        GetListResponse listResponse1 = TestHelper.deserializeJson(list1, GetListResponse.class);

        Assert.assertEquals(listResponse1.getResults().size(), 1);
        Assert.assertEquals(listResponse1.getResults().get(0).getId(), 1668);
        Assert.assertEquals(listResponse1.getResults().get(0).getMedia_type(), "tv");

        test.info(String.format("Deleted item %s to the list successfully ", TestHelper.serializeToJson(itemsToBeDeleted)));
    }

    @Test(description = "To verify clearing all the items from the list")
    public void clearItemsFromList() {

        ItemsModel items = new ItemsModel();
        items.setMedia(1668, "tv", null);
        items.setMedia(597, "movie", null);

        Response response = listServer.addItemToList(createdList.getId(), TestHelper.serializeToJson(items));
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK);
        test.info(String.format("Added items %s to the list successfully ", TestHelper.serializeToJson(items)));

        AddItemResponse addItemResponse = TestHelper.deserializeJson(response, AddItemResponse.class);
        Assert.assertEquals(addItemResponse.getResults().size(), 2);

        Response clearList = listServer.clearList(createdList.getId());
        Assert.assertEquals(clearList.statusCode(), HttpStatus.SC_OK);

        Response clearedList = listServer.getList(createdList.getId());
        GetListResponse listResponse1 = TestHelper.deserializeJson(clearedList, GetListResponse.class);
        Assert.assertEquals(listResponse1.getResults().size(), 0, String.format("List is not cleared fully and has " +
                "%s items ", listResponse1.getResults().size()));

        test.info("Cleared all the items fro the list successfully");
    }
}
