package skrobotz.msu.edu.project2.Cloud.Models;

import org.simpleframework.xml.Attribute;

public class CancelUserResult {
    @Attribute
    private String status;

    @Attribute(name = "msg", required = false)
    private String message;

    public String getMessage() {return message;}

    public String getStatus() {return status;}


    public CancelUserResult(){}

    public CancelUserResult(String status, String message)
    {
        this.message = message;

        this.status = status;


    }
}
