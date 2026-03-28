## Setup & Usage Instructions

1. Start your local camunda instance by running `camunda-start.bat` in the camunda8 getting started bundle (not found in this repo)
2. Start the API (this repo) by running `ProBuildApiApplication`, start the service worker (in the worker repo) by running `ProBuildWorkerApplication`.
3. The database should already be populated. You can view the contents of it by going to: http://localhost:8081/h2-console. Login with
```
JDBC URL: jdbc:h2:file:./data/probuilddb;AUTO_SERVER=TRUE
User Name: sa
Password: (leave blank)
```
Then click Connect.
You'll land on a SQL workbench. In the left panel you'll see your TOOL table — click it to expand its columns. In the main query box you can run standard SQL, e.g.:


```sql
SELECT * FROM tool;
```

4. Launch the local Camunda Modeler (again from getting started bundle)
5. Deploy the diagram with the rocket ship icon (can use my example from the diagrams repo), then run with the play icon
6. Observe from http://localhost:8080/operate/login (user+password is demo), check api console for logging of which endpoints were hit, refresh database to see changes made.

## Manual Table Population

If you wish to populate the table manually, you can hit the endpoint using postman, eg:

```
POST http://localhost:8081/add?name=Drill&category=Power Tools
GET http://localhost:8081/tools
GET http://localhost:8081/tools/available
```
