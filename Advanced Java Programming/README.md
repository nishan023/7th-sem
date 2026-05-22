# Advanced Java Lab Works

This repository contains various lab tasks for the Advanced Java course, including JavaFX applications, JDBC console apps, Swing GUI applications, and Servlet/JSP web applications.

## Prerequisites
- **Java 17+**
- **Maven** (for building and running the web server)
- **PostgreSQL** (for database-related tasks)

---

## Installing Dependencies & Building (pom.xml)
This project uses Maven (`pom.xml`) to manage all external libraries (like JavaFX, PostgreSQL Drivers, Servlet API, and Jakarta Mail). 

Before running the applications for the first time, or if your IDE shows missing import errors, you should resolve the dependencies using the `pom.xml`:
1. Open your terminal and navigate to the `Advanced Java` folder.
2. Run the following command to download dependencies and compile the code:
   ```bash
   mvn clean install
   ```
*(Note: Whenever you add a new `<dependency>` to `pom.xml`, run this command to fetch it).*

---

## 1. Running the Web Application (Servlet & JSP)
The web application includes a Login System, session management via Cookies, and a JSP form processing system.

### Database Setup
Before running the web app, you must create a PostgreSQL database named `userdb` and run the following script in pgAdmin/SQL Shell:

```sql
-- Create users table
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL
);

-- Insert a default user for testing
INSERT INTO users (username, password) VALUES ('admin', 'admin123');

-- Create feedbacks table
CREATE TABLE feedbacks (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100),
    message TEXT
);
```

### Running the Server
1. Open a terminal and navigate to the `Advanced Java` folder.
2. Run the Jetty web server using Maven:
   ```bash
   mvn clean jetty:run
   ```
3. Wait for the `[INFO] Started ServerConnector` message.
4. Open your browser and go to: **http://localhost:8080/login.html**
5. To stop the server, press `Ctrl + C` in the terminal.

---

## 2. Running JavaFX Applications
Since Java 11+, JavaFX is not included in the standard JDK. To run the JavaFX applications in VS Code smoothly:

1. Expand `src/main/java/labQuestions/javaFX/`
2. **Do NOT** click "Run" on classes that extend `Application` (like `FileChooserApp.java` or `FormValidation.java`), as you will get a runtime component missing error.
3. Instead, **Click "Run"** on their respective Launcher classes:
   - Run `Launcher.java` (for FileChooserApp)
   - Run `FormValidationLauncher.java` (for FormValidation)

---

## 3. Running JDBC & Swing GUI Applications

### Contact Book (JDBC Console App)
1. Ensure your local PostgreSQL server is running.
2. The app connects to the default `postgres` database with user `postgres` and password `nishandhakal`.
3. Create the table in PostgreSQL first:
   ```sql
   CREATE TABLE contacts (
       id SERIAL PRIMARY KEY,
       name VARCHAR(100),
       email VARCHAR(100) UNIQUE,
       mobile VARCHAR(20) UNIQUE
   );
   ```
4. Run `ContactBook.java` directly using the "Run" button in VS Code.

### Email Sender with Attachment (Swing App)
1. This app connects to a PostgreSQL database called `maildb`.
2. Create the required table:
   ```sql
   CREATE TABLE sent_emails (
       id SERIAL PRIMARY KEY,
       recipient VARCHAR(200),
       subject VARCHAR(200),
       message TEXT,
       attachment VARCHAR(200),
       sent_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
   );
   ```
3. Open `EmailAttachSend.java` and make sure your Gmail credentials (including the 16-letter App Password) are set in the `from` and `password` variables.
4. Run `EmailAttachSend.java` directly using the "Run" button in VS Code.

---

## 4. Running RMI (Remote Method Invocation)
The RMI application acts as a distributed calculator. You must start the Server first, then the Client.

1. Open `CalculatorServer.java` and click the **Run** button. Wait for it to say `Calculator RMI Server is running...` in the terminal.
2. Ensure the Server is kept running.
3. Open `CalculatorClient.java` and click the **Run** button. It will connect to the server, perform remote calculations, and print the results in the terminal.