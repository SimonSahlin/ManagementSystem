# ManagementSystem
Web Application for a Management System

---------- Quick description of the project ----------

The program is a management system that creates, updates and deletes employees. There is employees, managers and Ceo:s as employees. 
All employees but the ceo does have a manager, also the managers. The manager of each employee is defiened by a managerId that correlates with each Employee Id. 

Example: If Johan with EmployeeId 1 is manager of Ove, then Ove does have managerId 1. 

------------------------------------------------------

---------- SET UP ----------
The database is named consid with the table employees. All you need to do is to create your own database in Workbench MySQL with w/e name you want. 
Set the correct url, username and password in the application.properties, also set the the same information in FunctionsController.java in the function checkForCeo().  

After this you are good to go, fire up the program and go to: localhost:8080 and try it out. The program will enter all the variables to the database with each entry, create coloumns etc so you dont have to.

----------------------------

---------- Languages and implementations ----------

The program is written in HTML and Java using Thymeleaf and Spring boot. Classic MVC-model and connection to a workbench MySQL. 

---------------------------------------------------
