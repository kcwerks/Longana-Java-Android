//     ************************************************************
//     * Name:  Kyle Calabro                                      *
//     * Project:  Longana - Java/Android Implementation          *
//     * Class:  CMPS 366 - Organization of Programming Languages *
//     * Date:  12/5/2017                                         *
//     ************************************************************

package edu.ramapo.kcalabro.longana.view;

import android.content.DialogInterface;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.content.Intent;
import android.view.Gravity;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.ramapo.kcalabro.longana.R;
import edu.ramapo.kcalabro.longana.model.Round;
import edu.ramapo.kcalabro.longana.model.Tournament;

import java.io.IOException;

public class RoundActivity extends AppCompatActivity
{
    //------------------------Data Members------------------------

    // The round to be played.
    private Round round;
    private Tournament tournament;

    private int indexToPlay;

    private HumanView humanView;
    private ComputerView computerView;
    private LayoutView layoutView;

    private Button playButton;
    private Button saveButton;
    private Button helpButton;

    private TextView turnView;
    private TextView humanTournamentScore;
    private TextView computerTournamentScore;
    private TextView tournamentScoreLimit;
    private TextView boneyardSize;
    private TextView playerPassed;
    private TextView roundNumberView;

    //------------------------Member Functions------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Removes the tile bar, for aesthetic purposes.
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round);

        humanView = new HumanView(this);
        computerView = new ComputerView(this);
        layoutView = new LayoutView(this);

        roundNumberView = findViewById(R.id.roundNumber);
        boneyardSize = findViewById(R.id.numTilesRemaining);
        playerPassed = findViewById(R.id.playerPassed);
        turnView = findViewById(R.id.currentPlayer);
        humanTournamentScore = findViewById(R.id.humanScore);
        computerTournamentScore = findViewById(R.id.computerScore);
        tournamentScoreLimit = findViewById(R.id.tournamentScore);

        playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(playButtonListener);

        helpButton = findViewById(R.id.helpButton);
        helpButton.setOnClickListener(helpButtonHandler);

        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(saveButtonListener);

        // Determine if the user wants to start a new game
        // Or load a saved game.
        Intent mainIntent = getIntent();
        boolean newRound = mainIntent.getExtras().getBoolean(MainActivity.EXTRA_NEWROUND);

        // If newRound is set to true.
        if (newRound)
        {
            round = new Round(this);
            tournament = new Tournament();
            round.drawPlayerHands();
            getExtras();

            // If the round number equals 1 it is a new tournament,
            // so we must ask the user for the tournament score.
            if(round.getRoundNumber() == 1)
            {
                setTournamentScore();
                round.getTournamentScore();
            }

            // Otherwise, the hands have been drawn and we must determine who will
            // make the first play.
            else
            {
                determineFirstPlay();
            }

            releaseButtons();
            updateView(true);
        }

        // Otherwise, the user wishes to load a serialized round from a file.
        else
        {
            round = new Round(this);
            round.getStock().emptyStock();

            resumeGame();
        }
    }

    //------------------------Output/Display Updating Functions------------------------

    /**
     * To display a toast message to the screen.
     *
     * @param message The String to be displayed to the screen via toast.
     */

    public void generateToastMessage(String message)
    {
        Toast toastToDisplay = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
        toastToDisplay.setGravity(Gravity.CENTER, 0, 0);
        toastToDisplay.show();
    }

    /**
     * To update the RoundActivity view/activity_round layout.
     *
     * @param isActive Boolean value indicating if the view is enabled.
     */

    public void updateView(boolean isActive)
    {
        // Determine if it is the human player's turn.
        if(round.getTurn() == 0)
        {
            turnView.setText("Current Player: Human");

            // Determine if the computer player passed its previous play.
            if(round.getPlayers()[1].isPlayerPassed())
            {
                playerPassed.setText("Previous Player Passed: Yes");
            }
            else
            {
                playerPassed.setText("Previous Player Passed: No");
            }
        }

        // Otherwise, it is the computer player's turn.
        else
        {
            turnView.setText("Current Player: Computer");

            // Determine if the human player passed their previous play.
            if(round.getPlayers()[0].isPlayerPassed())
            {
                playerPassed.setText("Previous Player Passed: Yes");
            }
            else
            {
                playerPassed.setText("Previous Player Passed: No");
            }
        }

        // Update the views utilizing TileView to display proper tiles.
        layoutView.updateLayoutView(round.getLayout(), isActive);
        humanView.updateHumanView(round.getPlayers()[0].getPlayerHand(), isActive);
        computerView.updateComputerView(round.getPlayers()[1].getPlayerHand(), isActive);

        // Update all the TextView's to display proper information.
        roundNumberView.setText("Round Number: " + round.getRoundNumber());
        boneyardSize.setText("Tiles Remaining in Boneyard: " + Integer.toString(round.getStock().getStockSize()));
        tournamentScoreLimit.setText("Tournament Score Limit: " + round.getTournamentScore());
        computerTournamentScore.setText("Computer's Tournament Score: " + round.getPlayers()[0].getTournamentScore());
        humanTournamentScore.setText("Your Tournament Score: " + round.getPlayers()[1].getTournamentScore());
    }

    //------------------------Game-Driver Functions------------------------

    /**
     * To enable the buttons of the activity_round layout.
     */

    public void releaseButtons()
    {
        saveButton.setEnabled(true);
        playButton.setEnabled(true);
        helpButton.setEnabled(true);
    }

    /**
     * To prompt the human player for a side to place a tile on, only called when computer has passed
     * or selected tile is a double.
     */

    public void askHumanForSide()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Select a side of the layout:");
        dialogBuilder.setPositiveButton("Right", sideSelectorListener);
        dialogBuilder.setNegativeButton("Left", sideSelectorListener);
        dialogBuilder.create().show();
    }

    /**
     * To prompt the user to enter a score limit for the tournament and set the Round class data member.
     */

    private void setTournamentScore()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        final EditText tournScore = new EditText(RoundActivity.this);

        dialogBuilder.setTitle("Tournament Score");
        dialogBuilder.setMessage("Please enter the max score of the tournament:");
        dialogBuilder.setView(tournScore);

        // Take in the tournament score from the user.
        dialogBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                round.setTournamentScore(Integer.parseInt(tournScore.getText().toString()));
                determineFirstPlay();
            }
        });

        // Display the dialog to the user.
        AlertDialog inputBox = dialogBuilder.create();
        inputBox.setCanceledOnTouchOutside(false);
        inputBox.show();
    }

    /**
     * To determine which player holds the engine and inform the user of such information.
     */

    private void determineFirstPlay()
    {
        String firstPlayer = round.determineFirstPlayer();
        String result = "";

        // If necessary draw from the boneyard for both players.
        while (firstPlayer.equals("draw"))
        {
            round.dualDrawFromBoneyard();
            firstPlayer = round.determineFirstPlayer();
        }

        if (firstPlayer.equals("human"))
        {
            result += "You had the engine, and played first!";

            // Let the computer play now...
            round.setTurn(1);
        }

        // Otherwise, the computer player had the engine.
        else
        {
            result += "Computer player had the engine, played first!";

            // Let the human player play now...
            round.setTurn(0);
        }

        Toast firstPlayerToast = Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG);
        firstPlayerToast.setGravity(Gravity.CENTER, 0, 0);
        firstPlayerToast.show();
        firstPlayerToast.show();

        updateView(true);
        releaseButtons();
    }

    //------------------------Data Manipulation Functions------------------------

    /**
     * To get information from the bundle passed in by an EndRoundActivity object, and update the
     * Round object with that data so the tournament can continue properly.
     */

    public void getExtras()
    {
        Bundle bundle = getIntent().getExtras();
        round.getPlayers()[1].setTournamentScore(bundle.getInt("humanTournamentScore", 0));
        round.getPlayers()[0].setTournamentScore(bundle.getInt("computerTournamentScore", 0));

        round.setTournamentScore(bundle.getInt("tournamentScoreLimit", 0));
        round.setRoundNumber(bundle.getInt("roundNumber", round.getRoundNumber()));
        round.setEngine(bundle.getInt("engine", 6));
    }

    /**
     * To place data in a bundle to be read from by an EndRoundActivity object.
     */

    public void endActivity()
    {
        Intent endRound = new Intent(RoundActivity.this, EndRoundActivity.class);
        endRound.putExtra("computerRoundScore", round.getPlayers()[1].getRoundScore());
        endRound.putExtra("computerTournamentScore", round.getPlayers()[1].getTournamentScore());

        endRound.putExtra("humanRoundScore", round.getPlayers()[0].getRoundScore());
        endRound.putExtra("humanTournamentScore", round.getPlayers()[0].getTournamentScore());

        endRound.putExtra("tournamentScoreLimit", round.getTournamentScore());
        endRound.putExtra("roundNumber", round.getRoundNumber());
        endRound.putExtra("engine", round.determineEngine());

        startActivity(endRound);
        finish();
    }

    /**
     * To load a round from a serialized file as entered by the user when prompted.
     */

    private void resumeGame()
    {
        // Build the dialog for the user to enter the filename.
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final EditText fileInput = new EditText(RoundActivity.this);
        dialogBuilder.setTitle("Filename");
        dialogBuilder.setMessage("Please enter the name of the file to be restored: ");
        dialogBuilder.setView(fileInput);

        // Take in the filename from the user.
        dialogBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String filename = fileInput.getText().toString();

                // Restore the file...
                if(round.getTournament().getSerializer().restoreFile(round, filename))
                {
                    // If the round has tiles placed on it, there is no need to draw for the engine.
                    if(round.getLayout().getLayoutSize() > 0)
                    {
                        releaseButtons();
                        updateView(true);
                    }

                    // Otherwise, we must draw for the engine.
                    else
                    {
                        determineFirstPlay();
                        releaseButtons();
                        updateView(true);
                    }
                }

                // If the file does not exist inform the user.
                else
                {
                    generateToastMessage("That file is not valid!");
                    resumeGame();
                }
            }
        });

        // Display the dialog to the user
        AlertDialog inputBox = dialogBuilder.create();
        inputBox.setCanceledOnTouchOutside(false);
        inputBox.show();
    }

    //------------------------Button Listeners & Handlers------------------------

    /**
     * To handle when the user clicks the save button.
     */

    View.OnClickListener saveButtonListener = (new View.OnClickListener() {
       @Override
       public void onClick(View view)
       {
            try
            {
                // Attempt to save the file.
                round.getTournament().getSerializer().serializeFile(round);
            }
            catch(IOException ioException)
            {
                ioException.printStackTrace();
            }
            finish();
       }
    });

    /**
     * To handle when the user clicks the play button.
     */

    View.OnClickListener playButtonListener = (new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {

            // If it's the computer player's turn.
            if(round.getTurn() == 1)
            {
                // Have the computer make a move...
                round.getPlayers()[1].playerPlayTile(round, 0);

                if(round.hasRoundEnded())
                {
                    endActivity();
                }

                updateView(true);
            }
        }
    });

    /**
     * To handle when the user clicks the help button.
     */

    View.OnClickListener helpButtonHandler = (new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            // If it's the human player's turn give them a recommendation.
            if(round.getTurn() == 0)
            {
                round.getPlayers()[0].helpMode(round);
            }

            // Otherwise, inform the player it's the computer's turn.
            else
            {
                generateToastMessage("It's the computer's turn!");
            }
        }
    });

    /**
     * To handle when the user clicks on a tile in their hand.
     */

    View.OnClickListener tileButtonHandler = (new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            // Traverse the human player's hand...
            for(int i = 0; i < round.getPlayers()[0].getPlayerHand().getHandSize(); i++)
            {
                // Look for the tile in the player's hand that matches the id of the
                // tile they clicked from the TileView.
                if(humanView.getView().elementAt(i).getTileView().getId() == view.getId())
                {
                    // If it's the human player's turn...
                    if(round.getTurn() == 0)
                    {
                        // Attempt to place the tile.
                        round.getPlayers()[0].playerPlayTile(round, i);
                        indexToPlay = i;
                        if(round.hasRoundEnded())
                        {
                            endActivity();
                        }
                        updateView(true);
                    }

                    // Otherwise it's the computer's turn and the human should not be playing.
                    else
                    {
                        generateToastMessage("Sorry, it is the computer's turn."
                                + " Press play for the computer's play!");
                    }
                }
            }
        }
    });

    /**
     * To prompt the user to select a side to place a tile on.
     */

    DialogInterface.OnClickListener sideSelectorListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i)
        {
            // The Alert Dialog box is only ever displayed if the selected tile
            // is a double or the computer passed its play, in both cases the tile can be placed
            // on either side of the layout (assuming the tile is valid).
            switch(i)
            {
                // For placement to the right of the layout.
                case DialogInterface.BUTTON_NEGATIVE:
                    round.getPlayers()[0].playTileEitherSide(round, indexToPlay, true);
                    break;

                // For placement to the left of the layout.
                case DialogInterface.BUTTON_POSITIVE:
                    round.getPlayers()[0].playTileEitherSide(round, indexToPlay, false);
                    break;
            }

            if(round.hasRoundEnded())
            {
                endActivity();
            }

            updateView(true);
        }
    };
}
