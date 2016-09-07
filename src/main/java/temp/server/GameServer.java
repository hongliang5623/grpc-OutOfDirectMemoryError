package temp.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GameServer {

    private final int port = 50052;
    private final Server server;
    private final GameStateProviderService gameStateProviderService = new GameStateProviderService();

    public GameServer() throws IOException {
        server = ServerBuilder
                .forPort(port)
                .addService(gameStateProviderService)
                .build()
                .start();
        System.out.println("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("*** shutting down gRPC server since JVM is shutting down");
                GameServer.this.stop();
                System.out.println("*** server shut down");
            }
        });
    }

    public void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        GameServer server = new GameServer();
        server.blockUntilShutdown();
    }

}
