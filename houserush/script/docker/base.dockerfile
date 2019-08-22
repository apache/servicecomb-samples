FROM mcr.microsoft.com/java/maven:8u202-zulu-debian9
RUN echo "deb http://mirrors.aliyun.com/debian  stretch main contrib non-free" > /etc/apt/sources.list \
    && echo "deb-src http://mirrors.aliyun.com/debian  stretch main contrib non-free" >> /etc/apt/sources.list   \
    && echo "deb http://mirrors.aliyun.com/debian stretch-updates main contrib non-free" >> /etc/apt/sources.list  \
    && echo "deb-src http://mirrors.aliyun.com/debian stretch-updates main contrib non-free" >> /etc/apt/sources.list  \
    && echo "deb http://mirrors.aliyun.com/debian-security stretch/updates main contrib non-free" >> /etc/apt/sources.list  \
    && echo "deb-src http://mirrors.aliyun.com/debian-security stretch/updates main contrib non-free" >> /etc/apt/sources.list \
    && apt-get update \
    && apt-get install -y git \
    && rm -rf /var/lib/apt/lists/* \
    && git clone https://github.com/linzb0123/servicecomb-samples
ADD ./settings.xml /root
WORKDIR /servicecomb-samples/houserush
RUN mvn install -s /root/settings.xml
WORKDIR /servicecomb-samples/houserush/gateway
RUN mvn -s /root/settings.xml package spring-boot:repackage
