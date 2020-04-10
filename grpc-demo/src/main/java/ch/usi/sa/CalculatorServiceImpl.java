package ch.usi.sa;

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
        for (int i = 0 ; i < 100 ; ++i){
            responseObserver.onNext(
                    MyNumber
                            .newBuilder()
                            .setNumber(fib.next())
                            .build()
            );
        }
        responseObserver.onCompleted();
    }

    @Override
    public void greeting(Nothing request, StreamObserver<Nothing> responseObserver) {
        System.out.println("called greeting");
        responseObserver.onNext(Nothing.getDefaultInstance());
        responseObserver.onCompleted();
    }

    class Fibonacci {
        int n1 = 0, n2 = 1, n3;

        int next(){
            int ret = n1;
            n3 = n1 + n2;
            n1 = n2;
            n2 = n3;
            return ret;
        }
    }
}
