package pages;

import constants.APIConstants;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pojoModels.BookingDates;
import pojoModels.BookingDetails;
import utils.APIUtils;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class BookingAPI {

    private final String token;

    public BookingAPI(String token){
        this.token = token;
    }

    @Step("Get all the existing booking ids")
    public Response getAllBookingByIds(){
        RequestSpecification specification = APIUtils.getRequestSpec();
        return APIUtils.get(APIConstants.GET_BOOKING_IDS,specification);
    }

    @Step("Create booking for {bookingDetails}")
    public Response createBooking(BookingDetails bookingDetails){
        return APIUtils.post(APIConstants.CREATE_BOOKING, APIUtils.getRequestSpec(),
                bookingDetails);
    }

    @Step("Get booking by filtering with booking id {bookingId}")
    public Response getBookingById(int bookingId){
        return APIUtils.get(APIConstants.BOOKING_By_ID.replace("{id}", String.valueOf(bookingId)),
                APIUtils.getRequestSpec());
    }

    @Step("Delete booking by filtering with booking id {bookingId}")
    public Response deleteBooking(int bookingId){
        return APIUtils.delete(APIConstants.DELETE_BOOKING.replace("{id}", String.valueOf(bookingId)),
                APIUtils.getRequestSpec(token));
    }

    @Step("Delete booking by filtering with booking id {bookingId} and passing token {token}")
    public Response deleteBooking(int bookingId, String token){
        return APIUtils.delete(APIConstants.DELETE_BOOKING.replace("{id}", String.valueOf(bookingId)),
                APIUtils.getRequestSpec(token));
    }

    @Step("Get booking id after filtering with {queryParam}")
    public Response getBookingByFilter(Map<String,Object> queryParam){
        return APIUtils.get(APIConstants.GET_BOOKING_IDS,
                APIUtils.getRequestSpec(queryParam));
    }

    @Step("Get random booking id form list of bookings")
    public int getRandomBookingId(){
        List<Integer> bookingIds = getAllBookingByIds()
                .jsonPath()
                .getList("bookingid", Integer.class);
        Random random = new Random();
        int randomIndex = random.nextInt(bookingIds.size());
       return bookingIds.get(randomIndex);
    }

    @Step("Update {bookingId} partially with {updatedBody}")
    public Response partialUpdateBooking(Integer bookingId, Map<String, Object> updatedBody) {
        return APIUtils.patch(APIConstants.UPDATE_BOOKING.replace("{id}", String.valueOf(bookingId)),
                APIUtils.getRequestSpec(token), updatedBody);
    }

    @Step("Update {bookingId} partially with {updatedBody} and {token}")
    public Response partialUpdateBooking(Integer bookingId, Map<String, Object> updatedBody, String token) {
        return APIUtils.patch(APIConstants.UPDATE_BOOKING.replace("{id}", String.valueOf(bookingId)),
                APIUtils.getRequestSpec(token), updatedBody);
    }
}
