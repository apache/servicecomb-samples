FROM servicecomb-samples/base
WORKDIR /servicecomb-samples
RUN git pull && cd /servicecomb-samples/houserush && \
  mvn -s /root/settings.xml install
