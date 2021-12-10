## Trust Sample
This sample shows using public key mechanism to authenticate user and black/white user list. The public key mechanism security is based on Service Center, and users should first configure Service Center authentication. This sample does not show how to configure Service Center authentication.

In this sample, both customer and hacker try to access store, while store configure it's black list to deny hacker's access.

## Precondition
see [Precondition](../../README.md)

## How to run

You can run the samples from your IDE. First start TrustMain in store service. And then start HackerMain in 
hacker service, the running result should be `InvocationException: code=401;msg={message=UNAUTHORIZED}`. And then
start CustomerMain in customer service, the running result should be `a=1, b=2, result=3` .

