package net.geforcemods.teleportals.network.packets;

import io.netty.buffer.ByteBuf;
import net.geforcemods.teleportals.Teleportals;
import net.geforcemods.teleportals.misc.HelpfulMethods;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PacketCBeaconTeleport implements IMessage{
	
	private int x, y, z;
	
	public PacketCBeaconTeleport(){
		
	}
	
	public PacketCBeaconTeleport(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void fromBytes(ByteBuf buf) {
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
	}

	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
	}

public static class Handler extends PacketHelper implements IMessageHandler<PacketCBeaconTeleport, IMessage> {

	@SideOnly(Side.CLIENT)
	public IMessage onMessage(PacketCBeaconTeleport message, MessageContext ctx) {
		if(Minecraft.getMinecraft().theWorld.getBlock(message.x, message.y, message.z) == Blocks.beacon && HelpfulMethods.isActiveBeacon(Minecraft.getMinecraft().theWorld, message.x, message.y, message.z)){
			this.teleportPlayerUpwards(Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer);
		}
		return null;
	}
	
	private void teleportPlayerUpwards(World par1World, EntityPlayer par2EntityPlayer) {
		for(int i = 1; i <= Teleportals.beaconTeleportationRange; i++){
			if(par1World.getBlock((int) par2EntityPlayer.posX, (int) par2EntityPlayer.posY + i, (int) par2EntityPlayer.posZ) == Blocks.glass){
				par2EntityPlayer.setPositionAndUpdate(par2EntityPlayer.posX, (par2EntityPlayer.posY + i) + 2, par2EntityPlayer.posZ);
				Teleportals.network.sendToServer(new PacketSPlaySoundAtPos(par2EntityPlayer.posX, par2EntityPlayer.posY, par2EntityPlayer.posZ, "mob.endermen.portal"));
			}
		}
	}
	
}

}
