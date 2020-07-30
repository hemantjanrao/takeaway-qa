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
import org.takeaway.server.entity.model.Media;
import org.takeaway.server.entity.model.UpdateList;
import org.takeaway.server.entity.response.AddUpdateItemResponse;
import org.takeaway.server.entity.response.GetListResponse;
import org.takeaway.server.entity.response.ListResponse;
import org.testng.Assert;
import org.testng.annotations.*;

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

        var list = new List();
        list.setName(RandomStringUtils.randomAlphabetic(10));
        list.setIso_639_1("en");

        createdList = listServer.createList(TestHelper.serializeToJson(list));

        test.pass(String.format("Created list with Id %s successfully ", createdList.getId()));

        Assert.assertEquals(createdList.getStatus_message(), "The item/record was created successfully.",
                String.format("List creation with name: %s failed", list.getName()));
    }

    @AfterMethod(description = "Delete list")
    public void testDeleteList() {

        var response = listServer.deleteList(createdList.getId());
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

        var updateList = new UpdateList();
        updateList.setDescription("Updated description");
        updateList.setName("Updated name");

        var updatedList = listServer.updateList(createdList.getId(), TestHelper.serializeToJson(updateList));
        Assert.assertEquals(updatedList.statusCode(), HttpStatus.SC_CREATED);

        var getListResponse = listServer.getList(createdList.getId());

        Assert.assertEquals(getListResponse.getDescription(), "Updated description");
        Assert.assertEquals(getListResponse.getName(), "Updated name");

        test.pass(String.format("Updated list with %s successfully ", TestHelper.serializeToJson(updateList)));
    }

    @DataProvider
    public Object[][] getItems() {
        return new Object[][]{
                {new Media(1668, "tv", null)},
                {new Media(597, "movie", null)}
        };
    }

    @Test(dataProvider = "getItems",description = "To verify adding item to the created list")
    public void testAddItemsToList(Media media) {

        var items = new Items();
        items.setMedia(media.getMedia_id(), media.getMedia_type().strip(), media.getComment());

        var addItemsResponse = listServer.addItemsToList(createdList.getId(), items);
        Assert.assertTrue(addItemsResponse.isSuccess(), "Failed to add items to the list");

        Assert.assertTrue(listServer.doesItemsExistsInList(createdList.getId(), items.getItems()),
                String.format("Item %s does not exists",TestHelper.serializeToJson(items)));
        test.pass(String.format("Added items %s to the list successfully ", TestHelper.serializeToJson(items)));
    }

    @Test(description = "To verify updating items from the created list")
    public void testUpdateItemsFromList() {

        var items = new Items();
        items.setMedia(1668, "tv", null);
        items.setMedia(597, "movie", null);

        var addItemsResponse = listServer.addItemsToList(createdList.getId(), items);
        Assert.assertTrue(addItemsResponse.isSuccess(), "Failed to add items to the list");
        test.pass(String.format("Added items %s to the list successfully ", TestHelper.serializeToJson(items)));

        var itemsToBeUpdated = new Items();
        itemsToBeUpdated.setMedia(1668, "tv", "great tv show");

        var updatedItemsResponse = listServer.updateItemsToList(createdList.getId(), itemsToBeUpdated);
        Assert.assertTrue(updatedItemsResponse.isSuccess(), "Failed to update items to the list");

        Assert.assertTrue(listServer.doesItemsExistsInList(createdList.getId(), itemsToBeUpdated.getItems()),
                String.format("Item %s does not exists",TestHelper.serializeToJson(itemsToBeUpdated)));
        test.pass(String.format("Updated items %s to the list successfully ", TestHelper.serializeToJson(itemsToBeUpdated)));
    }

    @Test(description = "To verify items deletion from the created list")
    public void testDeleteItemsFromList() {

        var items = new Items();
        items.setMedia(1668, "tv", null);
        items.setMedia(597, "movie", null);

        AddUpdateItemResponse addItemsResponse = listServer.addItemsToList(createdList.getId(), items);
        Assert.assertTrue(addItemsResponse.isSuccess(), "Failed to add items to the list");
        test.pass(String.format("Added items %s to the list successfully ", TestHelper.serializeToJson(items)));

        var itemsToBeDeleted = new Items();
        itemsToBeDeleted.setMedia(597, "movie", null);

        var deletedItemsResponse = listServer.deleteItemFromList(createdList.getId(), itemsToBeDeleted);
        Assert.assertEquals(deletedItemsResponse.statusCode(), HttpStatus.SC_OK);

        Assert.assertFalse(listServer.doesItemsExistsInList(createdList.getId(), itemsToBeDeleted.getItems()),
                String.format("Item %s does exists",TestHelper.serializeToJson(itemsToBeDeleted)));
        test.pass(String.format("Deleted item %s to the list successfully ", TestHelper.serializeToJson(itemsToBeDeleted)));
    }

    @Test(description = "To verify clearing all the items from the list")
    public void testClearItemsFromList() {

        var items = new Items();
        items.setMedia(1668, "tv", null);
        items.setMedia(597, "movie", null);

        AddUpdateItemResponse addItemsResponse = listServer.addItemsToList(createdList.getId(), items);
        Assert.assertTrue(addItemsResponse.isSuccess(), "Failed to add items to the list");
        test.pass(String.format("Added items %s to the list successfully ", TestHelper.serializeToJson(items)));

        Assert.assertEquals(listServer.getListSize(createdList.getId()), 2);

        Response clearListResponse = listServer.clearList(createdList.getId());
        Assert.assertEquals(clearListResponse.statusCode(), HttpStatus.SC_OK);

        Assert.assertFalse(listServer.doesItemsExistsInList(createdList.getId(), items.getItems()),
                String.format("Item %s does exists",TestHelper.serializeToJson(items)));

        test.pass("Cleared all the items from the list successfully");
    }
}
