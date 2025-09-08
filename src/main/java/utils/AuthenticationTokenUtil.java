package utils;

import constants.APIConstants;
import io.restassured.response.Response;
import pojoModels.AuthRequest;
import pojoModels.AuthResponse;

public class AuthenticationTokenUtil {
    public static String token;
    public static String getAuthToken(String username, String password){
        if(token !=null) {
            return token;
        }
        //payload for authentication request
        AuthRequest authRequest = new AuthRequest(username, password);
        Response response = APIUtils.getRequestSpec().body(authRequest).post(APIConstants.AUTH);
        //validating token successfully generated
        response.then().statusCode(APIConstants.HTTP_SUCCESS);
        //deserialize the response to pojo
        AuthResponse authResponse = response.as(AuthResponse.class);
        token = authResponse.getToken();
        return token;
    }
}
