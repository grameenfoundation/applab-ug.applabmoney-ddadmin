package org.applab.ledgerlink.client.mob.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.applab.digdata.client.mob.admin.R;
import org.applab.ledgerlink.client.mob.admin.database.DatabaseHandler;
import org.applab.ledgerlink.client.mob.admin.model.VslaAdminUser;
import org.applab.ledgerlink.client.mob.admin.tasks.GetVslasTask;
import org.json.JSONObject;

public class LoginActivity extends Activity {

    Button btnLogin;
    EditText inputEmail;
    EditText inputPassword;
    TextView loginErrorMsg;
    String userName;
    String password;
    private DatabaseHandler dbHandler;

    /**
     * Called to render the login screen when the app is first launched.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!checkIfUserIsActivated()) {
            Intent activationScreen = new Intent(getApplicationContext(), ActivateDDAdminActivity.class);
            startActivity(activationScreen);
        }
        setContentView(R.layout.login);

        // Importing all assets like buttons, text fields
        btnLogin = (Button) findViewById(R.id.btnLogin);
        loginErrorMsg = (TextView) findViewById(R.id.login_error);
        inputEmail = (EditText) findViewById(R.id.loginEmail);
        inputPassword = (EditText) findViewById(R.id.loginPassword);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loginErrorMsg.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        };
        inputEmail.addTextChangedListener(textWatcher);
        inputPassword.addTextChangedListener(textWatcher);

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                dbHandler = new DatabaseHandler(getApplicationContext());
                loginErrorMsg.setText("");
                userName = inputEmail.getText().toString().trim();
                password = inputPassword.getText().toString().trim();


                if (authenticate(userName, password)) {
                    Intent listVsla = new Intent(getApplicationContext(), ListVslaActivity.class);

                    // Get UserName
                    listVsla.putExtra("userName", userName);

                    // Get VSLA list from local cache
                    listVsla.putExtra("vslaList", dbHandler.getAllVslasFromDb());

                    // Close all views before launching Search screen
                    listVsla.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(listVsla);

                } else {

                    //Error in login
                    loginErrorMsg.setText("Incorrect username/password");
                }
            }
        });
    }

    public void errorMessaging() {
        if (userName.trim().equals("")) {
            loginErrorMsg.setText("Email Field Required");
        } else if (password.trim().equals("")) {
            loginErrorMsg.setText("Password Field Required");

        } else if (userName.trim().equals(" ") && password.trim().equals(" ")) {
            loginErrorMsg.setText("Email and Password Fields Required");
        } else if (GetVslasTask.responseCode != 200) {
            loginErrorMsg.setText("Login Failed!! Please try again");
        }
    }

    private boolean checkIfUserIsActivated() {

        dbHandler = new DatabaseHandler(getApplicationContext());
        int userCount = dbHandler.getVslaAdminUserCount();
        if (userCount != 0) {
            return true;
        }
        return false;
    }

    private boolean authenticate(String userName, String password) {
        VslaAdminUser vslaAdminUser;
        dbHandler = new DatabaseHandler(getApplicationContext());
        vslaAdminUser = dbHandler.getVslaAdminUser(userName);
        if (vslaAdminUser == null) {
            return false;
        } else {
            if (vslaAdminUser.getPassword().equals(password)) {
                loginErrorMsg.setText("");
                return true;
            } else {
                return false;
            }
        }
    }

    public void postLoginListVslas(JSONObject json) {
        //To change body of created methods use File | Settings | File Templates.
    }
}