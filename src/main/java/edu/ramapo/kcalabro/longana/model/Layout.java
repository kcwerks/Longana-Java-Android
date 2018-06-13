//     ************************************************************
//     * Name:  Kyle Calabro                                      *
//     * Project:  Longana - Java/Android Implementation          *
//     * Class:  CMPS 366 - Organization of Programming Languages *
//     * Date:  12/5/2017                                         *
//     ************************************************************

package edu.ramapo.kcalabro.longana.model;

import java.util.Vector;

public class Layout
{
    //------------------------Data Members------------------------

    // The layout for Longana.
    private Vector<Tile> layout;

    //------------------------Member Functions------------------------

    /**
     * Default constructor for the layout class.
     */

    public Layout()
    {
        layout = new Vector<Tile>();
    }

    /**
     * To determine if a player can place a tile given their hand.
     *
     * @param playerHand The hand of a player.
     * @param isHuman Boolean flag to indicate if the player is the human player.
     * @param playerPassed Boolean flag to indicate if the previous player passed their play.
     * @return Boolean value indicating if the player can place a tile from their hand.
     */

    public boolean canPlaceTile(Hand playerHand, boolean isHuman, boolean playerPassed)
    {
        // Iterate through the player's hand.
        for(int i = 0; i < playerHand.getHandSize(); i++)
        {
            Tile tileToCheck = playerHand.getTileAt(i);

            // If the current tile is NOT a double AND the previous player did NOT pass.
            if(!tileToCheck.isDouble() && !playerPassed)
            {
                // If the tile is not a double and the previous player has not passed,
                // Verify that the tile can be placed to the left or right of the board,
                // dependent on the player, left(true) -> Human, right(false) -> Computer.
                if(validatePlacement(isHuman, tileToCheck))
                {
                    return true;
                }
            }

            // If the tile is a double or the previous player passed their turn,
            // the tile can be placed on either side of the board, assuming the tile is valid.
            else
            {
                if(validatePlacement(true, tileToCheck) || validatePlacement(false, tileToCheck))
                {
                    return true;
                }
            }
        }

        // If the player cannot place a tile.
        return false;
    }

    /**
     * To determine if a given domino can be placed.
     *
     * @param leftPlacement Boolean flag indicating if the tile is to be placed to the left.
     * @param tileToValidate Tile object representing the domino that is to be placed.
     * @return Boolean value indicating if the tile can be placed on the given side.
     */

    public boolean validatePlacement(boolean leftPlacement, Tile tileToValidate)
    {
        if(layout.size() > 0)
        {
            // If the player wishes to place the tile to the left.
            if(leftPlacement)
            {
                // Check that the tile can be placed to the left.
                return verifyLeftPlacement(tileToValidate);
            }

            // Otherwise, the player wishes to place the tile to the right.
            else
            {
                // Check that the tile can be placed to the right.
                return verifyRightPlacement(tileToValidate);
            }
        }

        // If the layout is empty, only the engine can be placed...
        return true;
    }

    /**
     * To determine if a given tile can be placed on the left side of the layout.
     *
     * @param tileToCheck A tile object representing the tile to be placed.
     * @return Boolean value indicating if the tile can be placed to left.
     */

    public boolean verifyLeftPlacement(Tile tileToCheck)
    {
        // If the left-most pip of the layout matches the
        // right pip of the player's domino, it is playable.
        if(getLeftMostPip() == tileToCheck.getRightPip())
        {
            return true;
        }

        // Otherwise, determine if the tile must be transposed
        // in order to be placed, transpose if necessary.
        else if(getLeftMostPip() == tileToCheck.getLeftPip())
        {
            tileToCheck.transposeTile();
            return true;
        }

        // Otherwise, the tile can not be placed to the left.
        else
        {
            return false;
        }
    }

    /**
     * To determine if a given tile can be placed on the right side of the layout.
     *
     * @param tileToCheck A tile object representing the tile to be placed.
     * @return Boolean value indicating if the tile can be placed to right.
     */

    public boolean verifyRightPlacement(Tile tileToCheck)
    {
        // If the right-most pip of the layout matches the
        // right pip of the player's domino, it is placeable.
        if(getRightMostPip() == tileToCheck.getLeftPip())
        {
            return true;
        }

        // Otherwise, determine if the tile must be transposed
        // in order to be placed, transpose if necessary.
        else if(getRightMostPip() == tileToCheck.getRightPip())
        {
            tileToCheck.transposeTile();
            return true;
        }

        // Otherwise, the tile can not be placed to the right.
        else
        {
            return false;
        }
    }

    /**
     * To get the left-most pip on the layout.
     *
     * @return Integer value representing the left-most pip on the layout.
     */

    public int getLeftMostPip()
    {
        return layout.get(0).getLeftPip();
    }

    /**
     * To get the right-most pip on the layout.
     *
     * @return Integer value representing the right-most pip on the layout.
     */

    public int getRightMostPip()
    {
        return layout.get(getLayoutSize() - 1).getRightPip();
    }

    /**
     * To add a tile to the right side of the layout.
     *
     * @param tileToAdd The tile to add to the right side of the layout.
     */

    public void addToRightSide(Tile tileToAdd)
    {
        layout.addElement(tileToAdd);
    }

    /**
     * To add a tile to the left side of the layout.
     *
     * @param tileToAdd The tile to add to the left side of the layout.
     */

    public void addToLeftSide(Tile tileToAdd)
    {
        layout.add(0, tileToAdd);
    }

    /**
     * To display the current layout to the terminal window.
     */

    public void displayLayout()
    {
        System.out.println("The layout thus far: ");
        for(int i = 0; i < layout.size(); i++)
        {
            layout.elementAt(i).printTile();
        }
        System.out.println();
    }

    /**
     * To get the tile on the layout at a given index.
     *
     * @param index The index of the tile to be returned.
     * @return A tile object representing a domino at the given index of the layout.
     */

    public Tile getTileAt(int index)
    {
        return layout.elementAt(index);
    }

    /**
     * To get the size of the current layout.
     *
     * @return The size of the current layout.
     */

    public int getLayoutSize()
    {
        return layout.size();
    }
}
