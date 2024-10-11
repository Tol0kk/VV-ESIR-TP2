
# Using PMD


Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset (see the [pmd install instruction](./pmd-help.md)). Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.


## Answer

`projects_codebase/commons-cli/src/main/java/org/apache/commons/cli/PatternOptionBuilder.java:133:	SwitchStmtsShouldHaveDefault:	Switch statements should be exhaustive, add a default case (or missing enum branches)`

### False-positive example 

We ran the command `pmd check -f text -R rulesets/java/quickstart.xml -d projects_codebase/commons-collections -r outputs/commons-collections/java/quickstart.out`

Here is the output from pmd : 

`projects_codebase/commons-collections/src/main/java/org/apache/commons/collections4/ListUtils.java:314:	CompareObjectsWithEquals:	Use equals() to compare object references.`

And here is the code corresponding to this output :

```java
/**
     * Tests two lists for value-equality as per the equality contract in
     * {@link java.util.List#equals(Object)}.
     * <p>
     * This method is useful for implementing {@code List} when you cannot
     * extend AbstractList. The method takes Collection instances to enable other
     * collection types to use the List implementation algorithm.
     * <p>
     * The relevant text (slightly paraphrased as this is a static method) is:
     * <blockquote>
     * Compares the two list objects for equality.  Returns
     * {@code true} if and only if both
     * lists have the same size, and all corresponding pairs of elements in
     * the two lists are <em>equal</em>.  (Two elements {@code e1} and
     * {@code e2} are <em>equal</em> if {@code (e1==null ? e2==null :
     * e1.equals(e2))}.)  In other words, two lists are defined to be
     * equal if they contain the same elements in the same order.  This
     * definition ensures that the equals method works properly across
     * different implementations of the {@code List} interface.
     * </blockquote>
     *
     * <b>Note:</b> The behavior of this method is undefined if the lists are
     * modified during the equals comparison.
     *
     * @see java.util.List
     * @param list1  the first list, may be null
     * @param list2  the second list, may be null
     * @return whether the lists are equal by value comparison
     */
    public static boolean isEqualList(final Collection<?> list1, final Collection<?> list2) {
        if (list1 == list2) {
            return true;
        }
        if (list1 == null || list2 == null || list1.size() != list2.size()) {
            return false;
        }

        final Iterator<?> it1 = list1.iterator();
        final Iterator<?> it2 = list2.iterator();

        while (it1.hasNext() && it2.hasNext()) {
            final Object obj1 = it1.next();
            final Object obj2 = it2.next();

            if (!Objects.equals(obj1, obj2)) {
                return false;
            }
        }

        return !(it1.hasNext() || it2.hasNext());
    }
```

As we see in the implementation of the method, the first comparison `if (list1 == list2)` is a comparison of references and not a deep comparison. In fact, we can see with the `while` loop that the deep comparison comes after and that the `equals()` is not the expected behavior here. Moreover, this check avoid unnecessary iterations when both references point to the same object. The current implementation is valid and efficient, so no modification is needed.

### True-positive example

Here is the pmd ouput of the commande `pmd check -f text -R rulesets/java/quickstart.xml -d projects_codebase/commons-cli -r outputs/commons-cli/java/quickstart.out`

`projects_codebase/commons-cli/src/main/java/org/apache/commons/cli/PatternOptionBuilder.java:133:    SwitchStmtsShouldHaveDefault:    Switch statements should be exhaustive, add a default case (or missing enum branches)`

Here is the code corresponding to this pmd finding :

```java
    /**
     * Retrieve the class that {@code ch} represents.
     *
     * @param ch the specified character
     * @return The class that {@code ch} represents
     * @since 1.7.0
     */
    public static Class<?> getValueType(final char ch) {
        switch (ch) {
        case '@':
            return OBJECT_VALUE;
        case ':':
            return STRING_VALUE;
        case '%':
            return NUMBER_VALUE;
        case '+':
            return CLASS_VALUE;
        case '#':
            return DATE_VALUE;
        case '<':
            return EXISTING_FILE_VALUE;
        case '>':
            return FILE_VALUE;
        case '*':
            return FILES_VALUE;
        case '/':
            return URL_VALUE;
        }

        return null;
    }
```

This is a True Positive, in fact, there is no default case in the switch statement, and that's the error returned by PMD. It would help to clarify the code and ensure that the switch statement handle every possible statement.