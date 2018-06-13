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

import android.os.Build;

import android.view.ViewGroup;
import android.view.Gravity;

import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Vector;

import edu.ramapo.kcalabro.longana.R;
import edu.ramapo.kcalabro.longana.model.Layout;
import edu.ramapo.kcalabro.longana.model.Tile;

public class LayoutView
{
    //------------------------Data Members------------------------

    private Vector<TileView> tileViews;
    private RoundActivity activity;
    private Tile tileAtIndex;

    //------------------------Member Function------------------------

    /**
     * Default constructor for the LayoutView class.
     *
     * @param activity
     */

    public LayoutView(RoundActivity activity)
    {
        this.activity = activity;
    }

    /**
     * To update the LayoutView object.
     *
     * @param layout
     * @param isActive
     */

    public void updateLayoutView(Layout layout, boolean isActive)
    {

        // Get the current layout and remove all tiles so we can start fresh.
        ViewGroup linearLayout = (ViewGroup) activity.findViewById(R.id.layout);
        linearLayout.removeAllViews();

        tileViews = new Vector<TileView>();

        // Format the layout's text.
        TextView textView = new TextView(activity);
        textView.setText("The Current Layout:   L");
        textView.setTextSize(28);

        // Add the TextView to the new linear layout.
        linearLayout.addView(textView);

        // Iterate through every tile on the layout,
        // adding it to the layout view so that it
        // displays properly.
        for (int i = 0; i < layout.getLayoutSize(); i++)
        {
            TileView tileView = new TileView(activity);

            tileAtIndex = layout.getTileAt(i);
            tileView.updateView(tileAtIndex, i, isActive);

            // If the tile is a double it must be displayed vertically.
            if(tileAtIndex.isDouble())
            {
                tileView.getTileView().setOrientation(LinearLayout.VERTICAL);
            }

            // Set the parameters to mirror those in the activity_round layout.
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            layoutParams.setMargins(10, 0, 10, 0);

            tileView.getTileView().setLayoutParams(layoutParams);
            linearLayout.addView(tileView.getTileView());
            tileViews.addElement(tileView);
        }

        // Format the layout's text.
        textView = new TextView(activity);
        textView.setText("   R");
        textView.setTextSize(28);

        // Add the formatted layout to the view.
        linearLayout.addView(textView);
    }
}
