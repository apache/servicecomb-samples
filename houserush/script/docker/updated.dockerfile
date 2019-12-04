FROM servicecomb-samples/base
COPY ./wait-for-it.sh /root/
WORKDIR /servicecomb-samples
RUN git pull && cd /servicecomb-samples/houserush && \
  mvn -s /root/settings.xml install
ENTRYPOINT ["/root/wait-for-it.sh"]
