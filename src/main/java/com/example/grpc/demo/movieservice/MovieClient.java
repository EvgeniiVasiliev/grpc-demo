package com.example.grpc.demo.movieservice;

import io.grpc.Channel;
import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.proto.common.Genre;
import com.proto.common.Movie;
import com.proto.movieservice.MovieRequest;
import com.proto.movieservice.MovieResponse;
import com.proto.movieservice.MovieServiceGrpc;

public class MovieClient {
    private static final Logger logger = LoggerFactory.getLogger(MovieClient.class.getName());

    private final MovieServiceGrpc.MovieServiceBlockingStub blockingStub;

    /**
     * Construct client for accessing MovieService server using the existing channel.
     */
    public MovieClient(Channel channel) {
        // 'channel' here is a Channel, not a ManagedChannel, so it is not this code's responsibility to
        // shut it down.

        // Passing Channels to code makes code easier to test and makes it easier to reuse Channels.
        blockingStub = MovieServiceGrpc.newBlockingStub(channel);
    }

    /**
     * Get Movie from Server.
     */
    public void getMovie() {
        logger.info("Will try to get movie...");

        MovieRequest request = MovieRequest.newBuilder()
                .setUserid("100500")
                .setGenre(Genre.DRAMA)
                .build();
        MovieResponse response;
        try {
            logger.info("MovieRequest= {}", request);
            response = blockingStub.getMovie(request);
        } catch (StatusRuntimeException e) {
            logger.warn("RPC failed: {}", e.getStatus());
            return;
        }
        Movie movie = response.getMovie();
        logger.info("Movie= {}", movie);
    }


    public static void main(String[] args) throws Exception {
        // Access a service running on the local machine on port 50051
        String target = "localhost:50051";


        // Create a communication channel to the server, known as a Channel. Channels are thread-safe
        // and reusable. It is common to create channels at the beginning of your application and reuse
        // them until the application shuts down.
        //
        // For the example we use plaintext insecure credentials to avoid needing TLS certificates. To
        // use TLS, use TlsChannelCredentials instead.
        ManagedChannel channel = Grpc.newChannelBuilder(target, InsecureChannelCredentials.create()).build();
        try {
            MovieClient client = new MovieClient(channel);
            client.getMovie();
        } finally {
            // ManagedChannels use resources like threads and TCP connections. To prevent leaking these
            // resources the channel should be shut down when it will no longer be used. If it may be used
            // again leave it running.
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }
}
