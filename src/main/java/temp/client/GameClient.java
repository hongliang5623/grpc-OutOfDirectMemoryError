package temp.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import temp.proto.GameConnectionRequest;
import temp.proto.GameState;
import temp.proto.GameStateProviderServiceGrpc;

import java.util.concurrent.TimeUnit;

public class GameClient {

    private final ManagedChannel channel;
    private final GameStateProviderServiceGrpc.GameStateProviderServiceStub asyncStub;
    private final StreamObserver<GameState> streamObserver;

    public GameClient(String host, int port) {
        channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext(true)
                .build();
        asyncStub = GameStateProviderServiceGrpc.newStub(channel);
        streamObserver = getStreamObserver();
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public void connectToGame(String name) {
        GameConnectionRequest request = GameConnectionRequest.newBuilder().setName(name).build();
        try {
            asyncStub.connectToGame(request, streamObserver);
        } catch (StatusRuntimeException e) {
            System.err.println("RPC failed: " + e.getStatus());
        }
    }

    private StreamObserver<GameState> getStreamObserver() {
        return new StreamObserver<GameState>() {
            @Override
            public void onNext(GameState value) {
//                System.out.println("Server msg received: " + value.getMessage());
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("Server error: ");
                t.printStackTrace();
            }

            @Override
            public void onCompleted() {
                System.out.println("Connection to server closed");
            }
        };
    }

    public static void main(String[] args) throws InterruptedException {
        GameClient client = new GameClient("localhost", 50052);
        client.connectToGame("Ryan");
        Thread.sleep(Integer.MAX_VALUE);
    }

}
