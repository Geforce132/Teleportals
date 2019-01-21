package net.geforcemods.teleportals.tileentities;

import net.geforcemods.teleportals.Teleportals;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityBasicPortal extends TileEntity {
	
	private int playerCounter = 0;
	
	public void updateEntity(){
		if(this.getWorld().isRemote){
			return;
		}else{
			if(playerCounter <= 99){
				playerCounter++;
			}
			
		}
	}
	
	public boolean canPlayerTransport(){
		return playerCounter >= 100 ? true : false;
	}
	
	public void onPlayerTransport(World par1World, int par2, int par3, int par4){
		if(playerCounter == 0){
			return;
		}

		playerCounter = 0;
		
		if(par1World.getBlock(par2, par3 + 1, par4) == Teleportals.portal){
        	((TileEntityBasicPortal)par1World.getTileEntity(par2, par3 + 1, par4)).onPlayerTransport(par1World, par2, par3 + 1, par4);
        	return;
        }
        
        if(par1World.getBlock(par2, par3 - 1, par4) == Teleportals.portal){
        	((TileEntityBasicPortal)par1World.getTileEntity(par2, par3 - 1, par4)).onPlayerTransport(par1World, par2, par3 - 1, par4);
        	return;
        }
        
        if(par1World.getBlock(par2 + 1, par3, par4) == Teleportals.portal){
        	((TileEntityBasicPortal)par1World.getTileEntity(par2 + 1, par3, par4)).onPlayerTransport(par1World, par2 + 1, par3, par4);
        	return;
        }
        
        if(par1World.getBlock(par2 - 1, par3, par4) == Teleportals.portal){
        	((TileEntityBasicPortal)par1World.getTileEntity(par2 - 1, par3, par4)).onPlayerTransport(par1World, par2 - 1, par3, par4);
        	return;
        }
        
        if(par1World.getBlock(par2, par3, par4 + 1) == Teleportals.portal){
        	((TileEntityBasicPortal)par1World.getTileEntity(par2, par3, par4 + 1)).onPlayerTransport(par1World, par2, par3, par4 + 1);
        	return;
        }
        
        if(par1World.getBlock(par2, par3, par4 - 1) == Teleportals.portal){
        	((TileEntityBasicPortal)par1World.getTileEntity(par2, par3, par4 - 1)).onPlayerTransport(par1World, par2, par3, par4 - 1);
        	return;
        }
	}

}
