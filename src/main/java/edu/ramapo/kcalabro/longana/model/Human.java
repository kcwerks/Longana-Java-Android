//     ************************************************************
//     * Name:  Kyle Calabro                                      *
//     * Project:  Longana - Java/Android Implementation          *
//     * Class:  CMPS 366 - Organization of Programming Languages *
//     * Date:  12/5/2017                                         *
//     ************************************************************

package edu.ramapo.kcalabro.longana.model;

import edu.ramapo.kcalabro.longana.view.RoundActivity;

public class Human extends Player
{

    /**
     * Constructor for the Human class.
     *
     * @param activity Object of the RoundActivity class.
     */

    public Human(RoundActivity activity)
    {
        this.activity = activity;
    }

    /**
     * For the human to place a tile.
     *
     * @param round Object of Round class representing the current round of Longana.
     * @param index The index of the tile in the Human's hand to be played.
     * @return Boolean value indicating if the given tile could be placed.
     */

    public boolean playerPlayTile(Round round, int index)
    {
        // First, verify that the human player has a tile that can be placed.
        if (canMakePlay(round))
        {
            Tile tileAtIndex = getPlayerHand().getTileAt(index);
            boolean computerPassed = round.getPlayers()[1].isPlayerPassed();
            Layout layout = round.getLayout();

            // If the tile is a double or the computer passed, a tile can be placed on either
            // side of the layout.
            if(tileAtIndex.isDouble() || computerPassed)
            {
                if(layout.validatePlacement(false, tileAtIndex) || layout.validatePlacement(true, tileAtIndex))
                {
                    // If the tile can indeed be placed on either side, prompt the user to decided
                    // which side of the layout they wish to place the tile on.

                    activity.askHumanForSide();
                    return true;
                }
            }

            // Otherwise, non-double tiles can only be placed on the left side of the layout
            // for the human player, if the desired tile can be placed, go ahead and place it.
            else
            {
                if(playNonDoubleTile(round, index))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * To determine if the human player has a tile that can be placed.
     *
     * @param round Round object representing the current round.
     * @return Boolean value indicating if the human player has a tile that can be placed in their hand.
     */

    public boolean canMakePlay(Round round)
    {
        String toastMessage;

        // Determine if the computer passed their turn.
        boolean computerPassed = round.getPlayers()[1].isPlayerPassed();

        // If the player cannot place a tile
        if (!round.getLayout().canPlaceTile(getPlayerHand(), true, computerPassed))
        {
            // If the boneyard is empty the human must pass their turn.
            if (round.getStock().getStockSize() == 0)
            {
                setPlayerPassed(true);
                round.swapTurns();

                activity.updateView(true);
                toastMessage = "There are no dominoes remaining in the boneyard, and no plays "
                        + "available for you to make!";
                activity.generateToastMessage(toastMessage);

                return false;
            }

            // Otherwise, attempt to draw a playable tile from the boneyard.
            else
            {
                if (drawTileAndAttempt(round))
                {
                    return true;
                }
            }
        }

        // The Human can place a tile.
        return true;
    }

    /**
     * To draw a tile from the boneyard and determine if it can placed.
     *
     * @param round Round object representing the current round.
     * @return Boolean value indicating if the drawn tile could be placed.
     */

    public boolean drawTileAndAttempt(Round round)
    {
        // Determine if the computer passed its turn.
        Boolean computerPassed = round.getPlayers()[1].isPlayerPassed();

        String toastMessage = "Drawing a tile from the boneyard!";
        activity.generateToastMessage(toastMessage);

        // Draw a tile from the boneyard and add it to the player's hand.
        getPlayerHand().addTile(round.getStock().draw());

        // Update the view so the user can actually see the newly drawn tile.
        activity.updateView(true);

        // Format output to display the tile to the user.
        toastMessage = getPlayerHand().getTileAt(getPlayerHand().getHandSize() - 1).printTileAsString()
                + " was drawn from the boneyard for you!";

        // Determine if the drawn tile can be placed, if so inform the user and return true.
        if (round.getLayout().canPlaceTile(getPlayerHand(), true, computerPassed))
        {
            toastMessage += " It can be placed.";
            activity.generateToastMessage(toastMessage);

            activity.updateView(true);

            return true;
        }

        // If the drawn tile cannot be placed, inform the user and return false.
        else
        {
            toastMessage += " It cannot be placed.";
            activity.generateToastMessage(toastMessage);

            setPlayerPassed(true);
            round.swapTurns();

            return false;
        }
    }

    /**
     * To verify that a given non-double tile can be placed as the human player instructed.
     *
     * @param round Round object representing the current round.
     * @param index The index of the tile in the human player's hand to verify the placement of.
     * @return Boolean value indicating if the given tile can be placed as desired.
     */

    public boolean playNonDoubleTile(Round round, int index)
    {
        // Non-double tiles can only be placed to the left unless the other player passed.
        // Verify that the given tile can be placed to the left.
        if(round.getLayout().validatePlacement(true, getPlayerHand().getTileAt(index)))
        {
            // Place the tile and let the computer play.
            round.getLayout().addToLeftSide(getPlayerHand().removeTile(index));
            activity.updateView(true);

            // The Human placed the tile, let the computer play...
            setPlayerPassed(false);
            round.swapTurns();

            return true;
        }
        else
        {
            String toastMessage = "That placement is not allowed!";
            activity.generateToastMessage(toastMessage);

            return false;
        }
    }

    /**
     * To determine if a given tile can be placed as the human player instructed.
     *
     * @param round Round object representing the current round.
     * @param index The index of the double in the human player's hand to verify the placement of.
     * @param leftPlacement Boolean value indicating if the double is to be placed to the left.
     * @return Boolean value indicating if the given double can be placed as desired.
     */

    public boolean playTileEitherSide(Round round, int index, boolean leftPlacement)
    {
        // If the user wishes to place a double to the left, verify that it can be placed on the left side.
        if(round.getLayout().validatePlacement(true, getPlayerHand().getTileAt(index)) && leftPlacement)
        {
            // Add the tile to the layout, and remove it from the player's hand.
            round.getLayout().addToLeftSide(getPlayerHand().removeTile(index));

            activity.updateView(true);

            // Since the player placed a tile, set the passed flag to false, and allow the computer to play.
            setPlayerPassed(false);
            round.swapTurns();

            return true;
        }

        // Otherwise, if the player wishes to place a double to the right, verify that it can be placed on the right side.
        else if(round.getLayout().validatePlacement(false, getPlayerHand().getTileAt(index)) && !leftPlacement)
        {
            // Add the tile to the layout, and remove it from the player's hand.
            round.getLayout().addToRightSide(getPlayerHand().removeTile(index));

            activity.updateView(true);

            // Since the player placed a tile, set the passed flag to false, and allow the computer to play.
            setPlayerPassed(false);
            round.swapTurns();

            return true;
        }

        // Otherwise, the tile could not be placed on the desired side, indicating an invalid move.
        else
        {
            String toastMessage = "That placement is invalid!";
            activity.generateToastMessage(toastMessage);

            return false;
        }
    }

    /**
     * The help mode for Longana.
     *
     * @param round Round object representing the current round.
     * @return Boolean value indicating if the player could place a tile.
     */

    public boolean helpMode(Round round)
    {
        // If the Human cannot place a tile the help mode is of no use.
        if(!canMakePlay(round))
        {
            String toastMessage = "Sorry, computer could not be of assistance!";
            activity.generateToastMessage(toastMessage);
            return false;
        }

        // Determines if the computer passed its play.
        boolean computerPassed = round.getPlayers()[1].isPlayerPassed();

        // The index of the tile with the highest sum of pips.
        int index = 0;

        // Keeps track of the highest sum of pips for tiles.
        int maxDominoSum = 0;

        boolean leftPlacement = true;

        // Traverse the player's hand, find the domino with the highest sum
        // of pips that can be placed.
        for(int i = 0; i < getPlayerHand().getHandSize(); i++)
        {
            Tile tileAtIndex = getPlayerHand().getTileAt(i);

            // If the domino is a double or the computer passed its play, the human
            // can place a domino on either side of the layout.
            if(tileAtIndex.isDouble() || computerPassed)
            {
                // Check if the domino can be placed to the left.
                if(round.getLayout().validatePlacement(true, tileAtIndex))
                {
                    if(tileAtIndex.calculateTileSum() > maxDominoSum)
                    {
                        index = i;
                        maxDominoSum = tileAtIndex.calculateTileSum();
                    }
                }

                // Check that the domino can be placed to the right.
                if(round.getLayout().validatePlacement(false, tileAtIndex))
                {
                    if(tileAtIndex.calculateTileSum() > maxDominoSum)
                    {
                        index = i;
                        maxDominoSum = tileAtIndex.calculateTileSum();
                        leftPlacement = false;
                    }
                }
            }

            // If the domino is not a double and the computer did not pass its play,
            // the human can only place a domino on the left side of the layout.
            else
            {
                // Check the domino can be placed to the left.
                if(round.getLayout().validatePlacement(true, tileAtIndex))
                {
                    if(tileAtIndex.calculateTileSum() > maxDominoSum)
                    {
                        index = i;
                        maxDominoSum = tileAtIndex.calculateTileSum();
                    }
                }
            }
        }

        // The tile to recommend to the user.
        Tile tileToRecommend = getPlayerHand().getTileAt(index);

        // Recommend the tile to be placed to the left.
        if(leftPlacement)
        {
            String toastMessage = "The computer recommends placing " + tileToRecommend.printTileAsString()
                    +" on the left, as that domino has the highest sum of pips in your hand!";
            activity.generateToastMessage(toastMessage);
        }

        // Otherwise, recommend the tile to be placed to the right.
        else
        {
            String toastMessage = "The computer recommends placing " + tileToRecommend.printTileAsString()
                    + " on the right, as that domino has the highest sum of pips in your hand!";
            activity.generateToastMessage(toastMessage);
        }

        // The human player is able to place a tile and a recommendation has been given.
        return true;
    }
}
