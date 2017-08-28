package com.wakaleo.myflix.movies.model;

import java.util.List;

public class Artist {

	String name;
	List<Movie> actedIn;
	List<Movie> directed;

	public Artist() {
	}

	public Artist(String name, List<Movie> actedIn, List<Movie> directed) {
		super();
		this.name = name;
		this.actedIn = actedIn;
		this.directed = directed;
	}

	public List<Movie> getActedIn() {
		return actedIn;
	}

	public void setActedIn(List<Movie> actedIn) {
		this.actedIn = actedIn;
	}

	public List<Movie> getDirected() {
		return directed;
	}

	public void setDirected(List<Movie> directed) {
		this.directed = directed;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
