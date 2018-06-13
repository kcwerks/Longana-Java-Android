//     ************************************************************
//     * Name:  Kyle Calabro                                      *
//     * Project:  Longana - Java/Android Implementation          *
//     * Class:  CMPS 366 - Organization of Programming Languages *
//     * Date:  12/5/2017                                         *
//     ************************************************************

package edu.ramapo.kcalabro.longana.model;

import java.util.Random;
import java.util.Vector;
import java.util.Collections;

public class Stock
{
    //------------------------Data Members------------------------

    // The stock for a round of Longana.
    private Vector<Tile> stock;

    //------------------------Member Functions------------------------

    /**
     * Default constructor for the Stock class.
     */

    public Stock()
    {
        stock = new Vector<Tile>();
        initializeStock();
        printStock();
    }

    /**
     * Initializes the double-six set of dominoes for a round of Longana.
     */

    public void emptyStock()
    {
        stock.clear();
    }

    /**
     * To generate the double six-set Stock for a round of Longana.
     */

    public void initializeStock()
    {
        // Generate double six-set of tiles.
        for(int i = 0; i < 7; i++)
        {
            for(int y = i; y < 7; y++)
            {
                Tile tileToAdd = new Tile(i, y);
                stock.addElement(tileToAdd);
            }
        }

        // Once the stock is generated, go ahead and shuffle it.
        shuffleStock();
    }

    /**
     * To add a tile to the stock.
     *
     * @param tileToAdd The Tile object to add to the stock.
     */

    public void addToStock(Tile tileToAdd)
    {
        stock.addElement(tileToAdd);
    }

    /**
     * To retrieve a tile at a given index of the stock.
     *
     * @param index The index of the tile in the stock to return.
     * @return Tile object at the given index in the Stock.
     */

    public Tile getTileAt(int index)
    {
        return stock.elementAt(index);
    }

    /**
     * To shuffle the stock of Longana.
     */

    public void shuffleStock()
    {
        Collections.shuffle(stock);
    }

    /**
     * To display the stock to the terminal window.
     */

    public void printStock()
    {
        System.out.println("\n\nThe Shuffled Stock: ");

        for(int i = 0; i < stock.size(); i++)
        {
            stock.elementAt(i).printTile();
        }

        System.out.println("\n\n");
    }

    /**
     * To get the size of the current stock.
     *
     * @return An integer that represents the size of the stock.
     */

    public int getStockSize()
    {
        return stock.size();
    }

    /**
     * To draw a tile from the stock of Longana, also removes the tile from the stock.
     *
     * @return A tile object representing a domino that was drawn from the stock.
     */

    public Tile draw()
    {
        Tile tileToDraw = stock.elementAt(0);
        stock.removeElementAt(0);
        return tileToDraw;
    }
}
