#!/bin/sh
# Run the app locally (mvnw) against db+redis running in Docker.
# Docker exposes postgres on host port 5434, so we override
# the container-network values from .env accordingly.

set -a
[ -f .env ] && . ./.env
set +a

DB_HOST=localhost DB_PORT=5434 REDIS_HOST=localhost ./mvnw spring-boot:run -DskipTests
