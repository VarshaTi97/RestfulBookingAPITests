// This class contains all the API constants to be used in the project
package constants;

public class APIConstants {
    //API Endpoints
    public static final String BASE_URL = "https://restful-booker.herokuapp.com";
    public static final String AUTH = "/auth";
    public static final String GET_BOOKING_IDS = "/booking";
    public static final String BOOKING_By_ID = "/booking/{id}";
    public static final String PING = "/ping";
    public static final String CREATE_BOOKING = "/booking";
    public static final String UPDATE_BOOKING = "/booking/{id}";
    public static final String DELETE_BOOKING = "/booking/{id}";
    //Http status codes
    public static final int HTTP_SUCCESS = 200;
    public static final int HTTP_CREATED= 201;
    public static final int HTTP_UNAUTHORIZED= 403;
    public static final int HTTP_NOT_FOUND= 404;
    public static final int INTERNAL_SERVER_ERROR= 500;


    //Headers
    public static final String CONTENT_TYPE = "application/json";
    public static final String ACCEPT = "application/json";
}
