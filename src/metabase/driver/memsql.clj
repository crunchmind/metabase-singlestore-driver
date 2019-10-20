(ns metabase.driver.memsql
  (:require [honeysql
             [core :as hsql]]
            [metabase.driver :as driver]
            [metabase.util
             [honeysql-extensions :as hx]]
            [metabase.driver.sql.query-processor :as sql.qp]))

(defmethod sql.qp/date [:memsql :year]    [_ _ expr] (hsql/call :date_trunc (hx/literal :year)     (hsql/call :timestamp expr)))
(defmethod sql.qp/date [:memsql :quarter] [_ _ expr] (hsql/call :date_trunc (hx/literal :quarter)  (hsql/call :timestamp expr)))
(defmethod sql.qp/date [:memsql :month]   [_ _ expr] (hsql/call :date_trunc (hx/literal :month)    (hsql/call :timestamp expr)))
(defmethod sql.qp/date [:memsql :week]    [_ _ expr] (hsql/call :date_trunc (hx/literal :week)     (hsql/call :timestamp expr)))
(defmethod sql.qp/date [:memsql :day]     [_ _ expr] (hsql/call :date_trunc (hx/literal :day)      (hsql/call :timestamp expr)))
(defmethod sql.qp/date [:memsql :hour]    [_ _ expr] (hsql/call :date_trunc (hx/literal :hour)     (hsql/call :timestamp expr)))
(defmethod sql.qp/date [:memsql :minute]  [_ _ expr] (hsql/call :date_trunc (hx/literal :minute)   (hsql/call :timestamp expr)))
(defmethod sql.qp/date [:memsql :second]  [_ _ expr] (hsql/call :date_trunc (hx/literal :second)   (hsql/call :timestamp expr)))
