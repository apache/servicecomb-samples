FROM servicecomb-samples/updated
WORKDIR /servicecomb-samples/houserush/house-order
ENTRYPOINT sleep 25 && mvn -s /root/settings.xml spring-boot:run

