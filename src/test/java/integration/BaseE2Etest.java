package integration;

import constants.APIConstants;
import io.qameta.allure.testng.AllureTestNg;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import pages.BookingAPI;
import utils.APIUtils;
import utils.AuthenticationTokenUtil;

@Listeners(AllureTestNg.class)
public class BaseE2Etest {
    protected BookingAPI bookingAPI;
    protected String token;
    @BeforeSuite(alwaysRun = true)
    public void checkAPIStatus(){
        Response response = APIUtils.get(APIConstants.PING, APIUtils.getRequestSpec());
        if(response.getStatusCode() != APIConstants.HTTP_CREATED){
            throw new RuntimeException("API is not live!!");
        }
        System.out.println("API is live , proceeding with tests!");
    }

    public void getToken(){
        token = AuthenticationTokenUtil.getAuthToken("admin","password123" );

    }
    @BeforeClass(alwaysRun = true)
    public void setup(){
        //Instantiated pages layer
        getToken();
        bookingAPI = new BookingAPI(token);
    }

}
