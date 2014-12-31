package com.bubbassurvival.endreset.main;

import java.io.*;

class V10chunk implements Serializable {

    private static final long serialVersionUID = 1L;
    final String world;
    final int x;
    final int z;
    long v;

    V10chunk(final String world, final int x, final int z){
        super();
        this.world = world;
        this.x = x;
        this.z = z;
    }

    @Override
    public int hashCode(){
        int result = 1;
        result = 31 * result + ((this.world == null) ? 0 : this.world.hashCode());
        result = 31 * result + this.x;
        result = 31 * result + this.z;
        return result;
    }

    @Override
    public boolean equals(final Object obj){
        if(this == obj){
            return true;
        }
        if(!(obj instanceof V10chunk)){
            return false;
        }
        final V10chunk other = (V10chunk) obj;
        return this.world.equals(other.world) && this.x == other.x && this.z == other.z;
    }
}
