package temp.server;

import io.grpc.stub.StreamObserver;
import temp.proto.GameConnectionRequest;
import temp.proto.GameState;
import temp.proto.GameStateProviderServiceGrpc;

public class GameStateProviderService extends GameStateProviderServiceGrpc.GameStateProviderServiceImplBase {

    @Override
    public void connectToGame(GameConnectionRequest request, StreamObserver<GameState> responseObserver) {
        GameState msg = GameState.newBuilder().setMessage("Hello, " + request.getName()).build();
        for (int i = 0; i < 3_000_000; i++) {
            responseObserver.onNext(msg);
        }
    }

}
