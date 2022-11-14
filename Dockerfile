FROM alpine as build-env
RUN mkdir -p /src
WORKDIR /src
RUN apk add clojure openjdk11 leiningen

FROM build-env as pre-build
WORKDIR /src
COPY project.clj ./
RUN LEIN_SNAPSHOTS_IN_RELEASE=true lein with-profile provided deps

FROM pre-build as build
WORKDIR /src
COPY . .
RUN LEIN_SNAPSHOTS_IN_RELEASE=true lein with-profile provided uberjar

FROM metabase/metabase
RUN adduser -D metabase metabase
RUN mkdir -p /app/plugins && chown metabase:metabase /app/plugins
ENV MB_PLUGINS_DIR /app/plugins
COPY --from=build --chown=metabase:metabase /src/target/uberjar/singlestore.metabase-driver.jar /app/plugins/
