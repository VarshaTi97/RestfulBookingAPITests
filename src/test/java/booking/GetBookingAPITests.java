package booking;

import constants.APIConstants;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import pojoModels.BookingDetails;
import pojoModels.GetBookingIds;
import java.util.List;
import java.util.Map;

public class GetBookingAPITests extends BaseTest{

    @Test(description = "Get list of all the booking ids and check list is not empty")
    public void getAllBookingIds() {
        //get list of all existing booking ids
        Response response = bookingAPI.getAllBookingByIds();
        Assert.assertEquals(response.getStatusCode(), APIConstants.HTTP_SUCCESS);
        //check that list of bookings is not empty
        List<GetBookingIds> bookingIdsList = response.jsonPath().getList("", GetBookingIds.class);
        Assert.assertFalse(bookingIdsList.isEmpty());
    }


    @Test(description = "Get booking details by single attribute filter", dataProvider = "excelBookingDetailsData")
    public void checkBookingBySingleField(BookingDetails bookingDetails) {

        //Create 3 new bookings with data coming from excel sheet
        Response createUserResponse = bookingAPI.createBooking(bookingDetails);
        Assert.assertEquals(createUserResponse.getStatusCode(), APIConstants.HTTP_SUCCESS);

        //Save the booking id
        int bookingId = createUserResponse.jsonPath().getInt("bookingid");

        //Prepare the list of the booking request attributes which needs to be filtered
        Map<String, Object> bookingDatesMap = Map.of(
                "checkin", bookingDetails.getBookingdates().getCheckin(),
                "checkout", bookingDetails.getBookingdates().getCheckout()
        );

        Map<String, Object> fieldsToBeFiltered = Map.of(
                "firstname", bookingDetails.getFirstname(),
                "lastname", bookingDetails.getLastname(),
                "bookingdates", bookingDatesMap
        );

        //Navigate over each attribute from the above map and pass it as query param and then validate the booking id returned as response
        for(Map.Entry<String, Object> filter : fieldsToBeFiltered.entrySet()){

            //filter with query param
            Map<String, Object> singleFieldFilterQueryParam = Map.of(filter.getKey(), filter.getValue());
            Response filterResponse = bookingAPI.getBookingByFilter(singleFieldFilterQueryParam);
            Assert.assertEquals(filterResponse.getStatusCode(), APIConstants.HTTP_SUCCESS);

            List<Integer> bookingIdsList = filterResponse.jsonPath().getList("bookingid", Integer.class);
            //Validate booking list not empty and contains the ID created in this test
            Assert.assertFalse(bookingIdsList.isEmpty());
            Assert.assertTrue(bookingIdsList.contains(bookingId), "Booking id appears in the "+filter.getKey()+" filter list");
        }
    }

    @Test(description = "Get booking details by multiple attribute filter", dataProvider = "excelBookingDetailsData")
    public void checkBookingByMultipleFields(BookingDetails bookingDetails) {

        //Create 3 new bookings with data coming from excel sheet
        Response createUserResponse = bookingAPI.createBooking(bookingDetails);
        Assert.assertEquals(createUserResponse.getStatusCode(), APIConstants.HTTP_SUCCESS);

        //Save the booking id
        int bookingId = createUserResponse.jsonPath().getInt("bookingid");

        //Prepare the list of the booking request attributes which needs to be filtered
        Map<String, Object> bookingDatesMap = Map.of(
                "checkin", bookingDetails.getBookingdates().getCheckin(),
                "checkout", bookingDetails.getBookingdates().getCheckout()
        );

        Map<String, Object> fieldsToBeFiltered = Map.of(
                "firstname", bookingDetails.getFirstname(),
                "lastname", bookingDetails.getLastname(),
                "bookingdates", bookingDatesMap
        );

            //filter the requests based on query params
            Response filterResponse = bookingAPI.getBookingByFilter(fieldsToBeFiltered);

            Assert.assertEquals(filterResponse.getStatusCode(), APIConstants.HTTP_SUCCESS);

        List<Integer> bookingIdsList = filterResponse.jsonPath().getList("bookingid", Integer.class);

            //Validate booking list not empty and contains the ID created in this test
            Assert.assertFalse(bookingIdsList.isEmpty());
            Assert.assertTrue(bookingIdsList.contains(bookingId), "Booking id appears in the filtered list");
        }


    @Test(description = "Validate filter with incorrect date format")
    public void checkInvalidDateFilter(){
        Map<String, Object> invalidDate = Map.of(
                "checkin", true
        );
        Response response = bookingAPI.getBookingByFilter(invalidDate);
        Assert.assertEquals(response.getStatusCode(), APIConstants.INTERNAL_SERVER_ERROR);
        Assert.assertEquals(response.getBody().asString(), "Internal Server Error");
    }

    @Ignore("API currently ignores unknown fields instead of returning error")
    @Test(description = "validate filter with incorrect parameter")
    public void checkInvalidParameterFilter(){
        Map<String, Object> invalidParam = Map.of(
                "nonexistentField", "abc"
        );
        Response response = bookingAPI.getBookingByFilter(invalidParam);
        Assert.assertEquals(response.getStatusCode(), APIConstants.INTERNAL_SERVER_ERROR);
        Assert.assertEquals(response.getBody().asString(), "Internal Server Error");
    }

    @Test(description = "Verify response structure and data type")
    public void checkResponseStructure(){
        //check response of the created booking id for test
        Response bookingResp = bookingAPI.getBookingById(createdBookingId);
        Assert.assertEquals(bookingResp.getStatusCode(), APIConstants.HTTP_SUCCESS);

        //Validate structure and data type
        BookingDetails booking = bookingResp.as(BookingDetails.class);
        Assert.assertNotNull(booking.getFirstname(), "Firstname should not be null");
        Assert.assertNotNull(booking.getLastname(), "Lastname should not be null");
        Assert.assertTrue(booking.getTotalprice() instanceof Integer, "Total price should be Integer");

        // For nested dates object
        Assert.assertNotNull(booking.getBookingdates(), "BookingDates should not be null");
        Assert.assertNotNull(booking.getBookingdates().getCheckin(), "Checkin should not be null");
        Assert.assertNotNull(booking.getBookingdates().getCheckout(), "Checkout should not be null");

        // validate additional fields if any
        Assert.assertNotNull(booking.getAdditionalneeds(), "Additional needs should not be null");
    }

    @Test(description = "Validate response time of the get bookings API")
    public void checkResponseTime(){
        long maxResponseTimeInMs = 2000;
        Response response = bookingAPI.getAllBookingByIds();
        Assert.assertEquals(response.getStatusCode(), APIConstants.HTTP_SUCCESS);
        //check execution time
        long responseTime = response.time();
        Assert.assertTrue(responseTime < maxResponseTimeInMs);
    }

    }


