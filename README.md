## Setup & Usage Instructions

1. Start your local camunda instance by running `camunda-start.bat` in the camunda8 getting started bundle (not found in this repo)
2. Start the API (this repo) by running ```ProBuildApiApplication```, start the service worker (in the worker repo) by running ```ProBuildWorkerApplication```.
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
4. seed the database via "curl.exe -X POST http://localhost:8081/seed" this fills it with dummy data that can be used down the line
5. Launch the local Camunda Modeler (again from getting started bundle)
6. Deploy the diagram and forms (go to the disp diagrams folder and in command prompt run ```python deploy.py```)
7. to start an instance from create purchase order run ```python start.py``` from Disp diagrams
8. Observe from http://localhost:8080/operate/login (user+password is demo), check api console for logging of which endpoints were hit, refresh database to see changes made.
9. when encountering a user task make sure to go to http://localhost:8080/tasklist to fill in the forms, be aware you will have to press, `Assign to Me` before you can fill them in.

**[NOTE] when running the camunda file you may notice when going to a throw event that crossess into another pool e.g FixPro you will have to switch processess to see what happened at said pool and may need to resolve some forms before flows can continue**

## Manual Table Population

If you wish to populate the table manually, you can hit the endpoint using postman, eg:

```
POST http://localhost:8081/tools?name=Drill&category=Power Tools
GET http://localhost:8081/tools
GET http://localhost:8081/tools/available
```
