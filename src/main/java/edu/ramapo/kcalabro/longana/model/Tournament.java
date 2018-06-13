//     ************************************************************
//     * Name:  Kyle Calabro                                      *
//     * Project:  Longana - Java/Android Implementation          *
//     * Class:  CMPS 366 - Organization of Programming Languages *
//     * Date:  12/5/2017                                         *
//     ************************************************************

package edu.ramapo.kcalabro.longana.model;

public class Tournament
{
    //------------------------Data Members------------------------

    private Serializer serializer;

    //------------------------Member Functions------------------------

    /**
     * Default constructor for the Tournament class.
     */

    public Tournament()
    {
        serializer = new Serializer();
    }

    /**
     * To get the Serializer.
     *
     * @return Object of the Serializer class.
     */

    public Serializer getSerializer()
    {
        return serializer;
    }
}
