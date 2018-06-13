//     ************************************************************
//     * Name:  Kyle Calabro                                      *
//     * Project:  Longana - Java/Android Implementation          *
//     * Class:  CMPS 366 - Organization of Programming Languages *
//     * Date:  12/5/2017                                         *
//     ************************************************************

package edu.ramapo.kcalabro.longana.model;

import java.util.Vector;

public class Hand
{
    //------------------------Data Members------------------------

    // A player's hand for Longana.
    private Vector<Tile> playerHand;

    //------------------------Member Functions------------------------

    /**
     * Default constructor for the Hand class.
     */

    public Hand()
    {
        playerHand = new Vector<Tile>();
    }

    /**
     * To add a tile to a player's hand.
     *
     * @param tileToAdd The tile to add to a player's hand.
     */

    public void addTile(Tile tileToAdd)
    {
        playerHand.addElement(tileToAdd);
    }

    /**
     * To determine if a player's hand is empty.
     *
     * @return Boolean value indicating if the given player's hand is empty.
     */

    public boolean isEmpty()
    {
        if(playerHand.isEmpty())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * To remove a tile from a player's hand.
     *
     * @param index The index of the tile to be removed from the player's hand.
     * @return Tile object representing the tile that has been removed.
     */

    public Tile removeTile(int index)
    {
        Tile tileToRemove = playerHand.elementAt(index);
        playerHand.removeElementAt(index);
        return tileToRemove;
    }

    /**
     * To calculate the sum of all pips in a given player's hand.
     *
     * @return Integer value representing the sum of all pips in a player's hand.
     */

    public int calculateHandSum()
    {
        int sum = 0;

        for(int i = 0; i < playerHand.size(); i++)
        {
            sum = sum + playerHand.elementAt(i).calculateTileSum();
        }

        return sum;
    }

    /**
     * To displayer a player's hand to the terminal window.
     */

    public void printHand()
    {
        System.out.println("Your Current Hand: ");

        for(int i = 0; i < playerHand.size(); i++)
        {
            playerHand.elementAt(i).printTile();
        }

        System.out.println();
    }

    /**
     * To get the size of a given player's hand.
     *
     * @return An integer value representing the size of the player's hand.
     */

    public int getHandSize()
    {
        return playerHand.size();
    }

    /**
     * To get the tile at a specific index in a player's hand.
     *
     * @param index The index of the tile object to be retrieved from a player's hand.
     * @return Tile object representing a domino at the given index of a player's hand.
     */

    public Tile getTileAt(int index)
    {
        return playerHand.elementAt(index);
    }
}
