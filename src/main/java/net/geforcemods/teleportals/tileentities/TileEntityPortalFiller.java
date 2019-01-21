package net.geforcemods.teleportals.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityPortalFiller extends TileEntity {
	
	private int[] apCoords = new int[3];
	
	/**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        if(apCoords[0] != 0 && apCoords[1] != 0 && apCoords[2] != 0){
        	par1NBTTagCompound.setIntArray("apXYZ", apCoords);
        }
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);

        if (par1NBTTagCompound.hasKey("apXYZ"))
        {
            this.apCoords = par1NBTTagCompound.getIntArray("apXYZ");
        }
    }

	public int[] getApCoords() {
		return apCoords;
	}

	public void setApCoords(int[] apCoords) {
		this.apCoords = apCoords;
	}

}
