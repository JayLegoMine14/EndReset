package com.bubbassurvival.endreset.main;

import org.bukkit.plugin.java.*;

import java.util.concurrent.atomic.*;
import java.util.logging.*;

import org.bukkit.scheduler.*;
import org.bukkit.plugin.*;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

import org.bukkit.event.world.*;
import org.bukkit.*;
import org.bukkit.event.entity.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;

import com.bubbassurvival.endreset.pillars.ColumnBuilder;
import com.bubbassurvival.endreset.pillars.PortalCreateListener;

import java.io.*;

public class EndReset extends JavaPlugin implements Listener, Runnable
{
	public Server s;
	
    private final HashMap<String, HashMap<String, Long>> v10chunks;
    private final HashMap<String, Long> cvs;
    private final HashSet<String> reg;
    private final HashMap<String, Integer> pids;
    private long it;
    private boolean save;
    private final AtomicBoolean saveLock;
    private final HashSet<String> dontHandle;
    private final HashMap<String, V10World> forceReset;
    private final HashMap<String, Short> dragonAmount;
    private final HashMap<String, RegenThread> threads;
    private final HashMap<String, Long> suspendedTasks;
    
    FileConfiguration config;
    public static double towerfrequency;
    
    public EndReset() {
        super();
        this.v10chunks = new HashMap<String, HashMap<String, Long>>();
        this.cvs = new HashMap<String, Long>();
        this.reg = new HashSet<String>();
        this.pids = new HashMap<String, Integer>();
        this.it = 1200L;
        this.save = false;
        this.saveLock = new AtomicBoolean(false);
        this.dontHandle = new HashSet<String>();
        this.forceReset = new HashMap<String, V10World>();
        this.dragonAmount = new HashMap<String, Short>();
        this.threads = new HashMap<String, RegenThread>();
        this.suspendedTasks = new HashMap<String, Long>();
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public void onEnable() {
        s = this.getServer();
        final Logger log = this.getLogger();
        
        config = this.getConfig();
		config.options().header("towerfrequency is the percent chance a tower will spawn on a viable block.");
		config.addDefault("towerfrequency", 10.0);
		config.options().copyDefaults(true);
		saveConfig();
		
		try{
			towerfrequency = config.getDouble("towerfrequency");
		}catch (IllegalArgumentException e){
			this.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Error loading config.yml!"
					+ " Unparsable value in config! Caused by: " + e.getMessage());
		}
        
        final BukkitScheduler bs = s.getScheduler();
        try {
            File f = new File(this.getDataFolder(), "EndReset.sav");
            boolean nf;
            if (!f.exists()) {
                final File f2 = new File("plugins/EndReset.sav");
                if (f2.exists()) {
                    this.getDataFolder().mkdirs();
                    f2.renameTo(f);
                    f = f2;
                    nf = false;
                }
                else {
                    nf = true;
                }
            }
            else {
                nf = false;
            }
            if (nf) {
                this.getDataFolder().mkdir();
            }
            else {
                final ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
                Object[] sa = null;
                int sfv;
                try {
                    final Object o = in.readObject();
                    if (o == null || !(o instanceof Object[])) {
                        log.info("ERROR: Can't read savefile!");
                        s.getPluginManager().disablePlugin((Plugin)this);
                        in.close();
                        return;
                    }
                    sa = (Object[])o;
                    sfv = (int)sa[0];
                }
                catch (OptionalDataException e10) {
                    sfv = in.readInt();
                }
                if (sfv < 6) {
                    boolean save = false;
                    if (sfv < 4) {
                        for (final Object vc : (ArrayList)sa[1]) {
                            HashMap<String, Long> tmpMap;
                            if (this.v10chunks.containsKey(((V10chunk)vc).world)) {
                                tmpMap = this.v10chunks.get(((V10chunk)vc).world);
                            }
                            else {
                                tmpMap = new HashMap<String, Long>();
                                this.v10chunks.put(((V10chunk)vc).world, tmpMap);
                            }
                            tmpMap.put(String.valueOf(((V10chunk)vc).x) + "/" + ((V10chunk)vc).z, ((V10chunk)vc).v);
                        }
                        save = true;
                    }
                    else {
                        for (final Object e : ((HashMap)sa[1]).entrySet()) {
                        	Map.Entry<String, HashMap<String, Long>> e2 = (Map.Entry<String, HashMap<String, Long>>) e;
                            this.v10chunks.put(e2.getKey(), e2.getValue());
                        }
                    }
                    for (final Object e2 : ((HashMap)sa[2]).entrySet()) {
                    	Map.Entry<String, Long> e3 = (Map.Entry<String, Long>) e2;
                        this.cvs.put(e3.getKey(), e3.getValue());
                    }
                    int i;
                    if (sfv < 2) {
                        i = 4;
                    }
                    else {
                        i = 3;
                    }
                    for (final Object regen : (HashSet)sa[i]) {
                        this.reg.add((String)regen);
                    }
                    this.it = (long)sa[i + 1];
                    if (sfv > 2) {
                        for (final Object dh : (HashSet)sa[5]) {
                            this.dontHandle.add((String)dh);
                        }
                        for (final Object e3 : ((HashMap)sa[6]).entrySet()) {
                            this.forceReset.put(((Map.Entry<String, V10World>)e3).getKey()
                            		, ((Map.Entry<String, V10World>)e3).getValue());
                        }
                        for (final Object e4 : ((HashMap)sa[7]).entrySet()) {
                            this.dragonAmount.put(((Map.Entry<String, Short>)e4).getKey(), ((Map.Entry<String, Short>)e4).getValue());
                        }
                    }
                    this.save = save;
                }
                else {
                    for (final Object e5 : ((HashMap)in.readObject()).entrySet()) {
                        this.v10chunks.put(((Map.Entry<String, HashMap<String, Long>>)e5).getKey(), 
                        		((Map.Entry<String, HashMap<String, Long>>)e5).getValue());
                    }
                    for (final Object e6 : ((HashMap)in.readObject()).entrySet()) {
                        this.cvs.put(((Map.Entry<String, Long>)e6).getKey(), ((Map.Entry<String, Long>)e6).getValue());
                    }
                    for (final Object regen2 : (HashSet)in.readObject()) {
                        this.reg.add((String)regen2);
                    }
                    this.it = in.readLong();
                    for (final Object dh2 : (HashSet)in.readObject()) {
                        this.dontHandle.add((String)dh2);
                    }
                    for (final Object e7 : ((HashMap)in.readObject()).entrySet()) {
                        this.forceReset.put(((Map.Entry<String, V10World>)e7).getKey(), ((Map.Entry<String, V10World>)e7).getValue());
                    }
                    for (final Object e8 : ((HashMap)in.readObject()).entrySet()) {
                        this.dragonAmount.put(((Map.Entry<String, Short>)e8).getKey(), ((Map.Entry<String, Short>)e8).getValue());
                    }
                    for (final Object e6 : ((HashMap)in.readObject()).entrySet()) {
                        final String w = ((Map.Entry<String, Long>)e6).getKey();
                        this.reg.add(w);
                        final World wo = s.getWorld(w);
                        if (wo != null) {
                            final long tr = ((Map.Entry<String, Long>)e6).getValue();
                            final RegenThread rt = new RegenThread(w, wo.getFullTime() + tr);
                            bs.scheduleSyncDelayedTask((Plugin)this, (Runnable)rt, tr);
                        }
                    }
                }
                in.close();
            }
        }
        catch (Exception e9) {
            log.info("can't read savefile!");
            e9.printStackTrace();
            s.getPluginManager().disablePlugin((Plugin)this);
            return;
        }
        
        this.saveConfig();
        for (final World w2 : s.getWorlds()) {
            if (w2.getEnvironment() != World.Environment.THE_END) {
                continue;
            }
            this.onWorldLoad(new WorldLoadEvent(w2));
        }
        
        //bs.scheduleSyncRepeatingTask((Plugin)this, new HealthThread(), 1L, 50L);
        bs.scheduleSyncRepeatingTask((Plugin)this, new SaveThread(), 36000L, 36000L);
        bs.scheduleSyncRepeatingTask((Plugin)this, (Runnable)this, 1L, 1L);
        bs.scheduleSyncRepeatingTask((Plugin)this, new ForceThread(), 20L, 72000L);
        final PluginManager pm = s.getPluginManager();
        pm.registerEvents((Listener)this, (Plugin)this);
        pm.registerEvents(new PortalCreateListener(), (Plugin)this);
        log.info("v" + this.getDescription().getVersion() + " enabled!");
    }
    
    public void onDisable() {
        final Server s = this.getServer();
        s.getScheduler().cancelTasks((Plugin)this);
        if (this.save) {
            new SaveThread().run();
        }
        s.getLogger().info("[" + this.getName() + "] disabled!");
    }
    
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!sender.hasPermission("endreset.config")) {
            return true;
        }
        if (args.length >= 1) {
            try {
                this.it = Integer.parseInt(args[0]);
                sender.sendMessage("New inactive time: " + this.it + " minutes");
                this.it = this.it * 20L * 60L;
            }
            catch (NumberFormatException e) {
                if (args[0].equalsIgnoreCase("force")) {
                    if (args.length < 3) {
                        sender.sendMessage("/EndReset force add/remove World_Name");
                        return true;
                    }
                    if (args[1].equalsIgnoreCase("add")) {
                        if (args.length < 4) {
                            sender.sendMessage("/EndReset force add World_Name hours");
                            return true;
                        }
                        try {
                            this.forceReset.put(args[2], new V10World(Integer.parseInt(args[3])));
                        }
                        catch (NumberFormatException ex) {
                            sender.sendMessage("Invalid hours: " + args[3]);
                            return true;
                        }
                        sender.sendMessage(ChatColor.GREEN + args[2] + " will reset every " + args[3] + " hours now!");
                    }
                    else {
                        if (!args[1].equalsIgnoreCase("remove")) {
                            sender.sendMessage("/EndReset force add/remove");
                            return true;
                        }
                        if (!this.forceReset.containsKey(args[2])) {
                            sender.sendMessage("World " + args[2] + " not found!");
                            return true;
                        }
                        this.forceReset.remove(args[2]);
                        sender.sendMessage(ChatColor.GOLD + args[2] + " won't reset at a given time intervall anymore!");
                    }
                }
                else if (args[0].equalsIgnoreCase("ignore")) {
                    if (args.length < 2) {
                        sender.sendMessage("/EndReset ignore World_Name");
                        return true;
                    }
                    if (this.dontHandle.contains(args[1])) {
                        this.dontHandle.remove(args[1]);
                        sender.sendMessage(ChatColor.YELLOW + args[1] + " is no longer ignored!");
                    }
                    else {
                        this.dontHandle.add(args[1]);
                        sender.sendMessage(ChatColor.YELLOW + args[1] + " is ignored now!");
                    }
                }
                else if (args[0].equalsIgnoreCase("amount")) {
                    if (args.length < 3) {
                        sender.sendMessage("/EndReset amount World_Name X");
                        return true;
                    }
                    short a;
                    try {
                        a = Short.parseShort(args[2]);
                    }
                    catch (NumberFormatException ex2) {
                        sender.sendMessage("Invalid amount: " + args[2]);
                        return true;
                    }
                    if (a == 1) {
                        this.dragonAmount.remove(args[1]);
                    }
                    else {
                        this.dragonAmount.put(args[1], a);
                    }
                    sender.sendMessage(ChatColor.BLUE + "New dragon amount for world " + args[1] + ": " + a);
                }
                else {
                    if (!args[0].equalsIgnoreCase("list")) {
                        return false;
                    }
                    final List<World> wl = (List<World>)this.getServer().getWorlds();
                    if (wl.isEmpty()) {
                        sender.sendMessage(ChatColor.RED + "No worlds found!");
                        return true;
                    }
                    final StringBuilder sb = new StringBuilder();
                    boolean first = true;
                    for (int i = 1; i < wl.size(); ++i) {
                        final World world = wl.get(i);
                        if (world != null && world.getEnvironment() == World.Environment.THE_END) {
                            if (!first) {
                                sb.append(' ');
                            }
                            else {
                                first = false;
                            }
                            sb.append(world.getName());
                        }
                    }
                    sb.insert(0, ChatColor.LIGHT_PURPLE);
                    sender.sendMessage(sb.toString());
                }
            }
            return this.save = true;
        }
        if (!(sender instanceof Player)) {
            return true;
        }
        final World world2 = ((Player)sender).getWorld();
        if (world2.getEnvironment() != World.Environment.THE_END) {
            sender.sendMessage(ChatColor.RED + "Not an end world!");
            return true;
        }
        for (final Player p : world2.getPlayers()) {
            p.teleport(this.getServer().getWorlds().get(0).getSpawnLocation());
            p.sendMessage("This world is resetting!");
        }
        final String wn = world2.getName();
        final long toRun = 1L;
        final RegenThread t = new RegenThread(wn, world2.getFullTime() + toRun);
        this.pids.put(wn, this.getServer().getScheduler().scheduleSyncDelayedTask((Plugin)this, (Runnable)t, toRun));
        this.threads.put(wn, t);
        return true;
    }
    
    private void regen(final World world) {
        final String wn = world.getName();
        long cv = this.cvs.get(wn) + 1L;
        if (cv == Long.MAX_VALUE) {
            cv = Long.MIN_VALUE;
        }
        this.cvs.put(wn, cv);
        Chunk[] loadedChunks;
        for (int length = (loadedChunks = world.getLoadedChunks()).length, j = 0; j < length; ++j) {
            final Chunk c = loadedChunks[j];
            this.onChunkLoad(new ChunkLoadEvent(c, false));
        }
        short a;
        if (this.dragonAmount.containsKey(wn)) {
            a = this.dragonAmount.get(wn);
        }
        else {
            a = 1;
        }
        if (a > 1) {
            --a;
            final Location loc = world.getSpawnLocation();
            loc.setY((double)(world.getMaxHeight() - 1));
            for (short i = 0; i < a; ++i) {
            	world.spawnEntity(loc, EntityType.ENDER_DRAGON);
            }
        }
        
        ColumnBuilder.placeColumns(world);
        
        this.save = true;
        this.getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + wn + " has been reset!");
    }
    
    public void run() {
        final BukkitScheduler s = this.getServer().getScheduler();
        for (final World w : this.getServer().getWorlds()) {
            if (w.getEnvironment() != World.Environment.THE_END) {
                continue;
            }
            final String wn = w.getName();
            if (!this.reg.contains(wn)) {
                continue;
            }
            final int pc = w.getPlayers().size();
            if (pc < 1 && !this.pids.containsKey(wn)) {
                long tr;
                if (!this.suspendedTasks.containsKey(wn)) {
                    tr = this.it;
                }
                else {
                    tr = this.suspendedTasks.get(wn);
                    this.suspendedTasks.remove(wn);
                }
                final RegenThread t = new RegenThread(wn, w.getFullTime() + tr);
                this.pids.put(wn, s.scheduleSyncDelayedTask((Plugin)this, (Runnable)t, tr));
                this.threads.put(wn, t);
            }
            else {
                if (pc <= 0 || !this.pids.containsKey(wn)) {
                    continue;
                }
                final int pid = this.pids.get(wn);
                s.cancelTask(pid);
                this.pids.remove(wn);
                this.suspendedTasks.put(wn, this.threads.get(wn).getRemainingDelay());
                this.threads.remove(wn);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDeath(final EntityDeathEvent event) {
        final Entity e = (Entity)event.getEntity();
        if (!(e instanceof EnderDragon)) {
            return;
        }
        final World w = e.getWorld();
        if (w.getEnvironment() != World.Environment.THE_END) {
            return;
        }
        final String wn = w.getName();
        if (this.dontHandle.contains(wn)) {
            return;
        }
        this.reg.add(wn);
        this.save = true;
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onChunkLoad(final ChunkLoadEvent event) {
        if (event.getWorld().getEnvironment() != World.Environment.THE_END) {
            return;
        }
        final World world = event.getWorld();
        final String wn = world.getName();
        HashMap<String, Long> worldMap;
        if (this.v10chunks.containsKey(wn)) {
            worldMap = this.v10chunks.get(wn);
        }
        else {
            worldMap = new HashMap<String, Long>();
            this.v10chunks.put(wn, worldMap);
        }
        final Chunk chunk = event.getChunk();
        final int x = chunk.getX();
        final int z = chunk.getZ();
        final String hash = String.valueOf(x) + "/" + z;
        final long cv = this.cvs.get(wn);
        if (worldMap.containsKey(hash)) {
            if (worldMap.get(hash) != cv) {
                Entity[] entities;
                for (int length = (entities = chunk.getEntities()).length, i = 0; i < length; ++i) {
                    final Entity e = entities[i];
                    e.remove();
                }
                world.regenerateChunk(x, z);
                worldMap.put(hash, cv);
                this.save = true;
            }
        }
        else {
            worldMap.put(hash, cv);
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onWorldLoad(final WorldLoadEvent event) {
        final World w = event.getWorld();
        if (w.getEnvironment() != World.Environment.THE_END) {
            return;
        }
        final String wn = w.getName();
        if (!this.cvs.containsKey(wn)) {
            this.cvs.put(wn, Long.MIN_VALUE);
            this.save = true;
        }
    }
    
    static /* synthetic */ void access$5(final EndReset endReset, final boolean save) {
        endReset.save = save;
    }
    
    private class RegenThread implements Runnable
    {
        private final String wn;
        private final long toRun;
        
        private RegenThread(final String wn, final long toRun) {
            super();
            this.wn = wn;
            this.toRun = toRun;
        }
        
        @Override
        public void run() {
            if (!EndReset.this.pids.containsKey(this.wn)) {
                return;
            }
            final World w = EndReset.this.getServer().getWorld(this.wn);
            if (w != null) {
                EndReset.this.regen(w);
            }
            EndReset.this.reg.remove(this.wn);
            EndReset.this.pids.remove(this.wn);
            EndReset.this.threads.remove(this);
        }
        
        long getRemainingDelay() {
            final World w = EndReset.this.getServer().getWorld(this.wn);
            if (w == null) {
                return -1L;
            }
            return this.toRun - w.getFullTime();
        }
    }
    
    /*private class HealthThread implements Runnable
    {
        @Override
        public void run() {
        	for(World w : s.getWorlds()){
            	if(w.getEnvironment() == World.Environment.THE_END){
            		for(Entity ae : w.getEntities()){
            			if(ae.getType() == EntityType.ENDER_DRAGON){
            				LivingEntity e = (LivingEntity) ae;
            				 if(e.getHealth() + 1 < e.getMaxHealth() && e.getHealth() != 0.0)
            	        		  e.setHealth(e.getHealth() + 1);
            			}
            		}
            	}
        	}
        }
    }*/
    
    private class ForceThread implements Runnable
    {
        @Override
        public void run() {
            if (EndReset.this.forceReset.isEmpty()) {
                return;
            }
            final long now = System.currentTimeMillis() * 1000L;
            final Server s = EndReset.this.getServer();
            for (final Map.Entry<String, V10World> e : EndReset.this.forceReset.entrySet()) {
                final V10World vw = e.getValue();
                if (vw.lastReset + vw.hours >= now) {
                    EndReset.this.regen(s.getWorld((String)e.getKey()));
                    vw.lastReset = now;
                    EndReset.access$5(EndReset.this, true);
                }
            }
        }
    }
    
    private class SaveThread implements Runnable
    {
        @SuppressWarnings("deprecation")
		@Override
        public void run() {
            if (!EndReset.this.save) {
                return;
            }
            EndReset.access$5(EndReset.this, false);
            while (!EndReset.this.saveLock.compareAndSet(false, true)) {}
            try {
                final File f = new File(EndReset.this.getDataFolder(), "EndReset.sav");
                if (!f.exists()) {
                    f.createNewFile();
                }
                final ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
                out.writeInt(6);
                out.writeObject(EndReset.this.v10chunks);
                out.writeObject(EndReset.this.cvs);
                out.writeObject(EndReset.this.reg);
                out.writeLong(EndReset.this.it);
                out.writeObject(EndReset.this.dontHandle);
                out.writeObject(EndReset.this.forceReset);
                out.writeObject(EndReset.this.dragonAmount);
                out.writeObject(EndReset.this.suspendedTasks);
                EndReset.this.getServer().getScheduler().scheduleAsyncDelayedTask((Plugin)EndReset.this, (Runnable)new AsyncSaveThread(out));
            }
            catch (Exception e) {
                EndReset.this.saveLock.set(false);
                EndReset.this.getServer().getLogger().info("[" + EndReset.this.getName() + "] can't write savefile!");
                e.printStackTrace();
            }
        }
    }
    
    public class AsyncSaveThread implements Runnable
    {
        private final ObjectOutputStream out;
        
        private AsyncSaveThread(final ObjectOutputStream out) {
            super();
            this.out = out;
        }
        
        @Override
        public void run() {
            try {
                this.out.flush();
                this.out.close();
            }
            catch (Exception e) {
                EndReset.this.getServer().getLogger().info("[" + EndReset.this.getName() + "] can't write savefile!");
                e.printStackTrace();
            }
            EndReset.this.saveLock.set(false);
        }
    }
}
