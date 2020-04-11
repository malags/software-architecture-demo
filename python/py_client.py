#!/usr/bin/env python
import grpc

import CalculatorService_pb2_grpc
import CalculatorService_pb2

from CalculatorService_pb2 import MyNumbers, MyNumber, Nothing

#python -m grpc_tools.protoc -I./proto --python_out=. --grpc_python_out=. ./proto/CalculatorService.proto

with grpc.insecure_channel('localhost:8080') as channel:
    stub = CalculatorService_pb2_grpc.CalculatorServiceStub(channel)

    num1 = MyNumber(number = 1)
    num2 = MyNumber(number = 2)
    numbers = MyNumbers(numbers = [num1, num2])
    result = stub.add(numbers)
    print "result =",result

    #sync request
    print "calling wait"
    stub.waitSomeTime(Nothing())
    print "wait returned"


    #async request
    print "fibonacci but async"
    future = stub.waitSomeTime.future(Nothing())
    print "waiting"
    result = future.result()
    print "done"

    #cancel request
    print "fibonacci but cancel"
    future = stub.waitSomeTime.future(Nothing())
    print "cancel"
    future.cancel()
    print "cancelled request"
