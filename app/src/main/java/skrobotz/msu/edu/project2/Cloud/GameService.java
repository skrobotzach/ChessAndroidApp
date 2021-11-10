package skrobotz.msu.edu.project2.Cloud;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import skrobotz.msu.edu.project2.Cloud.Models.CancelUserResult;
import skrobotz.msu.edu.project2.Cloud.Models.EndGameResult;
import skrobotz.msu.edu.project2.Cloud.Models.GetUserResult;
import skrobotz.msu.edu.project2.Cloud.Models.LoadGameResult;
import skrobotz.msu.edu.project2.Cloud.Models.LoginUserResult;
import skrobotz.msu.edu.project2.Cloud.Models.SaveFCMResult;
import skrobotz.msu.edu.project2.Cloud.Models.SaveGameResult;
import skrobotz.msu.edu.project2.Cloud.Models.SaveUserResult;

import static skrobotz.msu.edu.project2.Cloud.Cloud.CANCEL_USER_PATH;
import static skrobotz.msu.edu.project2.Cloud.Cloud.END_GAME_PATH;
import static skrobotz.msu.edu.project2.Cloud.Cloud.GET_USER_PATH;
import static skrobotz.msu.edu.project2.Cloud.Cloud.LOAD_GAME_PATH;
import static skrobotz.msu.edu.project2.Cloud.Cloud.LOGIN_LOAD_PATH;
import static skrobotz.msu.edu.project2.Cloud.Cloud.SAVE_FCM_PATH;
import static skrobotz.msu.edu.project2.Cloud.Cloud.SAVE_GAME_PATH;
import static skrobotz.msu.edu.project2.Cloud.Cloud.USER_SAVE_PATH;


public interface GameService {
    @GET(LOGIN_LOAD_PATH)
    Call<LoginUserResult> getUser(
            @Query("gameuser") String gameUserId,
            @Query("magic") String magic,
            @Query("gameuserpw") String gameUserPw
    );

    @GET(END_GAME_PATH)
    Call<EndGameResult> endGame(
            @Query("magic") String magic
    );

    @GET(SAVE_FCM_PATH)
    Call<SaveFCMResult> saveFCM(
            @Query("magic") String magic,
            @Query("user") String user,
            @Query("id") String id
    );

    @GET(GET_USER_PATH)
    Call<GetUserResult> getUsers(
            @Query("magic") String magic
    );



    @GET(LOAD_GAME_PATH)
    Call<LoadGameResult> loadGame(
            @Query("magic") String magic
    );

    @GET(CANCEL_USER_PATH)
    Call<CancelUserResult> cancelGame(
            @Query("magic") String magic,
            @Query("user") String user
    );

    @GET(SAVE_GAME_PATH)
    Call<SaveGameResult> saveGame(
            @Query("magic") String magic,
            @Query("game") String game
    );

    @FormUrlEncoded
    @POST(USER_SAVE_PATH)
    Call<SaveUserResult> saveUser(@Field("xml") String xmlData);

}
