package com.bubbassurvival.endreset.pillars;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;

import com.bubbassurvival.endreset.main.EndReset;

public class ColumnBuilder {

    public static void placeColumns(World w){
        Chunk[] chunks = w.getLoadedChunks();

        for(Chunk c: chunks){
            if(chunkDoesNotContainPillarAndIsPartOfIsland(c)){
                double chance = Math.random();
                if(chance < (EndReset.towerfrequency / 100)) createPillar(c);
            }
        }
    }

    private static void createPillar(Chunk c){

        PillarType pt = PillarType.chooseRandomPillarType();
        int topX = 0, topZ = 0, hieght = generateRandomHieght();

        int base = c.getChunkSnapshot().getHighestBlockYAt(8, 8) - 1;
        if(c.getBlock(8, base, 8).getType() != Material.ENDER_STONE || base < 32) return;

        int mh = 0;

        for(int layers = pt == PillarType.Mega ? 4 : 1; layers > 0; layers--){

            if(pt == PillarType.Mega) hieght = (int) Math.pow(layers, 2.5);
            ArrayList<Character> cs = PillarType.getBluePrints(pt, layers);

            for(int y = 0; y <= hieght; y++){
                for(int x = 0; x <= 15; x++){
                    for(int z = 0; z <= 15; z++){
                        char block = cs.get(z + (x * 16));

                        if(block == '■'){
                            if(c.getBlock(x, base + y + mh, z).getType() == Material.AIR)
                                c.getBlock(x, base + y + mh, z).setType(Material.OBSIDIAN);
                        }else if(block == 'T'){
                            topX = x;
                            topZ = z;
                            if(c.getBlock(x, base + y + mh, z).getType() == Material.AIR)
                                c.getBlock(x, base + y + mh, z).setType(Material.OBSIDIAN);
                        }
                    }
                }
            }

            mh += hieght;
        }
        
        mh -= 1;
        Location loc = c.getBlock(topX, base + hieght, topZ).getLocation();

        if(pt == PillarType.Mega){
            
            buildTopper(loc, 0, mh, 0);
            buildTopper(loc, 3, mh, 0);
            buildTopper(loc, -3, mh, 0);
            buildTopper(loc, 0, mh, 3);
            buildTopper(loc, 0, mh, -3);
            
        }else{
            
            buildTopper(loc, 0, 0, 0);    
        }
    }
    
    private static void buildTopper(Location loc, int xOff, int yOff, int zOff){
        World w = loc.getWorld();
        
        w.getBlockAt(new Location(w, loc.getX() + xOff, loc.getY() + yOff + 1, loc.getZ() + zOff)).setType(Material.BEDROCK);
        w.getBlockAt(new Location(w, loc.getX() + xOff, loc.getY() + yOff + 2, loc.getZ() + zOff)).setType(Material.FIRE);
        w.spawnEntity(new Location(w, loc.getX() + xOff + 0.5, loc.getY() + yOff + 1, loc.getZ() + zOff + 0.5), EntityType.ENDER_CRYSTAL);
    }

    private static int generateRandomHieght(){
        return new Random().nextInt(35 - 8) + 8;
    }

    private static boolean chunkDoesNotContainPillarAndIsPartOfIsland(Chunk c){
        boolean partOfIsland = false, containsPillar = false;

        for(int y = 32; y <= 64; y++){
            for(int x = 0; x <= 15; x++){
                for(int z = 0; z <= 15; z++){
                    if(c.getBlock(x, y, z).getType() == Material.OBSIDIAN)
                        containsPillar = true;
                    else if(c.getBlock(x, y, z).getType() == Material.ENDER_STONE){
                        partOfIsland = true;
                    }
                }
            }
        }
        return partOfIsland && !containsPillar;
    }

}
