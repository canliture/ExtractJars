### 1. What ?
Extract Libraries jars from IDEA Project, usage:
- code analysis to extract jar dependencies automatically after building a IDEA Project
- ...

### 2. usage
1. compile the project (>= Java 8)
2. usage for example: Main project/xxx/path/.idea/libraries ./libs
   - "project/xxx/path/.idea/libraries" is ".idea/libraries" directory under IDEA Project
   - "./libs" is target directory to place all the dependencies jars
3. after running Main, we can get all libs under directory ./libs

## 3. Adapt your specific need
you maybe change the code to adapt your specific need

replace Main#extract method body to adapt your specific need:
```java
// "/Users/liture" must be replace to your "user home"
jar = jar.replace("$USER_HOME$", "/Users/liture");

// "/Users/liture/.m2/repository" must be replace to your "maven home"
jar = jar.replace("$MAVEN_REPOSITORY$", "/Users/liture/.m2/repository");
```
