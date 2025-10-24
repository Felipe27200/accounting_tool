# ACCOUNTING TOOL BACKEND

This guide provides instructions for building, running, and managing a Dockerized application using the provided Dockerfile and commands.

## Building the Docker Image

To build the Docker image, run the following command in the directory containing the `Dockerfile`:

`docker build -t accounting-tool-backend .`

- `-t accounting-tool-backend`: Tags the image with the name `accounting-tool-backend`.
- `.`: Specifies that the `Dockerfile` is located in the current directory.

## Running the Application in a Container

To run the application in a container, use:

`docker run --name accounting-backend -d -p 8080:8080 accounting-tool-backend`

- `--name`: Sets a name for the container.
- `-d`: Runs the container in detached mode (in the background).
- `-p 8080:8080`: Maps port 3000 on the host to port 3000 in the container.
- `accounting-tool-backend`: The name of the image used to create the container.

To check running containers:

`docker ps`

## Updating the Image

To update the image with changes, rebuild it using the same build command:

`docker build -t accounting-tool-backend .`

## Removing Old Containers

1. Stop the container:
   `docker stop <the-container-id>`

2. Remove the container:
   `docker rm <the-container-id>`

Replace `<the-container-id>` with the container ID from `docker ps`.