(ns metabase.driver.singlestore
  (:require [honeysql
             [core :as hsql]]
            [metabase.driver :as driver]
            [metabase.util
             [honeysql-extensions :as hx]]
            [metabase.driver.sql.query-processor :as sql.qp]))

(defmethod sql.qp/date [:singlestore :year]    [_ _ expr] (hsql/call :date_trunc (hx/literal :year)     (hsql/call :timestamp expr)))
(defmethod sql.qp/date [:singlestore :quarter] [_ _ expr] (hsql/call :date_trunc (hx/literal :quarter)  (hsql/call :timestamp expr)))
(defmethod sql.qp/date [:singlestore :month]   [_ _ expr] (hsql/call :date_trunc (hx/literal :month)    (hsql/call :timestamp expr)))
(defmethod sql.qp/date [:singlestore :week]    [_ _ expr] (hsql/call :date_trunc (hx/literal :week)     (hsql/call :timestamp expr)))
(defmethod sql.qp/date [:singlestore :day]     [_ _ expr] (hsql/call :date_trunc (hx/literal :day)      (hsql/call :timestamp expr)))
(defmethod sql.qp/date [:singlestore :hour]    [_ _ expr] (hsql/call :date_trunc (hx/literal :hour)     (hsql/call :timestamp expr)))
(defmethod sql.qp/date [:singlestore :minute]  [_ _ expr] (hsql/call :date_trunc (hx/literal :minute)   (hsql/call :timestamp expr)))
(defmethod sql.qp/date [:singlestore :second]  [_ _ expr] (hsql/call :date_trunc (hx/literal :second)   (hsql/call :timestamp expr)))
