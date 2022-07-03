# hua-api

This is a forked project from https://github.com./geogall/hua-api  

New packages are pushed at https://github.com/users/ba055482/packages/container/package/hua-api

## Overview

This is the backend component of the application. It is implemented in Java, using Spring/Springboot framework.  
The reason we selected this technology stack is because of prior experience on Java/Spring.
Its puprose is to handle HTTP requests originating from the [UI component](https://github.com/ba055482/hua-ui) and perform basic CRUD operations.

Below are enlisted some key points:
- Authentication is implemented with JWT.
- Data are persisted on a PostgreSQL database, which is not part of this project.
- Tests use the H2 (in-memory) database for simplicity.
- No embedded web servers are used.

You can use `docker-compose` to deploy the application with a dabatase as a container locally.
