//     ************************************************************
//     * Name:  Kyle Calabro                                      *
//     * Project:  Longana - Java/Android Implementation          *
//     * Class:  CMPS 366 - Organization of Programming Languages *
//     * Date:  12/5/2017                                         *
//     ************************************************************

package edu.ramapo.kcalabro.longana.model;

import android.os.Environment;
import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Serializer {
    //------------------------Data Members------------------------

    // The pattern to be matched for player's hands, layout, and boneyard.
    private String pattern = "(([0-6])-([0-6]))";

    // The regular expression to use in conjunction with the pattern.
    private Pattern regex = Pattern.compile(pattern);

    //------------------------Member Functions------------------------

    /**
     * The default constructor for the Serializer class.
     */
    public Serializer()
    {
    }

    /**
     * To restore a tournament from a serialized file properly.
     *
     * @param round    Round object representing the current round.
     * @param fileName The name of the serialized file to load from.
     * @return Boolean value indicating if the given file could be loaded.
     */

    public boolean restoreFile(Round round, String fileName) {
        File sdcard = Environment.getExternalStorageDirectory().getAbsoluteFile();
        File serializedFile = new File(sdcard, fileName);

        try {
            String fileLine;
            boolean isPlayerPassed = false;
            int lineNumber = 0;

            BufferedReader bufferedReader = new BufferedReader(new FileReader(serializedFile));

            try {
                while ((fileLine = bufferedReader.readLine()) != null) {
                    if (!fileLine.equals("")) {
                        lineNumber++;

                        // First line contains the tournament score.
                        if (lineNumber == 1) {
                            String tournamentScore = fileLine.substring(fileLine.indexOf(':') + 2);
                            int tournScore = Integer.parseInt(tournamentScore);

                            System.out.println("Tournament Score: " + tournScore);
                            round.setTournamentScore(tournScore);
                        }

                        // Second line contains the round number.
                        else if (lineNumber == 2) {
                            String roundNum = fileLine.substring(fileLine.indexOf(':') + 2);
                            int roundNumber = Integer.parseInt(roundNum);

                            System.out.println("Round Number: " + roundNumber);

                            round.setRoundNumber(roundNumber);
                            round.setEngine(round.determineEngine());
                        }

                        // Computer data comes next.
                        else if (lineNumber == 4) {
                            // Restore the computer player's hand.
                            restorePlayerHand(round.getPlayers()[1], fileLine);
                        } else if (lineNumber == 5) {
                            // Restore the computer player's score.
                            String playerScore = fileLine.substring(fileLine.indexOf(':') + 2);
                            int computerScore = Integer.parseInt(playerScore);

                            round.getPlayers()[1].setTournamentScore(computerScore);
                        }

                        // Human data comes next.
                        else if (lineNumber == 7) {
                            // Restore the human player's hand.
                            restorePlayerHand(round.getPlayers()[0], fileLine);
                        } else if (lineNumber == 8) {
                            // Restore the human player's score.
                            String playerScore = fileLine.substring(fileLine.indexOf(':') + 2);
                            int humanScore = Integer.parseInt(playerScore);

                            round.getPlayers()[0].setTournamentScore(humanScore);
                        }

                        // Layout comes next.
                        else if (lineNumber == 10) {
                            restoreLayout(round.getLayout(), fileLine);
                        }

                        // Boneyard comes next.
                        else if (lineNumber == 12) {
                            restoreBoneyard(round.getStock(), fileLine);
                        }

                        // Previous player passed comes next.
                        else if (lineNumber == 13) {
                            String playerPassed = fileLine.substring(fileLine.indexOf(':') + 2);

                            // Determine if the previous player passed their play.
                            if (playerPassed.equals("Yes")) {
                                isPlayerPassed = true;
                            } else {
                                isPlayerPassed = false;
                            }
                        }

                        // Next Player comes next.
                        else if (lineNumber == 14) {
                            String nextPlayer = fileLine.substring(fileLine.indexOf(':') + 2);

                            if (nextPlayer.equals("Human")) {
                                round.setTurn(0);
                                round.getPlayers()[1].setPlayerPassed(isPlayerPassed);
                            } else {
                                round.setTurn(1);
                                round.getPlayers()[0].setPlayerPassed(isPlayerPassed);
                            }
                        }
                    }
                }

                // Clean up the BufferedReader.
                bufferedReader.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
                return false;
            }
        } catch (FileNotFoundException fileException) {
            fileException.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * To serialize a given round in the correct format.
     *
     * @param round Object of the round class representing the round to be saved.
     * @throws IOException If file could not be found.
     */

    public void serializeFile(Round round) throws IOException
    {
        // The filename to save the round as.
        String fileName = "savedGame.txt";

        String filepath = Environment.getExternalStorageDirectory().toString();

        File serializedFile = new File(filepath, fileName);

        // If the file exists delete it so we can write a new file.
        if(serializedFile.exists())
        {
            serializedFile.delete();
        }

        try
        {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(serializedFile));

            // Write the tournament score to the file.
            bufferedWriter.write("Tournament Score: ");
            bufferedWriter.write(round.getTournamentScore() + "\n\n");
            System.out.println(round.getTournamentScore());

            // Write the round number to the file.
            bufferedWriter.write("Round No.: ");
            bufferedWriter.write(round.getRoundNumber() + "\n\n");
            System.out.println("Round Number: " + round.getRoundNumber());

            // Write the computer's data to the file.
            bufferedWriter.write("Computer: \n");
            serializePlayerData(round.getPlayers()[1], bufferedWriter);

            // Write the human's data to the file.
            bufferedWriter.write("Human: \n");
            serializePlayerData(round.getPlayers()[0], bufferedWriter);

            // Write the layout to the file.
            bufferedWriter.write("Layout: \n");
            serializeLayout(round.getLayout(), bufferedWriter);

            // Write the Boneyard to the file.
            bufferedWriter.write("Boneyard: \n");
            serializeBoneyard(round.getStock(), bufferedWriter);

            // If the layout is empty, only the hands have been drawn
            // and the engine holder has not been determined.
            if(round.getLayout().getLayoutSize() == 0)
            {
                bufferedWriter.write("Previous Player Passed: \n");
                bufferedWriter.write("Next Player: \n");
            }
            else
            {
                // If it is the human player's turn.
                if(round.getTurn() == 0)
                {
                    // Determine if the computer player passed their previous play.
                    if(round.getPlayers()[1].isPlayerPassed())
                    {
                        bufferedWriter.write("Previous Player Passed: Yes \n\n");
                    }
                    else
                    {
                        bufferedWriter.write("Previous Player Passed: No \n\n");
                    }

                    bufferedWriter.write("Next Player: Human");
                }

                // Otherwise, its the computer player's turn.
                else
                {
                    // Determine if the human player passed their previous play.
                    if (round.getPlayers()[0].isPlayerPassed())
                    {
                        bufferedWriter.write("Previous Player Passed: Yes \n\n");
                    }
                    else
                    {
                        bufferedWriter.write("Previous Player Passed: No \n\n");
                    }

                    bufferedWriter.write("Next Player: Computer");
                }
            }

            // Clean up the buffered writer.
            bufferedWriter.close();
        }
        catch (FileNotFoundException fileException)
        {
            fileException.printStackTrace();
        }
    }

    //------------------------Restoring Functions------------------------

    /**
     * To restore the layout from a serialized file.
     *
     * @param layout The layout object to restore from the file.
     * @param layoutLine The String from the file holding the layout data.
     * @throws IOException
     */

    private void restoreLayout(Layout layout, String layoutLine) throws IOException
    {
        Matcher matcher = regex.matcher(layoutLine);

        // Traverse all the regular expression matches found in the String.
        while (matcher.find())
        {
            // Assign values from the matcher.
            int leftPip = Integer.parseInt(matcher.group(2));
            int rightPip = Integer.parseInt(matcher.group(3));

            // Construct a tile based on the above values.
            Tile tileToAdd = new Tile(leftPip, rightPip);

            // Add the tile to the layout.
            layout.addToRightSide(tileToAdd);
        }
    }

    /**
     * To restore a player's hand from a serialized file.
     *
     * @param player Object of the Player class, representing a Computer or Human object.
     * @param handLine The String from the file holding the Player's hand data.
     * @throws IOException
     */

    private void restorePlayerHand(Player player, String handLine) throws IOException {

        Matcher matcher = regex.matcher(handLine);

        // Traverse all the regular expressions found in the String.
        while (matcher.find()) {

            // Assign values from the matcher.
            int leftPip = Integer.parseInt(matcher.group(2));
            int rightPip = Integer.parseInt(matcher.group(3));

            // Construct a tile with those values.
            Tile tileToAdd = new Tile(leftPip, rightPip);

            // Add the tile to the Player's hand.
            player.getPlayerHand().addTile(tileToAdd);
        }
    }

    /**
     * To restore the Stock/Boneyard from a serialized file.
     *
     * @param stock Object of the stock class, which needs restoring.
     * @param stockLine The String from the file holding the boneyard/stock data.
     * @throws IOException
     */

    private void restoreBoneyard(Stock stock, String stockLine) throws IOException {

        Matcher matcher = regex.matcher(stockLine);

        // Traverse all the matches found in the String.
        while (matcher.find()) {

            // Assign values from the matcher.
            int leftPip = Integer.parseInt(matcher.group(2));
            int rightPip = Integer.parseInt(matcher.group(3));

            // Construct a tile with those values.
            Tile tileToAdd = new Tile(leftPip, rightPip);

            // Add the tile to the stock/boneyard.
            stock.addToStock(tileToAdd);
        }
    }

    //------------------------Serializing Functions------------------------

    /**
     * To save the boneyard/stock to a file.
     *
     * @param stock The stock object which to save to a file.
     * @param bufferedWriter The bufferedWriter which writes to the open serialization file.
     * @throws IOException
     */

    private void serializeBoneyard(Stock stock, BufferedWriter bufferedWriter) throws IOException {

        // Traverse the stock/boneyard.
        for (int i = 0; i < stock.getStockSize(); i++)
        {
            // Write the tile at the index to the file.
            bufferedWriter.write(stock.getTileAt(i).printTileAsString());
            bufferedWriter.write(" ");
        }

        bufferedWriter.write("\n\n");
    }

    /**
     * To save the layout to a file.
     *
     * @param layout The layout object which to save to a file.
     * @param bufferedWriter The bufferedWriter which writes to the open serialization file.
     * @throws IOException
     */

    private void serializeLayout(Layout layout, BufferedWriter bufferedWriter) throws IOException {

        bufferedWriter.write("   L");

        // Traverse the layout.
        for (int i = 0; i < layout.getLayoutSize(); i++) {

            // Write the tile to the file.
            bufferedWriter.write(layout.getTileAt(i).printTileAsString());
            bufferedWriter.write(" ");
        }

        bufferedWriter.write("   R");
        bufferedWriter.write("\n\n");
    }

    /**
     * To save a Player's data to a file.
     *
     * @param player Player Object representing either the Computer or Human player.
     * @param bufferedWriter The bufferedWriter which writes to the open serialization file.
     * @throws IOException
     */

    private void serializePlayerData(Player player, BufferedWriter bufferedWriter) throws IOException {

        bufferedWriter.write("   Hand: ");

        // Traverse the player's hand.
        for (int i = 0; i < player.getPlayerHand().getHandSize(); i++) {

            // Write the tile at the index to the file.
            bufferedWriter.write(player.getPlayerHand().getTileAt(i).printTileAsString());
            bufferedWriter.write(" ");
        }

        bufferedWriter.write("\n");
        bufferedWriter.write("\n   Score: ");
        bufferedWriter.write(player.getTournamentScore() + "\n\n");
    }
}
