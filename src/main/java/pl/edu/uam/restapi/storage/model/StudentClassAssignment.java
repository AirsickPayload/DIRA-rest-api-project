package pl.edu.uam.restapi.storage.model;

import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * Created by alan on 05.01.2015.
 */
public class StudentClassAssignment {
    private String id;
    private String pesel;
    private String classid;

    public StudentClassAssignment() {}

    public StudentClassAssignment(String id, String pesel, String classid) {
        this.id = id;
        this.pesel = pesel;
        this.classid = classid;
    }

    @ApiModelProperty(value = "[StudentClassAssignment] Get Assignment id.", required = true)
    public String getId() {
        return id;
    }

    @ApiModelProperty(value = "[StudentClassAssignment] Get Student's PESEL.", required = true)
    public String getPesel() {
        return pesel;
    }

    @ApiModelProperty(value = "[StudentClassAssignment] Get Class' id.", required = true)
    public String getClassid() {
        return classid;
    }
}
