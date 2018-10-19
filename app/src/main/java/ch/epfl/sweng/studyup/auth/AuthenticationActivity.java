package ch.epfl.sweng.studyup.auth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import ch.epfl.sweng.studyup.LoginActivity;
import ch.epfl.sweng.studyup.MainActivity;
import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.player.Player;

/**
 * AuthenticationActivity
 *
 * Code used in the activity_authentication.
 */
public class AuthenticationActivity extends AppCompatActivity {
    private final String TAG = AuthenticationActivity.class.getSimpleName();

    public void reportAuthError() {
        Toast.makeText(AuthenticationActivity.this,
                "Unable to authenticate.",
                Toast.LENGTH_SHORT).show();
    }

    public void runAuthentication(String code) {

        String token = Authenticator.getToken(code);

        if (token != null) {
            String greeting = Authenticator.getGreeting(token);

                if (greeting != null) {

                    Firestore.get().getAndSetUserData(
                        Player.get().getSciper(),
                        Player.get().getFirstName(),
                        Player.get().getLastName()
                    );

                    Intent initMainActivity = new Intent(AuthenticationActivity.this, MainActivity.class);
                    initMainActivity.putExtra(
                        getString(R.string.post_login_message_value),
                        getString(R.string.post_login_message_value)
                    );
                    startActivity(initMainActivity);
                }

            if (greeting != null) {
                TextView profileDataDisplay = findViewById(R.id.profileDataDisplay);
                profileDataDisplay.setText(greeting);
                return;
            }
        }
        reportAuthError();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Uri authCodeURI = getIntent().getData();

        String code = authCodeURI.getQueryParameter("code");
        String error = authCodeURI.getQueryParameter("error");

        try {
            code = authCodeURI.getQueryParameter("code");
            error = authCodeURI.getQueryParameter("error");
        } catch (NullPointerException e) {
            Log.i(TAG, "Problem extracting data from Intent's Uri.");
        }

        if (TextUtils.isEmpty(error) && !TextUtils.isEmpty(code)) {
            runAuthentication(code);
        } else {
            reportAuthError();
            Log.e("AUTH ERROR", error);
        }


        Intent returnToLoginActivity = new Intent(AuthenticationActivity.this, LoginActivity.class);
        returnToLoginActivity.putExtra(
            getString(R.string.post_login_message_key),
            getString(R.string.login_failed_value)
        );

        Firestore.get().getAndSetUserData(
                Player.get().getSciper(),
                Player.get().getFirstName(),
                Player.get().getLastName());
    }
}