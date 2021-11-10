package skrobotz.msu.edu.project2.Cloud.Models;

import org.simpleframework.xml.Attribute;

public class SaveFCMResult {
    @Attribute(name = "msg", required = false)
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Attribute
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public SaveFCMResult() {}

    public SaveFCMResult(String status, String msg) {
        this.status = status;
        this.message = msg;
    }
}
