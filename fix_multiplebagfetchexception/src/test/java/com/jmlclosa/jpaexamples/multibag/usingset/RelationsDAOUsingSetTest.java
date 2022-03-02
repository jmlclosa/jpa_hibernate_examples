package com.jmlclosa.jpaexamples.multibag.usingset;

import com.jmlclosa.jpaexamples.multibag.HibernateSessionFactoryBuilder;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class RelationsDAOUsingSetTest {

    @Rule
    public HibernateSessionFactoryBuilder sessionFactoryBuilder = new HibernateSessionFactoryBuilder(
            "sql/RelationsDAOTest.sql", Arrays.asList(Owner.class, Toy.class, Car.class));

    @Test
    public void test_with_cars_and_toys_preventing_MultipleBagFetchException_using_sets() {
        List<Owner> result = sessionFactoryBuilder.getSession()
                .createQuery(
                        "SELECT DISTINCT o FROM Owner o LEFT JOIN FETCH o.cars c LEFT JOIN FETCH o.toys t WHERE c.model = :model AND t.name LIKE :name ")
//                .createQuery("SELECT DISTINCT o FROM Owner o LEFT JOIN FETCH o.cars c LEFT JOIN FETCH o.toys t WHERE c.model = :model AND t.name LIKE :name  ORDER BY o.id, c.id, t.id")
                .setParameter("model", "Nissan Juke")
                .setParameter("name", "%Lego%")
                .list();

        System.out.println(result);
        Assert.assertEquals(2, result.size());
    }

}