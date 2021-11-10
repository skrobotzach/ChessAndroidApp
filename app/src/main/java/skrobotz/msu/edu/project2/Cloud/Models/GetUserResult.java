package skrobotz.msu.edu.project2.Cloud.Models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "users")
public class GetUserResult {
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

    @Element(name = "user1", required = false)
    private user1 player1;

    public user1 getPlayer1() {return player1;}

    @Element(name = "user2", required = false)
    private user2 player2;

    public user2 getPlayer2() {return player2;}


    public GetUserResult() {}

    public GetUserResult(String status, String msg, user1 player1, user2 player2) {
        this.status = status;
        this.message = msg;
        this.player1 = player1;
        this.player2 = player2;
    }
}
