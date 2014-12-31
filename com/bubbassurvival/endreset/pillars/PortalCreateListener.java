package com.bubbassurvival.endreset.pillars;

import java.util.List;

import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCreatePortalEvent;

public class PortalCreateListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityCreatePortal(EntityCreatePortalEvent ecp){
        if(ecp.getEntity() instanceof EnderDragon){
            if(numberOfDragonsLeftInWorld(ecp) > 1) ecp.setCancelled(true);
        }
    }

    private int numberOfDragonsLeftInWorld(EntityCreatePortalEvent ecp){

        int rtnNum = 0;
        List<Entity> entities = ecp.getEntity().getWorld().getEntities();

        for(Entity e: entities){
            if(e.getType() == EntityType.ENDER_DRAGON) rtnNum++;
        }

        return rtnNum;
    }
}
