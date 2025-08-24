# build
FROM node:20-alpine AS build
WORKDIR /app/client
COPY client/package.json client/yarn.lock ./
RUN corepack enable && yarn install --frozen-lockfile
COPY client/ .
RUN yarn build

# run
FROM node:20-alpine
WORKDIR /srv
RUN npm i -g serve
COPY --from=build /app/client/dist ./dist
ENV PORT=3000
EXPOSE 3000
CMD ["serve","-s","dist","-l","3000"]
