package com.jmlclosa.jpaexamples.multibag;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.loader.MultipleBagFetchException;
import org.hibernate.sql.JoinType;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RelationsDAOTest {

    @Rule
    public HibernateSessionFactoryBuilder sessionFactoryBuilder = new HibernateSessionFactoryBuilder(
            "sql/RelationsDAOTest.sql", Arrays.asList(Car.class, Toy.class, Owner.class));


    @Test(expected = MultipleBagFetchException.class)
    public void reproducing_MultipleBagFetchException_using_criteria() {
        sessionFactoryBuilder.getSession()
                .createCriteria(Owner.class, "o")
                .createAlias("o.cars", "c", JoinType.LEFT_OUTER_JOIN)
                .createAlias("o.toys", "t", JoinType.LEFT_OUTER_JOIN)
                .add(Restrictions.ilike("c.model", "%Nissan%"))
                .add(Restrictions.ilike("t.name", "%Lego%"))
                .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
                .list();
    }

    @Test
    public void test_with_cars_and_toys_preventing_MultipleBagFetchException_using_criteria() {
        List<Owner> result = sessionFactoryBuilder.getSession()
                .createCriteria(Owner.class, "o")
                .createAlias("o.cars", "c", JoinType.LEFT_OUTER_JOIN)
                .add(Restrictions.ilike("c.model", "%Nissan%"))
                .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
//                .addOrder(Order.asc("o.id"))
//                .addOrder(Order.asc("c.id"))
                .list();
        result = sessionFactoryBuilder.getSession()
                .createCriteria(Owner.class, "o")
                .createAlias("o.toys", "t", JoinType.LEFT_OUTER_JOIN)
                .add(Restrictions.ilike("t.name", "%Lego%"))
                .add(Restrictions.in("o.id", result.stream().map(Owner::getId).collect(Collectors.toList())))
                .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
//                .addOrder(Order.asc("o.id"))
//                .addOrder(Order.asc("t.id"))
                .list();

//        System.out.println(result);
        Assert.assertEquals(2, result.size());
    }

    @Test
    public void test_with_cars_and_toys_preventing_MultipleBagFetchException_using_jpql() {
        List<Owner> result = sessionFactoryBuilder.getSession()
                .createQuery("SELECT DISTINCT o FROM Owner o LEFT JOIN FETCH o.cars c WHERE c.model = :model ")
//                .createQuery("SELECT DISTINCT o FROM Owner o LEFT JOIN FETCH o.cars c WHERE c.model = :model ORDER BY o.id, c.id")
                .setParameter("model", "Nissan Juke")
                .list();
        result = sessionFactoryBuilder.getSession()
                .createQuery("SELECT DISTINCT o FROM Owner o LEFT JOIN FETCH o.toys t WHERE t.name LIKE :name AND o IN :owners ")
//                .createQuery("SELECT DISTINCT o FROM Owner o LEFT JOIN FETCH o.toys t WHERE t.name LIKE :name AND o IN :owners ORDER BY o.id, t.id")
                .setParameter("name", "Lego%")
                .setParameterList("owners", result)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                .list();

//        System.out.println(result);
        Assert.assertEquals(2, result.size());
    }

}