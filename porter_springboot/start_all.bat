set BASE_DIR=%~dp0

REM run user-service:
cd %BASE_DIR%\user-service\target
start java -Ddb.url="jdbc:mysql://localhost/porter_user_db?useSSL=false" -Ddb.username=root -Ddb.password=root -jar porter-user-service-2.0-SNAPSHOT.jar

REM run file-service:
cd %BASE_DIR%\file-service\target
start java -jar porter-file-service-2.0-SNAPSHOT.jar

REM run gateway-service:
cd %BASE_DIR%\gateway-service\target
start java -jar porter-gateway-service-2.0-SNAPSHOT.jar

REM run website:
cd %BASE_DIR%\website\target
start java -jar porter-website-2.0-SNAPSHOT.jar

cd %BASE_DIR%
