import com.grpc.demo.ScoreGrpc;
import com.grpc.demo.ScoreRequest;
import com.grpc.demo.ScoreResponse;
import com.grpc.demo.ScoreService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ScoreClient {

    private final static String HOST = "localhost";
    private final static int PORT = 9100;

    private final static Logger logger = Logger.getLogger(ScoreClient.class.getName());

    private static final ArrayList<String> items = new ArrayList<String>() {{
        add("lamp");
        add("oil");
        add("matches");
    }};

    public static void main(String[] args) {
        // Creating the channel
        ManagedChannel channel = ManagedChannelBuilder.forAddress(HOST, PORT)
                .usePlaintext(true)
                .build();

        StreamObserver<ScoreResponse> responseObserver = new StreamObserver<ScoreResponse>() {
            int score = 0;

            public void onNext(ScoreResponse scoreResponse) {
                score = scoreResponse.getScore();
            }

            public void onError(Throwable throwable) {
                logger.log(Level.WARNING, "Encountered error in ScoreClient: " + throwable);
            }

            public void onCompleted() {
                System.out.println(">>> Score: " + score);
            }
        };

        // Retrieving the async stub
        ScoreGrpc.ScoreStub asyncStub = ScoreGrpc.newStub(channel);

        StreamObserver<ScoreRequest> requestObserver = asyncStub.getScore(responseObserver);

        for (String item : items) {
            ScoreRequest request = ScoreRequest.newBuilder().setItem(item).build();
            requestObserver.onNext(request);

            System.out.println(">>> Item Collected : " + item);
        }

        requestObserver.onCompleted();
    }

}
