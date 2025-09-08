package utils;


import constants.APIConstants;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class APIUtils {

    // base request specification
    public static RequestSpecification getRequestSpec() {
        return given()
                .baseUri(APIConstants.BASE_URL)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json");
    }

    //request specification with token
    public static RequestSpecification getRequestSpec(String token){
        RequestSpecification requestSpecification = getRequestSpec();
        if(token !=null && !token.isEmpty()){
            requestSpecification.header("Cookie", "token="+token);
        }
        return requestSpecification;
    }

    // request specification with token and query params
    public static RequestSpecification getRequestSpec(String token, Map<String, Object> queryParam){
        RequestSpecification requestSpecification = getRequestSpec(token);
        if(queryParam !=null && !queryParam.isEmpty()){
            requestSpecification.queryParams(queryParam);
        }
        return requestSpecification;
    }

    // request specification with query params
    public static RequestSpecification getRequestSpec(Map<String, Object> queryParam){
        RequestSpecification requestSpecification = getRequestSpec();
        if(queryParam !=null && !queryParam.isEmpty()){
            requestSpecification.queryParams(queryParam);
        }
        return requestSpecification;
    }

    //generic get method
    public static Response get(String endpoint, RequestSpecification requestSpecification) {
        RequestSpecification spec = (requestSpecification != null)
                ? requestSpecification
                : getRequestSpec();

        return given(spec)
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
    }

    public static Response post(String endpoint, RequestSpecification requestSpecification, Object body){
        return given(requestSpecification)
                .body(body)
                .when()
                .post(endpoint)
                .then()
                .extract()
                .response();
    }

    public static Response patch(String endpoint, RequestSpecification requestSpecification, Object body){
        return given(requestSpecification)
                .body(body)
                .when()
                .patch(endpoint)
                .then()
                .extract()
                .response();
    }

    public static Response delete(String endpoint, RequestSpecification requestSpecification){
        return given(requestSpecification)
                .when()
                .delete(endpoint)
                .then()
                .extract()
                .response();
    }
}
