# School MVC (.NET 8)

This sample ASP.NET Core MVC application demonstrates how to connect to the `SchoolDB` SQL Server database defined in the prompt. The project targets **.NET 8** and is ready to open with **Visual Studio 2022**.

## 1. Prepare the database

1. Open SQL Server Management Studio (SSMS) or Azure Data Studio.
2. Execute the following script to create the database and tables:

   ```sql
   CREATE DATABASE SchoolDB;
   GO

   USE SchoolDB;
   GO

   CREATE TABLE Subject (
       Id INT PRIMARY KEY IDENTITY(1,1),
       Name NVARCHAR(100) NOT NULL,
       NumberOfSlot INT NOT NULL
   );

   CREATE TABLE Student (
       Id INT PRIMARY KEY IDENTITY(1,1),
       Name NVARCHAR(100) NOT NULL,
       Dob DATE NOT NULL
   );

   CREATE TABLE Course (
       Id INT PRIMARY KEY IDENTITY(1,1),
       Title NVARCHAR(100) NOT NULL,
       StartDate DATE NOT NULL,
       EndDate DATE NOT NULL,
       SubjectId INT NOT NULL,
       FOREIGN KEY (SubjectId) REFERENCES Subject(Id)
   );

   CREATE TABLE StudentCourse (
       StudentId INT NOT NULL,
       CourseId INT NOT NULL,
       Mark FLOAT NULL,
       PRIMARY KEY (StudentId, CourseId),
       FOREIGN KEY (StudentId) REFERENCES Student(Id),
       FOREIGN KEY (CourseId) REFERENCES Course(Id)
   );
   ```

## 2. Configure the project

1. Update the connection string in `appsettings.json` to point to your SQL Server instance.
2. If you use SQL authentication, replace `Trusted_Connection=True` with `User ID=...;Password=...`.
3. Restore NuGet packages via Visual Studio (`Build` > `Restore NuGet Packages`).

## 3. Run the application

1. Open `SchoolMvc.sln` in Visual Studio 2022.
2. Set **SchoolMvc** as the startup project.
3. Press `F5` to build and run. The default URL is `https://localhost:7144`.
4. Navigate using the top menu to manage subjects, students, and courses.

## 4. Optional: Apply EF Core migrations

This project is configured for Database-First usage. If you want to generate migrations from the existing schema:

```powershell
Add-Migration InitialCreate
Update-Database
```

Ensure the `SchoolDB` database exists before running `Update-Database`.

## 5. Project structure

- `Controllers` – MVC controllers with detailed comments.
- `Data/SchoolContext.cs` – EF Core DbContext mapping the SQL tables.
- `Models` – Entity classes representing the database tables.
- `Views` – Razor views for listing and creating entities.
- `wwwroot` – Static assets (CSS/JS).

Feel free to extend the project with edit/delete pages or additional validation rules.
