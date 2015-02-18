package pl.edu.uam.restapi.storage.model;

import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * Created by alan on 23.12.14.
 */
public class Student {
    private String pesel;
    private String name;
    private String lastname;
    private String birthdate;
    private String city;

    public Student() {}

    public Student(String pesel, String name, String lastname, String birthdate, String city) {
        this.pesel = pesel;
        this.name = name;
        this.lastname = lastname;
        this.birthdate = birthdate;
        this.city = city;
    }

    @ApiModelProperty(value = "Student's name", required = true)
    public String getName() {
        return name;
    }

    @ApiModelProperty(value = "Studen't lastname", required = true)
    public String getLastname() {
        return lastname;
    }

    @ApiModelProperty(value = "Student's PESEL", required = true)
    public String getPesel() {
        return pesel;
    }

    @ApiModelProperty(value = "Student's city", required = true)
    public String getCity() {
        return city;
    }

    @ApiModelProperty(value = "Student's date of birth", required = true)
    public String getBirthdate() {
        return birthdate;
    }
}
