//     ************************************************************
//     * Name:  Kyle Calabro                                      *
//     * Project:  Longana - Java/Android Implementation          *
//     * Class:  CMPS 366 - Organization of Programming Languages *
//     * Date:  12/5/2017                                         *
//     ************************************************************

package edu.ramapo.kcalabro.longana.view;

/**
 * Created by KyleCalabro on 11/30/17.
 */

import android.view.ViewGroup;
import android.view.View;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import edu.ramapo.kcalabro.longana.R;
import edu.ramapo.kcalabro.longana.model.Hand;
import edu.ramapo.kcalabro.longana.model.Tile;

import java.util.Vector;

public class HumanView
{
    //------------------------Data Members------------------------

    private RoundActivity activity;

    Vector<TileView> humanView;

    //------------------------Member Functions------------------------

    /**
     * Default constructor for the HumanView class.
     *
     * @param activity RoundActivity object representing current round.
     */

    public HumanView(RoundActivity activity)
    {
        this.activity = activity;
    }

    /**
     * To get the current humanView.
     *
     * @return Object of HumanView class representing the current view.
     */

    public Vector<TileView> getView()
    {
        return humanView;
    }

    /**
     * To update the HumanView object.
     *
     * @param humanHand The human player's current hand.
     * @param isActive Boolean value indicating if the view is enabled.
     */

    public void updateHumanView(Hand humanHand, boolean isActive)
    {
        humanView = new Vector<TileView>();

        // Get the current view of the human player's hand and remove it to start fresh.
        ViewGroup linearLayout = (ViewGroup) activity.findViewById(R.id.humanHand);
        linearLayout.removeAllViews();

        TextView textView = new TextView(activity);
        textView.setText("Your Hand: ");
        textView.setTextSize(28);

        // Add the TextView to the new linear layout.
        linearLayout.addView(textView);

        // Iterate through the human player's hand adding each tile to the view properly.
        for(int tileIndex = 0; tileIndex < humanHand.getHandSize(); tileIndex++)
        {
            Tile tileAtIndex = humanHand.getTileAt(tileIndex);

            TileView tileView = new TileView(activity);
            tileView.updateView(tileAtIndex, tileIndex, isActive);

            // Set the parameters to mirror those in the activity_round layout.
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            layoutParams.setMargins(10, 0, 10, 0);

            tileView.getTileView().setLayoutParams(layoutParams);
            linearLayout.addView(tileView.getTileView());

            humanView.addElement(tileView);
        }
    }
}
