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
    public void createList() {

        List list = new List();
        list.setName(RandomStringUtils.randomAlphabetic(10));
        list.setIso_639_1("en");

        createdList = listServer.createList(TestHelper.serializeToJson(list));

        test.pass(String.format("Created list with Id %s successfully ", createdList.getId()));

        Assert.assertEquals(createdList.getStatus_message(), "The item/record was created successfully.",
                String.format("List creation with name: %s failed", list.getName()));
    }

    @AfterMethod(description = "Delete list")
    public void testDeleteList() {

        Response response = listServer.deleteList(createdList.getId());
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK);

        Assert.assertFalse(listServer.doesListExists(createdList.getId()));
        test.pass(String.format("List with id %s deleted successfully", createdList.getId()));
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

        test.pass(String.format("Updated list with %s successfully ", TestHelper.serializeToJson(updateList)));
    }

    @Test(description = "To verify adding item to the created list")
    public void testAddItemsToList() {

        Items items = new Items();
        items.setMedia(1668, "tv", null);
        items.setMedia(597, "movie", null);

        AddUpdateItemResponse response = listServer.addItemsToList(createdList.getId(), items);
        Assert.assertTrue(response.isSuccess(), "Failed to add items to the list");

        Assert.assertTrue(listServer.doesItemsExistsInList(createdList.getId(), items.getItems()));
        test.pass(String.format("Added items %s to the list successfully ",
                TestHelper.serializeToJson(items)));
    }

    @Test(description = "To verify updating items from the created list")
    public void testUpdateItemsFromList() {

        Items items = new Items();
        items.setMedia(1668, "tv", null);
        items.setMedia(597, "movie", null);

        AddUpdateItemResponse response = listServer.addItemsToList(createdList.getId(), items);
        Assert.assertTrue(response.isSuccess());
        test.pass(String.format("Added items %s to the list successfully ", TestHelper.serializeToJson(items)));

        Items itemsToBeUpdated = new Items();
        itemsToBeUpdated.setMedia(1668, "tv", "great tv show");
        itemsToBeUpdated.setMedia(597, "movie", "great movie");

        AddUpdateItemResponse updatedItemsResponse = listServer.updateItemsToList(createdList.getId(), itemsToBeUpdated);
        Assert.assertTrue(updatedItemsResponse.isSuccess());

        Assert.assertTrue(listServer.doesItemsExistsInList(createdList.getId(), itemsToBeUpdated.getItems()));
        test.pass(String.format("Updated items %s to the list successfully ", TestHelper.serializeToJson(itemsToBeUpdated)));
    }

    @Test(description = "To verify items deletion from the created list")
    public void testDeleteItemsFromList() {

        Items items = new Items();
        items.setMedia(1668, "tv", null);
        items.setMedia(597, "movie", null);

        AddUpdateItemResponse response = listServer.addItemsToList(createdList.getId(), items);
        Assert.assertTrue(response.isSuccess());
        test.pass(String.format("Added items %s to the list successfully ", TestHelper.serializeToJson(items)));

        Items itemsToBeDeleted = new Items();
        itemsToBeDeleted.setMedia(597, "movie", null);

        Response deletedItemsResponse = listServer.deleteItemFromList(createdList.getId(), itemsToBeDeleted);
        Assert.assertEquals(deletedItemsResponse.statusCode(), HttpStatus.SC_OK);

        Assert.assertFalse(listServer.doesItemsExistsInList(createdList.getId(), itemsToBeDeleted.getItems()));
        test.pass(String.format("Deleted item %s to the list successfully ", TestHelper.serializeToJson(itemsToBeDeleted)));
    }

    @Test(description = "To verify clearing all the items from the list")
    public void testClearItemsFromList() {

        Items items = new Items();
        items.setMedia(1668, "tv", null);
        items.setMedia(597, "movie", null);

        AddUpdateItemResponse addItemsResponse = listServer.addItemsToList(createdList.getId(), items);
        Assert.assertTrue(addItemsResponse.isSuccess());
        test.pass(String.format("Added items %s to the list successfully ", TestHelper.serializeToJson(items)));

        Assert.assertEquals(listServer.getListSize(createdList.getId()), 2);

        Response clearListResponse = listServer.clearList(createdList.getId());
        Assert.assertEquals(clearListResponse.statusCode(), HttpStatus.SC_OK);

        Assert.assertFalse(listServer.doesItemsExistsInList(createdList.getId(), items.getItems()));

        test.pass("Cleared all the items from the list successfully");
    }
}
