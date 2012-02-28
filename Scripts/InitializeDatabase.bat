ECHO OFF

REM The bin directory from your PostgreSQL installation should be in your current PATH
REM The user password can be saved into the file %WINDOWS_USER_HOME%/AppData/Roaming/postgresql/pgpass.conf
REM Mine only contains the following line:
REM localhost:5432:*:postgres:postgres

ECHO ON

psql -h localhost -p 5432 -U postgres -f ./CreateDatabase.sql
psql -h localhost -p 5432 -d toursims -U postgres -f ./CreateSchema.sql

psql -h localhost -p 5432 -d toursims -U postgres -f ./CreateTablesUser.sql
psql -h localhost -p 5432 -d toursims -U postgres -f ./CreateTablesCityCoursePOI.sql
psql -h localhost -p 5432 -d toursims -U postgres -f ./CreateTablesStepTrial.sql

psql -h localhost -p 5432 -d toursims -U postgres -f ./PopulateTablesUser.sql
psql -h localhost -p 5432 -d toursims -U postgres -f ./PopulateTablesCityCoursePOI.sql
psql -h localhost -p 5432 -d toursims -U postgres -f ./PopulateTablesStepTrial.sql

PAUSE
