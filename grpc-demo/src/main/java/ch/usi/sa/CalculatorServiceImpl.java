package ch.usi.sa;

import com.google.longrunning.WaitOperationRequest;
import io.grpc.stub.ServerCallStreamObserver;
import io.grpc.stub.StreamObserver;

import java.util.List;

public class CalculatorServiceImpl extends CalculatorServiceGrpc.CalculatorServiceImplBase {
    @Override
    public void add(MyNumbers request, StreamObserver<MyNumber> responseObserver) {
        System.out.println("called add");

        List<MyNumber> numbersList = request.getNumbersList();
        int value = 0;
        for(MyNumber myNumber : numbersList)
            value += myNumber.getNumber();

        MyNumber response = MyNumber.newBuilder()
                .setNumber(value)
                .build();

        //set response
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void fibonacci(Nothing request, StreamObserver<MyNumber> responseObserver) {
        Fibonacci fib = new Fibonacci();
        System.out.println("called fibonacci");
        for (int i = 0 ; i < 10 ; ++i){
            responseObserver.onNext(
                    MyNumber
                            .newBuilder()
                            .setNumber(fib.next())
                            .build()
            );
        }
        responseObserver.onCompleted();
        System.out.println("Done fibonacci");
    }

    @Override
    public StreamObserver<MyNumber> sumAll(final StreamObserver<MyNumber> responseObserver) {
        StreamObserver<MyNumber> streamObserver = new StreamObserver<MyNumber>() {
            int sum = 0;
            @Override
            public void onNext(MyNumber myNumber) {
                sum += myNumber.getNumber();
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(MyNumber.newBuilder().setNumber(sum).build());
                responseObserver.onCompleted();
                System.out.println("Completed sumAll");
            }
        };
        return streamObserver;
    }

    @Override
    public StreamObserver<MyNumber> backAndForth(final StreamObserver<MyNumber> responseObserver) {
        StreamObserver streamObserver = new StreamObserver() {
            @Override
            public void onNext(Object o) {
                MyNumber myNumber = (MyNumber) o;
                System.out.println("backAndForth received "+myNumber);
                responseObserver.onNext(MyNumber.newBuilder().setNumber(myNumber.getNumber()).build());
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                System.out.println("backAndForth Completed");
                responseObserver.onCompleted();
            }
        };
        return streamObserver;
    }

    @Override
    public void fibonacciServerCrash(Nothing request, StreamObserver<MyNumber> responseObserver) {
        System.out.println("called fibonacciServerCrash");
        for (int i = 0; i < 10; ++i) {
            System.exit(0);
        }
        responseObserver.onCompleted();
        System.out.println("Done fibonacci");
    }

    @Override
    public void waitSomeTime(Nothing request, StreamObserver<Nothing> responseObserver) {
        System.out.println("called waitSomeTime");

        ServerCallStreamObserver scso = ((ServerCallStreamObserver<Nothing>) responseObserver);

        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < 1000 && !scso.isCancelled());

        responseObserver.onNext(Nothing.getDefaultInstance());
        System.out.println("waitSomeTime Over");
        responseObserver.onCompleted();
    }

    class Fibonacci {
        int n1 = 0, n2 = 1, n3 = 0;

        int next(){
            int ret = n1;
            n3 = n1 + n2;
            n1 = n2;
            n2 = n3;
            return ret;
        }
    }
}
