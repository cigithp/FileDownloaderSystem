-- create schema
CREATE DATABASE IF NOT EXISTS fds;

USE fds;

-- create file details table
CREATE TABLE IF NOT EXISTS file_detail (
id BINARY(16) NOT NULL,
name VARCHAR(255) NULL DEFAULT 'File Name',
source VARCHAR(255) NOT NULL DEFAULT 'File source url',
destination VARCHAR(255) NOT NULL DEFAULT 'File destination url',
protocol VARCHAR(50) NOT NULL DEFAULT 'protocol used to download',
status VARCHAR(50) NOT NULL DEFAULT 'status',
creation_date TIMESTAMP NOT NULL,
last_modified TIMESTAMP NOT NULL,
PRIMARY KEY (id));

-- create file details history table
CREATE TABLE IF NOT EXISTS file_detail_history (
hist_id BINARY(16) NOT NULL,
id BINARY(16) NOT NULL,
name VARCHAR(255) NULL DEFAULT 'File Name',
source VARCHAR(255) NOT NULL DEFAULT 'File source url',
destination VARCHAR(255) NOT NULL DEFAULT 'File destination url',
protocol VARCHAR(50) NOT NULL DEFAULT 'protocol used to download',
status VARCHAR(50) NOT NULL DEFAULT 'status',
version NUMERIC(2) NOT NULL,
creation_date TIMESTAMP NOT NULL,
last_modified TIMESTAMP NOT NULL,
PRIMARY KEY (hist_id));