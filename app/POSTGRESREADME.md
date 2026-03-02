Download postgres from postgressql.org
Installer configuration: set password to admin and don't change port number (should be 5432)

Update your user variables via system environment. Edit the user variable "Path" to include the path to the postgres/18/bin folder in program files. Not sure what it is like for MAC OS. As long as you can run psql --version in terminal and it prints fine, then you're good.

Open "psql" via windows explorer
Create your profile:
1. CREATE ROLE testdev WITH LOGIN PASSWORD 'admin' SUPERUSER CREATEDB CREATEROLE;
verify using \du (there should be a role named testdev with 3 permissions)

Create database:
1. CREATE DATABASE test;
2. GRANT ALL PERMISSIONS ON DATABASE test TO testdev; #do the same for postgres

The application.properties file is already updated so when we run, we can check the postgres database via terminal.
1. Login to database
    psql -U testdev -d test -h localhost
2. enter password (should be admin)
3. check table
    \d (prints all the relations/tables in the database)
    SELECT * FROM accounts;
4. look at user dan.

Now we have a decent, but confusing layout and know how to save data to our local database.