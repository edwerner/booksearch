# booksearch
Reactive Java book search application built on Spring Webflux, MongoDB and Apache Solr

Project created in Eclipse

# Installation
Download and install [Apache Maven](https://maven.apache.org/download.cgi)

Download and install [Apache Solr](https://lucene.apache.org/solr/downloads.html)

Download and install [MongoDB](https://www.mongodb.com/download-center/community)

Download [MongoDB Compass Community](https://www.mongodb.com/download-center/compass) GUI tool to view and delete database records 

## Add all directories to PATH

### Maven
(Windows) `C:\Program Files\Apache Software Foundation\apache-maven-3.5.3`

Run `mvn clean install` to install dependencies

### MongoDB
(Windows) `C:\Program Files\MongoDB\Server\4.2\bin`

There should only be 10K total database records stored

### Solr
(Windows) `C:\Users\Edward\hadoop-3.1.0\hadoop-3.1\hadoop-3.1.0\solr-6.3.0\bin`

Run `http://localhost:8983/solr/bookstore/update?commit=true&stream.body=%3Cdelete%3E%3Cquery%3E*%3A*%3C/query%3E%3C/delete%3E` in browser to delete all indices

There should only be 10K total indices

# Persistence and Indexing

Both persistence and indexing must be done concurrently

Uncomment method calls listed below to persist and index data

## Parse CSV and Save Records to MongoDB
Uncomment `CSVReader` instantiation and `saveBooks()` method call on lines `18` and `19` of `DataWriter.java`

Recomment lines when all entities are persisted to MongoDB

## Index Entities to Solr server instance

Create Solr core named `bookstore` in Solr admin panel found at http://localhost:8983/solr/#/~cores/

Uncomment `indexBooks()` method call on line `113` of `CSVRreader.java`

Recomment line when all entities are indexed by Solr

# Running the Application

## Start/Stop Solr on Windows
solr.cmd start
solr.cmd stop -p 8983

## Start/Stop Solr on Mac
solr start
solr stop

## Start/Stop MongoDB on Mac (Installed With Homebrew)
To start, run `mongod`
To stop run, get label with `launchctl list | grep mongo` and `launchctl stop <job label>` to stop MongoDB

## Star/Stop MongoDB on Windows
Run `mongod.exe` or start/stop service using Windows service app

## Start the web server with Maven
Run `mvn exec:java` or right-click  `BooksearchApplication.java > Run As > Java Application`

# Viewing the Application
Navigate to `http://localhost:8080/search`
