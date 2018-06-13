//     ************************************************************
//     * Name:  Kyle Calabro                                      *
//     * Project:  Longana - Java/Android Implementation          *
//     * Class:  CMPS 366 - Organization of Programming Languages *
//     * Date:  12/5/2017                                         *
//     ************************************************************


package edu.ramapo.kcalabro.longana.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import edu.ramapo.kcalabro.longana.R;
import edu.ramapo.kcalabro.longana.model.Tournament;

public class EndRoundActivity extends AppCompatActivity {

    //------------------------Data Members------------------------

    public final static String EXTRA_NEWROUND = "edu.ramapo.kcalabro.longana.newRound";

    private int tournamentScoreLimit;
    private int engine;
    private int roundNumber;
    private int computerTournScore;
    private int humanTournScore;
    private int humanRoundScore;
    private int computerRoundScore;

    private TextView computerTournScoreView;
    private TextView humanTournScoreView;
    private TextView computerRoundScoreView;
    private TextView humanRoundScoreView;
    private TextView winnerView;
    private TextView tournScoreLimitView;

    private Button newRoundButton;

    //------------------------Member Functions------------------------

    /**
     * onCreate function for the EndRoundActivity class.
     *
     * @param savedInstanceState Bundle containing pertinent data.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_end_round);
        Bundle bundle = getIntent().getExtras();

        newRoundButton = (Button) findViewById(R.id.newRoundButton);
        newRoundButton.setOnClickListener(newRoundButtonListener);

        computerTournScoreView = (TextView) findViewById(R.id.computerTournamentScore);
        humanTournScoreView = (TextView) findViewById(R.id.humanTournamentScore);

        computerRoundScoreView = (TextView) findViewById(R.id.computerRoundScore);
        humanRoundScoreView = (TextView) findViewById(R.id.humanRoundScore);

        tournScoreLimitView = (TextView) findViewById(R.id.tournamentScoreLimit);

        winnerView = (TextView) findViewById(R.id.winner);

        computerRoundScore = bundle.getInt("computerRoundScore", 0);
        humanRoundScore = bundle.getInt("humanRoundScore", 0);

        computerTournScore = bundle.getInt("computerTournamentScore", 0);
        humanTournScore = bundle.getInt("humanTournamentScore", 0);

        roundNumber = bundle.getInt("roundNumber", 1);
        engine = bundle.getInt("engine", 6);

        tournamentScoreLimit = bundle.getInt("tournamentScoreLimit", 0);

        updateScores();
    }

    /**
     * Listener function for the newRound button in the activity_end_round layout.
     */

    View.OnClickListener newRoundButtonListener = (new View.OnClickListener() {
        public void onClick(View view) {
            Intent intent = new Intent(EndRoundActivity.this, RoundActivity.class);

            // To begin a new round, push through all the pertinent data so that it can be loaded
            // into the new round, and the tournament can continue.
            intent.putExtra("computerTournamentScore", computerTournScore);
            intent.putExtra("humanTournamentScore", humanTournScore);
            intent.putExtra("tournamentScoreLimit", tournamentScoreLimit);
            intent.putExtra("roundNumber", roundNumber + 1);
            intent.putExtra("engine", engine);
            intent.putExtra(EXTRA_NEWROUND, true);
            startActivity(intent);
            finish();
        }
    });

    /**
     * To determine the winner of the round.
     *
     * @return String representing the winner of the round; Human | Computer | Draw
     */

    public String determineWinner()
    {
        // The score's represent the sum of pips from the other player's hand.
        if(humanRoundScore > computerRoundScore)
        {
            return "Human";
        }
        else if(computerRoundScore > humanRoundScore)
        {
            return "Computer";
        }
        else
        {
            return "Draw";
        }
    }

    /**
     * To determine if the Longana tournament has ended.
     */

    public void hasTournamentEnded()
    {
        Intent intent = new Intent(EndRoundActivity.this, WinningActivity.class);
        intent.putExtra("computerTournamentScore", computerTournScore);
        intent.putExtra("humanTournamentScore", humanTournScore);
        intent.putExtra("winner", determineWinner());
        intent.putExtra("tournamentScoreLimit", tournamentScoreLimit);

        if(humanTournScore >= tournamentScoreLimit)
        {
            startActivity(intent);
            finish();
        }
        else if(computerTournScore >= tournamentScoreLimit)
        {
            startActivity(intent);
            finish();
        }
    }

    /**
     * To update the view to properly display the scores of the round.
     */

    public void updateScores()
    {
        winnerView.setText("Round Winner: " + determineWinner());
        hasTournamentEnded();

        tournScoreLimitView.setText("Tournament Score Limit: " + tournamentScoreLimit);
        computerRoundScoreView.setText("Computer Round Score: " + computerRoundScore);
        humanRoundScoreView.setText("Human Round Score: " + humanRoundScore);
        computerTournScoreView.setText("Computer Tournament Score: " + computerTournScore);
        humanTournScoreView.setText("Human Tournament Score: " + humanTournScore);

    }
}
