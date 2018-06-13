//     ************************************************************
//     * Name:  Kyle Calabro                                      *
//     * Project:  Longana - Java/Android Implementation          *
//     * Class:  CMPS 366 - Organization of Programming Languages *
//     * Date:  12/5/2017                                         *
//     ************************************************************

package edu.ramapo.kcalabro.longana.model;

public class Tile
{
    //------------------------Data Members------------------------

    // Array of two integers representing the left and right pips of
    // a domino.
    private int[] dominoPips;

    //------------------------Member Functions------------------------

    /**
     * Default constructor for the Tile class.
     */

    public Tile()
    {
        dominoPips = new int[2];
        dominoPips[0] = 0;
        dominoPips[1] = 0;
    }

    /**
     * Overload constructor for the Tile class.
     *
     * @param leftPip The left pip of a tile.
     * @param rightPip The right pip of a tile.
     */

    public Tile(int leftPip, int rightPip)
    {
        dominoPips = new int[2];
        dominoPips[0] = leftPip;
        dominoPips[1] = rightPip;
    }

    /**
     * To get the left and right pips of a given tile.
     *
     * @return An integer array holding the left and right pips of a tile.
     */

    public int[] getDominoPips()
    {
        return dominoPips;
    }

    /**
     * To set the pips of a tile.
     *
     * @param leftPip The left pip of a tile.
     * @param rightPip The right pip of a tile.
     */

    public void setDominoPips(int leftPip, int rightPip)
    {
        dominoPips[0] = leftPip;
        dominoPips[1] = rightPip;
    }

    /**
     * To get the left pip of a given tile.
     *
     * @return Integer value representing the left pip of a tile.
     */

    public int getLeftPip()
    {
        return dominoPips[0];
    }

    /**
     * To get the right pip of a given tile.
     *
     * @return Integer value representing the right pip of a tile.
     */

    public int getRightPip()
    {
        return dominoPips[1];
    }

    /**
     * To calculate the sum of a pips on a single tile.
     *
     * @return Integer value representing the sum of pips on a given tile.
     */

    public int calculateTileSum()
    {
        return dominoPips[0] + dominoPips[1];
    }

    /**
     * To transpose a given tile (swap the pips).
     */

    public void transposeTile()
    {
        int pipToSwap = dominoPips[0];
        dominoPips[0] = dominoPips[1];
        dominoPips[1] = pipToSwap;
    }

    /**
     * To determine if a given tile is a double (pips on both sides match).
     *
     * @return Boolean value indicating if tehe given tile is a double.
     */

    public boolean isDouble()
    {
        // If the number of pips on each side of the domino match,
        // the domino is a double.
        if(dominoPips[0] == dominoPips[1])
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * To print an individual tile to the terminal window in the proper format.
     */

    public void printTile()
    {
        System.out.print(" " + dominoPips[0] + "-" + dominoPips[1] + " ");
    }

    /**
     * To construct a String containing the given tile formatted for output properly.
     *
     * @return A String with a tile in the correct format for output.
     */

    public String printTileAsString()
    {
        String tileToPrint = dominoPips[0] + "-" + dominoPips[1];
        return tileToPrint;
    }

}
