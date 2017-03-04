# AWS Machine Learning - Real Time Prediction Example

## About
A quick & simple technical exercise to be able to make real-time predictions against an AWS Machine Learning model using
the AWS SDK for Java.

## Setup
1. Create an Amazon AWS account, if you don't already have one
1. Create an Amazon Machine Learning (ML) Model
1. Create an IAM that has access to the ML Model created earlier
1. Create a file to store those credentials here:
   > ~/.aws/credentials

   <pre>
   [default]
   aws_access_key_id={YOUR_ACCESS_KEY_ID}
   aws_secret_access_key={YOUR_SECRET_ACCESS_KEY}
   </pre>

## Known Issues
It takes time for a real-time prediction endpoint to become ready for use, so when you run the code for the first time, 
it will request a real-time endpoint and then _possibly_ crash, as the endpoint may not be ready. So just run the code 
again after a short-period and it will work without crashing if the endpoint is ready.
