package skrobotz.msu.edu.project2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private final static String USER = "user";
    private final static String PASS = "pass";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = PreferenceManager.getDefaultSharedPreferences(this);
        user = settings.getString(USER, "");
        pass = settings.getString(PASS, "");

        TextView userText = (TextView) findViewById(R.id.userText );
        TextView passText = (TextView) findViewById(R.id.passwordText );
        userText.setText(user);
        passText.setText(pass);
    }

    public void onLogin(View view)
    {
        CheckBox remember = (CheckBox) findViewById(R.id.checkBox);
        if (remember.isChecked())
        {
            TextView userText = (TextView) findViewById(R.id.userText);
            TextView passText = (TextView) findViewById(R.id.passwordText);
            user = userText.getText().toString();
            pass = passText.getText().toString();
            SharedPreferences.Editor edit = settings.edit();

            edit.putString(USER, user);
            edit.putString(PASS, pass);

            edit.commit();

        }
        LoggingInDlg dlg2 = new LoggingInDlg();
        dlg2.show(getSupportFragmentManager(), "login");
        return;

    }

    public void onCreateUser(View view)
    {
        Intent intent = new Intent(this, NewUserActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private SharedPreferences settings = null;

    private String user = "";
    private String pass = "";


}
