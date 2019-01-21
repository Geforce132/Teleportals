package net.geforcemods.teleportals.block;

import net.geforcemods.teleportals.Teleportals;
import net.geforcemods.teleportals.tileentities.TileEntityBasicPortal;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class BlockBasicTransportPortal extends BlockPortal implements ITileEntityProvider{

	public BlockBasicTransportPortal() {
		super();
		ReflectionHelper.setPrivateValue(Block.class, this, true, 24);
	}

	/**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor Block
     */
    public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_, Block p_149695_5_) {}
		
	/**
     * Triggered whenever an entity collides with this block (enters into the block). Args: world, x, y, z, entity
     */
    public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity)
    {
        if (par1World.isRemote){
            return;
        }else{
        	int l = MathHelper.floor_double((double)(par5Entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        	
        	if(par5Entity instanceof EntityLivingBase){
        		//We've got a player here :O
        	}else{
        		return;
        	}
        	
        	this.checkForNextBasicPortal(par1World, par2, par3, par4, (EntityLivingBase)par5Entity, l, null);
        }
    }
    
    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World p_149726_1_, int p_149726_2_, int p_149726_3_, int p_149726_4_)
    {
        super.onBlockAdded(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_);
    }

    public void breakBlock(World par1World, int par2, int par3, int par4, Block par5Block, int par6)
    {
        super.breakBlock(par1World, par2, par3, par4, par5Block, par6);
        par1World.removeTileEntity(par2, par3, par4);
        
        if(par1World.getBlock(par2, par3 + 1, par4) == Teleportals.portal){
        	par1World.breakBlock(par2, par3 + 1, par4, false);
        }
        
        if(par1World.getBlock(par2, par3 - 1, par4) == Teleportals.portal){
        	par1World.breakBlock(par2, par3 - 1, par4, false);
        }
        
        if(par1World.getBlock(par2 + 1, par3, par4) == Teleportals.portal){
        	par1World.breakBlock(par2 + 1, par3, par4, false);
        }
        
        if(par1World.getBlock(par2 - 1, par3, par4) == Teleportals.portal){
        	par1World.breakBlock(par2 - 1, par3, par4, false);
        }
        
        if(par1World.getBlock(par2, par3, par4 + 1) == Teleportals.portal){
        	par1World.breakBlock(par2, par3, par4 + 1, false);
        }
        
        if(par1World.getBlock(par2, par3, par4 - 1) == Teleportals.portal){
        	par1World.breakBlock(par2, par3, par4 - 1, false);
        }
        
    }

    public boolean onBlockEventReceived(World p_149696_1_, int p_149696_2_, int p_149696_3_, int p_149696_4_, int p_149696_5_, int p_149696_6_)
    {
        super.onBlockEventReceived(p_149696_1_, p_149696_2_, p_149696_3_, p_149696_4_, p_149696_5_, p_149696_6_);
        TileEntity tileentity = p_149696_1_.getTileEntity(p_149696_2_, p_149696_3_, p_149696_4_);
        return tileentity != null ? tileentity.receiveClientEvent(p_149696_5_, p_149696_6_) : false;
    }
    
    private void checkForNextBasicPortal(World par1World, int par2, int par3, int par4, EntityLivingBase par5Entity, int dirOfEntity, TileEntityBasicPortal teIfNeeded){
    	TileEntityBasicPortal extendedTE = null;
    	if(teIfNeeded != null){
    		extendedTE = teIfNeeded;
    	}

    	if(Direction.directions[dirOfEntity] == "NORTH"){
    		for(int i = 1; i <= 50; i++){
    			if(par1World.getBlock(par2, par3, par4 - i) == Teleportals.portal){
    				if((par1World.getTileEntity(par2, par3, par4) != null && par1World.getTileEntity(par2, par3, par4) instanceof TileEntityBasicPortal && ((TileEntityBasicPortal)par1World.getTileEntity(par2, par3, par4)).canPlayerTransport() && !((EntityLivingBase)par5Entity).isSneaking()) || (extendedTE != null && extendedTE.canPlayerTransport() && !((EntityLivingBase)par5Entity).isSneaking())){
    					Teleportals.log("Teleporting NORTH");
    					((TileEntityBasicPortal)par1World.getTileEntity(par2, par3, par4 - i)).onPlayerTransport(par1World, par2, par3, par4 - i);
    					((EntityLivingBase)par5Entity).setPositionAndUpdate((double)par2 + 0.5D, par3, (double)par4 - i); // - 0.5D
    					par1World.playSoundAtEntity(par5Entity, "mob.endermen.portal", 1.0F, 1.0F);
    					if(extendedTE != null){
    						extendedTE.onPlayerTransport(par1World, par2, par3, par4 - i);
    					}else{
    						((TileEntityBasicPortal)par1World.getTileEntity(par2, par3, par4)).onPlayerTransport(par1World, par2, par3, par4 - i);
    					}

    				}
    				break;
    			}else if(par1World.getBlock(par2, par3, par4 - i) == Blocks.glass){
    				Teleportals.log("Focal lens found! | North");
    				this.checkForNextBasicPortal(par1World, par2, par3, par4 - i, par5Entity, dirOfEntity, extendedTE == null ? (TileEntityBasicPortal) par1World.getTileEntity(par2, par3, par4) : extendedTE);
    				break;
    			}
    		}
    	}else if(Direction.directions[dirOfEntity] == "SOUTH"){
    		for(int i = 1; i <= 50; i++){
    			if(par1World.getBlock(par2, par3, par4 + i) == Teleportals.portal){
    				if((par1World.getTileEntity(par2, par3, par4) != null && par1World.getTileEntity(par2, par3, par4) instanceof TileEntityBasicPortal && ((TileEntityBasicPortal)par1World.getTileEntity(par2, par3, par4)).canPlayerTransport() && !((EntityLivingBase)par5Entity).isSneaking()) || (extendedTE != null && extendedTE.canPlayerTransport() && !((EntityLivingBase)par5Entity).isSneaking())){
    					Teleportals.log("Teleporting SOUTH");
    					((TileEntityBasicPortal)par1World.getTileEntity(par2, par3, par4 + i)).onPlayerTransport(par1World, par2, par3, par4 + i);
    					((EntityLivingBase)par5Entity).setPositionAndUpdate((double)par2 + 0.5D, par3, (double)par4 + i);
    					par1World.playSoundAtEntity(par5Entity, "mob.endermen.portal", 1.0F, 1.0F);
    					if(extendedTE != null){
    						extendedTE.onPlayerTransport(par1World, par2, par3, par4 + i);
    					}else{
    						((TileEntityBasicPortal)par1World.getTileEntity(par2, par3, par4)).onPlayerTransport(par1World, par2, par3, par4 + i);
    					}
    				}
    				break;
    			}else if(par1World.getBlock(par2, par3, par4 + i) == Blocks.glass){
    				Teleportals.log("Focal lens found! | South");
    				this.checkForNextBasicPortal(par1World, par2, par3, par4 + i, par5Entity, dirOfEntity, extendedTE == null ? (TileEntityBasicPortal) par1World.getTileEntity(par2, par3, par4) : extendedTE);
    				break;
    			}
    		}
    	}else if(Direction.directions[dirOfEntity] == "EAST"){
    		for(int i = 1; i <= 50; i++){
    			if(par1World.getBlock(par2 + i, par3, par4) == Teleportals.portal){
    				if((par1World.getTileEntity(par2, par3, par4) != null && par1World.getTileEntity(par2, par3, par4) instanceof TileEntityBasicPortal && ((TileEntityBasicPortal)par1World.getTileEntity(par2, par3, par4)).canPlayerTransport() && !((EntityLivingBase)par5Entity).isSneaking()) || (extendedTE != null && extendedTE.canPlayerTransport() && !((EntityLivingBase)par5Entity).isSneaking())){
    					Teleportals.log("Teleporting EAST");
    					((TileEntityBasicPortal)par1World.getTileEntity(par2 + i, par3, par4)).onPlayerTransport(par1World, par2 + i, par3, par4);
    					((EntityLivingBase)par5Entity).setPositionAndUpdate((double)par2 + i, par3, (double)par4 + 0.5D);
    					par1World.playSoundAtEntity(par5Entity, "mob.endermen.portal", 1.0F, 1.0F);
    					if(extendedTE != null){
    						extendedTE.onPlayerTransport(par1World, par2 + i, par3, par4);
    					}else{
    						((TileEntityBasicPortal)par1World.getTileEntity(par2, par3, par4)).onPlayerTransport(par1World, par2 + i, par3, par4);
    					}
    				}
    				break;
    			}else if(par1World.getBlock(par2 + i, par3, par4) == Blocks.glass){
    				Teleportals.log("Focal lens found! | East");
    				this.checkForNextBasicPortal(par1World, par2 + i, par3, par4, par5Entity, dirOfEntity, extendedTE == null ? (TileEntityBasicPortal) par1World.getTileEntity(par2, par3, par4) : extendedTE);
    				break;
    			}
    		}
    	}else if(Direction.directions[dirOfEntity] == "WEST"){
    		for(int i = 1; i <= 50; i++){
    			if(par1World.getBlock(par2 - i, par3, par4) == Teleportals.portal){
    				if((par1World.getTileEntity(par2, par3, par4) != null && par1World.getTileEntity(par2, par3, par4) instanceof TileEntityBasicPortal && ((TileEntityBasicPortal)par1World.getTileEntity(par2, par3, par4)).canPlayerTransport() && !((EntityLivingBase)par5Entity).isSneaking()) || (extendedTE != null && extendedTE.canPlayerTransport() && !((EntityLivingBase)par5Entity).isSneaking())){
    					Teleportals.log("Teleporting WEST");
    					((TileEntityBasicPortal)par1World.getTileEntity(par2 - i, par3, par4)).onPlayerTransport(par1World, par2 - i, par3, par4);
    					((EntityLivingBase)par5Entity).setPositionAndUpdate((double)par2 - i, par3, ((double)par4) + 0.5D);
    					par1World.playSoundAtEntity(par5Entity, "mob.endermen.portal", 1.0F, 1.0F);
    					if(extendedTE != null){
    						extendedTE.onPlayerTransport(par1World, par2 - i, par3, par4);
    					}else{
    						((TileEntityBasicPortal)par1World.getTileEntity(par2, par3, par4)).onPlayerTransport(par1World, par2 - i, par3, par4);
    					}
    				} 		 
    				break;
    			}else if(par1World.getBlock(par2 - i, par3, par4) == Blocks.glass){
    				Teleportals.log("Focal lens found! | West");
    				this.checkForNextBasicPortal(par1World, par2 - i, par3, par4, par5Entity, dirOfEntity, extendedTE == null ? (TileEntityBasicPortal) par1World.getTileEntity(par2, par3, par4) : extendedTE);
    				break;
    			}
    		}
    	}	   
    }

	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityBasicPortal();
	}

}
