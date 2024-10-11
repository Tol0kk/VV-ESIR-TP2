# Run App

```sh
cd code/Exercise4/find-missing-getters
mvn package
java -jar target/find-missing-getters-1.0-jar-with-dependencies.jar ../../../projects_codebase/commons-cli/src/ > ../../../outputs/commons-cli/java/missing-getters.md
java -jar target/find-missing-getters-1.0-jar-with-dependencies.jar ../../../projects_codebase/commons-lang/src/ > ../../../outputs/commons-lang/java/missing-getters.md
java -jar target/find-missing-getters-1.0-jar-with-dependencies.jar ../../../projects_codebase/commons-math/src/ > ../../../outputs/commons-math/java/missing-getters.md
java -jar target/find-missing-getters-1.0-jar-with-dependencies.jar ../../../projects_codebase/commons-collections/src/ > ../../../outputs/commons-collections/java/missing-getters.md
```