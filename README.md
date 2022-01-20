## Instructions to run

### Step 1: Install JDK
Follow instructions here: https://openjdk.java.net/install/

### Step 2: Install Maven
Follow instructions here: https://maven.apache.org/install.html

### Step 3: Install MySQL
Follow instructions here: https://dev.mysql.com/doc/mysql-installation-excerpt/5.7/en/installing.html

### Step 4: Create a database and user in MySQL
Open a terminal (command prompt in Microsoft Windows) and open a MySQL client as a user who can create new users.
```
$ sudo mysql --password
```
Create the database and user in MySQL
```
mysql> create database inventory; -- Creates the new database
mysql> create user 'springuser'@'%' identified by 'ThePassword'; -- Creates the user
mysql> grant all on inventory.* to 'springuser'@'%'; -- Gives all privileges to the new user on the newly created database
```
### Step 5: Clone the project
```
$ git clone https://github.com/kvwadhwa/Shopify_Backend
```

### Step 6: Enter into the project directory and run the server (and tests)
```
$ cd Shopify_Backend
$ ./mvnw spring-boot:run  
```

### Step 7: Go to Warehouse Console to manage Warehouses. See output under the Output section.

http://localhost:8080/warehouse.html

### Step 8: Go to Inventory Console to manage Inventory (and assign it across Warehouses). See output under the Output section.

http://localhost:8080/inventory.html