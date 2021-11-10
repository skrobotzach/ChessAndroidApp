package skrobotz.msu.edu.project2.Cloud.Models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "Game")
public class LoadGameResult {
    @Attribute
    private String status;

    @Attribute(name = "msg", required = false)
    private String message;

    public String getMessage() {return message;}

    public String getStatus() {return status;}

    @Attribute(name = "game", required = false)
    private String game;

    public String getGame() {return game;}

    public LoadGameResult(){}

    public LoadGameResult(String status, String message, String game)
    {
        this.game = game;

        this.message = message;

        this.status = status;


    }

}
