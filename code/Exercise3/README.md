# Code of your exercise

Put here all the code created for this exercise

**Run Pmd NestedIf Check for all code base**

```sh 
pmd-check -f text -R code/Exercise3/java/NestedIf.xml -d projects_codebase/commons-cli -r outputs/commons-cli/java/NestedIf.out
pmd-check -f text -R code/Exercise3/java/NestedIf.xml -d projects_codebase/commons-collections -r outputs/commons-collections/java/NestedIf.out
pmd-check -f text -R code/Exercise3/java/NestedIf.xml -d projects_codebase/commons-lang -r outputs/commons-lang/java/NestedIf.out
pmd-check -f text -R code/Exercise3/java/NestedIf.xml -d projects_codebase/commons-math -r outputs/commons-math/java/NestedIf.outs

cat outputs/commons-cli/java/NestedIf.out | wc -l
cat outputs/commons-collections/java/NestedIf.out | wc -l
cat outputs/commons-lang/java/NestedIf.out | wc -l
cat outputs/commons-math/java/NestedIf.out | wc -l
```