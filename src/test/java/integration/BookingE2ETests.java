package integration;

import constants.APIConstants;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.BookingAPI;
import pojoModels.BookingDates;
import pojoModels.BookingDetails;
import utils.PropertyFileUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingE2ETests extends BaseE2Etest{

    @Test(description = "Test entire lifecycle: cration, updation, verification, deletion")
    public void checkBookingLifecycle(){
        // --- CREATE ---
        BookingDates bookingDates = new BookingDates(PropertyFileUtil.get("checkin"), PropertyFileUtil.get("checkout"));
        BookingDetails booking = new BookingDetails(
                PropertyFileUtil.get("firstname"),
                PropertyFileUtil.get("lastname"),
                Integer.parseInt(PropertyFileUtil.get("totalprice")),
                Boolean.parseBoolean(PropertyFileUtil.get("depositpaid")),
                bookingDates,
                PropertyFileUtil.get("additionalneeds")
        );

        Response createResponse = bookingAPI.createBooking(booking);
        Assert.assertEquals(createResponse.getStatusCode(), APIConstants.HTTP_SUCCESS);
        int bookingId = createResponse.jsonPath().getInt("bookingid");

        // --- PARTIAL UPDATE ---
        Map<String, Object> updates = new HashMap<>();
        Map<String, String> updatedDates = new HashMap<>();
        updatedDates.put("checkin", "2026-12-01");
        updatedDates.put("checkout", "2026-12-05");
        updates.put("bookingdates", updatedDates);

        Response patchResponse = bookingAPI.partialUpdateBooking(bookingId, updates, token);
        Assert.assertEquals(patchResponse.getStatusCode(), APIConstants.HTTP_SUCCESS);

        // --- VERIFY ---
        Response response = bookingAPI.getBookingById(bookingId);
        Assert.assertEquals(response.jsonPath().getString("bookingdates.checkin"), "2026-12-01");
        Assert.assertEquals(response.jsonPath().getString("bookingdates.checkout"), "2026-12-05");

        //Validate unchanged fields
        Assert.assertEquals(response.jsonPath().getString("firstname"), PropertyFileUtil.get("firstname"), "firstname is updated");
        Assert.assertEquals(response.jsonPath().getString("lastname"), PropertyFileUtil.get("lastname"), "lastname is updated");
        Assert.assertEquals(response.jsonPath().getString("totalprice"), PropertyFileUtil.get("totalprice"), "total price is updated");
        Assert.assertEquals(response.jsonPath().getString("depositpaid"), PropertyFileUtil.get("depositpaid"), "deposit paid is updated");
        Assert.assertEquals(response.jsonPath().getString("additionalneeds"), PropertyFileUtil.get("additionalneeds"), "additional needs is updated");

        // --- DELETE ---
        Response deleteResponse = bookingAPI.deleteBooking(bookingId);
        Assert.assertTrue(deleteResponse.getStatusCode() == 200 || deleteResponse.getStatusCode() == 201);

    }

    @Test(description = "Validate cross endpoint consistency")
    public void testCrossEndpointResults(){
        // --- CREATE ---
        BookingDates bookingDates = new BookingDates(PropertyFileUtil.get("checkin"), PropertyFileUtil.get("checkout"));
        BookingDetails booking = new BookingDetails(
                PropertyFileUtil.get("firstname"),
                PropertyFileUtil.get("lastname"),
                Integer.parseInt(PropertyFileUtil.get("totalprice")),
                Boolean.parseBoolean(PropertyFileUtil.get("depositpaid")),
                bookingDates,
                PropertyFileUtil.get("additionalneeds")
        );

        Response createResponse = bookingAPI.createBooking(booking);
        Assert.assertEquals(createResponse.getStatusCode(), APIConstants.HTTP_SUCCESS);
        int bookingId = createResponse.jsonPath().getInt("bookingid");

        //Verify get by id
        Response getResp = bookingAPI.getBookingById(bookingId);
        Assert.assertEquals(getResp.jsonPath().getString("firstname"), PropertyFileUtil.get("firstname"));
        Assert.assertEquals(getResp.jsonPath().getInt("totalprice"), Integer.parseInt(PropertyFileUtil.get("totalprice")));

        // Get all the bookings and check if booking id exists
        Response responseAllBookings = bookingAPI.getAllBookingByIds();
        List<Integer> bookingList = responseAllBookings.jsonPath().getList("bookingid");
        Assert.assertTrue(bookingList.contains(bookingId));
        // --- DELETE ---
        Response deleteResponse = bookingAPI.deleteBooking(bookingId);
        Assert.assertTrue(deleteResponse.getStatusCode() == 200 || deleteResponse.getStatusCode() == 201);

    }
}
