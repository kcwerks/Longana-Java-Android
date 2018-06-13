//     ************************************************************
//     * Name:  Kyle Calabro                                      *
//     * Project:  Longana - Java/Android Implementation          *
//     * Class:  CMPS 366 - Organization of Programming Languages *
//     * Date:  12/5/2017                                         *
//     ************************************************************

package edu.ramapo.kcalabro.longana.model;

import edu.ramapo.kcalabro.longana.view.RoundActivity;


public class Player
{
    //------------------------Data Members------------------------

    public RoundActivity activity;

    // A player's hand.
    private Hand playerHand;

    // The tournament score for a tournament of Longana.
    private int tournamentScore;

    // The round score for the current round of a tournament.
    private int roundScore;

    // Whether or not the previous player passed their play.
    private boolean playerPassed;

    //------------------------Member Functions------------------------

    /**
     * Default constructor for the Player class.
     */

    public Player()
    {
        playerHand = new Hand();
        roundScore = 0;
        tournamentScore = 0;
        playerPassed = false;
    }

    /**
     * To get a player's current hand.
     *
     * @return A Hand object representing a player's current hand.
     */

    public Hand getPlayerHand()
    {
        return playerHand;
    }

    /**
     * To append a tile to a player's hand.
     *
     * @param tileToAdd the tile to append to the player's hand.
     */

    public void appendToHand(Tile tileToAdd)
    {
        playerHand.addTile(tileToAdd);
    }

    /**
     * To get the tournament score of a player.
     *
     * @return The tournament score of a player.
     */

    public int getTournamentScore()
    {
        return tournamentScore;
    }

    /**
     * To set the tournament score of a player.
     *
     * @param tournamentScore The value to set the player's score in the tournament to.
     */

    public void setTournamentScore(int tournamentScore)
    {
        this.tournamentScore = tournamentScore;
    }

    /**
     * To get the round score of a player.
     *
     * @return The round score of a player.
     */

    public int getRoundScore()
    {
        return roundScore;
    }

    /**
     * To set the round score of a player.
     *
     * @param roundScore The value to set the round score of a player to.
     */

    public void setRoundScore(int roundScore)
    {
        this.roundScore = roundScore;
    }

    /**
     * To set the playerPassed flag.
     *
     * @param passed A boolean value indicating whether or not the player passed.
     */

    public void setPlayerPassed(boolean passed)
    {
        this.playerPassed = passed;
    }


    /**
     * To determine if the player passed their previous play.
     *
     * @return A boolean value indicating if the player passed their previous play.
     */

    public boolean isPlayerPassed()
    {
        return playerPassed;
    }


    /**
     * Virtual Function definition for the helpMode of Longana
     *
     * @param round
     * @return
     */
    public boolean helpMode(Round round)
    {
        return true;
    }

    /**
     * Virtual function definition for the playTileEitherSide function.
     *
     * @param round Object of the round class representing the current round.
     * @param index The index of the tile to be placed by a Player.
     * @param leftPlacement Boolean value indicating left of right placement.
     * @return Boolean value indicating if the given tile can be placed.
     */
    public boolean playTileEitherSide(Round round, int index, boolean leftPlacement)
    {
        return false;
    }

    /**
     * Virtual function definition for the playerPlayTile function.
     *
     * @param round Object of the round class representing the current round.
     * @param index The index of the tile to be placed by a player.
     * @return Boolean value indicating if the player could place the given tile.
     */
    public boolean playerPlayTile(Round round, int index)
    {
        return false;
    }
}
