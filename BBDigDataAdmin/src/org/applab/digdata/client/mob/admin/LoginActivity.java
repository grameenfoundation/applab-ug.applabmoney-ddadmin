package org.applab.digdata.client.mob.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.jetbrains.annotations.NotNull;

public class LoginActivity extends Activity {

    Button btnLogin;
    EditText inputEmail;
    EditText inputPassword;
    TextView loginErrorMsg;

    // JSON Response node names
    //private static String KEY_SUCCESS = "success";
    //private static String KEY_ERROR = "error";
    //private static String KEY_ERROR_MSG = "error_msg";
    //private static String KEY_UID = "uid";
    @NotNull
    private static String KEY_NAME = "name";
    @NotNull
    private static String KEY_EMAIL = "email";
    // private static String KEY_LAST_LOGIN = "last_login";

    /**
     * Called to render the login screen when the app is first launched.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Importing all assets like buttons, text fields
        inputEmail = (EditText) findViewById(R.id.loginEmail);
        inputPassword = (EditText) findViewById(R.id.loginPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        loginErrorMsg = (TextView) findViewById(R.id.login_error);

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();
                // VslaAdminFunctions vslaAdminFunction = new VslaAdminFunctions();
                //  JSONObject json = vslaAdminFunction.loginUser(email, password);

                // Check for login response
                /**
                 try {
                 if (json.getString(KEY_SUCCESS) != null) {
                 loginErrorMsg.setText("");
                 String res = json.getString(KEY_SUCCESS);
                 if(Integer.parseInt(res) == 1){
                 // user successfully logged in
                 // Store user details in SQLite Database
                 DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                 JSONObject json_user = json.getJSONObject("user");

                 // Clear all previous data in database
                 vslaAdminFunction.logoutUser(getApplicationContext());
                 db.addUser(json_user.getString(KEY_NAME), json_user.getString(KEY_EMAIL), json.getString(KEY_UID), json_user.getString(KEY_LAST_LOGIN));

                 // Launch Dashboard Screen
                 Intent dashboard = new Intent(getApplicationContext(), VslaAdminDashboardActivity.class);

                 // Close all views before launching Dashboard
                 dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                 startActivity(dashboard);

                 // Close Login Screen
                 finish();
                 }else{
                 // Error in login
                 loginErrorMsg.setText("Incorrect username/password");
                 }
                 }
                 } catch (JSONException e) {
                 e.printStackTrace();
                 }  */
                if (email.equals("xxx@xxx.com") && password.equals("xxx")) {

                    // Launch Search screen
                   // Intent searchVsla = new Intent(getApplicationContext(), FindVslaActivity.class);
                    Intent searchVsla = new Intent(getApplicationContext(), ListVslaActivity.class);

                    // Close all views before launching Search screen
                    searchVsla.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(searchVsla);

                    // Close Login Screen
                    finish();
                } else {
                    // Error in login
                    loginErrorMsg.setText("Incorrect username/password");
                }
            }
        });
    }
}