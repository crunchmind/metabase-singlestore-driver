FROM alpine as build-env
RUN mkdir -p /src
WORKDIR /src
RUN apk add openjdk11 curl
RUN apk add bash git
RUN \
  curl -O https://download.clojure.org/install/linux-install-1.11.1.1200.sh && \
  chmod +x linux-install-1.11.1.1200.sh && \
  bash ./linux-install-1.11.1.1200.sh

ENV VERSION=${METABASE_VER}

FROM build-env as pre-build
WORKDIR /src

RUN mkdir singlestore-metabase-driver
COPY . ./singlestore-metabase-driver/

RUN git clone https://github.com/metabase/metabase
RUN env
RUN cd metabase && git checkout v1.46.6.4
RUN cd metabase && clojure -Sdeps "{:aliases {:singlestore {:extra-deps {com.metabase/singlestore-driver {:local/root \"/src/singlestore-metabase-driver\"}}}}}"  \
  -X:build:singlestore build-drivers.build-driver/build-driver! \
  "{:driver :singlestore, :project-dir \"/src/singlestore-metabase-driver\", :target-dir \"/src/singlestore-metabase-driver/target\"}"

FROM metabase/metabase-enterprise:v1.46.6.4
RUN adduser -D metabase metabase
RUN mkdir -p /app/plugins && chown metabase:metabase /app/plugins
ENV MB_PLUGINS_DIR /app/plugins
COPY --from=pre-build --chown=metabase:metabase /src/singlestore-metabase-driver/target/singlestore.metabase-driver.jar /app/plugins/
