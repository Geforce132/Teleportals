package net.geforcemods.teleportals.tileentities;

import cpw.mods.fml.common.FMLCommonHandler;
import net.geforcemods.teleportals.Teleportals;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityAdvancedPortal extends TileEntity{
	
	private int[] cornerBlockIDs = new int[4];
	private int[] cornerBlocksMeta = new int[4];
	private int[] adjecentPortal1 = new int[3];
	private int[] adjecentPortal2 = new int[3];
	private String direction = "";
	private boolean isAdded = false;
	private int playerCounter = 0;
	private int dimension = 0;
	
	public void updateEntity(){
		if(this.getWorld().isRemote){
			return;
		}else{
			if(playerCounter <= 99){
				playerCounter++;
			}

			if(!isAdded && cornerBlockIDs[0] >= 0 && cornerBlockIDs[1] >= 0 && cornerBlockIDs[2] >= 0 && cornerBlockIDs[3] >= 0 && adjecentPortal1[0] != 0 && adjecentPortal1[1] != 0 && adjecentPortal1[2] != 0 && adjecentPortal2[0] != 0 && adjecentPortal2[1] != 0 && adjecentPortal2[2] != 0){
				addPortalToEventHandler();
				sendChangeToClient();
				isAdded = true;
			}
			
			if(getPlane() == "X"){
				if(!test(new int[]{Block.getIdFromBlock(this.getWorld().getBlock(xCoord + 2, yCoord + 3, zCoord)), Block.getIdFromBlock(this.getWorld().getBlock(xCoord - 2, yCoord + 3, zCoord)), Block.getIdFromBlock(this.getWorld().getBlock(xCoord - 2, yCoord - 1, zCoord)), Block.getIdFromBlock(this.getWorld().getBlock(xCoord + 2, yCoord - 1, zCoord))}, cornerBlockIDs)){
					Teleportals.log("X-plane portal corner-block change detected! Updating arrays, and sending change to client.");
					cornerBlockIDs[0] = Block.getIdFromBlock(this.getWorld().getBlock(xCoord + 2, yCoord + 3, zCoord));
					cornerBlockIDs[1] = Block.getIdFromBlock(this.getWorld().getBlock(xCoord - 2, yCoord + 3, zCoord));
					cornerBlockIDs[2] = Block.getIdFromBlock(this.getWorld().getBlock(xCoord - 2, yCoord - 1, zCoord));
					cornerBlockIDs[3] = Block.getIdFromBlock(this.getWorld().getBlock(xCoord + 2, yCoord - 1, zCoord));
					Teleportals.log("The corner blocks are now " + cornerBlockIDs[0] + ", " + cornerBlockIDs[1] + ", " + cornerBlockIDs[2] + ", " + cornerBlockIDs[3]);
					resetPortal();
				}
			}else if(getPlane() == "Z"){
				if(!test(new int[]{Block.getIdFromBlock(this.getWorld().getBlock(xCoord, yCoord + 3, zCoord + 2)), Block.getIdFromBlock(this.getWorld().getBlock(xCoord, yCoord + 3, zCoord - 2)), Block.getIdFromBlock(this.getWorld().getBlock(xCoord, yCoord - 1, zCoord - 2)), Block.getIdFromBlock(this.getWorld().getBlock(xCoord, yCoord - 1, zCoord + 2))}, cornerBlockIDs)){
					Teleportals.log("Z-plane portal corner-block change detected! Updating arrays, and sending change to client.");
					cornerBlockIDs[0] = Block.getIdFromBlock(this.getWorld().getBlock(xCoord, yCoord + 3, zCoord + 2));
					cornerBlockIDs[1] = Block.getIdFromBlock(this.getWorld().getBlock(xCoord, yCoord + 3, zCoord - 2));
					cornerBlockIDs[2] = Block.getIdFromBlock(this.getWorld().getBlock(xCoord, yCoord - 1, zCoord - 2));
					cornerBlockIDs[3] = Block.getIdFromBlock(this.getWorld().getBlock(xCoord, yCoord - 1, zCoord + 2));
					Teleportals.log("The corner blocks are now " + cornerBlockIDs[0] + ", " + cornerBlockIDs[1] + ", " + cornerBlockIDs[2] + ", " + cornerBlockIDs[3]);
					resetPortal();
				}
			}
		}
		
	}
	
	private boolean test(int[] tempArray, int[] otherArray){
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
	
	private void addPortalToEventHandler(){	
		if(!Teleportals.eventHandler.getAllPortals().contains(this)){ //TODO
			Teleportals.eventHandler.getAllPortals().add(this);
		}
		Teleportals.log("Adding portal to event handler (size: [" + Teleportals.eventHandler.getAllPortals().size() + "])");
	}
	
	public void removePortalFromEventHandler(){
		for(int i = 0; i < Teleportals.eventHandler.getAllPortals().size(); i++){
			if(Teleportals.eventHandler.getAllPortals().get(i) != null && Teleportals.eventHandler.getAllPortals().get(i).xCoord == this.xCoord && Teleportals.eventHandler.getAllPortals().get(i).yCoord == this.yCoord && Teleportals.eventHandler.getAllPortals().get(i).zCoord == zCoord){
				Teleportals.eventHandler.getAllPortals().remove(i);
				break;
			}else{
				continue;
			}	
		}
	}

	private void resetPortal(){
		removePortalFromEventHandler();
		addPortalToEventHandler();
		sendChangeToClient();
	}
	
	private void sendChangeToClient(){
		//PacketUpdateArray packet = new PacketUpdateArray(xCoord, yCoord, zCoord, cornerBlockIDs, new int[]{adjecentPortal1[0], adjecentPortal1[1], adjecentPortal1[2], adjecentPortal2[0], adjecentPortal2[1], adjecentPortal2[2]});
	}
	
	/**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        if(this.cornerBlockIDs[0] != 0 && this.cornerBlockIDs[1] != 0 && this.cornerBlockIDs[2] != 0 && this.cornerBlockIDs[3] != 0){
        	par1NBTTagCompound.setIntArray("cornerBlocks", cornerBlockIDs);
        }
        
        if(this.adjecentPortal1[0] != 0 && this.adjecentPortal1[1] != 0 && this.adjecentPortal1[2] != 0){
        	par1NBTTagCompound.setIntArray("ap1", adjecentPortal1);
        }
        
        if(this.adjecentPortal2[0] != 0 && this.adjecentPortal2[1] != 0 && this.adjecentPortal2[2] != 0){
        	par1NBTTagCompound.setIntArray("ap2", adjecentPortal2);
        }
        
        par1NBTTagCompound.setIntArray("cornerBlocksMeta", cornerBlocksMeta);
        
        if(this.direction != null && this.direction != ""){
        	par1NBTTagCompound.setString("dir", direction);
        }
        
        par1NBTTagCompound.setInteger("dim", dimension);
       
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);

        if(par1NBTTagCompound.hasKey("cornerBlocks")){
            this.cornerBlockIDs = par1NBTTagCompound.getIntArray("cornerBlocks");
        }
        
        if(par1NBTTagCompound.hasKey("ap1")){
        	this.adjecentPortal1 = par1NBTTagCompound.getIntArray("ap1");
        }
        
        if(par1NBTTagCompound.hasKey("ap2")){
        	this.adjecentPortal2 = par1NBTTagCompound.getIntArray("ap2");
        }
        
        if(par1NBTTagCompound.hasKey("dir")){
        	this.direction = par1NBTTagCompound.getString("dir");
        }
        
        if(par1NBTTagCompound.hasKey("cornerBlocksMeta")){
        	this.cornerBlocksMeta = par1NBTTagCompound.getIntArray("cornerBlocksMeta");
        }
        
        if(par1NBTTagCompound.hasKey("dim")){
        	this.dimension = par1NBTTagCompound.getInteger("dim");
        }
       
    }

	public int[] getCornerBlockIDs() {
		return cornerBlockIDs;
	}

	public void setCornerBlockIDs(int[] cornerBlockIDs) {
		Teleportals.log("Recieved (" + cornerBlockIDs[0] + ", " + cornerBlockIDs[1] + ", " + cornerBlockIDs[2] + ", " + cornerBlockIDs[3] + ") | " + FMLCommonHandler.instance().getEffectiveSide());
		this.cornerBlockIDs = cornerBlockIDs;
		addPortalToEventHandler();
	}
	
	public int[] getCornerBlocksMetadata(){
		return cornerBlocksMeta;
	}

	public void setCornerBlocksMetadata(int[] cornerBlocksMeta) {
		this.cornerBlocksMeta = cornerBlocksMeta;
	}

	public Block[] getAdjecentPortalBlocks(World par1World){
		if(this.adjecentPortal1[0] != 0 && this.adjecentPortal1[1] != 0 && this.adjecentPortal1[2] != 0 && this.adjecentPortal2[0] != 0 && this.adjecentPortal2[1] != 0 && this.adjecentPortal2[2] != 0){
			return new Block[]{par1World.getBlock(adjecentPortal1[0], adjecentPortal1[1], adjecentPortal1[2]), par1World.getBlock(adjecentPortal2[0], adjecentPortal2[1], adjecentPortal2[2])};
		}
		
		return null;
	}
	
	public void setAdjecentPortalBlocks(int[] par1IntArray){
		this.adjecentPortal1[0] = par1IntArray[0];
		this.adjecentPortal1[1] = par1IntArray[1];
		this.adjecentPortal1[2] = par1IntArray[2];
		this.adjecentPortal2[0] = par1IntArray[3];
		this.adjecentPortal2[1] = par1IntArray[4];
		this.adjecentPortal2[2] = par1IntArray[5];
	}
	
	public boolean canPlayerTransport(){
		return playerCounter >= 100 ? true : false;
	}
	
	public void onPlayerTransport(){
		playerCounter = 0;
	}

	public void setPlane(String string) {
		this.direction = string;
	}
	
	public String getPlane() {
		return direction;
	}

	public int getDimension() {
		return dimension;
	}

	public void setDimension(int dimension) {
		this.dimension = dimension;
	}

}
