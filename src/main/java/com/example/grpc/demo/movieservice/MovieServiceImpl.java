package com.example.grpc.demo.movieservice;

import io.grpc.stub.StreamObserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.grpc.server.service.GrpcService;

import com.proto.common.Genre;
import com.proto.common.Movie;
import com.proto.movieservice.MovieRequest;
import com.proto.movieservice.MovieResponse;
import com.proto.movieservice.MovieServiceGrpc;

@GrpcService
public class MovieServiceImpl extends MovieServiceGrpc.MovieServiceImplBase {
    private static final Logger logger = LoggerFactory.getLogger(MovieServiceImpl.class.getName());

    @Override
    public void getMovie(final MovieRequest request, final StreamObserver<MovieResponse> responseObserver) {
        logger.info("MovieRequest= {}", request);
        Movie movie = switch (request.getGenre()) {
            case ACTION -> Movie.newBuilder().setTitle("Inception").setDescription("Sci fi action").setRating(8.8f)
                    .setGenre(Genre.ACTION).build();
            case COMEDY -> Movie.newBuilder().setTitle("The Hangover").setDescription("Hilarious ride").setRating(7.7f)
                    .setGenre(Genre.COMEDY).build();
            case DRAMA -> Movie.newBuilder().setTitle("Gladiator").setDescription("Period drama").setRating(8.5f)
                    .setGenre(Genre.DRAMA).build();
            case THRILLER -> Movie.newBuilder().setTitle("Jaws").setDescription("Shark thrills").setRating(8.0f)
                    .setGenre(Genre.THRILLER).build();
            default -> null;
        };
        if (movie == null) {
            responseObserver.onError(new IllegalStateException("The movie is not defined"));
            return;
        }
        MovieResponse response = MovieResponse.newBuilder().setMovie(movie).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
        logger.info("Movie= {}", movie);
    }
}
