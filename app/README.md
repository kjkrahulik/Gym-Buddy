START HERE: 
*Download Git and GitHub Desktop, for Git's installation set VSCode as Git's default editor. Everything else can be the default option. Git is used in terminal, whereas GitHub Desktop allows for easy use of Git.
*Create a branch and name it after yourself. We will be working in branches, and push our code to main when we are finished. It is a little confusing but we can discuss this later.


Springboot Intro
Spring is a framework. Like a toolbox.
It is modular so we can work with only what we need.
But since it is a framework, a lot of configuration is needed.
Springboot is bult ontop of the spring framework to simplify a lot of steps in production.

Development environment.
*Download JDK 25. Run java -version to verify in your terminal.
Use VSCode

Build Tool
Download Maven via chocolately.org. If you are on Mac install homebrew and run: brew update, and brew install maven.
Once chocolately is installed, type choco install maven. Verify with mvn -v. You might need to type refreshenv. MAKE SURE YOU ARE RUNNING IN ADMIN, if terminal doesn't work, run in cmd.

VSCode Extensions: 
Gitlens - Git Supercharged
Prettier
GitHub Copilot Chat
Extension Pack for Java
SpringBoot Extension Pack
Open In Browser (if not using edge or chrome as browser)

Project Structure:
.mvn, .vscode: folders for the VSCode and Maven, we don't care about these. .mvn contains the version of maven so it is consistent across this project.
src: where we code in.
target:
.gitattributes & .gitignore: we don't care about these.
mvnw, mvnw.cmd: maven files for mac and Linux (mvnw) and mvnw.cmd (windows).
pom (project object model): heart of maven project, basically defines all dependencies + configuration about or project. (DONT TOUCH)

main: where our actual code will be. (frontend, backend, classes, etc...)
test: folder for automated testing functions.
resources: static (front end files), application.properties is used for server port, database linkage, etc...
java: where all the java code will be.

When adding dependencies, make sure to run mvn -U clean install to refresh the dependencies.
We can view all dependencies via "Java Projects" in VSCode in the bottom left, then clicking on "Maven Dependencies"
Do not add any <version></version> tags to the dependencies as Spring Boot manages them automatically.

We are using Spring MVC, MVC stands for Model, View, Controller.
Model is where the Data + Logic is, as well as Database connection.
View is what the user sees, a.k.a front end.
Controller is the mediator between Model and View that allows the user to interact with the website.

HomeController will act as the Controller for this project. (Denoted by the @Controller annotation)
Annotations are just extra instructions for the compiler, it isn't used that often but is very helpful.

To run the website. You can click on Run -> Start Debugging or Start Without Debugging. You should have a window open up automatically and should preview the page.

NOTES: when it says "blah blah no debugger for XML", just open a .java file and run.
if you ever run into a " Process terminated with exit code: 1 -> [Help 1]
org.apache.maven.lifecycle.LifecycleExecutionException: Failed to execute goal org.springframework.boot:spring-boot-maven-plugin:4.0.3:run (default-cli) on project app: Process terminated with exit code: 1 " error, this means you have it running already. This would only appear if you have multiple windows, or have it run in VSCode + terminal. If using terminal, remember to use ctrl + c to end process.