ECHO ON

psql -h localhost -p 5432 -d toursims -U postgres -f ./TruncateTables.sql

PAUSE
