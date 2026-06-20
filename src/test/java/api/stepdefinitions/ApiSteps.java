package api.stepdefinitions;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import static org.junit.jupiter.api.Assertions.*;

public class ApiSteps {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    private Response response;

    // ── Requests ──────────────────────────────────────────────────────────────

    @Given("User sends POST request to {string} with title {string} body {string} and userId {int}")
    public void sendPostRequest(String endpoint, String title, String body, int userId) {
        JSONObject payload = new JSONObject();
        payload.put("title", title);
        payload.put("body", body);
        payload.put("userId", userId);

        response = RestAssured
                .given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .body(payload.toString())
                .when()
                .post(endpoint);

        response.prettyPrint();
    }

    @Given("User sends GET request to {string}")
    public void sendGetRequest(String endpoint) {
        response = RestAssured
                .given()
                .baseUri(BASE_URL)
                .when()
                .get(endpoint);

        response.prettyPrint();
    }

    @Given("User sends DELETE request to {string}")
    public void sendDeleteRequest(String endpoint) {
        response = RestAssured
                .given()
                .baseUri(BASE_URL)
                .when()
                .delete(endpoint);

        response.prettyPrint();
    }

    @Given("User sends PUT request to {string} with title {string} body {string} and userId {int}")
    public void sendPutRequest(String endpoint, String title, String body, int userId) {
        JSONObject payload = new JSONObject();
        payload.put("title", title);
        payload.put("body", body);
        payload.put("userId", userId);

        response = RestAssured
                .given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .body(payload.toString())
                .when()
                .put(endpoint);

        response.prettyPrint();
    }

    // ── Status Code ───────────────────────────────────────────────────────────

    @Then("Response status code should be {int}")
    public void validateStatusCode(int expectedCode) {
        assertEquals(expectedCode, response.getStatusCode(),
                "Expected status code " + expectedCode + " but got " + response.getStatusCode());
    }

    // ── Field Validation ──────────────────────────────────────────────────────

    @Then("Response title should be {string}")
    public void validateTitle(String expectedTitle) {
        String actualTitle = response.jsonPath().getString("title");
        assertEquals(expectedTitle, actualTitle,
                "Title mismatch: expected '" + expectedTitle + "' but got '" + actualTitle + "'");
    }

    @Then("Response body should be {string}")
    public void validateBody(String expectedBody) {
        String actualBody = response.jsonPath().getString("body");
        assertEquals(expectedBody, actualBody,
                "Body mismatch: expected '" + expectedBody + "' but got '" + actualBody + "'");
    }

    @Then("Response userId should be {int}")
    public void validateUserId(int expectedUserId) {
        int actualUserId = response.jsonPath().getInt("userId");
        assertEquals(expectedUserId, actualUserId,
                "UserId mismatch: expected " + expectedUserId + " but got " + actualUserId);
    }

    // ── GET All Posts Validation ──────────────────────────────────────────────

    @Then("All posts should have non-null id")
    public void validateAllPostsHaveId() {
        JSONArray posts = new JSONArray(response.getBody().asString());

        for (int i = 0; i < posts.length(); i++) {
            JSONObject post = posts.getJSONObject(i);
            assertNotNull(post.get("id"),
                    "Post at index " + i + " has null id");
            assertTrue(post.getInt("id") > 0,
                    "Post at index " + i + " has invalid id: " + post.getInt("id"));
        }

        System.out.println("✅ All " + posts.length() + " posts have non-null id");
    }

    // ── Delete Validation ─────────────────────────────────────────────────────

    @Then("Response body is empty object")
    public void validateEmptyResponseBody() {
        String body = response.getBody().asString().trim();
        assertEquals("{}", body, "Expected empty object '{}' but got: " + body);
    }

    // ── JSON Schema Validation ────────────────────────────────────────────────

    @Then("Response matches post schema")
    public void validatePostSchema() {
        response.then().assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/post_schema.json"));
    }

    @Then("Response matches posts array schema")
    public void validatePostsArraySchema() {
        response.then().assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/posts_array_schema.json"));
    }

    @Then("Response matches delete schema")
    public void validateDeleteSchema() {
        response.then().assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/delete_schema.json"));
    }
}