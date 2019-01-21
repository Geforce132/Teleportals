package net.geforcemods.teleportals.entity;

import org.lwjgl.input.Keyboard;

import net.geforcemods.teleportals.Teleportals;
import net.geforcemods.teleportals.network.packets.PacketCMinecartCheck;
import net.minecraft.entity.EntityMinecartCommandBlock;
import net.minecraft.entity.ai.EntityMinecartMobSpawner;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.entity.item.EntityMinecartFurnace;
import net.minecraft.entity.item.EntityMinecartHopper;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

public class EntityMinecartPortal extends EntityMinecart{
	
	public int cooldown = 0;

	public EntityMinecartPortal(World par1World) {
		super(par1World);
	}
	
	public EntityMinecartPortal(World par0World, double par1, double par3, double par5) {
		super(par0World, par1, par3, par5);
	}

	/**
     * Creates a new minecart of the specified type in the specified location in the given world. par0World - world to
     * create the minecart in, double par1,par3,par5 represent x,y,z respectively. int par7 specifies the type: 1 for
     * MinecartChest, 2 for MinecartFurnace, 3 for MinecartTNT, 4 for MinecartMobSpawner, 5 for MinecartHopper and 0 for
     * a standard empty minecart
     */
    public static EntityMinecart createMinecart(World par0World, double par1, double par3, double par5, int par7)
    {
        switch (par7)
        {
            case 1:
                return new EntityMinecartChest(par0World, par1, par3, par5);
            case 2:
                return new EntityMinecartFurnace(par0World, par1, par3, par5);
            case 3:
                return new EntityMinecartTNT(par0World, par1, par3, par5);
            case 4:
                return new EntityMinecartMobSpawner(par0World, par1, par3, par5);
            case 5:
                return new EntityMinecartHopper(par0World, par1, par3, par5);
            case 6:
                return new EntityMinecartCommandBlock(par0World, par1, par3, par5);
            case 1337:
            	return new EntityMinecartPortal(par0World, par1, par3, par5);  
            default:
                return new EntityMinecartEmpty(par0World, par1, par3, par5);
        }
    }
    
    public void onUpdate(){
    	super.onUpdate();
    	
    	if(this.worldObj.isRemote){
    		return;
    	}else{
    		if(this.cooldown > 0){
    			this.cooldown--;
    			return;
    		}

    		if(this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer){
    			EntityPlayer player = (EntityPlayer) this.riddenByEntity;
    			
    			Teleportals.network.sendTo(new PacketCMinecartCheck(Keyboard.KEY_SPACE), (EntityPlayerMP) player);
    		}
    	}
    }

	/**
     * First layer of player interaction
     */
    public boolean interactFirst(EntityPlayer par1EntityPlayer)
    {
        if(net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.minecart.MinecartInteractEvent(this, par1EntityPlayer))) return true;
        if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer && this.riddenByEntity != par1EntityPlayer)
        {
            return true;
        }
        else if (this.riddenByEntity != null && this.riddenByEntity != par1EntityPlayer)
        {
            return false;
        }
        else
        {
            if (!this.worldObj.isRemote)
            {
                par1EntityPlayer.mountEntity(this);
            }

            return true;
        }
    }
    
    public String getMinecartDirection(){
    	if(this.motionX > 0.0F){
    		return "EAST";
    	}else if(this.motionX < 0.0F){
    		return "WEST";
    	}else if(this.motionZ > 0.0F){
    		return "SOUTH";
    	}else if(this.motionZ < 0.0F){
    		return "NORTH";
    	}else{
    		return "UNKNOWN";
    	}
    }

	public int getMinecartType() {
		return 1337; // :feelschromosomeman:
	}

}
