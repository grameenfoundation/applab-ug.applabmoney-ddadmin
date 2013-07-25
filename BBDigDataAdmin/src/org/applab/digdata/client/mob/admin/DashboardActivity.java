package org.applab.digdata.client.mob.admin;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 *
 *
 */
public class DashboardActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * onDestroy The final call you receive before your activity is destroyed.
     * This can happen either because the activity is finishing (someone called
     * finish() on it, or because the system is temporarily destroying this
     * instance of the activity to save space. You can distinguish between these
     * two scenarios with the isFinishing() method.
     */

    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * onPause Called when the system is about to start resuming a previous
     * activity. This is typically used to commit unsaved changes to persistent
     * data, stop animations and other things that may be consuming CPU, etc.
     * Implementations of this method must be very quick because the next
     * activity will not be resumed until this method returns. Followed by
     * either onResume() if the activity returns back to the front, or onStop()
     * if it becomes invisible to the user.
     */

    protected void onPause() {
        super.onPause();
    }

    /**
     * onRestart Called after your activity has been stopped, prior to it being
     * started again. Always followed by onStart().
     */

    protected void onRestart() {
        super.onRestart();
    }

    /**
     * onResume Called when the activity will start interacting with the user.
     * At this point your activity is at the top of the activity stack, with
     * user input going to it. Always followed by onPause().
     */

    protected void onResume() {
        super.onResume();
    }

    /**
     * onStart Called when the activity is becoming visible to the user.
     * Followed by onResume() if the activity comes to the foreground, or
     * onStop() if it becomes hidden.
     */

    protected void onStart() {
        super.onStart();
    }

    /**
     * onStop Called when the activity is no longer visible to the user because
     * another activity has been resumed and is covering this one. This may
     * happen either because a new activity is being started, an existing one is
     * being brought in front of this one, or this one is being destroyed.
     * <p/>
     * Followed by either onRestart() if this activity is coming back to
     * interact with the user, or onDestroy() if this activity is going away.
     */

    protected void onStop() {
        super.onStop();
    }

    public void setHeader(boolean btnHomeVisible, boolean btnHelpVisible) {
        try {
            ViewStub stub = (ViewStub) findViewById(R.id.vsHeader);
            View inflated = stub.inflate();

            /**TextView txtTitle = (TextView) inflated.findViewById(R.id.txtHeading);
             txtTitle.setText(title);  */
            TextView btnHome = (TextView) inflated.findViewById(R.id.btnHome);
            if (btnHomeVisible)
                btnHome.setVisibility(View.VISIBLE);
            Button btnHelp = (Button) inflated.findViewById(R.id.btnHelp);
            if (btnHelpVisible)
                btnHelp.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            Log.e("ERROR", "Error in Code:" + e.toString());
        }

    }

    public void showToast(Activity activity, String toastString) {
        Toast.makeText(activity,
                toastString, Toast.LENGTH_SHORT).show();
    }

    /**
     * Home button click handler
     *
     * @param view
     */
    public void btnHomeClick(View view) {

        /**Intent intent = new Intent(Intent.ACTION_MAIN);
         intent.addCategory(Intent.CATEGORY_HOME);
         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         startActivity(intent); */
        finish();
    }
}
