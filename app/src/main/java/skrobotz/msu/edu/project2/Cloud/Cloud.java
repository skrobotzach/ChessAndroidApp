package skrobotz.msu.edu.project2.Cloud;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import skrobotz.msu.edu.project2.Cloud.Models.CancelUserResult;
import skrobotz.msu.edu.project2.Cloud.Models.EndGameResult;
import skrobotz.msu.edu.project2.Cloud.Models.GetUserResult;
import skrobotz.msu.edu.project2.Cloud.Models.LoadGameResult;
import skrobotz.msu.edu.project2.Cloud.Models.LoginUserResult;
import skrobotz.msu.edu.project2.Cloud.Models.SaveFCMResult;
import skrobotz.msu.edu.project2.Cloud.Models.SaveGameResult;
import skrobotz.msu.edu.project2.Cloud.Models.SaveUserResult;

@SuppressWarnings("deprecation")
public class Cloud {
    private static final String MAGIC = "NechAtHa6RuzeR8x";
    private static final String USER = "skrobotz";
    private static final String PASSWORD = "Hatting1";
    private static final String BASE_URL = "https://webdev.cse.msu.edu/~skrobotz/cse476/project2/";
    public static final String USER_SAVE_PATH = "user-save.php";
    public static final String LOGIN_LOAD_PATH = "user-load.php";
    public static final String LOAD_GAME_PATH = "game-load.php";
    public static final String SAVE_GAME_PATH = "game-save.php";
    public static final String SAVE_FCM_PATH = "join-user.php";
    public static final String GET_USER_PATH = "get-user.php";
    public static final String END_GAME_PATH = "end-game.php";
    public static final String CANCEL_USER_PATH = "cancel-user.php";
    private static final String UTF8 = "UTF-8";

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build();

    /**
     * Open a connection to a user in the cloud.
     * @return reference to an input stream or null if this fails
     */
    public boolean loginUserCloud(final String gameuser, final String gameuserpw) {
        GameService service = retrofit.create(GameService.class);
        try {
            Response<LoginUserResult> response = service.getUser(gameuser, MAGIC, gameuserpw).execute();

            // check if request failed
            if (!response.isSuccessful()) {
                Log.e("LoginUserCloud", "Failed to find user, response code is = " + response.code());
                return false;
            }

            LoginUserResult result = response.body();
            if (result.getStatus().equals("yes")) {
                return true;
            }

            Log.e("LoginUserCloud", "Failed to login user, message is = '" + result.getMessage() + "'");
            return false;
        } catch (IOException e) {
            Log.e("LoginUserCloud", "Exception occurred while loading hat!", e);
            return false;
        }

    }

    public void endGame()
    {
        GameService service = retrofit.create(GameService.class);
        try {
            EndGameResult result = service.endGame(MAGIC).execute().body();
            if (result.getStatus() != null && result.getStatus().equals("yes")) {
                return;
            }
            Log.e("EndGame", "Failed to save, message = '" + result.getMessage() + "'");
            return;
        } catch (IOException e) {
            Log.e("EndGame", "Exception occurred while trying to save user!", e);
            return;
        }

    }

    public ArrayList<String> getUsers()
    {

        ArrayList<String> users = new ArrayList<String>();

        GameService service = retrofit.create(GameService.class);
        try {
            Response<GetUserResult> response = service.getUsers(MAGIC).execute();

            // check if request failed
            if (!response.isSuccessful()) {
                Log.e("GetUsersCloud", "Failed to find user, response code is = " + response.code());
                return users;
            }

            GetUserResult result = response.body();
            if (result.getStatus().equals("yes")) {
                if (result.getPlayer1() != null)
                {
                    users.add(result.getPlayer1().getUsername());
                }
                else
                {
                    users.add(null);
                }
                if (result.getPlayer2() != null)
                {
                    users.add(result.getPlayer2().getUsername());
                }
                else
                {
                    users.add(null);
                }
                return users;
            }

            Log.e("GetUsersCloud", "Failed to login user, message is = '" + result.getMessage() + "'");
            return users;
        } catch (IOException e) {
            Log.e("GetUsersCloud", "Exception occurred while loading hat!", e);
            return users;
        }
    }


    public void joinUser(String user, String id)
    {

        user = user.trim();
        id = id.trim();
        GameService service = retrofit.create(GameService.class);
        try {
            Response<SaveFCMResult> response = service.saveFCM(MAGIC, user, id).execute();

            // check if request failed
            if (!response.isSuccessful()) {
                Log.e("JoinUserCloud", "Failed to find user, response code is = " + response.code());
                return;
            }

            SaveFCMResult result = response.body();
            if (result.getStatus().equals("yes")) {
                return;
            }

            Log.e("JoinUserCloud", "Failed to join user, message is = '" + result.getMessage() + "'");
            return;
        } catch (IOException e) {
            Log.e("JoinUserCloud", "Exception occurred while joining user!", e);
            return;
        }

    }

    private String getId() {
        return this.id;
    }

    private void setId(String token) {
        this.id = token;
    }

    /**
     * Save a hatting to the cloud.
     * This should be run in a thread.
     *
     * @return true if successful
     */
    public boolean createUserCloud(String username, String password) {
        username = username.trim();
        password = password.trim();
        if(username.length() == 0 || password.length() == 0) {
            return false;
        }

        // Create an XML packet with the information about the current image
        XmlSerializer xml = Xml.newSerializer();
        StringWriter writer = new StringWriter();

        try {
            xml.setOutput(writer);

            xml.startDocument("UTF-8", true);

            xml.startTag(null, "user");

            xml.attribute(null, "gameuser", username);
            xml.attribute(null, "pw", password);
            xml.attribute(null, "magic", MAGIC);

//            gameView.saveXml(username, xml); USE THIS IN SAVING GAME STATE

            xml.endTag(null, "user");

            xml.endDocument();

        } catch (IOException e) {
            // This won't occur when writing to a string
            return false;
        }

        GameService service = retrofit.create(GameService.class);
        final String xmlStr = writer.toString();
        try {
            SaveUserResult result = service.saveUser(xmlStr).execute().body();
            if (result.getStatus() != null && result.getStatus().equals("yes")) {
                return true;
            }
            Log.e("CreateUser", "Failed to save, message = '" + result.getMessage() + "'");
            return false;
        } catch (IOException e) {
            Log.e("CreateUser", "Exception occurred while trying to save user!", e);
            return false;
        }
    }


    private String id;

    public boolean saveGame(String xml) {

        xml = xml.replaceAll("<", "less");
        xml = xml.replaceAll(">", "great");
        xml = xml.replaceAll("=", "equal");
        xml = xml.replaceAll("\"", "quote");
        try {
            GameService service = retrofit.create(GameService.class);
            SaveGameResult result = service.saveGame(MAGIC, xml).execute().body();
            if (result.getStatus() != null && result.getStatus().equals("yes")) {
                return true;
            }
            Log.e("SaveGame", "Failed to save, message = '" + result.getMessage() + "'");
            return false;
        } catch (IOException e) {
            Log.e("SaveGame", "Exception occurred while trying to save user!", e);
            return false;
        }

    }

    public String loadGame() {
        try {
            GameService service = retrofit.create(GameService.class);
            LoadGameResult result = service.loadGame(MAGIC).execute().body();
            if (result.getStatus() != null && result.getStatus().equals("yes")) {
                if (result.getGame() == null)
                {
                    return "";
                }
                String res = result.getGame();
                res = res.replaceAll("less", "<");
                res = res.replaceAll("great", ">");
                res = res.replaceAll("equal", "=");
                res = res.replaceAll("quote", "\"");
                return res;
            }
            Log.e("LoadGame", "Failed to save, message = '" + result.getMessage() + "'");
            return "";
        } catch (IOException e) {
            Log.e("LoadGame", "Exception occurred while trying to save user!", e);
            return "";
        }
    }

    public boolean cancelUserCloud(String user) {
        try {
            GameService service = retrofit.create(GameService.class);
            CancelUserResult result = service.cancelGame(MAGIC, user).execute().body();
            if (result.getStatus() != null && result.getStatus().equals("yes")) {
                return true;
            }
            Log.e("CancelUser", "Failed to save, message = '" + result.getMessage() + "'");
            return false;
        } catch (IOException e) {
            Log.e("CancelUser", "Exception occurred while trying to save user!", e);
            return false;
        }
    }
}
