ECHO OFF

REM The POI are from a local OpenStreetMap database named osm

ECHO ON

psql -h localhost -p 5432 -d toursims -U postgres -f ./PopulateTablesUser.sql
psql -h localhost -p 5432 -d toursims -U postgres -f ./PopulateTablesCityCoursePOI.sql
psql -h localhost -p 5432 -d toursims -U postgres -f ./PopulateTablesStepTrial.sql

PAUSE
