package net.geforcemods.teleportals.network.packets;

import org.lwjgl.input.Keyboard;

import io.netty.buffer.ByteBuf;
import net.geforcemods.teleportals.Teleportals;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PacketCMinecartCheck implements IMessage{
	
	private int keyCode;
	
	public PacketCMinecartCheck(){
		
	}
	
	public PacketCMinecartCheck(int keyCode){
		this.keyCode = keyCode;
	}

	public void fromBytes(ByteBuf buf) {
		this.keyCode = buf.readInt();
	}

	public void toBytes(ByteBuf buf) {
		buf.writeInt(keyCode);
	}
	
public static class Handler extends PacketHelper implements IMessageHandler<PacketCMinecartCheck, IMessage> {

	@SideOnly(Side.CLIENT)
	public IMessage onMessage(PacketCMinecartCheck message, MessageContext ctx) {
		Teleportals.network.sendToServer(new PacketSMinecartCheck(Keyboard.isKeyDown(message.keyCode)));
		return null;
	}
	
}

}
