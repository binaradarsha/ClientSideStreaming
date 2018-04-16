package com.grpc.demo;

import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ScoreService extends ScoreGrpc.ScoreImplBase {

    private final static Logger logger = Logger.getLogger(ScoreService.class.getName());

    Map<String, Integer> itemScoreMap = new HashMap<String, Integer>() {{
        put("lamp", 5);
        put("oil", 10);
        put("matches", 15);
    }};

    @Override
    public StreamObserver<ScoreRequest> getScore(final StreamObserver<ScoreResponse> responseObserver) {
        final ArrayList<String> items = new ArrayList<String>();

        return new StreamObserver<ScoreRequest>() {
            public void onNext(ScoreRequest scoreRequest) {
                String item = scoreRequest.getItem();
                items.add(item);
                System.out.println(">>> Retrieved item: " + item);
            }

            public void onError(Throwable throwable) {
                logger.log(Level.WARNING, "Encountered error in getScore: " + throwable);
            }

            public void onCompleted() {
                int totalScore = 0;
                for(String item : items) {
                    Integer score = itemScoreMap.get(item);
                    totalScore += score;
                }

                ScoreResponse response = ScoreResponse.newBuilder().setScore(totalScore).build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();

                System.out.println(">>> Score sent: " + totalScore);
            }
        };
    }
}
