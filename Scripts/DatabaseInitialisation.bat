REM The bin directory from your PostgreSQL installation should be in your current PATH

psql -h localhost -p 5432 -U postgres -f ./CreateDatabase.sql
psql -h localhost -p 5432 -d toursims_debug -U postgres -f ./CreateSchema.sql
psql -h localhost -p 5432 -d toursims_debug -U postgres -f ./CreateTablesUser.sql
psql -h localhost -p 5432 -d toursims_debug -U postgres -f ./CreateTablesCoursePOI.sql
psql -h localhost -p 5432 -d toursims_debug -U postgres -f ./CreateTablesStepTrial.sql

PAUSE
