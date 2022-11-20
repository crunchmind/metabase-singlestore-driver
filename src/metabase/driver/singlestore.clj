(ns metabase.driver.singlestore
  (:require [honeysql
             [core :as hsql]]
            ;[clojure.string :as str]
            ;[honeysql.format :as hformat]
            [metabase.driver.sql-jdbc.sync :as sql-jdbc.sync]
            [metabase.driver.sql-jdbc.sync.describe-table :as sql-jdbc.describe-table]
            [metabase.driver.sql-jdbc.connection :as sql-jdbc.conn]
            [metabase.util
             [honeysql-extensions :as hx]]
            ;[metabase.models.field :as field]
            [metabase.driver.sql.query-processor :as sql.qp]))

(defmethod sql.qp/date [:singlestore :year] [_ _ expr] (hsql/call :date_trunc (hx/literal :year) (hsql/call :timestamp expr)))
(defmethod sql.qp/date [:singlestore :quarter] [_ _ expr] (hsql/call :date_trunc (hx/literal :quarter) (hsql/call :timestamp expr)))
(defmethod sql.qp/date [:singlestore :month] [_ _ expr] (hsql/call :date_trunc (hx/literal :month) (hsql/call :timestamp expr)))
(defmethod sql.qp/date [:singlestore :week] [_ _ expr] (hsql/call :date_trunc (hx/literal :week) (hsql/call :timestamp expr)))
(defmethod sql.qp/date [:singlestore :day] [_ _ expr] (hsql/call :date_trunc (hx/literal :day) (hsql/call :timestamp expr)))
(defmethod sql.qp/date [:singlestore :hour] [_ _ expr] (hsql/call :date_trunc (hx/literal :hour) (hsql/call :timestamp expr)))
(defmethod sql.qp/date [:singlestore :minute] [_ _ expr] (hsql/call :date_trunc (hx/literal :minute) (hsql/call :timestamp expr)))
(defmethod sql.qp/date [:singlestore :second] [_ _ expr] (hsql/call :date_trunc (hx/literal :second) (hsql/call :timestamp expr)))
(defmethod sql-jdbc.sync/describe-nested-field-columns :singlestore
           [driver database table]
           (let [spec   (sql-jdbc.conn/db->pooled-connection-spec database)
                 fields (sql-jdbc.describe-table/describe-nested-field-columns :mysql spec table)]
                (if (> (count fields) 100)
                  #{}
                  fields)))

;(defmethod metabase.driver.sql.query-processor/preprocess :singlestore [driver, inner_query] (
;                                                                                               prn inner_query
;                                                                                               ))

;(def ^:private database-type->mysql-cast-type-name
;  "MySQL supports the ordinary SQL standard database type names for actual type stuff but not for coercions, sometimes.
;  If it doesn't support the ordinary SQL standard type, then we coerce it to a different type that MySQL does support here"
;  {"integer"          "signed"
;   "text"             "char"
;   "double precision" "double"
;   "bigint"           "unsigned"})
;
;(defmethod sql.qp/json-query :singlestore
;           [_ unwrapped-identifier stored-field]
;           (letfn [(handle-name [x] (str "\"" (if (number? x) (str x) (name x)) "\""))]
;                  (let [field-type            (:database_type stored-field)
;                        field-type            (get database-type->mysql-cast-type-name field-type field-type)
;                        nfc-path              (:nfc_path stored-field)
;                        parent-identifier     (field/nfc-field->parent-identifier unwrapped-identifier stored-field)
;                        jsonpath-query        (format "$.%s" (str/join "." (map handle-name (rest nfc-path))))
;                        json-extract+jsonpath (hsql/call :json_extract_json (hsql/raw (hformat/to-sql parent-identifier)) jsonpath-query)]
;                       (case field-type
;                             ;; If we see JSON datetimes we expect them to be in ISO8601. However, MySQL expects them as something different.
;                             ;; We explicitly tell MySQL to go and accept ISO8601, because that is JSON datetimes, although there is no real standard for JSON, ISO8601 is the de facto standard.
;                             "timestamp" (hsql/call :convert
;                                                    (hsql/call :str_to_date json-extract+jsonpath "\"%Y-%m-%dT%T.%fZ\"")
;                                                    (hsql/raw "DATETIME"))
;
;                             "boolean" json-extract+jsonpath
;
;                             (hsql/call :convert json-extract+jsonpath (hsql/raw (str/upper-case field-type)))))))