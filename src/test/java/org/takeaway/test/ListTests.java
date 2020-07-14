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
import org.takeaway.server.entity.model.MediaModel;
import org.takeaway.server.entity.model.UpdateListModel;
import org.takeaway.server.entity.response.GetList;
import org.takeaway.server.entity.response.ListResponse;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.List;

public class ListTests extends BaseTest {

    private ListServer listServer;
    private ListResponse listResponse;

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

        Response createdList = listServer.createList(list);
        Assert.assertEquals(createdList.statusCode(), HttpStatus.SC_CREATED);

        listResponse = TestHelper.deserializeJson(createdList, ListResponse.class);

        Assert.assertEquals(listResponse.getStatus_message(), "The item/record was created successfully.",
                String.format("List creation with name: %s failed", list.getName()));
    }

    @AfterMethod
    public void deleteList() {
        listServer.deleteList(listResponse.getId());
    }

    @Test
    public void createList() {
        Assert.assertEquals(listResponse.getStatus_message(), "The item/record was created successfully.");
    }

    @Test
    public void updateList() {

        UpdateListModel updateList = new UpdateListModel();
        updateList.setDescription("Updated description");
        updateList.setName("Updated name");

        Response updatedList = listServer.updateList(listResponse.getId(), updateList);
        Assert.assertEquals(updatedList.statusCode(), HttpStatus.SC_CREATED);

        Response list1 = listServer.getList(listResponse.getId());
        GetList listResponse1 = TestHelper.deserializeJson(list1, GetList.class);

        Assert.assertEquals(listResponse1.getDescription(), "Updated description");
        Assert.assertEquals(listResponse1.getName(), "Updated name");
    }

    @Test
    public void addItemsToList() {

        MediaModel friends = listServer.createMedia(1668, "tv", null);
        MediaModel titanic = listServer.createMedia(597, "movie", null);

        List<MediaModel> mediaList = new ArrayList<>();
        mediaList.add(friends);
        mediaList.add(titanic);

        ItemsModel items = new ItemsModel();
        items.setItems(mediaList);

        Response response = listServer.addItemToList(listResponse.getId(), items);
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK);
    }

    @Test
    public void updateItemsFromList() {

        MediaModel friends = listServer.createMedia(1668, "tv", null);
        MediaModel titanic = listServer.createMedia(597, "movie", null);

        List<MediaModel> mediaList = new ArrayList<>();
        mediaList.add(friends);
        mediaList.add(titanic);

        ItemsModel items = new ItemsModel();
        items.setItems(mediaList);

        listServer.addItemToList(listResponse.getId(), items);

        MediaModel movie1 = listServer.createMedia(238, "movie", "NO comment");
        MediaModel movie2 = listServer.createMedia(490132, "movie", "NO comment");

        List<MediaModel> mediaList2 = new ArrayList<>();
        mediaList2.add(movie1);
        mediaList2.add(movie2);

        ItemsModel items1 = new ItemsModel();
        items1.setItems(mediaList2);

        Response response = listServer.updateItemFromList(listResponse.getId(), items1);

        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK);
    }

    @Test
    public void deleteItemsFromList() {

        MediaModel friends = listServer.createMedia(1668, "tv", null);
        MediaModel titanic = listServer.createMedia(597, "movie", null);

        List<MediaModel> mediaList = new ArrayList<>();
        mediaList.add(friends);
        mediaList.add(titanic);

        ItemsModel items = new ItemsModel();
        items.setItems(mediaList);

        listServer.addItemToList(listResponse.getId(), items);

        MediaModel toBeRemoved = listServer.createMedia(597, "movie", null);

        List<MediaModel> mediaList2 = new ArrayList<>();
        mediaList2.add(toBeRemoved);

        ItemsModel items1 = new ItemsModel();
        items1.setItems(mediaList2);

        Response response = listServer.deleteItemFromList(listResponse.getId(), items1);
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK);

        Response list1 = listServer.getList(listResponse.getId());
        GetList listResponse1 = TestHelper.deserializeJson(list1, GetList.class);

        Assert.assertEquals(listResponse1.getResults().size(), 1);
        Assert.assertEquals(listResponse1.getResults().get(0).getId(), friends.getMedia_id());
        Assert.assertEquals(listResponse1.getResults().get(0).getMedia_type(), friends.getMedia_type());
    }

}
