package net.geforcemods.teleportals.handlers;

import java.util.ArrayList;

import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.relauncher.Side;
import net.geforcemods.teleportals.Teleportals;
import net.geforcemods.teleportals.network.packets.PacketCBeaconTeleport;
import net.geforcemods.teleportals.tileentities.TileEntityAdvancedPortal;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

public class ForgeEventHandler {
		
	private ArrayList<TileEntityAdvancedPortal> portals = new ArrayList<TileEntityAdvancedPortal>();
	
	@SubscribeEvent
    public void onConfigChanged(OnConfigChangedEvent event) {
        if(event.modID.equals("teleportals")) {
        	Teleportals.configFile.save();
        }
	}
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event) {
		if(event.player.worldObj.isRemote) {
			return;
		}
		else {
			EntityPlayer player = event.player;
			Teleportals.network.sendTo(new PacketCBeaconTeleport(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY) - 1, MathHelper.floor_double(player.posZ)), (EntityPlayerMP)player);
		}
	}

	@SubscribeEvent
	public void onEntityInteract(PlayerInteractEvent event){
		if(FMLCommonHandler.instance().getEffectiveSide() != Side.SERVER){
			return;
		}
				
		if(event.action == Action.RIGHT_CLICK_BLOCK){
			if(event.entityPlayer.getCurrentEquippedItem() != null && event.entityPlayer.getCurrentEquippedItem().getItem() == Item.getItemFromBlock(Blocks.diamond_block)){
				if(createAdvancedPortal(event, Blocks.emerald_block)){
					event.setCanceled(true);
				}
			}else if(event.entityPlayer.getCurrentEquippedItem() != null && event.entityPlayer.getCurrentEquippedItem().getItem() == Item.getItemFromBlock(Blocks.emerald_block)){
				if(createBasicPortal(event, Blocks.emerald_block)){
					event.setCanceled(true);
				}
			}
		}
				
	}
	
	/**
	 * 
	 * Basic portal creation
	 */
	
	private boolean createBasicPortal(PlayerInteractEvent event, Block par2Block){
		boolean result = checkForXPortal(event.entityPlayer.worldObj, event.x, event.y, event.z, par2Block);
		if(result){
			//X PLANE
			Teleportals.log("We have a portal on X plane! :D");
			int dBlockX = event.x;
			int dBlockY = event.y + 1;
			int dBlockZ = event.z;
			
			event.entityPlayer.getCurrentEquippedItem().stackSize--;
			
			for(int i = 0; i < 3; i++){
				if(i == 0){
					event.entityPlayer.worldObj.setBlock(dBlockX, dBlockY + i, dBlockZ, Teleportals.portal);
				}else{
					event.entityPlayer.worldObj.setBlock(dBlockX, dBlockY + i, dBlockZ, Teleportals.portal);
				}
			}
			
			for(int i = 0; i < 3; i++){
				event.entityPlayer.worldObj.setBlock(dBlockX + 1, dBlockY + i, dBlockZ, Teleportals.portal);				
			}
			
			for(int i = 0; i < 3; i++){
				event.entityPlayer.worldObj.setBlock(dBlockX - 1, dBlockY + i, dBlockZ, Teleportals.portal);				
			}
			
			event.entityPlayer.worldObj.playSoundAtEntity(event.entityPlayer, "fire.ignite", 1.0F, 1.0F);
			return true;
		}else{
			Teleportals.log("No X plane portal found");
		}
		
		boolean result2 = checkForZPortal(event.entityPlayer.worldObj, event.x, event.y, event.z, par2Block);
		
		if(result2){
			//Z PLANE
			Teleportals.log("We have a portal on Z plane! :D");
			
			int dBlockX = event.x;
			int dBlockY = event.y + 1;
			int dBlockZ = event.z;
			
			event.entityPlayer.getCurrentEquippedItem().stackSize--;
			
			for(int i = 0; i < 3; i++){
				if(i == 0){
					event.entityPlayer.worldObj.setBlock(dBlockX, dBlockY + i, dBlockZ, Teleportals.portal);
				}else{
					event.entityPlayer.worldObj.setBlock(dBlockX, dBlockY + i, dBlockZ, Teleportals.portal);
				}
			}
			
			for(int i = 0; i < 3; i++){
				event.entityPlayer.worldObj.setBlock(dBlockX, dBlockY + i, dBlockZ + 1, Teleportals.portal);				
			}
			
			for(int i = 0; i < 3; i++){
				event.entityPlayer.worldObj.setBlock(dBlockX, dBlockY + i, dBlockZ - 1, Teleportals.portal);				
			}
			
			event.entityPlayer.worldObj.playSoundAtEntity(event.entityPlayer, "fire.ignite", 1.0F, 1.0F);
			return true;
		}else{
			Teleportals.log("No Z plane portal found");
		}
		
		return false;
	}

	/**
	 * 
	 * Advanced portal creation
	 */
	
	private boolean createAdvancedPortal(PlayerInteractEvent event, Block par2Block){
		boolean result = checkForXPortal(event.entityPlayer.worldObj, event.x, event.y, event.z, par2Block);
		if(result){
			//X PLANE
			Teleportals.log("We have a portal on X plane! :D");
			int dBlockX = event.x;
			int dBlockY = event.y + 1;
			int dBlockZ = event.z;
			
			int[] cornerBlocks = new int[4];
			int[] cornerBlocksMeta = new int[4];

			cornerBlocks[0] = Block.getIdFromBlock(event.entityPlayer.worldObj.getBlock(dBlockX + 2, dBlockY + 3, dBlockZ));
			cornerBlocks[1] = Block.getIdFromBlock(event.entityPlayer.worldObj.getBlock(dBlockX - 2, dBlockY + 3, dBlockZ));
			cornerBlocks[2] = Block.getIdFromBlock(event.entityPlayer.worldObj.getBlock(dBlockX - 2, dBlockY - 1, dBlockZ));
			cornerBlocks[3] = Block.getIdFromBlock(event.entityPlayer.worldObj.getBlock(dBlockX + 2, dBlockY - 1, dBlockZ));
			
			cornerBlocksMeta[0] = event.entityPlayer.worldObj.getBlockMetadata(dBlockX + 2, dBlockY + 3, dBlockZ);
			cornerBlocksMeta[1] = event.entityPlayer.worldObj.getBlockMetadata(dBlockX - 2, dBlockY + 3, dBlockZ);
			cornerBlocksMeta[2] = event.entityPlayer.worldObj.getBlockMetadata(dBlockX - 2, dBlockY - 1, dBlockZ);
			cornerBlocksMeta[3] = event.entityPlayer.worldObj.getBlockMetadata(dBlockX + 2, dBlockY - 1, dBlockZ);
			
			Teleportals.log("cornerBlocks[0] is " + cornerBlocks[0] + ", cornerBlocks[1] is " + cornerBlocks[1] + ", cornerBlocks[2] is " + cornerBlocks[2] + ", cornerBlocks[3] is " + cornerBlocks[3]);
			event.entityPlayer.getCurrentEquippedItem().stackSize--;
			
			for(int i = 0; i < 3; i++){
				if(i == 0){
					event.entityPlayer.worldObj.setBlock(dBlockX, dBlockY + i, dBlockZ, Teleportals.advancedPortal);
				}else{
					event.entityPlayer.worldObj.setBlock(dBlockX, dBlockY + i, dBlockZ, Teleportals.fillerPortal);
				}
			}
			
			for(int i = 0; i < 3; i++){
				event.entityPlayer.worldObj.setBlock(dBlockX + 1, dBlockY + i, dBlockZ, Teleportals.fillerPortal);				
			}
			
			for(int i = 0; i < 3; i++){
				event.entityPlayer.worldObj.setBlock(dBlockX - 1, dBlockY + i, dBlockZ, Teleportals.fillerPortal);				
			}
			
			TileEntityAdvancedPortal TEAP = (TileEntityAdvancedPortal) event.entityPlayer.worldObj.getTileEntity(dBlockX, dBlockY, dBlockZ);
			TEAP.setCornerBlockIDs(cornerBlocks);
			TEAP.setAdjecentPortalBlocks(new int[]{dBlockX + 1, dBlockY, dBlockZ, dBlockX - 1, dBlockY, dBlockZ});
			TEAP.setPlane("X");
			TEAP.setCornerBlocksMetadata(cornerBlocksMeta);
			TEAP.setDimension(event.entityPlayer.dimension);
			event.entityPlayer.worldObj.playSoundAtEntity(event.entityPlayer, "fire.ignite", 1.0F, 1.0F);
			return true;
		}else{
			Teleportals.log("No X plane portal found");
		}
		
		boolean result2 = checkForZPortal(event.entityPlayer.worldObj, event.x, event.y, event.z, par2Block);
		
		if(result2){
			//Z PLANE
			Teleportals.log("We have a portal on Z plane! :D");
			
			int dBlockX = event.x;
			int dBlockY = event.y + 1;
			int dBlockZ = event.z;
			
			int[] cornerBlocks = new int[4];
			int[] cornerBlocksMeta = new int[4];

			cornerBlocks[0] = Block.getIdFromBlock(event.entityPlayer.worldObj.getBlock(dBlockX, dBlockY + 3, dBlockZ + 2));
			cornerBlocks[1] = Block.getIdFromBlock(event.entityPlayer.worldObj.getBlock(dBlockX, dBlockY + 3, dBlockZ - 2));
			cornerBlocks[2] = Block.getIdFromBlock(event.entityPlayer.worldObj.getBlock(dBlockX, dBlockY - 1, dBlockZ - 2));
			cornerBlocks[3] = Block.getIdFromBlock(event.entityPlayer.worldObj.getBlock(dBlockX, dBlockY - 1, dBlockZ + 2));
			
			cornerBlocksMeta[0] = event.entityPlayer.worldObj.getBlockMetadata(dBlockX, dBlockY + 3, dBlockZ + 2);
			cornerBlocksMeta[1] = event.entityPlayer.worldObj.getBlockMetadata(dBlockX, dBlockY + 3, dBlockZ - 2);
			cornerBlocksMeta[2] = event.entityPlayer.worldObj.getBlockMetadata(dBlockX, dBlockY - 1, dBlockZ - 2);
			cornerBlocksMeta[3] = event.entityPlayer.worldObj.getBlockMetadata(dBlockX, dBlockY - 1, dBlockZ + 2);
							
			Teleportals.log("cornerBlocks[0] is " + cornerBlocks[0] + ", cornerBlocks[1] (" + dBlockX + ", " + (dBlockY + 3) + ", " + (dBlockZ - 1) + ") is " + cornerBlocks[1] + ", cornerBlocks[2] is (" + dBlockX + ", " + (dBlockY - 1) + ", " + (dBlockZ - 1) + ") is " + cornerBlocks[2] + ", cornerBlocks[3] is " + cornerBlocks[3]);
			event.entityPlayer.getCurrentEquippedItem().stackSize--;
			
			for(int i = 0; i < 3; i++){
				if(i == 0){
					event.entityPlayer.worldObj.setBlock(dBlockX, dBlockY + i, dBlockZ, Teleportals.advancedPortal);
				}else{
					event.entityPlayer.worldObj.setBlock(dBlockX, dBlockY + i, dBlockZ, Teleportals.fillerPortal);
				}
			}
			
			for(int i = 0; i < 3; i++){
				event.entityPlayer.worldObj.setBlock(dBlockX, dBlockY + i, dBlockZ + 1, Teleportals.fillerPortal);				
			}
			
			for(int i = 0; i < 3; i++){
				event.entityPlayer.worldObj.setBlock(dBlockX, dBlockY + i, dBlockZ - 1, Teleportals.fillerPortal);				
			}
			
			TileEntityAdvancedPortal TEAP = (TileEntityAdvancedPortal) event.entityPlayer.worldObj.getTileEntity(dBlockX, dBlockY, dBlockZ);
			TEAP.setCornerBlockIDs(cornerBlocks);
			TEAP.setAdjecentPortalBlocks(new int[]{dBlockX, dBlockY, dBlockZ + 1, dBlockX, dBlockY, dBlockZ - 1});
			TEAP.setPlane("Z");
			TEAP.setCornerBlocksMetadata(cornerBlocksMeta);
			TEAP.setDimension(event.entityPlayer.dimension);
			event.entityPlayer.worldObj.playSoundAtEntity(event.entityPlayer, "fire.ignite", 1.0F, 1.0F);
			return true;
		}else{
			Teleportals.log("No Z plane portal found");
		}
		
		return false;
	}
	
	private boolean checkForXPortal(World par1World, int par2, int par3, int par4, Block par5Block){
		boolean result = checkBaseOfXPortal(par1World, par2, par3, par4, par5Block);
		boolean result2 = checkSidesOfXPortal(par1World, par2, par3, par4, par5Block);
		boolean result3 = checkTopOfXPortal(par1World, par2, par3, par4, par5Block);
		Teleportals.log("(DBX: " + result + ", " + result2 + ", " + result3 + ")");
		if(result && result2 && result3){
			return true;
		}
		
		return false;
	}
	
	private boolean checkForZPortal(World par1World, int par2, int par3, int par4, Block par5Block){
		boolean result = checkBaseOfZPortal(par1World, par2, par3, par4, par5Block);
		boolean result2 = checkSidesOfZPortal(par1World, par2, par3, par4, par5Block);
		boolean result3 = checkTopOfZPortal(par1World, par2, par3, par4, par5Block);
		Teleportals.log("(DBZ: " + result + ", " + result2 + ", " + result3 + ")");
		if(result && result2 && result3){
			return true;
		}

		return false;
	}
	
	private boolean checkSidesOfXPortal(World par1World, int par2, int par3, int par4, Block par5Block){
		if(par1World.getBlock(par2 - 2, par3, par4) != Blocks.air && par1World.getBlock(par2 - 2, par3 + 1, par4) == par5Block && par1World.getBlock(par2 - 2, par3 + 2, par4) == par5Block && par1World.getBlock(par2 - 2, par3 + 3, par4) != Blocks.air){
			if(par1World.getBlock(par2 + 2, par3, par4) != Blocks.air && par1World.getBlock(par2 + 2, par3 + 1, par4) == par5Block && par1World.getBlock(par2 + 2, par3 + 2, par4) == par5Block && par1World.getBlock(par2 + 2, par3 + 3, par4) != Blocks.air){
				return true;
			}
		}else if(par1World.getBlock(par2 + 2, par3, par4) != Blocks.air && par1World.getBlock(par2 + 2, par3 + 1, par4) == par5Block && par1World.getBlock(par2 + 2, par3 + 2, par4) == par5Block && par1World.getBlock(par2 + 2, par3 + 3, par4) != Blocks.air){
			if(par1World.getBlock(par2 - 2, par3, par4) != Blocks.air && par1World.getBlock(par2 - 2, par3 + 1, par4) == par5Block && par1World.getBlock(par2 - 2, par3 + 2, par4) == par5Block && par1World.getBlock(par2 - 2, par3 + 3, par4) != Blocks.air){
				return true;
			}
		}
			
		return false;
		
	}

	private boolean checkTopOfXPortal(World par1World, int par2, int par3, int par4, Block par5Block){
		if(par1World.getBlock(par2 + 2, par3 + 4, par4) != Blocks.air && par1World.getBlock(par2 + 1, par3 + 4, par4) == par5Block && par1World.getBlock(par2, par3 + 4, par4) == par5Block && par1World.getBlock(par2 - 1, par3 + 4, par4) == par5Block && par1World.getBlock(par2 - 2, par3 + 4, par4) != Blocks.air){
			return true;
		}else if(par1World.getBlock(par2 - 2, par3 + 4, par4) != Blocks.air && par1World.getBlock(par2 - 1, par3 + 4, par4) == par5Block && par1World.getBlock(par2, par3 + 4, par4)  == par5Block && par1World.getBlock(par2 + 1, par3 + 4, par4)  == par5Block && par1World.getBlock(par2 + 2, par3 + 4, par4) != Blocks.air){
			return true;
		}else{
			return false;
		}
	}

	private boolean checkBaseOfXPortal(World par1World, int par2, int par3, int par4, Block par5Block){
		if(par1World.getBlock(par2 + 2, par3, par4) != Blocks.air && par1World.getBlock(par2 + 1, par3, par4) == par5Block && par1World.getBlock(par2, par3, par4) == par5Block && par1World.getBlock(par2 - 1, par3, par4) == par5Block && par1World.getBlock(par2 - 2, par3, par4) != Blocks.air){
			return true;
		}else if(par1World.getBlock(par2 - 2, par3, par4) != Blocks.air && par1World.getBlock(par2 - 1, par3, par4) == par5Block && par1World.getBlock(par2, par3, par4)  == par5Block && par1World.getBlock(par2 + 1, par3, par4)  == par5Block && par1World.getBlock(par2 + 2, par3, par4) != Blocks.air){
			return true;
		}else{
			return false;
		}
	}
		
	private boolean checkSidesOfZPortal(World par1World, int par2, int par3, int par4, Block par5Block){
		if(par1World.getBlock(par2, par3, par4 - 2) != Blocks.air && par1World.getBlock(par2, par3 + 1, par4 - 2) == par5Block && par1World.getBlock(par2, par3 + 2, par4 - 2) == par5Block && par1World.getBlock(par2, par3 + 3, par4 - 2) != Blocks.air){
			if(par1World.getBlock(par2, par3, par4 + 2) != Blocks.air && par1World.getBlock(par2, par3 + 1, par4 + 2) == par5Block && par1World.getBlock(par2, par3 + 2, par4 + 2) == par5Block && par1World.getBlock(par2, par3 + 3, par4 + 2) != Blocks.air){
				return true;
			}
		}else if(par1World.getBlock(par2, par3, par4 + 2) != Blocks.air && par1World.getBlock(par2, par3 + 1, par4 + 2) == par5Block && par1World.getBlock(par2, par3 + 2, par4 + 2) == par5Block && par1World.getBlock(par2, par3 + 3, par4 + 2) != Blocks.air){
			if(par1World.getBlock(par2, par3, par4 - 2) != Blocks.air && par1World.getBlock(par2, par3 + 1, par4 - 2) == par5Block && par1World.getBlock(par2, par3 + 2, par4 - 2) == par5Block && par1World.getBlock(par2, par3 + 3, par4 - 2) != Blocks.air){
				return true;
			}
		}
		
		return false;
	}
	
	private boolean checkTopOfZPortal(World par1World, int par2, int par3, int par4, Block par5Block){
		if(par1World.getBlock(par2, par3 + 4, par4 + 2) != Blocks.air && par1World.getBlock(par2, par3 + 4, par4 + 1) == par5Block && par1World.getBlock(par2, par3 + 4, par4) == par5Block && par1World.getBlock(par2, par3 + 4, par4 - 1) == par5Block && par1World.getBlock(par2, par3 + 4, par4 - 2) != Blocks.air){
			return true;
		}else if(par1World.getBlock(par2, par3 + 4, par4 - 2) != Blocks.air && par1World.getBlock(par2, par3 + 4, par4 - 1) == par5Block && par1World.getBlock(par2, par3 + 4, par4)  == par5Block && par1World.getBlock(par2, par3 + 4, par4 + 1)  == par5Block && par1World.getBlock(par2, par3 + 4, par4 + 2) != Blocks.air){
			return true;
		}else{
			return false;
		}
	}
	
	private boolean checkBaseOfZPortal(World par1World, int par2, int par3, int par4, Block par5Block){
		if(par1World.getBlock(par2, par3, par4 + 2) != Blocks.air && par1World.getBlock(par2, par3, par4 + 1) == par5Block && par1World.getBlock(par2, par3, par4) == par5Block && par1World.getBlock(par2, par3, par4 - 1) == par5Block && par1World.getBlock(par2, par3, par4 - 2) != Blocks.air){
			return true;
		}else if(par1World.getBlock(par2, par3, par4 - 2) != Blocks.air && par1World.getBlock(par2, par3, par4 - 1) == par5Block && par1World.getBlock(par2, par3, par4)  == par5Block && par1World.getBlock(par2, par3, par4 + 1)  == par5Block && par1World.getBlock(par2, par3, par4 + 2) != Blocks.air){
			return true;
		}else{
			return false;
		}
	}
		
	public ArrayList<TileEntityAdvancedPortal> getAllPortals(){
		return portals;
	}
	

}
