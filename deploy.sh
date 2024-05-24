#!/bin/bash

# Variables
PROJECT_ID="dawananonli"
IMAGE_NAME="be-auth-user-staff"
VERSION=$1

# Function to deploy a specific version
deploy_version() {
  echo "Deploying version $VERSION..."
  sudo docker pull $PROJECT_ID/$IMAGE_NAME:$VERSION
  sudo docker stop $(sudo docker ps -q --filter ancestor=$PROJECT_ID/$IMAGE_NAME)
  sudo docker run -d -p 80:8080 --name $IMAGE_NAME $PROJECT_ID/$IMAGE_NAME:$VERSION
}

# Main script logic
if [ -z "$VERSION" ]; then
  echo "Please provide the version to deploy. Usage: ./deploy.sh <version>"
  exit 1
fi

deploy_version
