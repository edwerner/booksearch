# booksearch
Reactive Java book search application built on Spring Webflux, MongoDB and Apache Solr

Project created in Eclipse

# Installation
Download and install [Apache Maven](https://maven.apache.org/download.cgi)

Download and install [Apache Solr](https://lucene.apache.org/solr/downloads.html)

Download and install [MongoDB](https://www.mongodb.com/download-center/community)

## Add all directories to path

### Maven
(Windows) C:\Program Files\Apache Software Foundation\apache-maven-3.5.3

### Solr
(Windows) C:\Users\Edward\hadoop-3.1.0\hadoop-3.1\hadoop-3.1.0\solr-6.3.0\bin

### MongoDB
(Windows) C:\Program Files\MongoDB\Server\4.2\bin

# Running the Application

## Start MongoDB on Mac
`mongod`

## Start MongoDB on Windows
`mongod.exe` or Start service using Windows service app

## Start the web server with Maven
`mvn exec:java`

Navigate to `http://localhost:8080/search`
