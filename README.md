# OpenLabReserve

Modern and open source tool designed for reserving lab spaces.
Currently designed for our computer labs.

## Setting up a development environment

1. Create a mariadb database `olr-dev` with user `olr-dev` and password `olr`
3. Start the frontend 
4. Start the backend

The frontend will proxy the backend, and you can start using the tool as-is.

## Deploying (Docker)

Requirements: Maven, Java 21, Node 21 (use nvm)

```bash 
cd ./dist/docker
./prepare.sh
```

This will generate a \*.zip file with the Dockerfile and docker-compose.yml

**Caution:** Update the passwords!