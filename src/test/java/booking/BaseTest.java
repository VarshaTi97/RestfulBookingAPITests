package booking;

import constants.APIConstants;
import constants.ExcelConstants;
import io.qameta.allure.testng.AllureTestNg;
import io.restassured.response.Response;
import org.testng.annotations.*;
import pages.BookingAPI;
import pojoModels.BookingDates;
import pojoModels.BookingDetails;
import utils.APIUtils;

import utils.AuthenticationTokenUtil;
import utils.ExcelUtils;
import utils.PropertyFileUtil;


import java.util.List;

@Listeners(AllureTestNg.class)
public class BaseTest {
    protected BookingAPI bookingAPI;
    protected String token;
    protected Integer createdBookingId;

    //Check status of the API before starting the execution
    @BeforeSuite(alwaysRun = true)
    public void checkAPIStatus(){
        Response response = APIUtils.get(APIConstants.PING, APIUtils.getRequestSpec());
        if(response.getStatusCode() != APIConstants.HTTP_CREATED){
            throw new RuntimeException("API is not live!!");
        }
        System.out.println("API is live , proceeding with tests!");
    }

    //Generates token
    public void getToken(){
        token = AuthenticationTokenUtil.getAuthToken(PropertyFileUtil.get("username"),PropertyFileUtil.get("password" ));

    }

    // Instantiating the page class and token to be used for other method calls
    @BeforeClass(alwaysRun = true)
    public void setup(){
        //Instantiated pages layer
        getToken();
        bookingAPI = new BookingAPI(token);
   }

   //Creating a single test booking by reading details from property file to be used in the tests
   @BeforeMethod
   public void createBooking(){
       BookingDates bookingDates = new BookingDates(
               PropertyFileUtil.get("checkin"),
               PropertyFileUtil.get("checkout")
       );
       BookingDetails booking = new BookingDetails(
               PropertyFileUtil.get("firstname"),
               PropertyFileUtil.get("lastname"),
               Integer.parseInt(PropertyFileUtil.get("totalprice")),
               Boolean.parseBoolean(PropertyFileUtil.get("depositpaid")),
               bookingDates,
               PropertyFileUtil.get("additionalneeds"));

       Response response = bookingAPI.createBooking(booking);
       if(response.getStatusCode() !=APIConstants.HTTP_SUCCESS){
           throw new RuntimeException("Test user creation failed");
       }

       createdBookingId = response.jsonPath().getInt("bookingid");
   }

   //Reads booking details from excel sheet to be used by test
    @DataProvider(name="excelBookingDetailsData")
    public Object[][] getBookingsFromExcelSheet(){
        ExcelUtils excelUtils = new ExcelUtils();
        excelUtils.loadExcel(ExcelConstants.TEST_DATA_FILE_PATH);
        List<BookingDetails> bookingDetailsList = excelUtils.getBookingDetailsFromExcel(ExcelConstants.BOOKING_DATA_SHEET_NAME);
        Object[][] data = new Object[bookingDetailsList.size()][1];
        for(int i =0; i< bookingDetailsList.size();i++){
            data[i][0] = bookingDetailsList.get(i);
        }
        return data;
    }

    //Cleans up the booking after test execution is complete
    @AfterMethod
    public void cleanupTestBookings(){
        if (createdBookingId != null) {
            Response response = bookingAPI.getBookingById(createdBookingId);
            if(response.getStatusCode() == 200) {
                Response deleteResponse = bookingAPI.deleteBooking(createdBookingId);
                int status = deleteResponse.getStatusCode();
                if(status != 200 && status != 201) {
                    throw new RuntimeException("Deletion of the test user failed");
                }
            }
            else {
                System.out.println("Booking already missing, skipping delete. Status: " + response.getStatusCode());
            }
        }

    }

}
