package com.telran.api.tests;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dto.AuthRequestDto;
import dto.AuthResponseDto;
import dto.ErrorResponseDto;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.time.Duration;

import static java.net.http.HttpRequest.newBuilder;
import static org.apache.http.HttpRequest.*;
import static org.apache.http.HttpResponse.*;


public class HttpClientTest {

    public static final ContentType APPLICATION_JSON = ContentType.APPLICATION_JSON;

    @Test
    public void loginPositiveTest() throws IOException {
        String email = "testlogin@gmail.com";
        String password = "Test123456789!_";

        Response response = Request.Post("https://contacts-telran.herokuapp.com/api/login")
                .bodyString("{\n" +
                        "  \"email\": \"" + email + "\",\n" +
                        "  \"password\": \"" + password + "\"\n" +
                        "}", APPLICATION_JSON)
                .execute();

        System.out.println("Response: " + response);

        HttpResponse httpResponse = response.returnResponse();

        System.out.println("Http response: " + httpResponse);

        System.out.println("Status: " + httpResponse.getStatusLine());
        int responseStatus = httpResponse.getStatusLine().getStatusCode();
        System.out.println("Status code: " + responseStatus);

        String bodyString = EntityUtils.toString(httpResponse.getEntity());
        System.out.println("Body: " + bodyString);

        JsonObject responseBodyJsonObj = JsonParser.parseString(bodyString).getAsJsonObject();
        System.out.println("Body as JsonObject: " + responseBodyJsonObj);
        String token = responseBodyJsonObj.get("token").toString();
        System.out.println("Token: " + token);

        Assert.assertEquals(responseStatus, 200, "API returns correct response code.");
        Assert.assertTrue(token.length() > 0, "API returns non-empty token" + token);
    }


    @Test
    public void loginPositiveTestWithDto() throws IOException {
        AuthRequestDto requestDto = AuthRequestDto.builder()
                .email("testlogin@gmail.com")
                .password("Test123456789!_")
                .build();

        Gson gson = new Gson();

        Response response = Request.Post(" ")
                .bodyString(gson.toJson(requestDto), APPLICATION_JSON)
                .execute();

        System.out.println("Response: " + response);

        HttpResponse httpResponse = response.returnResponse();

        System.out.println("Http response: " + httpResponse);

        System.out.println("Status: " + httpResponse.getStatusLine());
        int responseStatus = httpResponse.getStatusLine().getStatusCode();
        System.out.println("Status code: " + responseStatus);

        String bodyString = EntityUtils.toString(httpResponse.getEntity());
        System.out.println("Body: " + bodyString);

        AuthResponseDto responseDto = gson.fromJson(bodyString, AuthResponseDto.class);
        System.out.println("AuthResponseDto: " + responseDto);
        Assert.assertEquals(responseStatus, 200, "API returns correct response code.");
        Assert.assertTrue(responseDto.getToken().length() > 0, "API returns non-empty token" + responseDto.getToken());

//        JsonObject responseBodyJsonObj = JsonParser.parseString(bodyString).getAsJsonObject();
//        System.out.println("Body as JsonObject: " + responseBodyJsonObj);
//        String token = responseBodyJsonObj.get("token").toString();
//        System.out.println("Token: " + token);
//
//        Assert.assertEquals(responseStatus, 200, "API returns correct response code.");
//        Assert.assertTrue(token.length() > 0, "API returns non-empty token" + token);
    }

    @Test
    public void loginNegativeTestWithDto() throws IOException {
        AuthRequestDto requestDto = AuthRequestDto.builder()
                .email("testlogin@gmail.com")
                .password("1111")
                .build();

        Gson gson = new Gson();

        Response response = Request.Post("https://contacts-telran.herokuapp.com/api/login")
                .bodyString(gson.toJson(requestDto), APPLICATION_JSON)
                .execute();

        System.out.println("Response: " + response);

        HttpResponse httpResponse = response.returnResponse();

        System.out.println("Http response: " + httpResponse);

        System.out.println("Status: " + httpResponse.getStatusLine());
        int responseStatus = httpResponse.getStatusLine().getStatusCode();
        System.out.println("Status code: " + responseStatus);

        String bodyString = EntityUtils.toString(httpResponse.getEntity());
        System.out.println("Body: " + bodyString);

        ErrorResponseDto errorResponseDto = gson.fromJson(bodyString, ErrorResponseDto.class);
        System.out.println("errorResponseDto: " + errorResponseDto);

//        JsonObject responseBodyJsonObj = JsonParser.parseString(bodyString).getAsJsonObject();
//        System.out.println("Body as JsonObject: " + responseBodyJsonObj);
//        String message = responseBodyJsonObj.get("message").toString();
//        System.out.println("Message: " + message);

        Assert.assertEquals(responseStatus, 400, "API returns correct error code (400).");
        Assert.assertEquals(errorResponseDto.getMessage(), "Password length need be 8 or more symbols", "API returns correct error message");
        Assert.assertEquals(errorResponseDto.getCode(), "400", "API returns correct error code (400).");
        Assert.assertEquals(errorResponseDto.getDetails(), "uri=/api/login", "API returns correct details on error");
    }

    @Test
    public void loginNegativeLoginTestWithDto() throws IOException {
        AuthRequestDto requestDto = AuthRequestDto.builder()
                .email("invalidemail@gmail.com")
                .password("Test123456789!_")
                .build();

        Gson gson = new Gson();

        Response response = Request.Post("https://contacts-telran.herokuapp.com/api/login")
                .bodyString(gson.toJson(requestDto), APPLICATION_JSON)
                .execute();

        System.out.println("Response: " + response);

        HttpResponse httpResponse = response.returnResponse();

        System.out.println("Http response: " + httpResponse);

        System.out.println("Status: " + httpResponse.getStatusLine());
        int responseStatus = httpResponse.getStatusLine().getStatusCode();
        System.out.println("Status code: " + responseStatus);

        String bodyString = EntityUtils.toString(httpResponse.getEntity());
        System.out.println("Body: " + bodyString);

        ErrorResponseDto errorResponseDto = gson.fromJson(bodyString, ErrorResponseDto.class);
        System.out.println("errorResponseDto: " + errorResponseDto);

        Assert.assertEquals(responseStatus, 500, "API returns correct error code (500).");
        Assert.assertEquals(errorResponseDto.getMessage(), "Wrong email or password!", "API returns correct error message");
        Assert.assertEquals(errorResponseDto.getCode(), "500", "API returns correct error code (500).");
        Assert.assertEquals(errorResponseDto.getDetails(), "uri=/api/login", "API returns correct details on error");
    }

    @Test
    public void getAllContactsTestWithDto() throws IOException, URISyntaxException, InterruptedException {

//        AuthRequestDto requestDto = AuthRequestDto.builder()
//                .email("invalidemail@gmail.com")
//                .password("Test123456789!_")
//                .build();
//
//        Gson gson = new Gson();
//
//        Response authresponse = Request.Post("https://contacts-telran.herokuapp.com/api/login")
//                .bodyString(gson.toJson(requestDto), APPLICATION_JSON)
//                .execute();

        Response response = Request.Get("https://contacts-telran.herokuapp.com/api/contact").execute();


        System.out.println("Response: " + response);
        HttpResponse httpResponse = response.returnResponse();
        System.out.println("Http response: " + httpResponse);
        System.out.println("Status: " + httpResponse.getStatusLine());
        int responseStatus = httpResponse.getStatusLine().getStatusCode();
        System.out.println("Status code: " + responseStatus);

        String bodyString = EntityUtils.toString(httpResponse.getEntity());
        System.out.println("Body: " + bodyString);

        Assert.assertEquals(responseStatus, 200, "API returns correct response code.");

    }
}


