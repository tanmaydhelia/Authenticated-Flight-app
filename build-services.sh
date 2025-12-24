#!/bin/bash

# Exit immediately if a command exits with a non-zero status
set -e

# List of services in the requested order
SERVICES=(
    "flightapp-service-registry"
    "flightapp-identity-service"
    "flightapp-api-gateway"
    "flightapp-flight-service"
    "flightapp-booking-service"
)

echo "===================================================="
echo "Starting Maven Clean Package for Microservices"
echo "Order: Registry -> Identity -> Gateway -> Flight -> Booking"
echo "===================================================="

# Loop through each directory and run Maven
for SERVICE in "${SERVICES[@]}"
do
    echo ""
    echo "----------------------------------------------------"
    echo "Building: $SERVICE"
    echo "----------------------------------------------------"

    if [ -d "$SERVICE" ]; then
        cd "$SERVICE"
        mvn clean package
        cd ..
    else
        echo "Error: Directory $SERVICE not found!"
        exit 1
    fi
done

echo ""
echo "===================================================="
echo "SUCCESS: All services built successfully!"
echo "===================================================="