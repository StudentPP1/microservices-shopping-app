# stage 1: build project
FROM node:22 AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# stage 2: run nginx server
FROM nginx:alpine
COPY --from=build /app/dist/ /usr/share/nginx/html
