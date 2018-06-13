//     ************************************************************
//     * Name:  Kyle Calabro                                      *
//     * Project:  Longana - Java/Android Implementation          *
//     * Class:  CMPS 366 - Organization of Programming Languages *
//     * Date:  12/5/2017                                         *
//     ************************************************************

package edu.ramapo.kcalabro.longana.model;

import edu.ramapo.kcalabro.longana.view.RoundActivity;

public class Computer extends Player
{
    /**
     * Constructor for the Computer class.
     *
     * @param activity Object of the RoundActivity class.
     */

    public Computer(RoundActivity activity)
    {
        this.activity = activity;
    }

    /**
     * To determine if the computer player can make a play with its current hand.
     *
     * @param round Round object representing the current round.
     * @return Boolean value indicating if the computer player is able to make a play.
     */

    public boolean canMakePlay(Round round)
    {
        boolean humanPassed = round.getPlayers()[0].isPlayerPassed();

        // If the computer cannot place a tile with its current hand...
        if(!round.getLayout().canPlaceTile(getPlayerHand(), false, humanPassed))
        {
            // If there are tiles remaining in the boneyard, attempt to draw one and place it...
            if(round.getStock().getStockSize() > 0)
            {
               String toastMessage = "The computer could not place a tile, " +
                       "it will draw another from the boneyard!";
               activity.generateToastMessage(toastMessage);

               return drawTileAndAttempt(round);
            }

            // Otherwise, the computer must pass its turn as it cannot place it.
            else
            {
                String toastMessage = "The computer could not place a tile, " +
                        "and there are none remaining in the boneyard!";
                activity.generateToastMessage(toastMessage);

                setPlayerPassed(true);
                round.swapTurns();
                return false;
            }
        }
        return true;
    }

    /**
     * To draw a tile from the boneyard and attempt to place it.
     *
     * @param round Round object representing the current round.
     * @return Boolean value indicating if the drawn tile was able to placed.
     */

    public boolean drawTileAndAttempt(Round round)
    {
        boolean humanPassed = round.getPlayers()[0].isPlayerPassed();

        // Draw a tile from the boneyard and add it to the computer's hand.
        getPlayerHand().addTile(round.getStock().draw());

        // Check if the drawn tile can be placed.
        if(!round.getLayout().canPlaceTile(getPlayerHand(), false, humanPassed))
        {
            String toastMessage = "The computer could not place the drawn tile!";
            activity.generateToastMessage(toastMessage);
            activity.updateView(true);

            setPlayerPassed(true);
            round.swapTurns();
            return false;
        }

        // If the drawn tile can be placed inform the user.
        else
        {
            String toastMessage = "Computer was able to place the drawn tile!";
            activity.generateToastMessage(toastMessage);
            activity.updateView(true);

            return true;
        }
    }

    /**
     * For the computer player to actually place a tile on the layout.
     *
     * @param round Round object representing the current round.
     * @return Boolean value indicating if the computer player was able to place a tile.
     */

    public boolean playerPlayTile(Round round, int oldIndex)
    {
        // First check if the computer can place a tile with its current hand.
        if(!canMakePlay(round))
        {
            return false;
        }

        // If the computer can place a tile, go through and find the tile with the highest
        // sum of pips that can be placed.
        else
        {
            Layout layout = round.getLayout();
            Boolean humanPassed = round.getPlayers()[0].isPlayerPassed();

            int maxSumDomino = 0;
            int tileIndex = 0;
            boolean leftPlacement = false;

            for(int i = 0; i < getPlayerHand().getHandSize(); i++)
            {
                Tile tileAtIndex = getPlayerHand().getTileAt(i);

                // Check to see if either the domino is a double that can be placed.
                // Or if the human player passed, so the domino can be placed on either side.
                if(tileAtIndex.isDouble() || humanPassed)
                {
                    // Determine if the domino can be placed to the right.
                    if(layout.validatePlacement(false, tileAtIndex))
                    {
                        if (tileAtIndex.calculateTileSum() > maxSumDomino)
                        {
                            tileIndex = i;
                            maxSumDomino = tileAtIndex.calculateTileSum();
                        }
                    }

                    // Determine if the domino can be placed to the left.
                    if(layout.validatePlacement(true, tileAtIndex))
                    {
                        if (tileAtIndex.calculateTileSum() > maxSumDomino)
                        {
                            tileIndex = i;
                            maxSumDomino = tileAtIndex.calculateTileSum();
                            leftPlacement = true;
                        }
                    }
                }

                // If the tile is not a double and the human player did not pass their turn.
                // Placing to the right is the only option.
                else
                {
                    // Determine if the domino can be placed to the right.
                   if(layout.validatePlacement(false, tileAtIndex))
                   {
                       if(tileAtIndex.calculateTileSum() > maxSumDomino)
                       {
                           tileIndex = i;
                           maxSumDomino = tileAtIndex.calculateTileSum();
                       }
                   }
                }
            }

            // Describe the computer's move and strategy, displaying it to the user via a toast.
            if(!leftPlacement)
            {
                layout.addToRightSide(getPlayerHand().removeTile(tileIndex));
                String toastMessage = "The computer placed: " + layout.getTileAt(layout.getLayoutSize()-1).printTileAsString()
                        + " to the right, as that was the domino with the highest sum that could be placed "
                        + " in the computer's hand.";
                activity.generateToastMessage(toastMessage);
            }
            else
            {
                layout.addToLeftSide(getPlayerHand().removeTile(tileIndex));

                String toastMessage = "The computer placed: " + layout.getTileAt(0).printTileAsString()
                        + " to the left, as that was the domino with the highest sum that could be placed "
                        + "in the computer's hand.";
                activity.generateToastMessage(toastMessage);
            }

            activity.updateView(true);

            // The computer placed a tile, let the human play...
            setPlayerPassed(false);
            round.swapTurns();

            return true;
        }
    }
}
