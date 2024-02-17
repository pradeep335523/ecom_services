Running MySql container :
----------------
docker run -d -p 3307:3306 --name mysql-inventory -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=inventory-service -e MYSQL_USER=user -e MYSQL_PASSWORD=password mysql/mysql-server:latest
----------------