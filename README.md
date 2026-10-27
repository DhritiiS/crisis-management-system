# Crisis Management and Relief Distribution System

A Java and MySQL-based terminal application designed to manage emergency relief requests, track available resources, and coordinate supply dispatches during crisis situations.

## Overview

The Crisis Management and Relief Distribution System helps organize emergency response by maintaining volunteer details, relief requests, inventory records, and dispatch information in a relational database.

The application provides a menu-driven interface through which users can manage records, allocate supplies to relief requests, and update dispatch statuses.

This project was developed as an academic mini-project to apply Object-Oriented Programming concepts in Java and database management using MySQL.

## Features

- **Volunteer Management:** View volunteers and register new volunteers.
- **Inventory Management:** View available supplies and update stock quantities.
- **Relief Request Management:** View, register, and update relief requests.
- **Request Summary:** View a summary of relief requests and their associated items.
- **Supply Allocation:** Allocate available inventory to relief requests while checking stock availability.
- **Dispatch Management:** Create dispatch records, assign volunteers, and view dispatch details.
- **Dispatch Status Tracking:** Update dispatch statuses and record dispatch and delivery timestamps.
- **Database Integration:** Store and retrieve application data using JDBC and MySQL.

## Technologies Used

- **Java** — Application logic and object-oriented programming.
- **JDBC** — Connectivity between Java and MySQL.
- **MySQL 8.0** — Relational database for storing and managing project data.
- **MySQL Connector/J** — JDBC driver for database connectivity.
- **Visual Studio Code** — Development environment.
- **Git and GitHub** — Version control and team collaboration.

## Database Structure

The database is named `crisis_management_db` and contains the following seven tables:

| Table             | Purpose                                                                 |
| ----------------- | ----------------------------------------------------------------------- |
| `volunteers`      | Stores volunteer details, skills, and availability.                     |
| `relief_requests` | Stores emergency requests, locations, priorities, and statuses.         |
| `inventory_items` | Maintains available relief supplies and stock quantities.               |
| `request_items`   | Records items and quantities associated with relief requests.           |
| `allocations`     | Records the quantities of supplies allocated to relief requests.        |
| `dispatches`      | Stores dispatch details, assigned volunteers, statuses, and timestamps. |
| `dispatch_items`  | Records the supplies included in each dispatch.                         |

The database also includes the `vw_request_summary` view for summarizing relief requests.

## Project Structure

```text
crisis-management-system/
├── src/
│   ├── Main.java
│   ├── DBConnection.java
│   ├── TestConnection.java
│   ├── Person.java
│   ├── Volunteer.java
│   ├── InventoryItem.java
│   ├── ReliefRequest.java
│   └── Dispatch.java
├── database/
│   ├── schema.sql
│   └── updates.sql
├── lib/
│   └── mysql-connector-j-26.7.0.jar
├── .gitignore
└── README.md
```

## Prerequisites

Before running the application, ensure that you have:

- Java JDK installed and configured.
- MySQL Server 8.0 installed and running.
- MySQL Connector/J available in the project's `lib` directory.
- The project files downloaded or cloned from GitHub.

## Setup and Installation

### 1. Clone the repository

```bash
git clone https://github.com/Dhritiis/crisis-management-system.git
cd crisis-management-system
```

### 2. Set up the database

1. Open MySQL Workbench and connect to your local MySQL server.
2. Open and execute `database/schema.sql` to create the database, tables, sample records, and request summary view.
3. Open and execute `database/updates.sql` to create the additional `allocations` table required by the supply allocation feature.

Execute the scripts in this order. The database must be set up before running the application.

### 3. Configure the database connection

The application connects to the local `crisis_management_db` database using JDBC.

Configure the following environment variables with your own MySQL credentials:

- `CRISIS_DB_USER`
- `CRISIS_DB_PASSWORD`

The database connection settings are defined in `src/DBConnection.java`. Ensure that MySQL is running and that your credentials are valid.

**Security note:** Do not commit database passwords or other sensitive credentials to GitHub.

### 4. Compile the Java application

From the project root, run the following command in PowerShell:

```powershell
javac -cp "lib/*;src" -d out src/*.java
```

### 5. Run the application

Run:

```powershell
java -cp "lib/*;out" src.Main
```

The application opens a terminal-based menu through which the available features can be accessed.

## Sample Data

The database setup script inserts sample volunteers, relief requests, inventory items, and request-item records to help demonstrate the application.

The sample data is intended for academic demonstration and testing. Executing the setup scripts again does not automatically reset existing records to their original values.

## Limitations

- The project is a terminal-based academic application, not a deployed web or mobile application.
- It uses a locally configured MySQL database.
- The included sample data is intended for demonstration and testing.
- It does not provide a graphical user interface or a live emergency-response service.

## Team

This project was collaboratively developed by:

- **Srishti Srivastava** 
- **Dhriti Singh** 

Both team members contributed to the development and integration of the Java application and MySQL database, along with testing and debugging.

## Purpose

The project demonstrates the practical application of object-oriented programming, JDBC connectivity, relational database management, inventory tracking, and basic dispatch coordination in an emergency relief scenario.

