#!/bin/bash

# Define paths to your services and application
KAFKA_ZOOKEEPER_CONFIG="/opt/homebrew/etc/kafka/zookeeper.properties"
KAFKA_SERVER_CONFIG="/opt/homebrew/etc/kafka/server.properties"
JAVA_APP_JAR="/path/to/your-application.jar"

# Function to start Zookeeper
start_zookeeper() {
  echo "Starting Zookeeper..."
  if ! pgrep -f "zookeeper-server-start" > /dev/null; then
    nohup zookeeper-server-start $KAFKA_ZOOKEEPER_CONFIG > zookeeper.log 2>&1 &
    ZOOKEEPER_PID=$!
    sleep 5 # Give Zookeeper time to start
  else
    echo "Zookeeper is already running."
  fi
}

# Function to start Kafka
start_kafka() {
  echo "Starting Kafka..."
  if ! pgrep -f "kafka-server-start" > /dev/null; then
    nohup kafka-server-start $KAFKA_SERVER_CONFIG > kafka.log 2>&1 &
    KAFKA_PID=$!
    sleep 5 # Give Kafka time to start
  else
    echo "Kafka is already running."
  fi
}

# Function to start MySQL using Homebrew
start_mysql() {
  echo "Starting MySQL..."
  if ! brew services list | grep mysql | grep started > /dev/null; then
    brew services start mysql
  else
    echo "MySQL is already running."
  fi
}

# Function to start Redis using Homebrew
start_redis() {
  echo "Starting Redis..."
  if ! brew services list | grep redis | grep started > /dev/null; then
    brew services start redis
  else
    echo "Redis is already running."
  fi
}

# Function to start the Java application
start_java_application() {
  echo "Starting Java application..."
  # Run the Java application
  nohup java -jar $JAVA_APP_JAR > java_app.log 2>&1 &
  JAVA_APP_PID=$!
}

# Function to stop Zookeeper
stop_zookeeper() {
  echo "Stopping Zookeeper..."
  if [ -n "$ZOOKEEPER_PID" ]; then
    kill $ZOOKEEPER_PID
  fi
}

# Function to stop Kafka
stop_kafka() {
  echo "Stopping Kafka..."
  if [ -n "$KAFKA_PID" ]; then
    kill $KAFKA_PID
  fi
}

# Function to stop MySQL using Homebrew
stop_mysql() {
  echo "Stopping MySQL..."
  if brew services list | grep mysql | grep started > /dev/null; then
    brew services stop mysql
  fi
}

# Function to stop Redis using Homebrew
stop_redis() {
  echo "Stopping Redis..."
  if brew services list | grep redis | grep started > /dev/null; then
    brew services stop redis
  fi
}

# Function to stop the Java application
stop_java_application() {
  echo "Stopping Java application..."
  if [ -n "$JAVA_APP_PID" ]; then
    kill $JAVA_APP_PID
  fi
}

# Trap INT and TERM signals to stop services on script exit
trap 'stop_zookeeper; stop_kafka; stop_mysql; stop_redis; stop_java_application; exit 0' INT TERM

# Start services
start_zookeeper
start_kafka
start_mysql
start_redis

# Start Java application
#start_java_application

# Wait indefinitely to keep the script running
echo "All services and the Java application are running. Press Ctrl+C to stop."
while true; do
  sleep 1
done
