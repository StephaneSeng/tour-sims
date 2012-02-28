REM The bin directory from your PostgreSQL installation should be in your current PATH

psql -h localhost -p 5432 -U postgres -f ./CreateDatabase.sql
psql -h localhost -p 5432 -d toursims_debug -U postgres -f ./CreateSchema.sql

psql -h localhost -p 5432 -d toursims_debug -U postgres -f ./CreateTablesUser.sql
psql -h localhost -p 5432 -d toursims_debug -U postgres -f ./CreateTablesCityCourse.sql
psql -h localhost -p 5432 -d toursims_debug -U postgres -f ./CreateTablesStepTrial.sql

psql -h localhost -p 5432 -d toursims_debug -U postgres -f ./PopulateTablesUser.sql
psql -h localhost -p 5432 -d toursims_debug -U postgres -f ./PopulateTablesCityCourse.sql
psql -h localhost -p 5432 -d toursims_debug -U postgres -f ./PopulateTablesStepTrial.sql

PAUSE
