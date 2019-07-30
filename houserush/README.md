### The business background of my planing project:
Traditionally, a real estate developer in china will show their nearing completion buildings to the customers for a couple of days, then determine an open sale time for all customers to make real deal offline. Those customers have to get to sale place long before the open sale time and queue up,because only in this way they get the chances to pick the apartment which they like mostly. 
So recently, more and more real estate developers begin to build an online apartments/houses open sale system. The e-commerce system help their customer to pick the houses more easily and more fairly. This is the system which I plan to build with ServiceComb.

### Why I choose this project as a guiding sample of ServiceComb?
Real estate developers use this system to make a deal with their customer online. So, yes! This is a real e-commerce system with real commercial value. But this is also a real simple e-commerce system which has no payment and shipping module. So Its simplicity is good for the guiding purpose. Meanwhile, because these trading subjects of this system are very expensive，and there will be high QPS when the open sale time just arrives. So There are high requirements for the system’s scalability, availability and robustness.It’s an applicable project to show ServiceComb’s abilities.

### Requirements & Basic Setup
1. Setup the requirements
   - setup git, see [git install overview](https://git-scm.com/book/zh/v2/%E8%B5%B7%E6%AD%A5-%E5%AE%89%E8%A3%85-Git)
   - setup JDK 1.8, see [JDK install overview](https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html)
   - setup Maven 3.x, 详情可参考[Maven install guide](https://maven.apache.org/install.html)
2. Setup and run the service center
   - setup Docker, see [Docker setup guide](https://www.docker.com/get-started)。
   - input in the terminal <code>$ docker run -d -p 30100:30100 servicecomb/service-center:latest</code>run service-center at port 30100
3. Setup the database

4. check & change the src/main/resources/microservice.yaml and src/main/resources/application.yaml configuration file.

5. run each microservice process <code>by mvn spring-boot:run. </code>