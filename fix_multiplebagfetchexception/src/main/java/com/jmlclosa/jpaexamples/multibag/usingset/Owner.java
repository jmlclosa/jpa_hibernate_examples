package com.jmlclosa.jpaexamples.multibag.usingset;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Owner")
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer   id;
    private String    name;
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Car>  cars;
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Toy> toys;

    public Owner() {
    }

    public Owner(String name) {
        this.name = name;
        this.cars = new HashSet<>();
        this.toys = new HashSet<>();
    }

    public void addCar(Car car) {
        this.cars.add(car);
    }

    public void addCar(Toy toy) {
        this.toys.add(toy);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Set<Toy> getToys() {return toys;}

    @Override
    public String toString() {
        return "Owner{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cars=" + cars +
                ", toys=" + toys +
                '}';
    }
}
