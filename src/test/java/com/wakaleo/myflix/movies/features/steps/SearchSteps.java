package com.wakaleo.myflix.movies.features.steps;

import com.google.common.base.Splitter;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.wakaleo.myflix.movies.MovieServiceApplication;
import com.wakaleo.myflix.movies.features.serenitysteps.MovieCatalog;
import com.wakaleo.myflix.movies.model.Movie;
import cucumber.api.DataTable;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Steps;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.wakaleo.myflix.movies.features.steps.MovieComparators.byTitleAndDirector;
import static net.serenitybdd.rest.SerenityRest.rest;
import static org.assertj.core.api.Assertions.assertThat;

//@WebAppConfiguration
@SpringBootTest(classes = MovieServiceApplication.class, webEnvironment=WebEnvironment.RANDOM_PORT)
public class SearchSteps {

    @Steps
    MovieCatalog theMovieCatalog;

    @LocalServerPort
    int port;

    List<Movie> matchingMovies;

    @Before
    public void configurePorts() {
    }

    @Given("the catalog has the following movies:")
    public void catalogMovies(DataTable movieData) {
        List<Map<String,String>> movieRows = movieData.asMaps(String.class, String.class);
        List<Movie> movies = movieRows.stream()
                                      .map(SearchSteps::convertStringDataToMovie)
                                      .collect(Collectors.toList());
        theMovieCatalog.hasTheFollowingMovies(movies);
    }

    private static Movie convertStringDataToMovie(Map<String, String> movieRow) {
        return new Movie(movieRow.get("title"),
                movieRow.get("description"),
                movieRow.get("director"),
                Splitter.on(",").omitEmptyStrings().trimResults().splitToList(movieRow.get("actors")));
    }

    @When("I search for movies directed by (.*)")
    public void searchByDirector(String director) {
        Movie[] movies = rest().port(port).when().get("/movies/findByDirector/" + director).as(Movie[].class);
        matchingMovies = ImmutableList.copyOf(movies);
    }

    @When("I search for a movie called '(.*)'")
    public void searchByTitle(String title) {
        Movie  matchingMovie = rest().port(port).when().get("/movies/findByTitle/" + title).as(Movie.class);
        matchingMovies = ImmutableList.of(matchingMovie);
    }

    @Then("I should be presented with the following movies?:")
    public void shouldSeeMovies(List<Movie> expectedMovies) {
        assertThat(matchingMovies).usingElementComparator(byTitleAndDirector()).containsOnlyElementsOf(expectedMovies);
    }
}
