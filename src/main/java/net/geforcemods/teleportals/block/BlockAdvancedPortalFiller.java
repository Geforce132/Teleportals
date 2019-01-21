package net.geforcemods.teleportals.block;

import net.geforcemods.teleportals.Teleportals;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPortal;
import net.minecraft.entity.Entity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockAdvancedPortalFiller extends BlockPortal{

	public BlockAdvancedPortalFiller() {
		super();
	}
	
	/**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
        int l = func_149999_b(par1IBlockAccess.getBlockMetadata(par2, par3, par4));

        if (l == 0)
        {
        	if ((par1IBlockAccess.getBlock(par2 - 1, par3, par4) == Teleportals.fillerPortal && par1IBlockAccess.getBlock(par2 + 1, par3, par4) != Teleportals.fillerPortal) || (par1IBlockAccess.getBlock(par2 + 1, par3, par4) == Teleportals.fillerPortal && par1IBlockAccess.getBlock(par2 - 1, par3, par4) != Teleportals.fillerPortal) || (par1IBlockAccess.getBlock(par2 + 1, par3, par4) == Teleportals.fillerPortal && par1IBlockAccess.getBlock(par2 - 1, par3, par4) == Teleportals.fillerPortal) || (par1IBlockAccess.getBlock(par2, par3 + 1, par4) == Teleportals.fillerPortal && par1IBlockAccess.getBlock(par2 - 1, par3, par4) == Teleportals.advancedPortal) || (par1IBlockAccess.getBlock(par2, par3 + 1, par4) == Teleportals.fillerPortal && par1IBlockAccess.getBlock(par2 + 1, par3, par4) == Teleportals.advancedPortal))
        	{
                l = 1;
            }
            else
            {
                l = 2;
            }

            if (par1IBlockAccess instanceof World && !((World)par1IBlockAccess).isRemote)
            {
                ((World)par1IBlockAccess).setBlockMetadataWithNotify(par2, par3, par4, l, 2);
            }
        }

        float f = 0.125F;
        float f1 = 0.125F;

        if (l == 1)
        {
            f = 0.5F;
        }

        if (l == 2)
        {
            f1 = 0.5F;
        }

        this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f1, 0.5F + f, 1.0F, 0.5F + f1);
    }
	
	
	/**
     * Triggered whenever an entity collides with this block (enters into the block). Args: world, x, y, z, entity
     */
    public void onEntityCollidedWithBlock(World p_149670_1_, int p_149670_2_, int p_149670_3_, int p_149670_4_, Entity p_149670_5_){}
    
    public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_, Block p_149695_5_){}

    public void breakBlock(World par1World, int par2, int par3, int par4, Block par5Block, int par6){
    	super.breakBlock(par1World, par2, par3, par4, par5Block, par6);
    	
    	if(par1World.getBlock(par2, par3 + 1, par4) == Teleportals.fillerPortal){
        	par1World.breakBlock(par2, par3 + 1, par4, false);
        }
        
        if(par1World.getBlock(par2, par3 - 1, par4) == Teleportals.fillerPortal){
        	par1World.breakBlock(par2, par3 - 1, par4, false);
        }
        
        if(par1World.getBlock(par2 + 1, par3, par4) == Teleportals.fillerPortal){
        	par1World.breakBlock(par2 + 1, par3, par4, false);
        }
        
        if(par1World.getBlock(par2 - 1, par3, par4) == Teleportals.fillerPortal){
        	par1World.breakBlock(par2 - 1, par3, par4, false);
        }
        
        if(par1World.getBlock(par2, par3, par4 + 1) == Teleportals.fillerPortal){
        	par1World.breakBlock(par2, par3, par4 + 1, false);
        }
        
        if(par1World.getBlock(par2, par3, par4 - 1) == Teleportals.fillerPortal){
        	par1World.breakBlock(par2, par3, par4 - 1, false);
        }
        
        //----------------
        
        if(par1World.getBlock(par2, par3 + 1, par4) == Teleportals.advancedPortal){
        	par1World.breakBlock(par2, par3 + 1, par4, false);
        }
        
        if(par1World.getBlock(par2, par3 - 1, par4) == Teleportals.advancedPortal){
        	par1World.breakBlock(par2, par3 - 1, par4, false);
        }
        
        if(par1World.getBlock(par2 + 1, par3, par4) == Teleportals.advancedPortal){
        	par1World.breakBlock(par2 + 1, par3, par4, false);
        }
        
        if(par1World.getBlock(par2 - 1, par3, par4) == Teleportals.advancedPortal){
        	par1World.breakBlock(par2 - 1, par3, par4, false);
        }
        
        if(par1World.getBlock(par2, par3, par4 + 1) == Teleportals.advancedPortal){
        	par1World.breakBlock(par2, par3, par4 + 1, false);
        }
        
        if(par1World.getBlock(par2, par3, par4 - 1) == Teleportals.advancedPortal){
        	par1World.breakBlock(par2, par3, par4 - 1, false);
        }
    }

}
