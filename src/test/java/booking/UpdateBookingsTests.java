package booking;

import constants.APIConstants;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.PropertyFileUtil;

import java.util.HashMap;
import java.util.Map;

public class UpdateBookingsTests extends BaseTest {

    @Test(description = "Update firstname parameter of the existing booking")
    public void updateFirstNameFieldForExistingBooking() {
        Map<String, Object> updates = new HashMap<>();
        updates.put("firstname", "John");

        //Updating firstname of the test booking created by createBooking() in baseTest
        Response response = bookingAPI.partialUpdateBooking(createdBookingId, updates);

        Assert.assertEquals(response.getStatusCode(), APIConstants.HTTP_SUCCESS, "PATCH failed");

        //Validating the first name
        String updatedFirstName = response.jsonPath().getString("firstname");
        Assert.assertEquals(updatedFirstName, "John", "Firstname not updated");
    }

    @Test(description = "Update lastname parameter of the existing booking")
    public void updateLastNameFieldForExistingBooking() {
        Map<String, Object> updates = new HashMap<>();
        updates.put("lastname", "Smith");

        //Updating lastname of the test booking created in baseTest
        Response response = bookingAPI.partialUpdateBooking(createdBookingId, updates);

        Assert.assertEquals(response.getStatusCode(), APIConstants.HTTP_SUCCESS, "PATCH failed");

        //Validating the last name
        String updatedLastName = response.jsonPath().getString("lastname");
        Assert.assertEquals(updatedLastName, "Smith", "Lastname not updated");
    }

    @Test(description = "Update additional needs and total price ")
    public void updateAdditionalNeedsAndTotalPriceForExistingBooking() {
        Map<String, Object> updates = new HashMap<>();
        updates.put("additionalneeds", "coffee machine");
        updates.put("totalprice", 299);

        //updated multiple attributes
        Response response = bookingAPI.partialUpdateBooking(createdBookingId, updates);

        Assert.assertEquals(response.getStatusCode(), APIConstants.HTTP_SUCCESS, "PATCH failed");

        //get multiple attributes from the response
        String updatedAdditionalNeeds = response.jsonPath().getString("additionalneeds");
        String updatedTotalPrice = response.jsonPath().getString("totalprice");

        //Validate the updated values
        Assert.assertEquals(updatedAdditionalNeeds, "coffee machine", "Additional needs not updated");
        Assert.assertEquals(updatedTotalPrice, "299", "Total price not updated");
    }

    @Test(description = "Update checkin and checkout date and validate other fields remain unchanged")
    public void updateCheckinAndCheckoutDateAndCheckOtherFieldsUnchangedForExistingBooking() {
        //Updated dates to be used in the patch field
        Map<String, Object> updates = new HashMap<>();
        Map<String, String> bookingDates = new HashMap<>();
        bookingDates.put("checkin", "2026-09-02");
        bookingDates.put("checkout", "2026-09-05");
        updates.put("bookingdates", bookingDates);

        Response response = bookingAPI.partialUpdateBooking(createdBookingId, updates);

        Assert.assertEquals(response.getStatusCode(), APIConstants.HTTP_SUCCESS, "PATCH failed");

        //Validate updated fields
        Assert.assertEquals(response.jsonPath().getString("bookingdates.checkin"), "2026-09-02", "checkin date is not updated");
        Assert.assertEquals(response.jsonPath().getString("bookingdates.checkout"), "2026-09-05", "checkout date is not updated");

        //Validate unchanged fields
        Assert.assertEquals(response.jsonPath().getString("firstname"), PropertyFileUtil.get("firstname"), "firstname is updated");
        Assert.assertEquals(response.jsonPath().getString("lastname"), PropertyFileUtil.get("lastname"), "lastname is updated");
        Assert.assertEquals(response.jsonPath().getString("totalprice"), PropertyFileUtil.get("totalprice"), "total price is updated");
        Assert.assertEquals(response.jsonPath().getString("depositpaid"), PropertyFileUtil.get("depositpaid"), "deposit paid is updated");
        Assert.assertEquals(response.jsonPath().getString("additionalneeds"), PropertyFileUtil.get("additionalneeds"), "additional needs is updated");
    }

    @Test(description = "Update non-existing booking id")
    public void updateNonExistingBookingId() {

        //Patch test data
        Map<String, Object> updates = new HashMap<>();
        Map<String, String> bookingDates = new HashMap<>();
        bookingDates.put("checkin", "2026-10-01");
        bookingDates.put("checkout", "2026-10-05");
        updates.put("bookingdates", bookingDates);

        //Try to update non-existent booking ids
        Response response = bookingAPI.partialUpdateBooking(999999, updates);
        Assert.assertEquals(response.getStatusCode(), 405, "Expected 405 for invalid booking ID");
    }

    @Test(description = "Update existing booking id with invalid token")
    public void checkPatchWithIncorrectTokenForExistingBookingId(){
        //Patch test data
        Map<String, Object> updates = new HashMap<>();
        Map<String, String> bookingDates = new HashMap<>();
        bookingDates.put("checkin", "2026-10-01");
        bookingDates.put("checkout", "2026-10-05");
        updates.put("bookingdates", bookingDates);

        //Test patch with invalid token passed in parameter
        Response response = bookingAPI.partialUpdateBooking(createdBookingId, updates, null);
        Assert.assertEquals(response.getStatusCode(), APIConstants.HTTP_UNAUTHORIZED, "Expected 403 for invalid token");
    }

    @Test(description = "Verify idempotency of PATCH update")
    public void verifyIdempotencyOfUpdates() {
        // Prepare the update payload
        Map<String, Object> updates = new HashMap<>();
        Map<String, String> bookingDates = new HashMap<>();
        bookingDates.put("checkin", "2026-12-01");
        bookingDates.put("checkout", "2026-12-05");
        updates.put("bookingdates", bookingDates);

        // First PATCH
        Response firstResponse = bookingAPI.partialUpdateBooking(createdBookingId, updates);
        Assert.assertEquals(firstResponse.getStatusCode(), APIConstants.HTTP_SUCCESS);

        // Capture updated state
        String firstCheckin = firstResponse.jsonPath().getString("bookingdates.checkin");
        String firstCheckout = firstResponse.jsonPath().getString("bookingdates.checkout");

        // Second PATCH with the same payload
        Response secondResponse = bookingAPI.partialUpdateBooking(createdBookingId, updates);
        Assert.assertEquals(secondResponse.getStatusCode(), APIConstants.HTTP_SUCCESS);

        // Validate that fields did not change further
        Assert.assertEquals(secondResponse.jsonPath().getString("bookingdates.checkin"), firstCheckin, "Checkin changed unexpectedly");
        Assert.assertEquals(secondResponse.jsonPath().getString("bookingdates.checkout"), firstCheckout, "Checkout changed unexpectedly");

        //Validate that other fields remain unchanged
        Assert.assertEquals(secondResponse.jsonPath().getString("firstname"), PropertyFileUtil.get("firstname"));
        Assert.assertEquals(secondResponse.jsonPath().getString("lastname"), PropertyFileUtil.get("lastname"));
    }

}




