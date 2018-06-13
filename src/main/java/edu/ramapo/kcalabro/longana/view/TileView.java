//     ************************************************************
//     * Name:  Kyle Calabro                                      *
//     * Project:  Longana - Java/Android Implementation          *
//     * Class:  CMPS 366 - Organization of Programming Languages *
//     * Date:  12/5/2017                                         *
//     ************************************************************

package edu.ramapo.kcalabro.longana.view;

import android.widget.Button;
import android.widget.LinearLayout;

import edu.ramapo.kcalabro.longana.R;
import edu.ramapo.kcalabro.longana.model.Tile;

public class TileView
{
    //------------------------Data Members------------------------

    private RoundActivity activity;
    private LinearLayout tile;

    private Button leftPip;
    private Button rightPip;

    //------------------------Member Functions------------------------

    /**
     * Default constructor for the TileView class.
     *
     * @param activity RoundActivity object representing the current round.
     */

    public TileView(RoundActivity activity)
    {
        this.activity = activity;
    }

    /**
     * To get the TileView.
     *
     * @return TileView object.
     */

    public LinearLayout getTileView()
    {
        return tile;
    }

    /**
     * To update the TileView object.
     *
     * @param tileToUpdate The Tile object which to update the view based off of.
     * @param buttonId The buttonId to set the new linear layout's id to.
     * @param isActive Boolean value indicating if the view is enabled.
     */

    public void updateView(Tile tileToUpdate, int buttonId, boolean isActive)
    {
        tile = new LinearLayout(activity);

        tile.setId(buttonId);

        leftPip = new Button(activity);
        rightPip = new Button(activity);

        leftPip.setId(buttonId);
        rightPip.setId(buttonId);

        leftPip.setOnClickListener(activity.tileButtonHandler);
        rightPip.setOnClickListener(activity.tileButtonHandler);

        updateTileView(tileToUpdate, leftPip, rightPip, isActive);

        tile.addView(leftPip);
        tile.addView(rightPip);
    }

    /**
     * To Update the view of a tile by creating buttons for both pips.
     *
     * @param tileToUpdate The Tile object which to update based off of.
     * @param leftPip The button for the left pip of a tile.
     * @param rightPip The button for the right pip of a tile.
     * @param isActive Boolean value indicating if the view is enabled.
     */

    public void updateTileView(Tile tileToUpdate, Button leftPip, Button rightPip, boolean isActive)
    {
        // Create the button for both sides of the tile.
        updatePipView(tileToUpdate.getLeftPip(), leftPip);
        updatePipView(tileToUpdate.getRightPip(), rightPip);

        if(!isActive)
        {
            leftPip.setEnabled(isActive);
            rightPip.setEnabled(isActive);
        }
    }

    /**
     * To create a button with the proper background image based on the given number of pips.
     *
     * @param pip Integer representing how many pips to display.
     * @param button The button which to create.
     */

    public void updatePipView(int pip, Button button)
    {
        // Depending on the number of pips on either side of the tile,
        // set the button to be the correct image.
        switch(pip)
        {
            case 0:
                button.setBackgroundResource(R.drawable.buttonborder);
                break;
            case 1:
                button.setBackgroundResource(R.drawable.one_pip);
                break;
            case 2:
                button.setBackgroundResource(R.drawable.two_pips);
                break;
            case 3:
                button.setBackgroundResource(R.drawable.three_pips);
                break;
            case 4:
                button.setBackgroundResource(R.drawable.four_pips);
                break;
            case 5:
                button.setBackgroundResource(R.drawable.five_pips);
                break;
            case 6:
                button.setBackgroundResource(R.drawable.six_pips);
                break;
            default:
                button.setBackgroundResource(R.drawable.buttonborder);
                break;
        }
    }
}
