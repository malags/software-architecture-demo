const grpc = require('grpc');
const protoLoader = require('@grpc/proto-loader');
const packageDef = protoLoader.loadSync("CalculatorService.proto",{});
const grpcObject = grpc.loadPackageDefinition(packageDef);
// The protoDescriptor object has the full package hierarchy
const calculatorPackage = grpcObject.CalculatorPackage;
const _ = require('lodash');

const client = new calculatorPackage.CalculatorService("localhost:8080",
grpc.credentials.createInsecure())


//unary RPC
client.add({"numbers" : [
  {"number" : 1},
  {"number" : "2"},
]}, (err, response) => {
  if(err)
    console.log(err);
  console.log("\nadd 1 2 = "+JSON.stringify(response))
})

console.log("call notOnServer")
client.notOnServer({},(err,response) => {
  if (err)
    console.log(err)
  else
    console.log("notOnServer"+JSON.stringify(response))
})



//Server streaming
console.log("fibonacci start")
const fib = client.fibonacci({});
fib.on("data", item => {
  if(item.number != undefined)
    console.log("fibonacci "+JSON.stringify(item))
  else
    console.log("fibonacci "+JSON.stringify({"number":0}))
});
console.log("\nfibonacci end")




//Client Streaming
console.log("sumAll start")
const sumAll_call = client.sumAll( (error, response) => {
  console.log("sumAll result "+JSON.stringify(response))
});

_.each([1,2,3,4,5],function (number) {
  console.log("sending to sumAll "+number)
  sumAll_call.write({ "number": number });
});

//close connection
sumAll_call.end();
console.log("\nsumAll end")





//Bidirectional Streaming
console.log("backAndForth start")
const backAndForth_call = client.backAndForth()

backAndForth_call.on("data", item => {
  console.log("back "+JSON.stringify(item))
});

backAndForth_call.on('end', function() {
  console.log("\nbackAndForth end")
});

_.each([2,3,5,7,11], (number) => {
  console.log("forth "+JSON.stringify(number))
  backAndForth_call.write({ "number": number });
});

backAndForth_call.end();




//simulates client crash
/*
const fib = client.fibonacci({});
fib.on("data", item => {
  console.log('killing connection')
  process.exit();
  console.log(" "+item.number)
});
*/
