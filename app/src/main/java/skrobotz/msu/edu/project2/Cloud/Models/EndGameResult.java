package skrobotz.msu.edu.project2.Cloud.Models;

import org.simpleframework.xml.Attribute;

public class EndGameResult {
    @Attribute
    private String status;

    @Attribute(name = "msg", required = false)
    private String message;

    public String getMessage() {return message;}

    public String getStatus() {return status;}


    public EndGameResult(){}

    public EndGameResult(String status, String message)
    {
        this.message = message;

        this.status = status;


    }
}
