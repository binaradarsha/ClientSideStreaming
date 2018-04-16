package com.grpc.demo;

import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.logging.Logger;

public class GameServer {

    private final static int PORT = 9100;

    private final static Logger logger = Logger.getLogger(GameServer.class.getName());

    public static void main(String[] args) {
        try {

            logger.info("Grpc server started listening on port " + PORT);

            ServerBuilder.forPort(PORT)
                    .addService(new ScoreService())
                    .build()
                    .start()
                    .awaitTermination();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
