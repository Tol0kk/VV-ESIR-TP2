# Init Codebase submodules

You can init the codebase submodules with `git submodule update --init projects_codebase` from the project root. 

# Check with pmd

**Run Pmd Quickstart Check for all codebase**

```sh
pmd check -f text -R rulesets/java/quickstart.xml -d projects_codebase/commons-cli -r outputs/commons-cli/java/quickstart.out
pmd check -f text -R rulesets/java/quickstart.xml -d projects_codebase/commons-collections -r outputs/commons-collections/java/quickstart.out
pmd check -f text -R rulesets/java/quickstart.xml -d projects_codebase/commons-lang -r outputs/commons-lang/java/quickstart.out
pmd check -f text -R rulesets/java/quickstart.xml -d projects_codebase/commons-math -r outputs/commons-math/java/quickstart.out
```

