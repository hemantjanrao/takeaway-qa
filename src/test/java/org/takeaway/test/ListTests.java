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
import org.takeaway.server.entity.response.GetList;
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

    @BeforeMethod
    public void beforeMethod() {

        ListModel list = new ListModel();

        list.setName(RandomStringUtils.randomAlphabetic(10));
        list.setIso_639_1("en");

        Response createdList = listServer.createList(TestHelper.serializeToJson(list));
        Assert.assertEquals(createdList.statusCode(), HttpStatus.SC_CREATED);

        this.createdList = TestHelper.deserializeJson(createdList, ListResponse.class);

        Assert.assertEquals(this.createdList.getStatus_message(), "The item/record was created successfully.",
                String.format("List creation with name: %s failed", list.getName()));
    }

    @AfterMethod
    public void deleteList() {

        Response response = listServer.deleteList(createdList.getId());
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK);

        Response getList = listServer.getList(createdList.getId());
        Assert.assertEquals(getList.statusCode(), HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void createList() {
        Assert.assertEquals(createdList.getStatus_message(), "The item/record was created successfully.");
    }

    @Test
    public void updateList() {

        UpdateListModel updateList = new UpdateListModel();
        updateList.setDescription("Updated description");
        updateList.setName("Updated name");

        Response updatedList = listServer.updateList(createdList.getId(), TestHelper.serializeToJson(updateList));
        Assert.assertEquals(updatedList.statusCode(), HttpStatus.SC_CREATED);

        Response list1 = listServer.getList(createdList.getId());
        GetList listResponse1 = TestHelper.deserializeJson(list1, GetList.class);

        Assert.assertEquals(listResponse1.getDescription(), "Updated description");
        Assert.assertEquals(listResponse1.getName(), "Updated name");
    }

    @Test
    public void addItemsToList() {

        ItemsModel items = new ItemsModel();
        listServer.setMedia(1668, "tv", null);
        listServer.setMedia(597, "movie", null);
        items.setItems(listServer.getItems());

        Response response = listServer.addItemToList(createdList.getId(), TestHelper.serializeToJson(items));
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK);
    }

    @Test
    public void updateItemsFromList() {

        ItemsModel items = new ItemsModel();
        listServer.setMedia(1668, "tv", null);
        listServer.setMedia(597, "movie", null);
        items.setItems(listServer.getItems());

        Response response1 = listServer.addItemToList(createdList.getId(), TestHelper.serializeToJson(items));
        Assert.assertEquals(response1.statusCode(), HttpStatus.SC_OK);

        ItemsModel itemsToBeUpdated = new ItemsModel();
        listServer.setMedia(238, "movie", "NO comment");
        listServer.setMedia(490132, "movie", "NO comment");
        itemsToBeUpdated.setItems(listServer.getItems());

        Response response = listServer.updateItemFromList(createdList.getId(), TestHelper.serializeToJson(itemsToBeUpdated));

        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK);
    }

    @Test
    public void deleteItemsFromList() {

        ItemsModel items = new ItemsModel();
        listServer.setMedia(1668, "tv", null);
        listServer.setMedia(597, "movie", null);
        items.setItems(listServer.getItems());

        Response response1 = listServer.addItemToList(createdList.getId(), TestHelper.serializeToJson(items));
        Assert.assertEquals(response1.statusCode(), HttpStatus.SC_OK);

        ItemsModel itemsToBeDeleted = new ItemsModel();
        listServer.setMedia(597, "movie", null);
        itemsToBeDeleted.setItems(listServer.getItems());

        Response response = listServer.deleteItemFromList(createdList.getId(), TestHelper.serializeToJson(itemsToBeDeleted));
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK);

        Response list1 = listServer.getList(createdList.getId());
        GetList listResponse1 = TestHelper.deserializeJson(list1, GetList.class);

        Assert.assertEquals(listResponse1.getResults().size(), 1);
        Assert.assertEquals(listResponse1.getResults().get(0).getId(), 1668);
        Assert.assertEquals(listResponse1.getResults().get(0).getMedia_type(), "tv");
    }

    @Test
    public void clearItemsFromList() {

        ItemsModel items = new ItemsModel();
        listServer.setMedia(1668, "tv", null);
        listServer.setMedia(597, "movie", null);
        items.setItems(listServer.getItems());

        Response response = listServer.addItemToList(createdList.getId(), TestHelper.serializeToJson(items));
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK);

        AddItemResponse addItemResponse = TestHelper.deserializeJson(response, AddItemResponse.class);
        Assert.assertEquals(addItemResponse.getResults().size(), 2);

        Response clearList = listServer.clearList(createdList.getId());
        Assert.assertEquals(clearList.statusCode(), HttpStatus.SC_OK);

        Response list1 = listServer.getList(createdList.getId());
        GetList listResponse1 = TestHelper.deserializeJson(list1, GetList.class);

        Assert.assertEquals(listResponse1.getResults().size(), 0);
    }
}
