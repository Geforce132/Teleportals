package net.geforcemods.teleportals.network.packets;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.geforcemods.teleportals.Teleportals;
import net.minecraft.client.Minecraft;

public class PacketSetEntityHeadRotation implements IMessage{
	
	private float rotation;
	
	public PacketSetEntityHeadRotation(){
		
	}
	
	public PacketSetEntityHeadRotation(float rotation){
		this.rotation = rotation;		
	}
	
	public void fromBytes(ByteBuf par2ByteBuf) {
		this.rotation = par2ByteBuf.readFloat();
	}

	public void toBytes(ByteBuf par2ByteBuf) {
		par2ByteBuf.writeFloat(this.rotation);
	}
	
public static class Handler extends PacketHelper implements IMessageHandler<PacketSetEntityHeadRotation, IMessage> {

	@SideOnly(Side.CLIENT)
	public IMessage onMessage(PacketSetEntityHeadRotation message, MessageContext ctx) {
		Teleportals.log("Receiving PSEHR packet from player '" + Minecraft.getMinecraft().thePlayer.getCommandSenderName());

		Minecraft.getMinecraft().thePlayer.setPositionAndRotation(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY, Minecraft.getMinecraft().thePlayer.posZ, message.rotation, Minecraft.getMinecraft().thePlayer.rotationPitch);
		
		return null;
	}
	
}

}
