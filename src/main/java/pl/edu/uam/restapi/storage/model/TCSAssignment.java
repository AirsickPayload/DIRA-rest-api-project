package pl.edu.uam.restapi.storage.model;

import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * Created by alan on 09.01.2015.
 * Teacher - Class - Subject Assignment
 */
public class TCSAssignment {
    private String id;
    private String teacherid;
    private String classid;
    private String subjectid;

    public TCSAssignment() {}

    @ApiModelProperty(value = "Get Teacher - Class- Subject Assignment's ID.")
    public String getId() {
        return id;
    }

    @ApiModelProperty(value = "Get Teacher - Class- Subject Assignment's teacher ID.")
    public String getTeacherid() {
        return teacherid;
    }

    @ApiModelProperty(value = "Get Teacher - Class- Subject Assignment's class ID.")
    public String getClassid() {
        return classid;
    }

    @ApiModelProperty(value = "Get Teacher - Class- Subject Assignment's subject ID.")
    public String getSubjectid() {
        return subjectid;
    }

    public TCSAssignment(String id, String teacherid, String classid, String subjectid) {
        this.id = id;
        this.teacherid = teacherid;
        this.classid = classid;
        this.subjectid = subjectid;
    }
}
