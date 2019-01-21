package net.geforcemods.teleportals.network.packets;

import io.netty.buffer.ByteBuf;
import net.geforcemods.teleportals.Teleportals;
import net.geforcemods.teleportals.tileentities.TileEntityAdvancedPortal;
import net.minecraft.client.Minecraft;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PacketUpdateArray implements IMessage{
	
	private int x, y, z;
	private int[] array = new int[4];
	private int[] adjecentPortalBlocks = new int[6];
	
	public PacketUpdateArray(){
		
	}
	
    public PacketUpdateArray(int x, int y, int z, int array[], int adjecentBlocks[]){
        this.array = new int[4];
        this.adjecentPortalBlocks = new int[6];
        this.x = x;
        this.y = y;
        this.z = z;
        this.array = array;
        this.adjecentPortalBlocks = adjecentBlocks;
    }

    public void fromBytes(ByteBuf par1ByteBuf) {
		this.x = par1ByteBuf.readInt();
		this.y = par1ByteBuf.readInt();
		this.z = par1ByteBuf.readInt();
		
		for(int i = 0; i < 4; i++){
			this.array[i] = par1ByteBuf.readInt();
		}
		
		for(int i = 0; i < 6; i++){
            adjecentPortalBlocks[i] = par1ByteBuf.readInt();
		}
		
	}

	public void toBytes(ByteBuf par1ByteBuf) {
		par1ByteBuf.writeInt(x);
		par1ByteBuf.writeInt(y);
		par1ByteBuf.writeInt(z);

		for(int i = 0; i < 4; i++){
			par1ByteBuf.writeInt(array[i]);
		}
		
		for(int i = 0; i < 6; i++){
            par1ByteBuf.writeInt(adjecentPortalBlocks[i]);
		}
		
	}
	
public static class Handler extends PacketHelper implements IMessageHandler<PacketUpdateArray, IMessage> {

	@SideOnly(Side.CLIENT)
	public IMessage onMessage(PacketUpdateArray message, MessageContext ctx) {
		TileEntityAdvancedPortal TEAP = (TileEntityAdvancedPortal) Minecraft.getMinecraft().theWorld.getTileEntity(message.x, message.y, message.z);
		Teleportals.log(TEAP + " | " + Minecraft.getMinecraft().theWorld.getBlock(message.x, message.y, message.z).getUnlocalizedName().substring(5) + " | Coords: " + message.x + ", " + message.y + ", " + message.z);
		TEAP.setCornerBlockIDs(message.array);
		TEAP.setAdjecentPortalBlocks(message.adjecentPortalBlocks);
		
		return null;
	}
}
	

}
