package stepDefinitions;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;

import java.util.concurrent.ThreadLocalRandom;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class ManipulatingWithVotes {

    private RequestSpecification request;
    private Response response;
    private static int createdVoteId;
    private static JsonObject randomVoteJsonObject;
    private static JsonArray votesJasonArray;

    @Given("a request to get votes is prepared")
    public void a_request_to_get_votes_is_prepared() {
        request = RestAssured.given()
                .header("x-api-key", "DEMO-API-KEY")
                .header("Content-Type", "application/json");
    }

    @When("a GET request is made")
    public void a_get_request_is_made() {
        response = request.relaxedHTTPSValidation().when().get("https://api.thecatapi.com/v1/votes");
    }

    @Then("status code is {int}")
    public void status_code_is(Integer statusCode) {
        response.then().statusCode(statusCode);
    }

    @Then("length of the response is more than {int}")
    public void length_of_the_response_is_more_than(Integer unexpectedLength) {
        Assert.assertTrue(response.getBody().asString().length() > unexpectedLength);
    }

    @Then("votes are stored in proper object")
    public void votes_are_stored_in_proper_object() {
        votesJasonArray = JsonParser.parseString(response.getBody().asString()).getAsJsonArray();
    }

    @When("a GET request to retrieve specific vote is made")
    public void a_get_request_to_retrieve_specific_vote_is_made() {
        int randomIndex = ThreadLocalRandom.current().nextInt(0, votesJasonArray.size());
        randomVoteJsonObject = votesJasonArray.get(randomIndex).getAsJsonObject();
        int randomVoteId = randomVoteJsonObject.get("id").getAsInt();
        response = request.relaxedHTTPSValidation().when().get("https://api.thecatapi.com/v1/votes/" + randomVoteId);
    }

    @Then("retrieved vote is corresponding to the stored one")
    public void retrieved_vote_is_corresponding_to_the_stored_one() {
        response.then().
                assertThat().body("id", is(randomVoteJsonObject.get("id").getAsInt())).
                assertThat().body("image_id", is(randomVoteJsonObject.get("image_id").getAsString())).
                assertThat().body("created_at", is(randomVoteJsonObject.get("created_at").getAsString())).
                assertThat().body("value", is(randomVoteJsonObject.get("value").getAsInt()));
    }

    @Given("a request to post vote is prepared")
    public void a_request_to_post_vote_is_prepared() {
        JsonObject payload = new JsonObject();
        payload.addProperty("image_id","asf2");
        payload.addProperty("sub_id", "my-user-1234");
        payload.addProperty("value",1);
        request = RestAssured.given()
                .header("x-api-key", "DEMO-API-KEY")
                .header("Content-Type", "application/json")
                .body(payload.toString());
    }

    @When("a POST request to create a vote is made")
    public void a_post_request_to_create_a_vote_is_made() {
        response = request.relaxedHTTPSValidation().when().post("https://api.thecatapi.com/v1/votes");
    }
    @Then("response contains {string}: {string}")
    public void response_contains(String field, String value) {
        response.then().assertThat().body(field,is(value));
    }
    @Then("{string} in response is not empty")
    public void in_response_is_not_empty(String field) {
        response.then().assertThat().body(field,notNullValue());
        createdVoteId = response.jsonPath().get(field);
    }

    @When("a GET request to retrieve created vote is made")
    public void a_get_request_to_retrieve_created_vote_is_made() {
        response = request.relaxedHTTPSValidation().when().get("https://api.thecatapi.com/v1/votes/" + createdVoteId);
    }
    @Then("{string} in response is equal to required id")
    public void in_response_is_equal_to_required_id(String field) {
        response.then().assertThat().body(field,is(createdVoteId));
    }

    @Given("a request to delete vote is prepared")
    public void a_request_to_delete_vote_is_prepared() {
        request = RestAssured.given()
                .header("x-api-key", "DEMO-API-KEY")
                .header("Content-Type", "application/json");
    }
    @When("a DELETE request is made")
    public void a_delete_request_is_made() {
        response = request.relaxedHTTPSValidation().when().delete("https://api.thecatapi.com/v1/votes/" + createdVoteId);
    }

    @When("a GET request to retrieve deleted vote is made")
    public void a_get_request_to_retrieve_deleted_vote_is_made() {
        response = request.relaxedHTTPSValidation().when().get("https://api.thecatapi.com/v1/votes/" + createdVoteId);
    }

}
