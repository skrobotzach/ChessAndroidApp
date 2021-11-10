package skrobotz.msu.edu.project2.Cloud.Models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "user2")
public class user2 {
    @Attribute(name = "username")
    private String username;

    public String getUsername() {return username;}

    @Attribute (name = "id")
    private String id;

    public String getId() {return id;}

    public user2() {}

    public user2(String username, String id)
    {
        this.username = username;
        this.id = id;
    }

}
