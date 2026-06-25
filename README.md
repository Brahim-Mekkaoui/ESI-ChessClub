# ♟️ ESI Chess Club

<p align="center">
  <img src="https://img.shields.io/badge/Java-11+-orange.svg" alt="Java">
  <img src="https://img.shields.io/badge/Jakarta%20Servlet-6.0-red.svg" alt="Jakarta Servlet">
  <img src="https://img.shields.io/badge/PostgreSQL-14+-blue.svg" alt="PostgreSQL">
  <img src="https://img.shields.io/badge/Maven-3.6+-green.svg" alt="Maven">
</p>

<p align="center">
  <strong>Complete Chess Club Management Platform for the École des Sciences de l'Information (ESI)</strong>
</p>

---

## 📖 Overview

**ESI Chess Club** is a full-featured web application designed to manage the activities of the Chess Club at the **École des Sciences de l’Information (ESI)**.

The platform allows members to:

* Register and manage their profiles
* Participate in tournaments
* Solve daily chess puzzles
* Track their ELO rating
* Earn badges and achievements
* Follow club news and announcements

The application is built using **Java EE technologies**, **PostgreSQL**, and integrates with the **Lichess API** to provide daily chess challenges.

---

## 🚀 Features

### 🔐 Authentication & Security

* User registration and login
* Password hashing using SHA-256 with salt
* Role-based access (Admin / Member)
* Secure session management

### ♟️ Tournament Management

Supported tournament formats:

* Round Robin
* Swiss System
* Single Elimination

Features include:

* Tournament creation
* Participant registration
* Automatic match generation
* Round management
* Results tracking

### 🧩 Daily Puzzle Challenge

* Daily puzzle retrieval from the Lichess API
* Puzzle-solving statistics
* Fallback puzzle system when API is unavailable

### 📈 Dynamic ELO Ranking

* Automatic ELO calculation after matches
* Club ranking leaderboard
* Player of the Month system

### 🏆 Badges & Achievements

Examples:

* Puzzle Master
* Daily Player
* Tournament Winner
* Active Member

### 📰 News Management

* Publish announcements
* Display club news
* Remove outdated posts

### 👤 User Profiles

* Personal biography
* Chess level
* Profile picture
* Statistics dashboard
* Badge collection

### 🛠️ Administration Panel

* Manage tournaments
* Generate match schedules
* Moderate members
* Publish club news

---

## 🏗️ Technology Stack

| Layer           | Technology            |
| --------------- | --------------------- |
| Backend         | Java 11               |
| Web Framework   | Jakarta Servlet 6.0   |
| View Engine     | JSP + JSTL            |
| Database        | PostgreSQL 14+        |
| JSON Processing | Gson 2.10.1           |
| Frontend        | HTML, CSS, JavaScript |
| Chessboard UI   | Chessboard.js         |
| External API    | Lichess API           |
| Build Tool      | Maven 3.6+            |
| Server          | Apache Tomcat 10.1    |

---

## 📋 Prerequisites

Before running the application, make sure you have:

* Java 11+
* PostgreSQL 14+
* Maven 3.6+
* Internet connection (for Lichess integration)

---

## ⚙️ Installation

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/your-account/brahim-mekkaoui-esi-chessclub.git
cd brahim-mekkaoui-esi-chessclub
```

---

### 2️⃣ Configure PostgreSQL

Create the database:

```sql
CREATE DATABASE chessclub_db;

CREATE USER postgres
WITH PASSWORD 'postgres';

GRANT ALL PRIVILEGES
ON DATABASE chessclub_db
TO postgres;
```

Default credentials:

```text
Username: postgres
Password: postgres
```

Database configuration can be modified in:

```text
src/main/java/ma/ac/esi/chessclub/util/DBUtil.java
src/main/webapp/WEB-INF/web.xml
```

---

### 3️⃣ Initialize Database

```bash
psql -U postgres -d chessclub_db -f schema.sql
```

---

### 4️⃣ Build the Project

```bash
mvn clean package
```

---

### 5️⃣ Run the Application

#### Standalone Mode

```bash
java -jar target/chessclub-standalone.jar
```

Application URL:

```text
http://localhost:8080/chessclub/
```

#### External Tomcat Deployment

Deploy:

```text
target/chessclub.war
```

to:

```text
tomcat/webapps/
```

---

## 🗄️ Database Schema

The project includes a complete SQL schema (`schema.sql`) containing:

| Table                   | Description                     |
| ----------------------- | ------------------------------- |
| users                   | Member accounts and ELO ratings |
| tournaments             | Tournament information          |
| tournament_participants | Registrations                   |
| matches                 | Match schedules and results     |
| puzzles                 | Daily chess puzzles             |
| user_puzzle_stats       | Puzzle statistics               |
| badges                  | Available achievements          |
| user_badges             | Assigned badges                 |
| news                    | Club announcements              |

---

## 👥 Default Accounts

| Role          | Username    | Password  |
| ------------- | ----------- | --------- |
| Administrator | admin       | Admin1234 |
| Member        | membre_test | Test1234  |

> ⚠️ Change these passwords after the first login.

---

## ♟️ Application Modules

### 🏠 Home

* Latest news
* Club statistics
* Player of the Month

### 🏆 Tournaments

* Browse tournaments
* Register participants
* View pairings and standings

### 🧩 Daily Puzzle

* Solve the daily challenge
* Earn achievements
* Track performance history

### 📊 Rankings

* Dynamic ELO leaderboard
* Monthly rankings
* Top players

### 👤 Profile

* Personal information
* Match history
* Puzzle statistics
* Earned badges

### 🛠️ Administration

* Tournament creation
* Match generation
* News publishing
* User management

---

## 📁 Project Structure

```text
brahim-mekkaoui-esi-chessclub/
├── pom.xml
├── schema.sql
│
└── src/
    └── main/
        ├── java/
        │   └── ma/ac/esi/chessclub/
        │       ├── controller/
        │       ├── model/
        │       ├── repository/
        │       ├── service/
        │       └── util/
        │
        └── webapp/
            ├── WEB-INF/
            │   ├── views/
            │   └── web.xml
            │
            ├── css/
            └── index.jsp
```

---

## 🔌 Lichess Integration

The application retrieves the **Daily Puzzle** from the public Lichess API.

Features:

* Automatic daily refresh
* Puzzle metadata retrieval
* Fallback puzzle mechanism
* User solving statistics

---

## 🔒 Security

Implemented security mechanisms:

* SHA-256 password hashing
* Salt generation
* Input validation
* Session-based authentication
* Role-based authorization

---

## 🛠️ Future Improvements

Potential enhancements:

* Email notifications
* Online match management
* PGN analysis tools
* Mobile responsive redesign
* Advanced statistics dashboard
* OAuth login (Google / GitHub)

---

## 🤝 Contributing

Contributions are welcome.

### Development Workflow

```bash
git checkout -b feature/my-feature

git commit -m "Add new feature"

git push origin feature/my-feature
```

Then create a Pull Request.

---

## 📄 License

This project was developed as part of an academic project at the **École des Sciences de l’Information (ESI)**.

All rights reserved © ESI Chess Club – 2026.

---

## 👨‍💻 Author

**Brahim Mekkaoui**


École des Sciences de l’Information (ESI)

---



<p align="center">
♟️ Empowering the ESI Chess Community Through Technology
</p>
