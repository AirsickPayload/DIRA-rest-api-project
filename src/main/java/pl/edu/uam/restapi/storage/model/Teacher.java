package pl.edu.uam.restapi.storage.model;

import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * Created by alan on 23.12.14.
 */
public class Teacher {
    private String id;
    private String name;
    private String lastname;
    private String employmentdate;
    private String qualifications;

    public Teacher() {}

    public Teacher(String name, String id, String lastname, String employmentdate, String qualifications) {
        this.name = name;
        this.id = id;
        this.lastname = lastname;
        this.employmentdate = employmentdate;
        this.qualifications = qualifications;
    }

    @ApiModelProperty(value = "Teacher's id", required = true)
    public String getId() {
        return id;
    }

    @ApiModelProperty(value = "Teacher's name", required = true)
    public String getName() {
        return name;
    }

    @ApiModelProperty(value = "Teacher's lastname", required = true)
    public String getLastname() {
        return lastname;
    }

    @ApiModelProperty(value = "Teacher's date of employment at school" , required = true)
    public String getEmploymentdate() {
        return employmentdate;
    }

    @ApiModelProperty(value = "Teacher's qualifications", required = true)
    public String getQualifications() {
        return qualifications;
    }
}
