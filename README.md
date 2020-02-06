# FileDownloaderSystem
Application to download files from a list of sources

Technologies Used:
Java 11+
MySQL 
Spring Boot

How to run:
1. Run a mvn clean install
2. Run application FileDownloaderApp
3. Start localhost:8080
3. Service is up and running
4. Hit from any REST client
5. Use fds.sql and fds_drop.sql to create/update the DB schema setup

Design Concept:
Spring Boot initializes the application and starts listening on 8080 port. Using the REST client, 
the following request is sent
Request:
`POST /v1/download
{ 
     "urls":["https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf",
     "https://seeklogo.net/agoda-logo-vector-file91337-download.html",
     	"ftp://speedtest.tele2.net/1MB.zip",
     	"ftp://speedtest.tele2.net/3MB.zip"]
  }`
Response:
` 200 OK
{
     "id": "f79cfb0b-6477-4c2b-9281-1ee4797db6e0",
     "totalFiles": 4,
     "status": "IN_PROGRESS",
     "size": 0.0,
     "creationDate": "2020-02-05T23:50:08.432993",
     "lastModified": "2020-02-05T23:50:08.630152"
 }`

The service :
1. Validates the urls
2. For all valid urls, creates a record in download_metrics table DB for recording download metrics
3. Service spawns asynchronous thread per file from task executor for initiating download
4. All files in a download are tied with a download id in the table file_detail

Status check of download:
Request:
``GET /v1/download/f79cfb0b-6477-4c2b-9281-1ee4797db6e0/status
``
Response:
``200 OK
{
       "id": "f79cfb0b-6477-4c2b-9281-1ee4797db6e0",
       "totalFiles": 4,
       "status": "IN_PROGRESS",
       "size": 0.0,
       "creationDate": "2020-02-05T23:50:08.432993",
       "lastModified": "2020-02-05T23:50:08.630152"
   }
``