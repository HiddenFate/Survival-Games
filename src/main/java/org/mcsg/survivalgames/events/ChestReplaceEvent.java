package org.mcsg.survivalgames.events;

import java.util.HashSet;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.mcsg.survivalgames.Game;
import org.mcsg.survivalgames.GameManager;
import org.mcsg.survivalgames.Game.GameMode;
import org.mcsg.survivalgames.SurvivalGames;
import org.mcsg.survivalgames.util.ChestRatioStorage;



public class ChestReplaceEvent implements Listener{

	private Random rand = new Random();
	
    @EventHandler(priority = EventPriority.HIGHEST)
    public void ChestListener(PlayerInteractEvent e){
    	SurvivalGames.debug("Interact");
    	if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
    		SurvivalGames.debug("RIGHT CLICKED");
    		BlockState clicked = e.getClickedBlock().getState();
    		System.out.println(clicked.getType());
    		if(clicked instanceof Chest || clicked instanceof DoubleChest){
    			SurvivalGames.debug("clicked chest");
    			int gameid = GameManager.getInstance().getPlayerGameId(e.getPlayer());
    			if(gameid != -1){
    				SurvivalGames.debug("In a game");
    				Game game = GameManager.getInstance().getGame(gameid);
    				if(game.getMode() == GameMode.INGAME){
        				SurvivalGames.debug("Game in INGAME");    					
    					HashSet<Block>openedChest = GameManager.openedChest.get(gameid);
    					openedChest = (openedChest == null)? new HashSet<Block>() : openedChest;
    					if(!openedChest.contains(e.getClickedBlock())){
    						SurvivalGames.debug("New Chest");
    						Inventory[] invs = ((clicked instanceof Chest))? new Inventory[] {((Chest) clicked).getBlockInventory()}
    						: new Inventory[] {((DoubleChest)clicked).getLeftSide().getInventory(), ((DoubleChest)clicked).getRightSide().getInventory()};
    						ItemStack item = invs[0].getItem(0);
    						int level = (item != null && item.getType() == Material.WOOL)? item.getData().getData() + 1 : 1;
    						SurvivalGames.debug(invs +" "+level);
    						for(Inventory inv : invs){
    							inv.setContents(new ItemStack[inv.getContents().length]);
    							SurvivalGames.debug("Looping inv");
    				            for(ItemStack i: ChestRatioStorage.getInstance().getItems(level)){
    				            	SurvivalGames.debug("Looping items");
    				                int l = rand.nextInt(26);
    				                while(inv.getItem(l) != null)
    				                    l = rand.nextInt(26);
    				                inv.setItem(l, i);
    				            }
    						}
    					}
    					openedChest.add(e.getClickedBlock());
    					GameManager.openedChest.put(gameid, openedChest);
    				} else {
    					e.setCancelled(true);
    					return;
    				}
    			}
    		}
    	}
    }
    	
    	
    	
    	
    	
    	
    	
    	/*
    	 * 
    	 * OLD CRAP CODE
    	
        try{
        	
            HashSet<Block>openedChest3 = new HashSet<Block>();

            if(!(e.getAction()==Action.RIGHT_CLICK_BLOCK)) return;

            Block clickedBlock = e.getClickedBlock(); 
            int gameid = GameManager.getInstance().getPlayerGameId(e.getPlayer());
            if(gameid == -1) return;
            GameManager gm = GameManager.getInstance();
            
            if(!gm.isPlayerActive(e.getPlayer())){
                return;
            }
        
            if(gm.getGame(gameid).getMode() != GameMode.INGAME){
            	e.setCancelled(true);
                return;
            }
            
            if(GameManager.openedChest.get(gameid) !=null){
                openedChest3.addAll(GameManager.openedChest.get(gameid));
            }
            
            if(openedChest3.contains(clickedBlock)){
                return;
            }
            
            Inventory inv;
            int size = 0;
            
            if (clickedBlock.getState() instanceof Chest) {
                size = 1;
                inv  = ((Chest) clickedBlock.getState()).getInventory();

            }
            else if(clickedBlock.getState() instanceof DoubleChest){
                size = 2;
                inv = ((DoubleChest) clickedBlock.getState()).getInventory();

            }
            else return;

            inv.clear();
            Random r = new Random();

            for(ItemStack i: ChestRatioStorageOLD.getInstance().getItems()){
                int l = r.nextInt(26 * size);

                while(inv.getItem(l) != null)
                    l = r.nextInt(26 * size);
                inv.setItem(l, i);


            }
            openedChest3.add(clickedBlock);
            GameManager.openedChest.put(gameid, openedChest3);
        }
        catch(Exception e5){}*/



}
