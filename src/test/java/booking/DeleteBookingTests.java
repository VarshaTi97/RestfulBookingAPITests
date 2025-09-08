package booking;

import constants.APIConstants;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DeleteBookingTests extends  BaseTest{

    @Test(description="Check if you can delete existing booking details")
    public void checkDeleteExistingBooking(){
        Response response = bookingAPI.deleteBooking(createdBookingId);
        Assert.assertEquals(response.statusCode(), APIConstants.HTTP_CREATED);

        response = bookingAPI.getBookingById(createdBookingId);
        Assert.assertEquals(response.statusCode(),APIConstants.HTTP_NOT_FOUND);
    }

    @Test(description = "check non-existent booking id")
    public void checkNonExistentBookingId(){
        int invalidBookingId = 9090;
        Response response = bookingAPI.deleteBooking(invalidBookingId);
        Assert.assertEquals(response.statusCode(), 405);
    }

    @Test(description = "check deletion with invalid authentication token")
    public void checkDeletionWithInvalidToken(){
        Response response = bookingAPI.deleteBooking(bookingAPI.getRandomBookingId(), "123");
        Assert.assertEquals(response.statusCode(), APIConstants.HTTP_UNAUTHORIZED);
    }
}
