package com.wakaleo.myflix.movies.controllers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.wakaleo.myflix.movies.model.Artist;
import com.wakaleo.myflix.movies.model.ActorUnknown;
import com.wakaleo.myflix.movies.model.Movie;
import com.wakaleo.myflix.movies.model.MovieNotFound;
import com.wakaleo.myflix.movies.repository.MovieRepository;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import java.util.List;

import static org.apache.commons.lang3.text.WordUtils.capitalizeFully;

@RestController
@RequestMapping("/artists")
public class ArtistController {

    @Autowired
    private MovieRepository repository;

    @RequestMapping(value = "/{name}", method = GET)
    public Artist findById(@PathVariable String name) {
    	List<Movie> actedIn = repository.findByActors(name);
    	List<Movie> directed = repository.findByDirector(name);
    	
    	if(actedIn.isEmpty() && directed.isEmpty()) {
    		throw new ActorUnknown();
    	}
    	
    	return new Artist(name, actedIn, directed);
    }
    
}
