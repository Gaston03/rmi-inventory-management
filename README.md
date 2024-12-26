# Inventory Management

This project is an implementation of a distributed inventory management system using Java RMI (Remote Method Invocation). This system facilitates communication between a client and server for managing inventory operations.

## Key features

### 1. Client-Server Architecture: 
The repository implements a client-server model using Java RMI, enabling remote method calls.

### 2. Inventory Management: 
It includes functionalities for managing an inventory, likely including adding, updating, or querying items.

### 3. User Management:
It includes functionalities for managing users, likely logging or registering user.

### 4. Java-based Implementation: 
The entire project is written in Java, demonstrating how RMI can be used for distributed systems.

## Repository Structure
### 1. client/: 
Contains the client-side application code that interacts with the server.

### 2. server/: 
Contains the server-side logic, including the RMI implementation for inventory management.

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 21 or later
- Apache Maven
- XAMPP

### Clone the Repository
```shell
git clone https://github.com/Gaston03/rmi-inventory-management.git
cd rmi-inventory-management
```

### Build the Project
Run the following command to compile the code and package the application:
```shell
mvn clean install
```

### Run Server
Navigate to the `server` directory and run the server:
##### Compile the project
```shell
mvn compile
```
##### Generate jar file
```shell
mvn package
```
##### Execute the jar file
```shell
java -jar target/server-1.0.1.jar
```

### Run client
Navigate to the `client` directory and run the client:
##### Compile the project
```shell
mvn compile
```
##### Generate jar file
```shell
mvn package
```
##### Execute the jar file
```shell
java -jar target/client-1.0.1.jar
```

## Author
- Gaston03
```vbnet
If additional details about the inventory operations or specific classes are available, they can be included in the `README` for better clarity. Let me know if you'd like to customize this further!
```