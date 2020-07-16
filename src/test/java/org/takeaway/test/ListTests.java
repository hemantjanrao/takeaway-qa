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
import org.takeaway.server.entity.response.AddUpdateItemResponse;
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

        createdList = listServer.createList(TestHelper.serializeToJson(list));

        test.info(String.format("Created list with Id %s successfully ", TestHelper.serializeToJson(list)));

        Assert.assertEquals(createdList.getStatus_message(), "The item/record was created successfully.",
                String.format("List creation with name: %s failed", list.getName()));
    }

    @AfterMethod(description = "Delete list")
    public void testDeleteList() {
        Response response = listServer.deleteList(createdList.getId());
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK);

        Assert.assertFalse(listServer.doesListExists(createdList.getId()));
        test.info(String.format("List with id %s deleted successfully ", createdList.getId()));
    }

    @Test(description = "To verify list creation")
    public void testCreateList() {
        Assert.assertEquals(createdList.getStatus_message(), "The item/record was created successfully.");
    }

    @Test(description = "To verify list update")
    public void testUpdateList() {

        UpdateLis updateList = new UpdateLis();
        updateList.setDescription("Updated description");
        updateList.setName("Updated name");

        Response updatedList = listServer.updateList(createdList.getId(), TestHelper.serializeToJson(updateList));
        Assert.assertEquals(updatedList.statusCode(), HttpStatus.SC_CREATED);

        GetListResponse getListResponse = listServer.getList(createdList.getId());

        Assert.assertEquals(getListResponse.getDescription(), "Updated description");
        Assert.assertEquals(getListResponse.getName(), "Updated name");

        test.info(String.format("Updated list with %s successfully ", TestHelper.serializeToJson(updateList)));
    }

    @Test(description = "To verify adding item to the created list")
    public void testAddItemsToList() {

        Items items = new Items();
        items.setMedia(1668, "tv", null);
        items.setMedia(597, "movie", null);

        AddUpdateItemResponse response = listServer.addUpdateItemsToList(createdList.getId(),
                TestHelper.serializeToJson(items));

        Assert.assertEquals(response.getResults().get(0).getMedia_id(), 1668);
        Assert.assertEquals(response.getResults().get(1).getMedia_id(), 597);

        test.info(String.format("Added items %s to the list successfully ",
                TestHelper.serializeToJson(items)));
    }

    @Test(description = "To verify updating items from the created list")
    public void testUpdateItemsFromList() {

        Items items = new Items();
        items.setMedia(1668, "tv", null);
        items.setMedia(597, "movie", null);

        AddUpdateItemResponse response = listServer.addUpdateItemsToList(createdList.getId(),
                TestHelper.serializeToJson(items));
        Assert.assertTrue(response.success);
        test.info(String.format("Added items %s to the list successfully ", TestHelper.serializeToJson(items)));

        Items itemsToBeUpdated = new Items();
        itemsToBeUpdated.setMedia(238, "movie", "NO comment");
        itemsToBeUpdated.setMedia(490132, "movie", "NO comment");

        AddUpdateItemResponse updatedItemsResponse = listServer.addUpdateItemsToList(createdList.getId(),
                TestHelper.serializeToJson(itemsToBeUpdated));
        Assert.assertTrue(updatedItemsResponse.success);

        Assert.assertEquals(updatedItemsResponse.getResults().get(0).getMedia_id(), 238);
        Assert.assertEquals(updatedItemsResponse.getResults().get(1).getMedia_id(), 490132);

        test.info(String.format("Updated items %s to the list successfully ", TestHelper.serializeToJson(itemsToBeUpdated)));
    }

    @Test(description = "To verify items deletion from the created list")
    public void testDeleteItemsFromList() {

        Items items = new Items();
        items.setMedia(1668, "tv", null);
        items.setMedia(597, "movie", null);

        AddUpdateItemResponse response = listServer.addUpdateItemsToList(createdList.getId(), TestHelper.serializeToJson(items));
        Assert.assertTrue(response.success);
        test.info(String.format("Added items %s to the list successfully ", TestHelper.serializeToJson(items)));

        Items itemsToBeDeleted = new Items();
        itemsToBeDeleted.setMedia(597, "movie", null);

        Response deletedItemsResponse = listServer.deleteItemFromList(createdList.getId(),
                TestHelper.serializeToJson(itemsToBeDeleted));
        Assert.assertEquals(deletedItemsResponse.statusCode(), HttpStatus.SC_OK);

        GetListResponse getListResponse = listServer.getList(createdList.getId());

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

        AddUpdateItemResponse addItemsResponse = listServer.addUpdateItemsToList(createdList.getId(), TestHelper.serializeToJson(items));
        Assert.assertTrue(addItemsResponse.success);
        test.info(String.format("Added items %s to the list successfully ", TestHelper.serializeToJson(items)));

        Assert.assertEquals(listServer.getListSize(createdList.getId()), 2);

        Response clearListResponse = listServer.clearList(createdList.getId());
        Assert.assertEquals(clearListResponse.statusCode(), HttpStatus.SC_OK);

        Assert.assertEquals(listServer.getListSize(createdList.getId()), 0, String.format("List is not cleared fully and has " +
                "%s items ", listServer.getListSize(createdList.getId())));

        test.info("Cleared all the items fro the list successfully");
    }
}
