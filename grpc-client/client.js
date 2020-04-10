const grpc = require('grpc');
const protoLoader = require('@grpc/proto-loader');
const packageDef = protoLoader.loadSync("CalculatorService.proto",{});
const grpcObject = grpc.loadPackageDefinition(packageDef);
// The protoDescriptor object has the full package hierarchy
const calculatorPackage = grpcObject.CalculatorPackage;

const client = new calculatorPackage.CalculatorService("localhost:8080",
grpc.credentials.createInsecure())
//console.log(calculatorPackage)

/*client.greeting({}, (err, response) => {
  if(err)
    console.log(err);
  console.log(JSON.stringify(response));
})*/

client.add({"numbers" : [
  {"number" : 1},
  {"number" : 2},
]}, (err, response) => {
  if(err)
    console.log(err);
  console.log(JSON.stringify(response));
})
