const grpc = require('grpc');
const protoLoader = require('@grpc/proto-loader');
const packageDef = protoLoader.loadSync("CalculatorService.proto",{});
const grpcObject = grpc.loadPackageDefinition(packageDef);
// The protoDescriptor object has the full package hierarchy
const calculatorPackage = grpcObject.CalculatorPackage;

const client = new calculatorPackage.CalculatorService("localhost:8080",
grpc.credentials.createInsecure())


//unary RPC
client.add({"numbers" : [
  {"number" : 1},
  {"number" : "2"},
]}, (err, response) => {
  if(err)
    console.log(err);
  console.log("add 1 2")
  console.log(JSON.stringify(response));
})



//server streaming
console.log("fibonacci")
const fib = client.fibonacci({});
fib.on("data", item => {
  console.log(" "+item.number)
});



//simulates client crash
/*
const fib = client.fibonacci({});
fib.on("data", item => {
  console.log('killing connection')
  process.exit();
  console.log(" "+item.number)
});
*/
