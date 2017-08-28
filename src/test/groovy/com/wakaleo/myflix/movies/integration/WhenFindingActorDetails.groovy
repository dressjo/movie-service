package com.wakaleo.myflix.movies.integration;

import com.wakaleo.myflix.movies.MovieServiceApplication
import com.wakaleo.myflix.movies.model.Movie;
import com.wakaleo.myflix.movies.model.Artist;
import com.wakaleo.myflix.movies.repository.MovieRepository;

import io.restassured.specification.RequestSpecification

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import spock.lang.Specification

import io.restassured.RestAssured

import static io.restassured.RestAssured.*
import static io.restassured.matcher.RestAssuredMatchers.*
import static org.hamcrest.Matchers.*

@SpringBootTest(classes = MovieServiceApplication.class, webEnvironment=WebEnvironment.RANDOM_PORT)
class WhenFindingActorDetails extends Specification {

    @Autowired
    MovieRepository movieRepository;

	@LocalServerPort
    int port;

    def GLADIATOR = new Movie(title:"Gladiator", director:"Ridley Scott",
                              description:"Sword and sandles", actors:["Russell Crowe","Joaquin Phoenix"]);
    def THE_GOOD_THE_BAD_AND_THE_UGLY = new Movie(title:"The Good, the Bad and the Ugly", director:"Sergio Leone",
            description:" Spaghetti Western", actors:["Clint Eastwood"]);
    def LETTERS_FROM_IWO_JIMA = new Movie(title:"Letters from Iwo Jima", director:"Clint Eastwood",
                                          description:"The story of the battle of Iwo Jima...", actors:["Ken Watanabe"]);
    def GRAN_TORINO = new Movie(title:"Gran Torino", director:"Clint Eastwood",
                                description:"Disgruntled Korean War veteran", actors:["Clint Eastwood", "Bee Vang"]);

    def setup() {
        movieRepository.deleteAll();
		RestAssured.port = port;
    }

	def "should return actor name"() {
		given:
			movieCatalogContains([THE_GOOD_THE_BAD_AND_THE_UGLY, LETTERS_FROM_IWO_JIMA, GRAN_TORINO]);
		when:
			Artist artist = when().get("/artists/Clint Eastwood").as(Artist);
		then:
			artist.name == "Clint Eastwood"
	}
	
	def "should return actor's films"() { 
		given:
			movieCatalogContains([THE_GOOD_THE_BAD_AND_THE_UGLY, LETTERS_FROM_IWO_JIMA, GRAN_TORINO]);
		when:
			Artist artist = when().get("/artists/Clint Eastwood").as(Artist)
		then:
			artist.actedIn == [THE_GOOD_THE_BAD_AND_THE_UGLY, GRAN_TORINO]
	}	
	
	def "should return the films the actor directed"() {
		given:
			movieCatalogContains([THE_GOOD_THE_BAD_AND_THE_UGLY, LETTERS_FROM_IWO_JIMA, GRAN_TORINO]);
		when:
			Artist artist = when().get("/artists/Clint Eastwood").as(Artist)
		then:
			artist.directed == [LETTERS_FROM_IWO_JIMA, GRAN_TORINO]
	}
	
	def "should return details for actor who hasn't directed any films"() {
		given:
			movieCatalogContains([GLADIATOR, GRAN_TORINO])
		when:
			Artist artist = when().get("/artists/Russell Crowe").as(Artist)
		then:
			artist.directed == []
		and:
			artist.actedIn == [GLADIATOR]
	}
	
	def "should return 404 for a person who has not films on record"() { 
		when:
			movieCatalogContains([GLADIATOR, GRAN_TORINO])
		then:
			when().get("/artists/John Stevenson").then().statusCode(404)
 	}
	
    def movieCatalogContains(List<Movie> movies) {
        movieRepository.save(movies)
    }


}