package com.jmlclosa.jpaexamples.multibag;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Owner")
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer       id;
    private String    name;
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Car> cars;
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Toy> toys = new ArrayList<>();

    public Owner() {
    }

    public Owner(String name) {
        this.name = name;
        this.cars = new ArrayList<>();
        this.toys = new ArrayList<>();
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

    public List<Toy> getToys() {return toys;}

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    public void setToys(List<Toy> toys) {
        this.toys = toys;
    }

    public List<Car> getCars() {
        return cars;
    }

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
