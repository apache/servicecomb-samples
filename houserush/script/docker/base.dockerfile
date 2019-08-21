FROM mcr.microsoft.com/java/maven:8u202-zulu-debian9
RUN apt-get update \
    && apt-get install -y git \
    && rm -rf /var/lib/apt/lists/* \
    && git clone https://github.com/alec-z/servicecomb-samples
ADD ./settings.xml /root
WORKDIR /servicecomb-samples/houserush
RUN mvn install -s /root/settings.xml
WORKDIR /servicecomb-samples/houserush/gateway
RUN mvn -s /root/settings.xml package spring-boot:repackage
