//     ************************************************************
//     * Name:  Kyle Calabro                                      *
//     * Project:  Longana - Java/Android Implementation          *
//     * Class:  CMPS 366 - Organization of Programming Languages *
//     * Date:  12/5/2017                                         *
//     ************************************************************

package edu.ramapo.kcalabro.longana.view;

import android.view.ViewGroup;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import edu.ramapo.kcalabro.longana.R;
import edu.ramapo.kcalabro.longana.model.Hand;
import edu.ramapo.kcalabro.longana.model.Tile;

import java.util.Vector;

public class ComputerView
{
    //------------------------Data Members------------------------

    private RoundActivity activity;

    private Vector<TileView> computerView;

    //------------------------Member Functions------------------------

    /**
     * Default constructor for the ComputerView class.
     *
     * @param activity Object of the RoundActivity class representing current round.
     */

    public ComputerView(RoundActivity activity)
    {
        this.activity = activity;
    }

    /**
     * To get the ComputerView object.
     *
     * @return ComputerView object representing the current computerView.
     */

    public Vector<TileView> getView()
    {
        return computerView;
    }

    /**
     * To update the current computerView object.
     *
     * @param computerHand The computer player's current hand.
     * @param isActive Boolean Value indicating if the view is enabled.
     */

    public void updateComputerView(Hand computerHand, boolean isActive)
    {
        computerView = new Vector<TileView>();

        // Get the current view of the computer's hand and remove it so that we can start fresh.
        ViewGroup linearLayout = (ViewGroup) activity.findViewById(R.id.computerHand);
        linearLayout.removeAllViews();

        // Format the view of the computer's hand.
        TextView textView = new TextView(activity);
        textView.setText("Computer's Hand: ");
        textView.setTextSize(28);

        // Add the TextView to the new View Group.
        linearLayout.addView(textView);

        // Iterate through the computer's hand adding each tile to the view properly.
        for(int tileIndex = 0; tileIndex < computerHand.getHandSize(); tileIndex++)
        {
            Tile tileAtIndex = computerHand.getTileAt(tileIndex);

            TileView tileView = new TileView(activity);
            tileView.updateView(tileAtIndex, tileIndex, isActive);

            // Set the parameters to mirror those in the activity_round layout.
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            layoutParams.setMargins(10, 0, 10, 0);

            tileView.getTileView().setLayoutParams(layoutParams);
            linearLayout.addView(tileView.getTileView());

            computerView.addElement(tileView);
        }
    }
}
