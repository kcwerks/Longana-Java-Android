//     ************************************************************
//     * Name:  Kyle Calabro                                      *
//     * Project:  Longana - Java/Android Implementation          *
//     * Class:  CMPS 366 - Organization of Programming Languages *
//     * Date:  12/5/2017                                         *
//     ************************************************************


package edu.ramapo.kcalabro.longana.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import edu.ramapo.kcalabro.longana.R;

public class MainActivity extends AppCompatActivity {
    //------------------------Data Members------------------------

    public final static String EXTRA_NEWROUND = "edu.ramapo.kcalabro.longana.newRound";

    //------------------------Member Functions------------------------

    /**
     * Button Handler for the New Game button in the activity_main layout.
     *
     * @param view The current view.
     */

    public void startNewGame(View view)
    {
        // Set the intent to the RoundActivity class.
        Intent intent = new Intent(this, RoundActivity.class);

        // Set the newround flag to true.
        intent.putExtra(EXTRA_NEWROUND, true);

        // Start the activity.
        startActivity(intent);
    }

    public void loadGame(View view)
    {
        // Set the intent to the RoundActivity class.
        Intent intent = new Intent(this, RoundActivity.class);

        // Set the newround flag to false, player wishes to load a game.
        intent.putExtra(EXTRA_NEWROUND, false);

        // Start the activity.
        startActivity(intent);
    }

    /**
     * To handle permissions stuff.
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case 0:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                }
                else
                {
                }
                return;
            }
        }
    }

    /**
     * To handle permissions stuff for external storage readability/writability.
     *
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int permResponse = 0;

        // Check if we have read and write permissions.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]
                    {
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, permResponse);
        }
    }
}

