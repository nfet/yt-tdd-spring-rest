package com.tta.tddrest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResultAssert;

import static com.tta.tddrest.InMemoryUserService.EXISTING_USER_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@WebMvcTest(UserResource.class)
public class UserResourceAPITests {

    @Autowired
    MockMvcTester mvc;

    @Test
    void get_ExistingUser_ShouldReturn_200_OK() {
        assertThat(mvc.get().uri("/user/{userId}", EXISTING_USER_ID)
                .accept(APPLICATION_JSON))
                .hasStatus(HttpStatus.OK)
                .hasContentType(APPLICATION_JSON)
                .bodyJson().satisfies(json -> {
                    json.assertThat().extractingPath("$.userId")
                            .as("userId")
                            .isEqualTo(EXISTING_USER_ID);
                    json.assertThat().extractingPath("$.username")
                            .as("username")
                            .isEqualTo("captain");
                    json.assertThat().extractingPath("$.firstName")
                            .as("firstName")
                            .isEqualTo("Steve");
                    json.assertThat().extractingPath("$.lastName")
                            .as("lastName")
                            .isEqualTo("Rogers");
                });
    }

    @Test
    void get_NonRegisteredUser_ShouldReturn_404_NOT_FOUND() {
        assertThat(mvc.get().uri("/user/1")
                .accept(APPLICATION_JSON))
                .hasStatus(HttpStatus.NOT_FOUND);
    }

    @Test
    void create_ShouldReturn_201_CREATED() {
        var newUser = """
                {"username":"wonder","firstName":"Linda","lastName":"Carter"}
                """;

        var result = assertThat(mvc.post().uri("/users", newUser)
                .contentType(APPLICATION_JSON)
                .content(newUser)
                .accept(APPLICATION_JSON))
                .hasStatus(HttpStatus.CREATED)
                .hasContentType(APPLICATION_JSON);
        // We can assert the JSON body next
        // Then, assert location  /users/{userId}
        result.bodyJson().convertTo(User.class).isNotNull()
                .satisfies(u -> {
                    result.hasHeader("Location", String.format("/user/%d", u.getUserId()));
                });
    }
}
