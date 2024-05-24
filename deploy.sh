#!/bin/bash

# Variables
PROJECT_ID="dawananonli"
IMAGE_NAME="be-auth-user-staff"
VERSION=$1
CONTAINER_NAME="be-auth-user-staff"

# Function to deploy a specific version
deploy_version() {
  echo "Deploying version $VERSION..."

  # Check if a container with the same name is already running
  if [ "$(sudo docker ps -q -f name=$CONTAINER_NAME)" ]; then
    echo "Stopping and removing existing container..."
    sudo docker stop $CONTAINER_NAME
    sudo docker rm $CONTAINER_NAME
  fi

  # Pull the new image version
  sudo docker pull $PROJECT_ID/$IMAGE_NAME:$VERSION

  # Run the new container
  sudo docker run -d -p 80:8080 --name $CONTAINER_NAME $PROJECT_ID/$IMAGE_NAME:$VERSION
}

# Main script logic
if [ -z "$VERSION" ]; then
  echo "Please provide the version to deploy. Usage: ./deploy.sh <version>"
  exit 1
fi

deploy_version
