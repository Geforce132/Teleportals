package net.geforcemods.teleportals.block;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.geforcemods.teleportals.Teleportals;
import net.geforcemods.teleportals.network.packets.PacketSetEntityHeadRotation;
import net.geforcemods.teleportals.tileentities.TileEntityAdvancedPortal;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockAdvancedTransportPortal extends BlockPortal implements ITileEntityProvider{

	public BlockAdvancedTransportPortal() {
		super();
		ReflectionHelper.setPrivateValue(Block.class, this, true, 24);
	}

	public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity) {
		if(par1World.isRemote) {
    		return;
    	}
		else {
    		if(par5Entity instanceof EntityLivingBase) {
        		//We've got a player here :O
        	}
    		else {
        		return;
        	}

			int[] cornerBlockIDs = ((TileEntityAdvancedPortal)par1World.getTileEntity(par2, par3, par4)).getCornerBlockIDs();
			int[] cornerBlockMeta = ((TileEntityAdvancedPortal)par1World.getTileEntity(par2, par3, par4)).getCornerBlocksMetadata();
			Teleportals.log("Checking portal list, size " + Teleportals.eventHandler.getAllPortals().size());
			for(int i = 0; i < Teleportals.eventHandler.getAllPortals().size(); i++){
				if(Teleportals.eventHandler.getAllPortals().get(i) == null){
					Teleportals.log(i + " is null! Breaking!");
					break;
				}else if(Teleportals.eventHandler.getAllPortals().get(i).xCoord == par2 && Teleportals.eventHandler.getAllPortals().get(i).yCoord == par3 && Teleportals.eventHandler.getAllPortals().get(i).zCoord == par4){
					Teleportals.log("Checking own portal...");
					continue;
				}else if(!Teleportals.eventHandler.getAllPortals().get(i).canPlayerTransport()){
					Teleportals.log("player can not teleport to portal " + i + " in dimension " + Teleportals.eventHandler.getAllPortals().get(i).getDimension() + " from dimension " +  ((EntityLivingBase)par5Entity).dimension + ". Continuing...");
					continue;
				}else{
					int[] tempArray = Teleportals.eventHandler.getAllPortals().get(i).getCornerBlockIDs();
					int[] tempArrayMeta = Teleportals.eventHandler.getAllPortals().get(i).getCornerBlocksMetadata();
					Teleportals.log("Comparing corner blocks (from portal " + i + " at " + Teleportals.eventHandler.getAllPortals().get(i).xCoord + ", " + Teleportals.eventHandler.getAllPortals().get(i).yCoord + ", " + Teleportals.eventHandler.getAllPortals().get(i).zCoord + ") (" + (Block.getBlockById(tempArray[0]).getUnlocalizedName().substring(5)) + ", " + (Block.getBlockById(tempArray[1]).getUnlocalizedName().substring(5)) + ", " + (Block.getBlockById(tempArray[2]).getUnlocalizedName().substring(5)) + ", " + (Block.getBlockById(tempArray[3]).getUnlocalizedName().substring(5)) + ") to (" + (Block.getBlockById(cornerBlockIDs[0]).getUnlocalizedName().substring(5)) + ", " + (Block.getBlockById(cornerBlockIDs[1]).getUnlocalizedName().substring(5)) + ", " + (Block.getBlockById(cornerBlockIDs[2]).getUnlocalizedName().substring(5)) + ", " + (Block.getBlockById(cornerBlockIDs[3]).getUnlocalizedName().substring(5)) + ")");
					if(test(tempArray, cornerBlockIDs) && test(tempArrayMeta, cornerBlockMeta)){
						Teleportals.log("Other portal detected");
						
							Teleportals.log("Has filler blocks!");
							if(Teleportals.eventHandler.getAllPortals().get(i).canPlayerTransport()){
								Teleportals.log("Portal to teleport to is going along the " + Teleportals.eventHandler.getAllPortals().get(i).getPlane() + " plane.");
								Teleportals.log("Teleporting!");
								
								if(Teleportals.eventHandler.getAllPortals().get(i).getDimension() != ((EntityLivingBase)par5Entity).dimension){
									((EntityLivingBase)par5Entity).travelToDimension(Teleportals.eventHandler.getAllPortals().get(i).getDimension());
								}else{
									Teleportals.eventHandler.getAllPortals().get(i).onPlayerTransport();	
								}
								
								if(Teleportals.eventHandler.getAllPortals().get(i).getPlane().matches("Z")){
									((EntityLivingBase)par5Entity).setPositionAndUpdate(Teleportals.eventHandler.getAllPortals().get(i).xCoord + 2, Teleportals.eventHandler.getAllPortals().get(i).yCoord, Teleportals.eventHandler.getAllPortals().get(i).zCoord);
									
									if(par5Entity instanceof EntityPlayer){
										Teleportals.log("PSEHR is being sent | Z Plane");
										Teleportals.network.sendTo(new PacketSetEntityHeadRotation(270F), (EntityPlayerMP) par5Entity);
									}
								}else{
									((EntityLivingBase)par5Entity).setPositionAndUpdate(Teleportals.eventHandler.getAllPortals().get(i).xCoord, Teleportals.eventHandler.getAllPortals().get(i).yCoord, Teleportals.eventHandler.getAllPortals().get(i).zCoord + 2);
									if(par5Entity instanceof EntityPlayer){
										Teleportals.log("PSEHR is being sent | X Plane");
										Teleportals.network.sendTo(new PacketSetEntityHeadRotation(359F), (EntityPlayerMP) par5Entity);
									}
								}
								
								par1World.playSoundAtEntity(par5Entity, "mob.endermen.portal", 1.0F, 1.0F);
								((TileEntityAdvancedPortal)par1World.getTileEntity(par2, par3, par4)).onPlayerTransport();
								break;
							}
								
					}else{
						//mod_Teleportals.log("[" + i + "] Not correct portal. Continuing...");
					}
				}
	    	}
	    }
	}

	/**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor Block
     */
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block par5Block) {}
    
    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
        super.onBlockAdded(par1World, par2, par3, par4);
    }

    public void breakBlock(World par1World, int par2, int par3, int par4, Block par5Block, int par6)
    {
    	String dir = ((TileEntityAdvancedPortal) par1World.getTileEntity(par2, par3, par4)).getPlane();
        ((TileEntityAdvancedPortal) par1World.getTileEntity(par2, par3, par4)).removePortalFromEventHandler();
        super.breakBlock(par1World, par2, par3, par4, par5Block, par6);
        
        if(dir == "X"){
        	par1World.setBlockToAir(par2 - 1, par3, par4);
        	par1World.setBlockToAir(par2 - 1, par3 + 1, par4);
        	par1World.setBlockToAir(par2 - 1, par3 + 2, par4);
        	par1World.setBlockToAir(par2, par3 + 1, par4);
        	par1World.setBlockToAir(par2, par3 + 2, par4);
        	par1World.setBlockToAir(par2 + 1, par3, par4);
        	par1World.setBlockToAir(par2 + 1, par3 + 1, par4);
        	par1World.setBlockToAir(par2 + 1, par3 + 2, par4);
        }else{
        	par1World.setBlockToAir(par2, par3, par4 - 1);
        	par1World.setBlockToAir(par2, par3 + 1, par4 - 1);
        	par1World.setBlockToAir(par2, par3 + 2, par4 - 1);
        	par1World.setBlockToAir(par2, par3 + 1, par4);
        	par1World.setBlockToAir(par2, par3 + 2, par4);
        	par1World.setBlockToAir(par2, par3, par4 + 1);
        	par1World.setBlockToAir(par2, par3 + 1, par4 + 1);
        	par1World.setBlockToAir(par2, par3 + 2, par4 + 1);
        }
    }

    public boolean onBlockEventReceived(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
        super.onBlockEventReceived(par1World, par2, par3, par4, par5, par6);
        TileEntity tileentity = par1World.getTileEntity(par2, par3, par4);
        return tileentity != null ? tileentity.receiveClientEvent(par5, par6) : false;
    }

    public boolean test(int[] tempArray, int[] otherArray){
		if((tempArray[0] != otherArray[0] && tempArray[0] != otherArray[1])){
			return false;
		}
		
		if((tempArray[1] != otherArray[1] && tempArray[1] != otherArray[0])){
			return false;
		}
		
		if((tempArray[2] != otherArray[2] && tempArray[2] != otherArray[3])){
			return false;
		}
		
		if((tempArray[3] != otherArray[3] && tempArray[3] != otherArray[2])){
			return false;
		}
		
		return true;
	    
	}
    
    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        int l = func_149999_b(par1IBlockAccess.getBlockMetadata(par2, par3, par4));

        if (l == 0)
        {
            if (par1IBlockAccess.getBlock(par2, par3, par4 + 1) == Teleportals.fillerPortal || par1IBlockAccess.getBlock(par2, par3, par4 - 1) == Teleportals.fillerPortal)
            {
                l = 2;
            }
            else
            {
                l = 1;
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

    public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityAdvancedPortal();
	}

}
