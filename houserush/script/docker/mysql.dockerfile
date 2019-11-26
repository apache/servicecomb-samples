FROM mysql:5.6
COPY ./small/*.sql /docker-entrypoint-initdb.d/
COPY ./my.cnf /etc/mysql/conf.d/
