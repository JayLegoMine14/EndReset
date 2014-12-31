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
        ArrayList<Character> cs = PillarType.getBluePrints(PillarType.chooseRandomPillarType());
        int base = c.getChunkSnapshot().getHighestBlockYAt(8, 8) - 1;

        int topX = 0, topZ = 0, hieght = generateRandomHieght();

        for(int y = 0; y <= hieght; y++){
            for(int x = 0; x <= 15; x++){
                for(int z = 0; z <= 15; z++){
                    char block = cs.get(z + (x * 16));

                    if(block == '■'){
                        if(c.getBlock(x, base + y, z).getType() == Material.AIR)
                            c.getBlock(x, base + y, z).setType(Material.OBSIDIAN);
                    }else if(block == 'T'){
                        topX = x;
                        topZ = z;
                        if(c.getBlock(x, base + y, z).getType() == Material.AIR)
                            c.getBlock(x, base + y, z).setType(Material.OBSIDIAN);
                    }
                }
            }
        }

        c.getBlock(topX, base + hieght + 1, topZ).setType(Material.BEDROCK);
        c.getBlock(topX, base + hieght + 2, topZ).setType(Material.FIRE);
        Location loc = c.getBlock(topX, base + hieght + 1, topZ).getLocation();
        c.getWorld()
                .spawnEntity(new Location(loc.getWorld(), loc.getX() + 0.5, loc.getY(), loc.getZ() + 0.5), EntityType.ENDER_CRYSTAL);
    }

    private static int generateRandomHieght(){
        return new Random().nextInt(35 - 8) + 8;
    }

    private static boolean chunkDoesNotContainPillarAndIsPartOfIsland(Chunk c){
        boolean partOfIsland = false, containsPillar = false;

        for(int y = 0; y <= 256; y++){
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
