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

        System.out.println("Base |  hieght:" + base + "   type:" + c.getBlock(8, base, 8).getType());

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

        Location loc = c.getBlock(topX, base + hieght + 1, topZ).getLocation();

        if(pt == PillarType.Mega){

            c.getBlock(topX, base + hieght + mh, topZ).setType(Material.BEDROCK);
            c.getBlock(topX, base + hieght + 1 + mh, topZ).setType(Material.FIRE);

            c.getWorld()
                    .spawnEntity(new Location(loc.getWorld(), loc.getX() + 0.5, loc.getY() - 1 + mh, loc.getZ() + 0.5), EntityType.ENDER_CRYSTAL);
        }else{

            c.getBlock(topX, base + hieght + 1, topZ).setType(Material.BEDROCK);
            c.getBlock(topX, base + hieght + 2, topZ).setType(Material.FIRE);

            c.getWorld()
                    .spawnEntity(new Location(loc.getWorld(), loc.getX() + 0.5, loc.getY(), loc.getZ() + 0.5), EntityType.ENDER_CRYSTAL);
        }
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
