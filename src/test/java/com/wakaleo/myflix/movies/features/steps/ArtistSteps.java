package com.wakaleo.myflix.movies.features.steps;

import com.wakaleo.myflix.movies.MovieServiceApplication;
import com.wakaleo.myflix.movies.features.serenitysteps.MovieCatalog;
import com.wakaleo.myflix.movies.model.Movie;

import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Steps;

import org.hamcrest.Matchers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static com.wakaleo.myflix.movies.features.steps.MovieComparators.byTitleAndDirector;
import static net.serenitybdd.rest.SerenityRest.rest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(classes = MovieServiceApplication.class, webEnvironment=WebEnvironment.RANDOM_PORT)
public class ArtistSteps {

	@LocalServerPort
    int port;

    @Before
    public void configurePorts() {
    	
    }

    String actor;
    
    @When("^I consult the filmography of (.*)$")
    public void i_consult_the_filmography_of_Ben_Stiller(String actor) throws Throwable {
    	this.actor = actor;
		rest().port(port).get("/artists/" + actor).then().body("name", equalTo(actor));
    }
    
    /*
     	"name": "Ben Stiller",
     	"acted-in": {...}
     	"directed-in": {...}
      
      
     */

    @Then("^I should see the following films that he has acted in:$")
    public void i_should_see_the_following_films_that_he_has_acted_in(List<String> moviesActedIn) throws Throwable {
    	rest().port(port).when().get("/artists/" + actor).then().body("actedIn.title", Matchers.hasItems(moviesActedIn.toArray()));
    }

    @Then("^I should see the following films that he has directed:$")
    public void i_should_see_the_following_films_that_he_has_directed(List<String> moviesDirected) throws Throwable {
    	rest().port(port).when().get("/artists/" + actor).then().body("directed.title", Matchers.hasItems(moviesDirected.toArray()));
    }
  
}