FROM mysql:5.6
COPY ./*.sql /docker-entrypoint-initdb.d/
