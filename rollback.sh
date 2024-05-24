#!/bin/bash

# Variables
PROJECT_ID="dawananonli"
IMAGE_NAME="be-auth-user-staff"

# Function to rollback to the previous version
rollback() {
  echo "Rolling back to the previous version..."
  PREVIOUS_VERSION=$(sudo docker images --format "{{.Tag}}" $PROJECT_ID/$IMAGE_NAME | sort -r | sed -n '2p')
  if [ -z "$PREVIOUS_VERSION" ]; then
    echo "No previous version found to rollback to."
    exit 1
  fi
  ./deploy.sh $PREVIOUS_VERSION
}

rollback
