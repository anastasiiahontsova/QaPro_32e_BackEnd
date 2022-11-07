package com.telran.api.tests;

import com.jayway.restassured.RestAssured;
import dto.*;
import org.asynchttpclient.util.Assertions;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class RestAssuredTests {

    private AuthRequestDto loginUserDto() {
        AuthRequestDto requestDto = AuthRequestDto.builder()
                .email("michael@gmail.com")
                .password("ZZxcv_1!")
                .build();
        return requestDto;
    }


    @BeforeMethod
    public void Init (){
        RestAssured.baseURI = "https://contacts-telran.herokuapp.com";
        RestAssured.basePath = "api";
        loginUserDto();
    }

    @Test
    public void loginTestPositive(){
        AuthRequestDto requestDto = loginUserDto();

        AuthResponseDto authResponseDto = given()
                .contentType("application/json")
                .body(requestDto)
                .post("login")
                .then()
                .assertThat().statusCode(200)
                .extract().response().as(AuthResponseDto.class);

        System.out.println("AuthResponseDto obj: " + authResponseDto);

        String expectedToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6Im1pY2hhZWxAZ21haWwuY29tIn0.jheijxR8UOgsp_2k9bQy0HJXDeq1ZsiEL9YhJd43Zxc";
        // could be flaky, because the token can depends on a timestamp

        given()
                .contentType("application/json")
                .body(requestDto)
                .post("login")
                .then()
                .assertThat()
                .statusCode(200)
                .body(containsString("token"))
                .body("token", Matchers.equalTo(expectedToken))
                .extract().response().as(AuthResponseDto.class);

    }



    @Test
    public void loginTestNegative(){
        AuthRequestDto requestDto = AuthRequestDto.builder()
                .email("michael@gmail.com")
                .password("1111")
                .build();

        ErrorResponseDto errorResponseDto = given()
                .contentType("application/json")
                .body(requestDto)
                .post("login")
                .then()
                .assertThat().statusCode(400)
                .extract().response().as(ErrorResponseDto.class);

        System.out.println("ErrorResponseDto obj: " + errorResponseDto);

        String expectedErrorMessage = "Password length need be 8 or more symbols";

        String timestamp = given()
                .contentType("application/json")
                .body(requestDto)
                .post("login")
                .then()
                .assertThat()
                .statusCode(400)
                .body(containsString("timestamp"))
                .body("message", Matchers.equalTo(expectedErrorMessage))
                .extract().path("timestamp").toString();

        System.out.println("TimeStamp: " + timestamp);
    }


    @Test
    public void addNewContactTest(){
        ContactAddContactDto requestAddContactDto = ContactAddContactDto.builder()
                .address("Berlin")
                .description("Andrew's contact")
                .email("andrew@gmail.com")
                .id(0)
                .lastName("Black")
                .name("Andrew")
                .phone(123456789)
                .build();

        ContactResponseDto contactResponseDto = given()
                .contentType("application/json")
                .body(requestAddContactDto)
                .post("contact")
                .then()
                .assertThat()
                .statusCode(200)
                .body(containsString("address"))
                .body(containsString("description"))
                .body(containsString("email"))
                .body(containsString("id"))
                .body(containsString("lastName"))
                .body(containsString("name"))
                .body(containsString("phone"))
                .extract().response().as(ContactResponseDto.class);

        System.out.println("AuthAddContactDto info is: " + contactResponseDto);


        Assert.assertEquals(200, "200", "Status code is expected as 200.");
}

}
