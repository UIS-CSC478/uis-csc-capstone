# UIS CSC478 Capstone – Group Project (Spring 2026)



This repository contains the CSC478 Group Capstone project. 

The application is a **Maven-based Java project**. Maven handles building the app and downloading any dependencies automatically. Recommended IDE is **Eclipse**.



This README explains:

- How to set up the project locally

- How to build and run the application

- How the team can use GitHub (branches, pull requests, merging)

Note: The GUI uses JavaFX. The recommended run command for all platforms is `mvn javafx:run`.


---



## Requirements



Before starting, make sure you have the following installed:



- **Git** (GitHub Desktop recommended)

- **Java JDK 17** (Temurin / OpenJDK recommended)

- **Eclipse IDE for Java Developers** (recommended)



### Verify Java Is Installed



Open Command Prompt and run:



```bat

java -version

```



You should see version **17.x**.

NOTE: When installing please include Set JAVA_HOME variable - this allows Maven to function correctly without needing the full installation path hardcoded into the configurations or startup script.


---



## 1) Clone the Repository



### Using GitHub Desktop



- Open GitHub Desktop

- Click **File → Clone repository**

- Select the repository

- Choose a local folder

- Click **Clone**



### Using Command Line



```bat

git clone <repo-url>

```



After cloning, the folder should contain:

- `pom.xml`

- `src/`

- `README.md`



---



## 2) Import the Project into Eclipse



- Open **Eclipse**

- Click **File → Import**

- Select **Maven → Existing Maven Projects**

- Click **Next**

- Browse to the repository folder (where `pom.xml` is located)

- Ensure the project is checked

- Click **Finish**



Allow Maven to download dependencies if prompted.



---



## 3) Pull Latest Changes



Do this **before starting work**.



### Using GitHub Desktop



- Click **Fetch origin**

- Click **Pull origin**



### Using Command Line



```bat

git pull

```



---



## 4) Update Maven Project in Eclipse



After pulling changes:



- Right-click the project

- Click **Maven → Update Project…**

- Click **OK**



This keeps Eclipse synchronized with Maven.



---



## 5) Build the Project



### From Eclipse



- Right-click the project

- Click **Run As → Maven build…**

- Enter into **Goals**:

```

clean package

```

- Click **Run**



### From Command Line (repo root)



```bat

mvn clean package

```



The build output is created in the `target/` folder (example: `scrabble-app-0.0.1-SNAPSHOT.jar`).



---

## 6) Run the Application (JavaFX GUI)

### From Eclipse (recommended)

- Open `Main.java`
- Right-click → **Run As → Java Application**

### From Command Line (repo root)

Build first (creates the jar):

```bat
mvn clean package
```
Run the jar:
```
java -jar target/scrabble-app-0.0.1-SNAPSHOT.jar
```
Run using Maven (best cross-platform option)

This is the most consistent option for Windows/macOS/Linux:
```
mvn javafx:run
```
---

## 7) Common Issues



### Java Is Not Recognized



- Install **JDK 17**

- Ensure Java is added to **PATH**

- Restart Command Prompt



### Eclipse Shows Errors After Pulling



- **Maven → Update Project**

- **Project → Clean…**

- Rebuild the project



---



## 8) Files to Commit



### Commit These Files



- `src/**`

- `pom.xml`

- `.gitignore`

- `README.md`



### Do NOT Commit



- `target/`

- `.project`

- `.classpath`

- `.settings/`



---



# Git Workflow (Branching, Pull Requests, and Merging)



Do **not** work directly on the `main` branch.



---



## Creating a Feature Branch

NOTE: to keep track of releases all feature branches must be built from the **development** branch. Oncce a stable version is reached we can merge development to Main.

### Using GitHub Desktop



- Click **Current Branch**

- Click **New Branch**

- Name the branch:

```

feature/short-description

```

- Click **Create Branch**



### Using Command Line



```bat

git checkout -b feature/short-description

```



---



## Making Changes and Committing



- Make code changes

- Commit with a clear summary, for example:

```

Add scoring logic

```



### Push the Branch



- GitHub Desktop → **Push origin**

- or:

```bat

git push -u origin feature/short-description

```



---



## Opening a Pull Request



- Go to the repository on GitHub

- Click **Compare & pull request**

- Set base branch to `main`

- Add a short description

- Click **Create pull request**



---



## Merging to Main



- Another teammate reviews the pull request

- Resolve any feedback

- Click **Merge pull request**

- Keep **Development** branch after merging



---



## After Merging



All teammates should update their local copy:



```bat

git checkout main

git pull

```



Then in Eclipse:



- Right-click project → **Maven → Update Project**



---



## Team Best Practices



- One feature per branch

- Small commits

- Pull before starting work

- Never commit directly to `main`



