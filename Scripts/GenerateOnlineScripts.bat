ECHO OFF

REM This script is used for generating the SQL scripts which will be used with the phpPgAdmin Web interface

ECHO ON

REM Generating _InitializeOnlineDatabase.sql...
DEL _InitializeOnlineDatabase.sql
ECHO -- Warning: This file is generated, it should not be updated manually > _InitializeOnlineDatabase.sql
ECHO -- Concatenation of the CREATE TABLE statements found in the other files >> _InitializeOnlineDatabase.sql
ECHO. >> _InitializeOnlineDatabase.sql

ECHO -- psql -h localhost -p 5432 -d toursims -U postgres -f ./CreateTablesUser.sql >> _InitializeOnlineDatabase.sql
ECHO. >> _InitializeOnlineDatabase.sql
TYPE CreateTablesUser.sql >> _InitializeOnlineDatabase.sql
ECHO. >> _InitializeOnlineDatabase.sql

ECHO -- psql -h localhost -p 5432 -d toursims -U postgres -f ./CreateTablesCityCoursePOI.sql >> _InitializeOnlineDatabase.sql
ECHO. >> _InitializeOnlineDatabase.sql
TYPE CreateTablesCityCoursePOI.sql >> _InitializeOnlineDatabase.sql
ECHO. >> _InitializeOnlineDatabase.sql

ECHO -- psql -h localhost -p 5432 -d toursims -U postgres -f ./CreateTablesStepTrial.sql >> _InitializeOnlineDatabase.sql
ECHO. >> _InitializeOnlineDatabase.sql
TYPE CreateTablesStepTrial.sql >> _InitializeOnlineDatabase.sql
ECHO. >> _InitializeOnlineDatabase.sql

REM Generating _PopulateOnlineDatabase.sql...
DEL _PopulateOnlineDatabase.sql
ECHO -- Warning: This file is generated, it should not be updated manually > _PopulateOnlineDatabase.sql
ECHO -- Concatenation of the INSERT INTO statements found in the other files >> _PopulateOnlineDatabase.sql
ECHO -- The COMMIT statements are used here after each INSERT INTO because of an error with an unknown FOREIGN KEY while using the phpPgAdmin Web interface >> _PopulateOnlineDatabase.sql
ECHO. >> _PopulateOnlineDatabase.sql

ECHO -- psql -h localhost -p 5432 -d toursims -U postgres -f ./PopulateTablesUser.sql >> _PopulateOnlineDatabase.sql
ECHO. >> _PopulateOnlineDatabase.sql
FOR /F "tokens=1* delims=]" %%i IN ('TYPE "PopulateTablesUser.sql" ^| FIND /V /N ""') DO (
IF "%%j"=="" (ECHO. >> _PopulateOnlineDatabase.sql) ELSE (ECHO.%%j COMMIT; >> _PopulateOnlineDatabase.sql)
)
ECHO. >> _PopulateOnlineDatabase.sql

ECHO -- psql -h localhost -p 5432 -d toursims -U postgres -f ./PopulateTablesCityCoursePOI.sql >> _PopulateOnlineDatabase.sql
ECHO. >> _PopulateOnlineDatabase.sql
FOR /F "tokens=1* delims=]" %%i IN ('TYPE "PopulateTablesCityCoursePOI.sql" ^| FIND /V /N ""') DO (
IF "%%j"=="" (ECHO. >> _PopulateOnlineDatabase.sql) ELSE (ECHO.%%j COMMIT; >> _PopulateOnlineDatabase.sql)
)
ECHO. >> _PopulateOnlineDatabase.sql

ECHO -- psql -h localhost -p 5432 -d toursims -U postgres -f ./PopulateTablesStepTrial.sql >> _PopulateOnlineDatabase.sql
ECHO. >> _PopulateOnlineDatabase.sql
FOR /F "tokens=1* delims=]" %%i IN ('TYPE "PopulateTablesStepTrial.sql" ^| FIND /V /N ""') DO (
IF "%%j"=="" (ECHO. >> _PopulateOnlineDatabase.sql) ELSE (ECHO.%%j COMMIT; >> _PopulateOnlineDatabase.sql)
)
ECHO. >> _PopulateOnlineDatabase.sql

REM Generating _DropOnlineTables.sql
DEL _DropOnlineTables.sql
ECHO -- Warning: This file is generated, it should not be updated manually > _DropOnlineTables.sql
ECHO -- DROP statements with the same order as the TRUNCATE statements one >> _DropOnlineTables.sql
ECHO. >> _DropOnlineTables.sql

TYPE DropTables.sql >> _DropOnlineTables.sql
ECHO. >> _DropOnlineTables.sql

PAUSE
