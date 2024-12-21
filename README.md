# Diet Monitoring System

This is a robust 2-tier application that facilitates diet management and recommendation. The system is split into two roles: **User** and **Dietitian**, each operating on separate ports. The application is designed with scalability, security, and reliability in mind.

---


## **Swagger UI**
Explore and test the API endpoints interactively:
![Swagger UI](swaggerui.png)

Hosted at: [Swagger UI](https://cs544-app.azurewebsites.net/swagger-ui/index.html#/)


---

## **Entities**

### 1. **Users**
- Represents individuals using the application to track their diet and receive recommendations.
- Attributes include username, email, password, and role (USER or DIETITIAN).

### 2. **Goals**
- Represents objectives set by users, such as weight loss or achieving a specific BMI.
- Includes attributes like goal name, description, start date, end date, and status (`ACHIEVED` or `NOT ACHIEVED`).
- Linked to a specific user.

### 3. **FoodLogs**
- Logs meals consumed by users, including details like meal type, food items, calories, and nutrients.
- Associated with a specific user.

### 4. **Health Vitals**
- Captures vital health information for users, such as weight, Heartbeat Rate, SPO2, BMI, and medical conditions.
- Provides critical data for tailored diet recommendations.

### 5. **Diet Review**
- Allows dietitians to review and provide feedback on user meal plans.
- Includes attributes like review text and status (PENDING, APPROVED).

### 6. **Diet Recommendation**
- System-generated or dietitian-approved meal plans tailored to user health vitals and food logs.
- Includes status tracking (PENDING, APPROVED, REJECTED).

---

## **Features**

### **1. CRUD Operations**
- Fully functional endpoints for Create, Read, Update, and Delete operations on all entities.

### **2. Query Capabilities**
- **Dynamic Queries**: Example - Fetch all food logs for a specific user within a date range.
- **Named Queries**: Example - Retrieve diet recommendations by status (PENDING, APPROVED).
- **Criteria Queries**: Example - Fetch diet reviews for users with a specific BMI range.

### **3. 5-Layer Architecture**
- **Presentation Layer**: Exposes endpoints through REST API.
- **Control Layer**: Coordinates business logic.
- **Service Layer**: Contains core logic and transaction management.
- **Domain Layer**: Represents business objects like Users, FoodLogs, etc.
- **Persistence Layer**: Handles database interactions using Spring Data JPA.

### **4. Logging Transactions with AOP**
- Logs every REST API call to `log.txt` for monitoring and debugging.
- Example: When a food log is saved, a log entry is recorded.

### **5. JMS Integration**
- **User Updates Meal Plan**: A message is sent to ActiveMQ, notifying the dietitian.
- **Dietitian Approves Meal Plan**: A message is sent to ActiveMQ, notifying the user.
- Uses ActiveMQ queues for communication between roles.

### **6. Profiles for Development and Production**
- **Development Profile**: Uses `application_h2.properties` for H2 in-memory database.
- **Production Profile**: Uses `application_mysql.properties` for MySQL database.

### **7. Security**
- **Spring Security Implementation**:
    - **Public Endpoint**: Health-check API for status monitoring.
    - **Role-Based Access Control**:
        - `ADMIN`: Access to all APIs for managing users, food logs, and recommendations. `username: admin, password: admin`
        - `USER`: Access limited to personal food logs, vitals, and goals.

### **8. Stateless REST API**
- Fully stateless design ensures scalability and session independence.

### **9. Concurrency Handling**
- **Optimistic Locking**: Implemented via the `@Version` annotation for entities like `FoodLogs` and `Goals`.
- **Pessimistic Locking**: Explicitly implemented for high-contention queries using `LockModeType.PESSIMISTIC_WRITE`.

### **10. Swagger Integration**
- Swagger UI for testing and exploring APIs.
- Hosted at: [Swagger UI](https://cs544-app.azurewebsites.net/swagger-ui/index.html#/)

### **11. Deployment**
- Deployed on Microsoft Azure as a scalable App Service.
- Production URL: [Diet Monitoring System](https://cs544-app.azurewebsites.net/swagger-ui/index.html#/)

---

## **System Architecture**

### **JMS Architecture**
![JMS Architecture](jmsarch.png)

---

## **Ports**
- **User System**: Runs on port `6060`.
- **Dietitian System**: Runs on port `7070`.

---

## **How to Run Locally**
1. Clone the repository.
2. Build the application:
   ```bash
   mvn clean package
   ```
3. Run the application:
   ```bash
   java -jar target/Monitoring-0.0.1-SNAPSHOT.jar
   ```
4. Access the application at:
   - Swagger UI: [http://localhost:6060/swagger-ui/index.html](http://localhost:6060/swagger-ui/index.html)

---

## **How to Deploy to Azure**
1. Create a resource group:
   ```bash
   az group create --name diet_group --location canadacentral
   ```
2. Create an App Service Plan:
   ```bash
   az appservice plan create --name cs544_a --resource-group diet_group --sku B1 --is-linux
   ```
3. Create a Web App:
   ```bash
   az webapp create --resource-group diet_group --plan cs544_a --name cs544-app --runtime "JAVA:21-java21"
   ```
4. Deploy the WAR file:
   ```bash
   az webapp deploy --resource-group diet_group --name cs544-app --src-path target/Monitoring-0.0.1-SNAPSHOT.war
   ```

---

## **Contact Information**
For questions or issues, please contact Mercel Vubangsi `vmercel@yahoo.com`

---

Enjoy using the Diet Monitoring System!

