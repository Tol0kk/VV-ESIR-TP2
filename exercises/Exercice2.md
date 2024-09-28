
# Using PMD


Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset (see the [pmd install instruction](../pmd-help.md)). Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.


## Answer

`projects_codebase/commons-cli/src/main/java/org/apache/commons/cli/PatternOptionBuilder.java:133:	SwitchStmtsShouldHaveDefault:	Switch statements should be exhaustive, add a default case (or missing enum branches)`

`projects_codebase/commons-lang/src/main/java/org/apache/commons/lang3/StringUtils.java:5673:	ForLoopCanBeForeach:	This for loop can be replaced by a foreach loop`