# dbcopy

dbcopy copies database tables from source to target.
It uses a json configuration file and jdbc drivers

## Building
Building 

1. Download oracle and sql server driver jars an unpack jars into drivers folder.
2. Install jars to local directory using maven:
    
    Oracle:
    ``mvn org.apache.maven.plugins:maven-install-plugin:2.3.1:install-file -Dfile=drivers/ojdbc6.jar -DgroupId=com.oracle -DartifactId=jdbc -Dversion=6.0 -Dpackaging=jar``
    
    MS SQL Server
    ``mvn org.apache.maven.plugins:maven-install-plugin:2.3.1:install-file -Dfile=drivers/sqljdbc4.jar -DgroupId=com.microsoft.sqlserver -DartifactId=jdbc -Dversion=4.0 -Dpackaging=jar``

3. mvn package

## Running

To run the application use the following command:

    java -jar dbcopy-1.0.jar config.json

### configuration file

    {
      "source": {
        "server": {
          "driver": "org.postgresql.Driver",
          "url": "jdbc:postgresql://localhost:5432/db1",
          "username": "myuser",
          "password": "mypass"
        },
        "before": [],
        "after": [],
        "table": "nodes",
        "columns": [
          "id",
          "st_astext(geom) as geom"
        ]
      },
    
      "target": {
        "server": {
          "driver": "org.postgresql.Driver",
          "url": "jdbc:postgresql://localhost:5432/db2",
          "username": "myuser",
          "password": "mypass"
        },
        "before": [
          "drop table if exists tmp_test",
          "create table tmp_test(id serial not null, atext varchar(5) )"
        ],
        "after": [
          "select now()"
        ],
        "query": "insert into tmp_test(id, atext) values (?,?)"
      }
    }

### The server object

Connections to the source and target databases are configured through a server object.
The driver class and URL:s are driver specific and can usually be found on driver

Oracle
  
    "server": {
        "driver": "oracle.jdbc.OracleDriver",
        "url": "jdbc:oracle:thin:@localhost:1521:database",
        "username": "seponr",
        "password": "seponr"
    }
        
MS SQL Server

    "server": {
        "driver": "com.microsoft.sqlserver.jdbc.SQLServerDriver",
        "url": "jdbc:sqlserver://localhost:1433;databaseName=AdventureWorks",
        "username": "seponr",
        "password": "seponr"
    }
   
PostgreSQL

    "server": {
        "driver": "org.postgresql.Driver",
        "url": "jdbc:postgresql://localhost:5432/osm2",
        "username": "seponr",
        "password": "seponr"
    }


### Before and after
The before and after is an array of optional SQL statements executed 
before and after the actual data queries are executed. 
This can be useful, for example, where the destination table needs to be created before data is copied. 

Before:

    "before": [
        "drop table if exists tmp_test",
        "create table tmp_test(id serial not null, atext varchar(5) )"
    ]

After: 
    
    "after": [
        "select count(*) from tmp_test"
    ]
    

### table and columns or query
The queries used for selecting on the source and inserting on the target can be specified either by
specifying table names
