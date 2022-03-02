# 🤔 Description of the issue
Using two or more relations in an `@Entity` may result in the following exception when trying to fetch more than 1 at same time:
```
org.hibernate.loader.MultipleBagFetchException: cannot simultaneously fetch multiple bags
```

The "problem" is reproduced by [RelationsDAOTest.java](src/test/java/com/jmlclosa/jpaexamples/multibag/RelationsDAOTest.java#reproducing_MultipleBagFetchException_using_criteria) 

# 🥳 Solution
https://vladmihalcea.com/hibernate-multiplebagfetchexception/

Execute two Criteria/JPQL at time, fetching only one association in each one.

This is the solution used in [RelationsDAOTest.java](src/test/java/com/jmlclosa/jpaexamples/multibag/RelationsDAOTest.java)

* It is recommended to use JPA Criteria instead of Hibernate Criteria because the last one is Deprecated since Hibernate 5.2

## Bad solution:
Change `List` to `Set`... Because it causes a Cartesian Product between Owner-Car-Toy. So, if we have 50 owner with 20
toy and 10 Car, the final result set will contain: 50x20x10 = 10.000 rows

This is the "solution" used in [RelationsDAOUsingSetTest.java](src/test/java/com/jmlclosa/jpaexamples/multibag/usingset/RelationsDAOUsingSetTest.java)


# 🧐 Analyzing performance impact

Launching tests with same query (same result and without ordering results) produces these results: 

| Solution     | JDBC statements | ns spent preparing JDBC statements | ns spent executing JDBC statements |
|--------------|-----------------|------------------------------------|------------------------------------|
| Using set    | 1               | 2.781.153                          | 138.350.818                        |
| Two Criteria | 2               | 3.923.370                          | 25.294.581                         |
| Two JPQL     | 2               | 5.231.568                          | 15.373.162                         |

So, executing two queries results in best performance