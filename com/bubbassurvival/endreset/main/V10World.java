package com.bubbassurvival.endreset.main;

import java.io.*;

class V10World implements Serializable
{
    private static final long serialVersionUID = 1L;
    final long hours;
    long lastReset;
    
    V10World(final long hours) {
        super();
        this.hours = hours * 60L * 60L;
        this.lastReset = System.currentTimeMillis() * 1000L;
    }
}
