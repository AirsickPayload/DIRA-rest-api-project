package pl.edu.uam.restapi.storage.model;

import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * Created by alan on 07.01.2015.
 */
public class Subject {
    private String id;
    private String name;
    private String shortname;

    public Subject() {}

    public Subject(String id, String name, String shortname) {
        this.name = name;
        this.id = id;
        this.shortname = shortname;
    }

    @ApiModelProperty(value = "Get Subject's shortened name.", required = true)
    public String getShortname() {
        return shortname;
    }

    @ApiModelProperty(value = "Get Subject's id.", required = true)
    public String getId() {
        return id;
    }

    @ApiModelProperty(value = "Get Subject's full name.", required = true)
    public String getName() {
        return name;
    }
}
