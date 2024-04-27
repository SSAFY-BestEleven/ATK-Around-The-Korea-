package com.ssafy.aroundthekorea.user.integration;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.aroundthekorea.user.controller.request.SignUpUserRequestDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserIntegrationTest {

	@LocalServerPort
	int port;

	@Autowired
	ObjectMapper objectMapper;

	@DisplayName("회원가입에 성공한다.")
	@Test
	void restAssured() throws JsonProcessingException {
		var request = new SignUpUserRequestDto("docker123",
			"{encryption} dn1!323nj@",
			"docweqe123@gmail.com");
		given().port(port).body(request).contentType(JSON).log().all().
			when().post("/api/v1/users")
			.then().log().all()
			.statusCode(HttpStatus.SC_OK);
	}
}
