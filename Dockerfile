FROM alpine as build-env
RUN mkdir -p /src
WORKDIR /src
RUN apk add openjdk11 curl
RUN apk add bash git
RUN \
  curl -O https://download.clojure.org/install/linux-install-1.11.1.1200.sh && \
  chmod +x linux-install-1.11.1.1200.sh && \
  bash ./linux-install-1.11.1.1200.sh

FROM build-env as pre-build
WORKDIR /src
RUN mkdir metabase && \
    cd metabase && \
    git init && \
    git remote add origin https://github.com/metabase/metabase && \
    git fetch --progress --depth=1 origin 64178eb5856c772bb3f9b4a3fad7bd98dde8810e && \
    git reset --hard FETCH_HEAD && \
    mkdir plugins
RUN mkdir singlestore-metabase-driver
COPY . ./singlestore-metabase-driver/
RUN cd ./singlestore-metabase-driver/ && clojure -Xbuild :project-dir \"`pwd`\"

FROM metabase/metabase-enterprise:v1.46.2
RUN adduser -D metabase metabase
RUN mkdir -p /app/plugins && chown metabase:metabase /app/plugins
ENV MB_PLUGINS_DIR /app/plugins
COPY --from=pre-build --chown=metabase:metabase /src/singlestore-metabase-driver/target/singlestore.metabase-driver.jar /app/plugins/

