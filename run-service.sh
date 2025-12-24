#!/bin/bash

BASE_DIR=$(pwd)

echo "Starting Microservices in Tabs..."

gnome-terminal --tab --title="REGISTRY" -- bash -c "cd $BASE_DIR/flightapp-service-registry && mvn spring-boot:run; exec bash"

echo "Waiting 15s for Registry..."
sleep 15

gnome-terminal --tab --title="IDENTITY" -- bash -c "cd $BASE_DIR/flightapp-identity-service && mvn spring-boot:run; exec bash"
gnome-terminal --tab --title="FLIGHT" -- bash -c "cd $BASE_DIR/flightapp-flight-service && mvn spring-boot:run; exec bash"
gnome-terminal --tab --title="BOOKING" -- bash -c "cd $BASE_DIR/flightapp-booking-service && mvn spring-boot:run; exec bash"
gnome-terminal --tab --title="GATEWAY" -- bash -c "cd $BASE_DIR/flightapp-api-gateway && mvn spring-boot:run; exec bash"

echo "All tabs initialized."