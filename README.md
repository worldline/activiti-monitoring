activiti-monitoring
===================

Technical monitoring for Activiti : list of all running processes.

Built with Tapestry.


Database connection for the web gui can be configured in ```./src/main/webapp/WEB-INF/applicationContext.xml```
Default configuration, expects database to be in localhost in a database named activiti and username and password both to be ```activiti```

To run the web gui, it is enough to type:  ```mvn jetty:run```
Web part could be accessed at ```http://localhost:8080/login```

Users are checked against ```act-id-user``` table in activiti. ```restoreUsers.sql``` script creates test users.

Fot test purposes, ```src/test/java/org/activiti/monitor/fillData``` class creates many test process instances in different stages. Number of processes can be configured in variables ```level1Percent``` to ```level4Percent```.
Database configuration for fillData is set in ```src/test/resources/activiti.cfg.xml```
