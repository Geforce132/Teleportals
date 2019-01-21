package net.geforcemods.teleportals.network.packets;

import net.minecraft.server.MinecraftServer;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketSPlaySoundAtPos implements IMessage{
	
	private double x, y, z;
	private String sound;
	
	public PacketSPlaySoundAtPos(){
		
	}
	
	public PacketSPlaySoundAtPos(double par1, double par2, double par3, String sound){
		this.x = par1;
		this.y = par2;
		this.z = par3;
		this.sound = sound;
	}

	public void fromBytes(ByteBuf buf) {
		this.x = buf.readDouble();
		this.y = buf.readDouble();
		this.z = buf.readDouble();
		this.sound = ByteBufUtils.readUTF8String(buf);
	}

	public void toBytes(ByteBuf buf) {
		buf.writeDouble(x);
		buf.writeDouble(y);
		buf.writeDouble(z);
		ByteBufUtils.writeUTF8String(buf, sound);
	}
	
public static class Handler extends PacketHelper implements IMessageHandler<PacketSPlaySoundAtPos, IMessage> {

	public IMessage onMessage(PacketSPlaySoundAtPos message, MessageContext ctx) {
		MinecraftServer.getServer().getEntityWorld().playSoundAtEntity(ctx.getServerHandler().playerEntity, message.sound, 1.0F, 1.0F);
		return null;
	}
	
}

}
