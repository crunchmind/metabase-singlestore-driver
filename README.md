# metabase-memsql-driver

A Metabase driver for MemSQL. This is 99% MySQL driver, with some changes to date truncation that is MemSQL specific.

## Installation

Beginning with Metabase 0.32, drivers must be stored in a `plugins` directory in the same directory where `metabase.jar` is, or you can specify the directory by setting the environment variable `MB_PLUGINS_DIR`.

### Download Metabase Jar and Run

1. Download a fairly recent Metabase binary release (jar file) from the [Metabase distribution page](https://metabase.com/start/jar.html).
2. Build the MemSQL driver jar from this repository's source code
3. Create a directory and copy the `metabase.jar` to it.
4. In that directory create a sub-directory called `plugins`.
5. Copy the MemSQL driver jar you compiled to the `plugins` directory.
6. Make sure you are the in the directory where your `metabase.jar` lives.
7. Run `java -jar metabase.jar`.

## Build

Use `lein` with the following command:

```
LEIN_SNAPSHOTS_IN_RELEASE=true lein uberjar 
```

Ensure you have the Metabase core installed. This required you to clone the `metabase/metabase` repo and run `lein install-for-building-drivers`.
