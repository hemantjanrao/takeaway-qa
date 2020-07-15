package org.takeaway.test;

import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.takeaway.core.base.BaseTest;
import org.takeaway.core.helper.TestHelper;
import org.takeaway.core.util.Environment;
import org.takeaway.core.util.PropertyUtils;
import org.takeaway.server.client.ListServer;
import org.takeaway.server.entity.model.Items;
import org.takeaway.server.entity.model.List;
import org.takeaway.server.entity.model.UpdateLis;
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

        List list = new List();

        list.setName(RandomStringUtils.randomAlphabetic(10));
        list.setIso_639_1("en");

        Response createListResponse = listServer.createList(TestHelper.serializeToJson(list));
        Assert.assertEquals(createListResponse.statusCode(), HttpStatus.SC_CREATED);
        test.info(String.format("Created list with %s successfully ", TestHelper.serializeToJson(list)));

        createdList = TestHelper.deserializeJson(createListResponse, ListResponse.class);

        Assert.assertEquals(createdList.getStatus_message(), "The item/record was created successfully.",
                String.format("List creation with name: %s failed", list.getName()));
    }

    @AfterMethod(description = "Delete list")
    public void testDeleteList() {

        Response response = listServer.deleteList(createdList.getId());
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK);

        Response getListResponse = listServer.getList(createdList.getId());
        Assert.assertEquals(getListResponse.statusCode(), HttpStatus.SC_NOT_FOUND);
        test.info(String.format("List with id %s deleted successfully ", createdList.getId()));
    }

    @Test(description = "To verify list creation")
    public void testCreateList() {
        Assert.assertEquals(createdList.getStatus_message(), "The item/record was created successfully.");

    }

    @Test(groups = {"update"}, description = "To verify list update")
    public void testUpdateList() {

        UpdateLis updateList = new UpdateLis();
        updateList.setDescription("Updated description");
        updateList.setName("Updated name");

        Response updatedList = listServer.updateList(createdList.getId(), TestHelper.serializeToJson(updateList));
        Assert.assertEquals(updatedList.statusCode(), HttpStatus.SC_CREATED);

        Response getListResponse = listServer.getList(createdList.getId());
        GetListResponse getListsResponse = TestHelper.deserializeJson(getListResponse, GetListResponse.class);

        Assert.assertEquals(getListsResponse.getDescription(), "Updated description");
        Assert.assertEquals(getListsResponse.getName(), "Updated name");

        test.info(String.format("Updated list with %s successfully ", TestHelper.serializeToJson(updateList)));
    }

    @Test(description = "To verify adding item to the created list")
    public void testAddItemsToList() {

        Items items = new Items();
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
    public void testUpdateItemsFromList() {

        Items items = new Items();
        items.setMedia(1668, "tv", null);
        items.setMedia(597, "movie", null);

        Response response1 = listServer.addItemToList(createdList.getId(), TestHelper.serializeToJson(items));
        Assert.assertEquals(response1.statusCode(), HttpStatus.SC_OK);

        test.info(String.format("Added items %s to the list successfully ", TestHelper.serializeToJson(items)));

        Items itemsToBeUpdated = new Items();
        itemsToBeUpdated.setMedia(238, "movie", "NO comment");
        itemsToBeUpdated.setMedia(490132, "movie", "NO comment");

        Response updatedItemsResponse = listServer.updateItemFromList(createdList.getId(), TestHelper.serializeToJson(itemsToBeUpdated));
        Assert.assertEquals(updatedItemsResponse.statusCode(), HttpStatus.SC_OK);

        AddItemResponse addItemResponse = TestHelper.deserializeJson(updatedItemsResponse, AddItemResponse.class);

        Assert.assertEquals(addItemResponse.getResults().get(0).getMedia_id(), 238);
        Assert.assertEquals(addItemResponse.getResults().get(1).getMedia_id(), 490132);

        test.info(String.format("Updated items %s to the list successfully ", TestHelper.serializeToJson(itemsToBeUpdated)));
    }

    @Test(description = "To verify items deletion from the created list")
    public void testDeleteItemsFromList() {

        Items items = new Items();
        items.setMedia(1668, "tv", null);
        items.setMedia(597, "movie", null);

        Response addItemsResponse = listServer.addItemToList(createdList.getId(), TestHelper.serializeToJson(items));
        Assert.assertEquals(addItemsResponse.statusCode(), HttpStatus.SC_OK);

        test.info(String.format("Added items %s to the list successfully ", TestHelper.serializeToJson(items)));

        Items itemsToBeDeleted = new Items();
        itemsToBeDeleted.setMedia(597, "movie", null);

        Response deletedItemsResponse = listServer.deleteItemFromList(createdList.getId(), TestHelper.serializeToJson(itemsToBeDeleted));
        Assert.assertEquals(deletedItemsResponse.statusCode(), HttpStatus.SC_OK);

        Response list1 = listServer.getList(createdList.getId());
        GetListResponse getListResponse = TestHelper.deserializeJson(list1, GetListResponse.class);

        Assert.assertEquals(getListResponse.getResults().size(), 1, String.format("Items are not successfully deleted " +
                "and has %d items", getListResponse.getResults().size()));
        Assert.assertEquals(getListResponse.getResults().get(0).getId(), 1668);
        Assert.assertEquals(getListResponse.getResults().get(0).getMedia_type(), "tv");

        test.info(String.format("Deleted item %s to the list successfully ", TestHelper.serializeToJson(itemsToBeDeleted)));
    }

    @Test(description = "To verify clearing all the items from the list")
    public void testClearItemsFromList() {

        Items items = new Items();
        items.setMedia(1668, "tv", null);
        items.setMedia(597, "movie", null);

        Response addItemsResponse = listServer.addItemToList(createdList.getId(), TestHelper.serializeToJson(items));
        Assert.assertEquals(addItemsResponse.statusCode(), HttpStatus.SC_OK);
        test.info(String.format("Added items %s to the list successfully ", TestHelper.serializeToJson(items)));

        AddItemResponse addItemResponse = TestHelper.deserializeJson(addItemsResponse, AddItemResponse.class);
        Assert.assertEquals(addItemResponse.getResults().size(), 2);

        Response clearListResponse = listServer.clearList(createdList.getId());
        Assert.assertEquals(clearListResponse.statusCode(), HttpStatus.SC_OK);

        Response clearedListResponse = listServer.getList(createdList.getId());
        GetListResponse listResponse1 = TestHelper.deserializeJson(clearedListResponse, GetListResponse.class);
        Assert.assertEquals(listResponse1.getResults().size(), 0, String.format("List is not cleared fully and has " +
                "%s items ", listResponse1.getResults().size()));

        test.info("Cleared all the items fro the list successfully");
    }
}
