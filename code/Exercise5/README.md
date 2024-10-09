# Code of your exercise

# Info 

The rpiter generate a CSV file separated with `;`. The columns are `module/class info`, `method name/type`, `cc`. 

# Usage

```sh
cd code/Exercise5
# Compile
mvn package

# Run 
java -jar target/CyclomaticComplexityAnalysis-1.0-jar-with-dependencies.jar ../../projects_codebase/commons-cli/src > ../../outputs/commons-cli/cc.csv
java -jar target/CyclomaticComplexityAnalysis-1.0-jar-with-dependencies.jar ../../projects_codebase/commons-collections/src > ../../outputs/commons-collections/cc.csv
java -jar target/CyclomaticComplexityAnalysis-1.0-jar-with-dependencies.jar ../../projects_codebase/commons-lang/src > ../../outputs/commons-lang/cc.csv
java -jar target/CyclomaticComplexityAnalysis-1.0-jar-with-dependencies.jar ../../projects_codebase/commons-math/src > ../../outputs/commons-math/cc.csv

# Visualize with BoxPlot
python script_graph/main.py ../../outputs/commons-math/cc.csv ../../outputs/commons-lang/cc.csv ../../outputs/commons-collections/cc.csv ../../outputs/commons-cli/cc.csv
```