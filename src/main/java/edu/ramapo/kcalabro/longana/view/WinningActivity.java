//     ************************************************************
//     * Name:  Kyle Calabro                                      *
//     * Project:  Longana - Java/Android Implementation          *
//     * Class:  CMPS 366 - Organization of Programming Languages *
//     * Date:  12/5/2017                                         *
//     ************************************************************

package edu.ramapo.kcalabro.longana.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import android.content.Intent;

import edu.ramapo.kcalabro.longana.R;

public class WinningActivity extends AppCompatActivity
{
    //------------------------Data Members------------------------

    private Button newTournamentButton;

    private int humanTournamentScore;
    private int computerTournamentScore;
    private int tournamentScoreLimit;

    private String winner;

    private TextView computerTournamentScoreView;
    private TextView humanTournamentScoreView;
    private TextView winnerView;
    private TextView tournamentScoreLimitView;

    //------------------------Member Functions------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_winner);

       Bundle bundle = getIntent().getExtras();
       newTournamentButton = findViewById(R.id.playButton);
       humanTournamentScoreView = findViewById(R.id.humanTournamentScore);
       computerTournamentScoreView = findViewById(R.id.computerTournamentScore);
       winnerView = findViewById(R.id.winner);
       tournamentScoreLimitView = findViewById(R.id.tournamentScoreLimit);

       winner = bundle.getString("winner", "draw");
       computerTournamentScore = bundle.getInt("computerTournamentScore", 0);
       humanTournamentScore = bundle.getInt("humanTournamentScore", 0);
       tournamentScoreLimit = bundle.getInt("tournamentScoreLimit", 0);

       newTournamentButton.setOnClickListener(newTournamentButtonListener);

       updateScores();
    }

    /**
     * To update the TextViews of the activity_winner layout to display the proper information.
     */

    public void updateScores()
    {
        winnerView.setText("Tournament Winner: " + winner);

        computerTournamentScoreView.setText("Computer Tournament Score: " + computerTournamentScore);
        humanTournamentScoreView.setText("Human Tournament Score: " + humanTournamentScore);
        tournamentScoreLimitView.setText("Tournament Score Limit: " + tournamentScoreLimit);
    }

    /**
     * To handle when the user clicks on the New Tournament button, launches activivty_main/MainActivity.
     */

    View.OnClickListener newTournamentButtonListener = (new View.OnClickListener()
    {
        public void onClick(View view)
        {
            Intent intent = new Intent(WinningActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    });

}
