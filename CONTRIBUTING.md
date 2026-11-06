Contributing to Ønskeseddel

Introduction:
Thank you for wanting to contribute to Ønskeseddel.

This guide describes how you will get started with this project, how the code is organised and guidelines we follow when working together.

About the project:
Ønskeseddel is a web application where users can create and administer their wishlist and share it with friends and family.

The project is developed as a group project at the Datamatikeruddannelsen at EK.

Technologies:
This project uses the following technologies:

Java - The applications backend.
Spring Boot - Framework for the web application.
Thymeleaf -  Dynamic generation of HTML-sites.
HTML & CSS - The applications user interfaces.
MySQL -  Database for storing users, wish lists and wishes.
JdbcTemplate - Communication between the java-application and database.
Maven -  Dependency management and built.
Git & GitHub - Versioncontrol and cooperation.
GitHub Actions -  Automation of build and tests.
Qodana - Static code analysis and quality assurance of the java-code.
Azure - Deployment and hosting

Get off to a good start:
To work with this project you need the following installed:

Java 21.
IntelliJ IDEA or any other Java compatible IDE.
Maven.
MySQL.
Git.

Installation:
Clone the project from GitHub: git clone <repository-url>.
Open the project in IntelliJ IDEA.
Wait for Maven to collect the project's dependencies.
Create a local MySQL-database with databaseschema that the project uses.
Configure the database connection via the project’s application properties and the necessary environment variables.
Start the application from the project Spring Boot main-class.
Open the application in the browser by using the local address that Spring Boot starts on.

The project structure:
The project is organized into different layers, each with its own responsibilities.

Controller -  Handles HTTP-requests and communication with user-interfaces.
Service - Contains the applications business logic.
Repository - Handles the database operations.
Model -  Contains the projects datamodels.

the frontend-files can be found in the following:

src/main/resources/templates -  Thymeleaf HTML-sites.
src/main/resources/static/css - CSS-styling.

Guidelines for the code:
When contributing to the project, we expect the following:

Follow the existing codestyle and naming.
Use describing names for classes, methods and variables.
Hold the Controller, Service, Repository and Model separate.
Place businesslogic in service instead of controller.
Avoid changing another member's code without coordination.
Avoid committing passwords, API-keys and other sensitive information.
Comment your code where it is necessary to explain the code's functionality.

Git & cooperation:
We use GitHub for version control and cooperation.

Before starting a new task:
Fetch the latest changes from the development branch.
Create a separate branch for your task.
Work only with the files that are relevant to your task.
Commit your changes regularly with descriptive commit-messages.
Push your branch to GitHub.
Create a pull-request, so that changes can be looked through before it merges.

Example for branch-names.
feature/wishlists
feature/wishrepository
bugfix/login-validation

Example for commit-messages.
Add wishlist creation
Fix login validation
Update shared wishlist page
