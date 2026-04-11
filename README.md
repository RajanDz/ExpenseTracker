# Expense Tracker API

A RESTful API for managing personal budgets and expenses. Users can create budgets, track spending by category, and monitor remaining balance in real time.

---

## Tech Stack

- **Java 17**
- **Spring Boot**
- **Spring Security / JWT**
- **MySQL**
- **Hibernate / JPA**
- **Maven**

---

## Getting Started

### Prerequisites

- Java 17+
- Maven
- MySQL

### Setup

```bash
git clone https://github.com/RajanDz/expense-tracker.git
cd expense-tracker
```

Configure `application.yml`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/expense_tracker
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

Run the application:

```bash
mvn spring-boot:run
```

---

## Database Setup

### Step 1: Open MySQL Command Prompt or MySQL Workbench

### Step 2: Login as administrator

```bash
mysql -u your_username -p
```

### Step 3: Copy and paste the following SQL

```sql
CREATE DATABASE IF NOT EXISTS expense_tracker;

USE expense_tracker;

CREATE TABLE IF NOT EXISTS user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    lastname VARCHAR(100) NOT NULL,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS user_role (
    user_id BIGINT,
    role_id BIGINT,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (role_id) REFERENCES role(id)
);

CREATE TABLE IF NOT EXISTS budget_list (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    budget DOUBLE,
    remaining_budget DOUBLE,
    start_date DATE,
    end_date DATE,
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

CREATE TABLE IF NOT EXISTS expense (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    amount DOUBLE NOT NULL,
    date_time DATETIME NOT NULL,
    category VARCHAR(100) NOT NULL,
    budget_list BIGINT,
    FOREIGN KEY (budget_list) REFERENCES budget_list(id)
);
```

> **Note:** If you have `spring.jpa.hibernate.ddl-auto=update` set in `application.yml`, Hibernate will create the tables automatically on first run. The SQL above is provided as a reference.

---

## API Endpoints

### Auth
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/signup` | Register a new user |
| POST | `/api/auth/signin` | Login and receive JWT token |

### Budgets
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/budget` | Create a new budget |
| GET | `/api/budget/{id}` | Get budget with expenses |
| DELETE | `/api/budget/{id}` | Delete a budget |

### Expenses
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/expense` | Add expense to a budget |
| PATCH | `/api/expense/{id}` | Update expense fields |
| DELETE | `/api/expense/{id}` | Delete an expense |

---

## Project Structure

```
src/
├── controller/
├── service/
├── repository/
├── model/
├── dto/
└── security/
```
