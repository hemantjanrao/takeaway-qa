import org.takeaway.core.util.Environment;
import org.takeaway.core.util.PropertyUtils;
import org.takeaway.server.ListServer;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class demo {

    private ListServer listServer;

    @BeforeClass
    public void beforeListClass(){
        listServer = new ListServer(PropertyUtils.get(Environment.API_PROTOCOL),
                PropertyUtils.get(Environment.API_URL),
                PropertyUtils.getInt(Environment.API_VERSION));
    }

    @Test
    public void sampleTest(){
        listServer.getList(550);
    }

}
