package ch.usi.sa;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main(String[] args) {
        Server server = ServerBuilder
                .forPort(8080)
                .addService(new CalculatorServiceImpl())
                .build();

        try {
            server.start();
            System.out.println("gRPC Server Running on port: "+server.getPort());
            server.awaitTermination();
            System.out.println("server Down");
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
