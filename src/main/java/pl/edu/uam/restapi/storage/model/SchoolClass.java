package pl.edu.uam.restapi.storage.model;

import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * Created by alan on 23.12.14.
 */
public class SchoolClass {
    private String id;
    private String profile;
    private String shortcut;
    private int year;

    public SchoolClass() {}


    public SchoolClass(String id, String profile, String shortcut, int year) {
        this.id = id;
        this.profile = profile;
        this.shortcut = shortcut;
        this.year = year;
    }

    @ApiModelProperty(value="Class' year", required = true)
    public int getYear() {return year;}

    @ApiModelProperty(value = "Class' id", required = true)
    public String getId() {
        return id;
    }

    @ApiModelProperty(value = "Class' profile name", required = true)
    public String getProfile() {
        return profile;
    }

    @ApiModelProperty(value = "Class' short name(shortcut)" , required = true)
    public String getShortcut() {
        return shortcut;
    }
}
