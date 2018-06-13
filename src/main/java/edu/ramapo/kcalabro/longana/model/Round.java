//     ************************************************************
//     * Name:  Kyle Calabro                                      *
//     * Project:  Longana - Java/Android Implementation          *
//     * Class:  CMPS 366 - Organization of Programming Languages *
//     * Date:  12/5/2017                                         *
//     ************************************************************

package edu.ramapo.kcalabro.longana.model;

import android.os.Environment;

import android.support.v4.app.ActivityCompat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;

import edu.ramapo.kcalabro.longana.view.RoundActivity;

public class Round
{
    //------------------------Data Members------------------------

    private RoundActivity activity;

    // An array of players for Longana.
    private Player[] players;

    // The double-six set stock of dominoes for Longana.
    private Stock stock;

    private Tournament tournament;

    // The layout in a round of Longana.
    private Layout layout;

    // The round number in the Longana tournament.
    private int roundNumber;

    // The maximum score of the tournament.
    private int tournamentScore;

    // The current player of the round.
    private String currentPlayer;

    // The engine for the current round of Longana.
    private int engine;

    // The current player of Longana, 0 for Human, 1 for Computer
    private int turn;

    //------------------------Member Functions------------------------

    /**
     * Default constructor for the round class.
     */

    public Round(RoundActivity activity)
    {
        this.activity = activity;
        tournament = new Tournament();
        stock = new Stock();
        layout = new Layout();
        players = new Player[2];
        players[0] = new Human(activity);
        players[1] = new Computer(activity);
        roundNumber = 1;
        turn = 0;
    }

    /**
     * To get the Tournament object of the Round.
     *
     * @return Tournament class object representing the current Tournament of Longana.
     */

    public Tournament getTournament()
    {
        return tournament;
    }

    /**
     * To draw the initial hand's of both the human and computer player from the stock.
     */

    public void drawPlayerHands()
    {
        for(int i = 0; i < 8; i++)
        {
            for (int y = 0; y < 2; y++)
            {
                players[y].appendToHand(stock.draw());
            }
        }
    }


    /**
     * To draw a single domino for each player from the boneyard.
     */

    public void dualDrawFromBoneyard()
    {
        players[0].appendToHand(stock.draw());
        players[1].appendToHand(stock.draw());
    }

    /**
     * To display both player's hand's to the terminal window.
     */

    public void displayPlayerHands()
    {
        for(int i = 0; i < 2; i++)
        {
            players[i].getPlayerHand().printHand();
        }
    }

    /**
     * To determine which player holds the engine at the beginning of a round.
     *
     * @return A string representing which player holds the engine; computer, engine, or draw.
     */

    public String determineFirstPlayer()
    {
        // The engine based on the current round.
       int engine = determineEngine();

       // Traverse the players array.
       for(int i = 0; i < 2; i++)
       {
           // Traverse each players hand.
           for(int y = 0; y < players[i].getPlayerHand().getHandSize(); y++)
           {
               // The tile at the current index of a player's hand.
               Tile playerTile = players[i].getPlayerHand().getTileAt(y);

               // First check that the tile is a double.
               if(playerTile.isDouble())
               {
                   // Then check if one of the pips matches the engine.
                   if(playerTile.getLeftPip() == engine)
                   {
                       // If the human player holds the engine.
                       if(i == 0)
                       {
                           // Add the engine to the layout
                           Tile tileToBePlaced = players[i].getPlayerHand().removeTile(y);
                           layout.addToLeftSide(tileToBePlaced);

                           layout.displayLayout();
                           displayPlayerHands();

                           setCurrentPlayer("computer");

                           // Let the computer play.
                           setTurn(0);

                           return "human";
                       }

                       // Otherwise, the computer player has the engine.
                       else
                       {
                           // Add the engine to the layout.
                           Tile tileToBePlaced = players[i].getPlayerHand().removeTile(y);
                           layout.addToLeftSide(tileToBePlaced);

                           layout.displayLayout();
                           displayPlayerHands();

                           setCurrentPlayer("human");

                           // Let the human play.
                           setTurn(1);

                           return "computer";
                       }
                   }
               }
           }
       }

       // If neither player had the engine, they must both draw from the boneyard.
       return "draw";
    }

    /**
     * To determine the winner of a round of Longana and add to the player's score appropriately.
     */

    public void determineWinner()
    {
        // The sum of pips for both player's hands.
        int humanHandSum = players[0].getPlayerHand().calculateHandSum();
        int computerHandSum = players[1].getPlayerHand().calculateHandSum();

        // If the human player's hand sum is less than that of the computer's hand:
        // The human player earns the computer's hand sum as its score for the round.
        if(humanHandSum < computerHandSum)
        {
            players[0].setRoundScore(computerHandSum);
            players[0].setTournamentScore(players[0].getTournamentScore() + computerHandSum);
        }

        // If the computer's hand sum is less than that of the human player's hand:
        // The computer player earns the human player's hand sum as its score for the round.
        else if (humanHandSum > computerHandSum)
        {
            players[1].setRoundScore(humanHandSum);
            players[1].setTournamentScore(players[1].getTournamentScore() + humanHandSum);
        }

        // Otherwise the computer and human player's hand sum is equivalent:
        // And the round is therefore a draw, neither player earns points.
        else
        {
        }
    }

    /**
     * To determine whether or not a round of Longana has ended.
     *
     * @return A boolean value indicating if the round has ended.
     */

    public boolean hasRoundEnded()
    {
        // Check if one of the player's has emptied their hands.
        if(players[0].getPlayerHand().isEmpty() || players[1].getPlayerHand().isEmpty())
        {
            determineWinner();
            return true;
        }

        // Check to see if the boneyard is empty and both player's have passed.
        else if (players[0].isPlayerPassed() && players[1].isPlayerPassed() && stock.getStockSize() == 0)
        {
            determineWinner();
            return true;
        }

        // Otherwise, the round is still ongoing.
        return false;
    }

    /**
     * To get the array of players for Longana.
     *
     * @return An array of Player objects.
     */

    public Player[] getPlayers()
    {
        return players;
    }

    /**
     * To set the round number for the tournament of Longana.
     *
     * @param roundNum The value to set the round number to in a tournament of Longana.
     */

    public void setRoundNumber(int roundNum)
    {
        this.roundNumber = roundNum;
    }

    /**
     * To get the current round number for a tournament of Longana.
     *
     * @return An integer representing the current round number of the tournament.
     */

    public int getRoundNumber()
    {
        return roundNumber;
    }

    /**
     * To get the current the current player.
     *
     * @return An integer value indicating the current player.
     */

    public int getTurn()
    {
        return turn;
    }

    /**
     * To set the turn, current player.
     *
     * @param turn The value to set the turn variable to.
     */

    public void setTurn(int turn)
    {
        this.turn = turn;
    }

    /**
     * To swap turns, current player.
     */

    public void swapTurns()
    {
        if(turn == 0)
        {
            turn = 1;
        }
        else
        {
            turn = 0;
        }
    }

    /**
     * To get the maximum score of the tournament for Longana.
     *
     * @return An integer value representing the maximum score of the tournament.
     */

    public int getTournamentScore() {
        System.out.println("Tournament Score: " + tournamentScore);
        return tournamentScore; }

    /**
     * To set the maximum tournament score for Longana.
     *
     * @param tournScore The value to set the tournament score to.
     */

    public void setTournamentScore(int tournScore) { tournamentScore = tournScore; }

    /**
     * To determine what the engine is depending on the round number.
     *
     * @return An integer value representing the engine for the current round.
     */

    public int determineEngine()
    {
        int num = (roundNumber - 1) % 7;
        num = 6 - num;

        return num;
    }

    public void setEngine(int newEngine)
    {
        this.engine = newEngine;
    }

    /**
     * To get the current layout in a round of Longana.
     *
     * @return A layout object, representing the current layout.
     */

    public Layout getLayout()
    {
        return layout;
    }

    /**
     * To get the stock/boneyard of Longana.
     *
     * @return A stock object representing the stock/boneyard.
     */

    public Stock getStock()
    {
        return stock;
    }

    /**
     * To set the current player in a round of Longana.
     *
     * @param player The value to set the current player to.
     */

    public void setCurrentPlayer(String player)
    {
        this.currentPlayer = player;
    }

    /**
     * To get the current player in a round of Longana.
     *
     * @return A string indicating the current player (computer or human).
     */

    public String getCurrentPlayer()
    {
        return currentPlayer;
    }

    /**
     * To determine if external storage is writable.
     *
     * @return A boolean value indicating if external storage is writable.
     */

    private boolean isExternalStorageWritable()
    {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) return true;
        return false;
    }

    /**
     * To determine if external storage is readable.
     *
     * @return A boolean value indicating if external storage is readable.
     */

    private boolean isExternalStorageReadable()
    {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) return true;
        return false;
    }
}
