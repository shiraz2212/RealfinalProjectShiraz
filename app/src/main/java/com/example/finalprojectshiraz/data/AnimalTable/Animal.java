package com.example.finalprojectshiraz.data.AnimalTable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.firebase.database.IgnoreExtraProperties;
import java.io.Serializable;

@Entity
@IgnoreExtraProperties
public class Animal implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public long reminderTime;

    public String name;
    public String type;
    public String breed;
    public String age;
    public String gender;
    public String image; 
    public String keyid;
    public String location;

    public Animal() { }

    // Getters and Setters
    public String getName() { return name != null ? name : ""; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type != null ? type : ""; }
    public void setType(String type) { this.type = type; }

    public String getBreed() { return breed != null ? breed : ""; }
    public void setBreed(String breed) { this.breed = breed; }

    public String getAge() { return age != null ? age : ""; }
    public void setAge(String age) { this.age = age; }

    public String getGender() { return gender != null ? gender : ""; }
    public void setGender(String gender) { this.gender = gender; }

    public String getImage() { return image != null ? image : ""; }
    public void setImage(String image) { this.image = image; }

    public String getKeyid() { return keyid != null ? keyid : ""; }
    public void setKeyid(String keyid) { this.keyid = keyid; }

    public String getLocation() { return location != null ? location : ""; }
    public void setLocation(String location) { this.location = location; }

    public long getReminderTime() { return reminderTime; }
    public void setReminderTime(long reminderTime) { this.reminderTime = reminderTime; }
}
