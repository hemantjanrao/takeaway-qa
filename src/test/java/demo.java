import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.takeaway.core.helper.TestHelper;
import org.takeaway.core.util.Environment;
import org.takeaway.core.util.PropertyUtils;
import org.takeaway.server.client.ListServer;
import org.takeaway.server.entity.model.ListModel;
import org.takeaway.server.entity.model.UpdateListModel;
import org.takeaway.server.entity.response.ListResponse;
import org.takeaway.server.entity.response.UpdateListResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class demo {

    private ListServer listServer;

    @BeforeClass
    public void beforeListClass() {
        listServer = new ListServer(PropertyUtils.get(Environment.API_PROTOCOL),
                PropertyUtils.get(Environment.API_URL),
                PropertyUtils.getInt(Environment.API_VERSION));
    }

    @Test
    public void createList() {
        ListModel list = new ListModel();

        list.setName("TEST");
        list.setIso_639_1("en");

        Response createdList = listServer.createList(list);
        Assert.assertEquals(createdList.statusCode(), HttpStatus.SC_CREATED);

        ListResponse listResponse = TestHelper.deserializeJson(createdList, ListResponse.class);

        Assert.assertEquals(listResponse.status_message, "The item/record was created successfully.");
    }

    @Test
    public void updateList(){

        ListModel list = new ListModel();
        list.setName("TEST");
        list.setIso_639_1("en");

        Response createdList = listServer.createList(list);
        Assert.assertEquals(createdList.statusCode(), HttpStatus.SC_CREATED);

        ListResponse listResponse = TestHelper.deserializeJson(createdList, ListResponse.class);

        Assert.assertEquals(listResponse.status_message, "The item/record was created successfully.");

        UpdateListModel updateList = new UpdateListModel();
        updateList.setDescription("Updated description");
        updateList.setName("Updated name");

        Response updatedList = listServer.updateList(listResponse.id, updateList);
        Assert.assertEquals(updatedList.statusCode(), HttpStatus.SC_CREATED);

        UpdateListResponse updatedListResponse = TestHelper.deserializeJson(createdList, UpdateListResponse.class);

        Assert.assertEquals(updatedListResponse.status_message, "Updated description");
        Assert.assertEquals(updatedListResponse.status_message, "Updated name");

    }


}
