# Leaderboard Application

## Overview

This application is a leaderboard service that manages and retrieves player scores. It uses Redis for caching and MySQL as the primary database. The application is designed to handle high scores efficiently, with fallback mechanisms in place to ensure reliability in case of service failures.

## Features

- **Leaderboard Management**: Add and retrieve top player scores.
- **Redis Caching**: Utilize Redis to cache player scores for fast access.
- **MySQL Database**: Store player scores in MySQL.
- **Kafka Integration**: Publish score updates to Kafka topics.

## Technologies Used

- **Spring Boot**: Framework for building the application.
- **Redis**: In-memory data structure store for caching.
- **MySQL**: Relational database for persistent storage.
- **Apache Kafka**: Distributed event streaming platform.

## Prerequisites

Before running the application, ensure you have the following services running locally:

- **MySQL**
- **Redis**
- **Apache Kafka**

## Setting Up Services Locally

### 1. MySQL

1. **Install MySQL**:

   - Follow the installation instructions for your operating system from the [official MySQL website](https://dev.mysql.com/downloads/).

2. **Start MySQL Server**:

   - On Unix-based systems: `sudo service mysql start`
   - On Windows: Start the MySQL service from the Services management console or use the command line.

3. **Create Database and User**:
   - Log in to MySQL: `mysql -u root -p`
   - Create a database:
     ```sql
     CREATE DATABASE leaderboard;
     ```
   - Create a user and grant privileges:
     ```sql
     CREATE USER 'leaderboard_user'@'localhost' IDENTIFIED BY 'your_password';
     GRANT ALL PRIVILEGES ON leaderboard.* TO 'leaderboard_user'@'localhost';
     FLUSH PRIVILEGES;
     ```

### 2. Redis

1. **Install Redis**:

   - Follow the installation instructions for your operating system from the [official Redis website](https://redis.io/download).

2. **Start Redis Server**:
   - On Unix-based systems: `redis-server`
   - On Windows: Use the Redis installation package or start it from the command line.

### 3. Apache Kafka

1. **Download and Extract Kafka**:

   - Download Kafka from the [Apache Kafka website](https://kafka.apache.org/downloads).
   - Extract the archive to a directory of your choice.

2. **Start Zookeeper**:

   - Open a terminal and navigate to the Kafka directory.
   - Run: `bin/zookeeper-server-start.sh config/zookeeper.properties` (Unix-based systems) or `bin\windows\zookeeper-server-start.bat config\zookeeper.properties` (Windows).

3. **Start Kafka Server**:

   - Open another terminal and navigate to the Kafka directory.
   - Run: `bin/kafka-server-start.sh config/server.properties` (Unix-based systems) or `bin\windows\kafka-server-start.bat config\server.properties` (Windows).

4. **Create Kafka Topics**:
   - Open a terminal and navigate to the Kafka directory.
   - Create a topic for player score updates:
     ```bash
     bin/kafka-topics.sh --create --topic player-scores --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
     ```

## Running the Application

1. Run the main function
