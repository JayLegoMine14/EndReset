package com.bubbassurvival.endreset.pillars;

import java.util.ArrayList;
import java.util.Arrays;

public enum PillarType {

    Small, Medium, Large;

    public static PillarType chooseRandomPillarType(){
        double chance = Math.random();

        if(chance < .3)
            return PillarType.Small;
        else if(chance > .3 && chance < .6)
            return PillarType.Medium;
        else
            return PillarType.Large;
    }

    public static ArrayList<Character> getBluePrints(PillarType pt){
        switch (pt){
        case Large:
            return new ArrayList<Character>(
                    Arrays.asList(  'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A',

                                    'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A',
                
                                    'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A',
                
                                    'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A',
                
                                    'A', 'A', 'A', 'A', 'A', 'A', '■', '■', '■', 'A', 'A', 'A', 'A', 'A', 'A', 'A',
                
                                    'A', 'A', 'A', 'A', 'A', '■', '■', '■', '■', '■', 'A', 'A', 'A', 'A', 'A', 'A',
                
                                    'A', 'A', 'A', 'A', '■', '■', '■', '■', '■', '■', '■', 'A', 'A', 'A', 'A', 'A',
                
                                    'A', 'A', 'A', 'A', '■', '■', '■', 'T', '■', '■', '■', 'A', 'A', 'A', 'A', 'A',
                
                                    'A', 'A', 'A', 'A', '■', '■', '■', '■', '■', '■', '■', 'A', 'A', 'A', 'A', 'A',
                
                                    'A', 'A', 'A', 'A', 'A', '■', '■', '■', '■', '■', 'A', 'A', 'A', 'A', 'A', 'A',
                
                                    'A', 'A', 'A', 'A', 'A', 'A', '■', '■', '■', 'A', 'A', 'A', 'A', 'A', 'A', 'A',
                
                                    'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A',
                
                                    'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A',
                
                                    'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A',
                
                                    'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A',
                
                                    'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A'));
        case Medium:
            return new ArrayList<Character>(
                    Arrays.asList(  'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A',

                                    'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A',
                
                                    'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A',
                
                                    'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A',
                
                                    'A', 'A', 'A', 'A', 'A', 'A', '■', '■', '■', 'A', 'A', 'A', 'A', 'A', 'A', 'A',
                
                                    'A', 'A', 'A', 'A', 'A', '■', '■', '■', '■', '■', 'A', 'A', 'A', 'A', 'A', 'A',
                
                                    'A', 'A', 'A', 'A', 'A', '■', '■', 'T', '■', '■', 'A', 'A', 'A', 'A', 'A', 'A',
                
                                    'A', 'A', 'A', 'A', 'A', '■', '■', '■', '■', '■', 'A', 'A', 'A', 'A', 'A', 'A',
                
                                    'A', 'A', 'A', 'A', 'A', 'A', '■', '■', '■', 'A', 'A', 'A', 'A', 'A', 'A', 'A',
                
                                    'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A',
                
                                    'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A',
                
                                    'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A',
                
                                    'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A',
                
                                    'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A',
                
                                    'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A',
                
                                    'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A'));
        case Small:
            return new ArrayList<Character>(
                    Arrays.asList(  'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A',

                                    'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A',

                                    'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A',
                
                                    'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A',
                
                                    'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A',
                
                                    'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A',
                
                                    'A', 'A', 'A', 'A', 'A', 'A', '■', '■', '■', 'A', 'A', 'A', 'A', 'A', 'A', 'A',
                
                                    'A', 'A', 'A', 'A', 'A', 'A', '■', 'T', '■', 'A', 'A', 'A', 'A', 'A', 'A', 'A',
                
                                    'A', 'A', 'A', 'A', 'A', 'A', '■', '■', '■', 'A', 'A', 'A', 'A', 'A', 'A', 'A',
                
                                    'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A',
                
                                    'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A',
                
                                    'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A',
                
                                    'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A',
                
                                    'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A',
                
                                    'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A',
                
                                    'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A'));
        default:
            return new ArrayList<Character>();

        }
    }

}
