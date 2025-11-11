package com.example.finalprojectshiraz.data.AnimalTable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Animal {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String name;
    public String type;
    public String breed;
    public String gender;
    public String age;
    public String color;
    public String size;
    public String location;
    public String image;
    public String description;
    public String status;
    public String keyid;

    @Override
    public String toString() {
        return "Animal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", breed='" + breed + '\'' +
                ", gender='" + gender + '\'' +
                ", age='" + age + '\'' +
                ", color='" + color + '\'' +
                ", size='" + size + '\'' +
                ", location='" + location + '\'' +
                ", image='" + image + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", keyid='" + keyid + '\'' +
                '}';
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getBreed() {
        return breed;
    }
    public void setBreed(String breed) {
        this.breed = breed;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getAge() {
        return age;
    }
    public void setAge(String age) {
        this.age = age;
    }
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public String getSize() {
        return size;
    }
    public void setSize(String size) {
        this.size = size;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getKeyid() {
        return keyid;
    }
    public void setKeyid(String keyid) {
        this.keyid = keyid;
    }
}
