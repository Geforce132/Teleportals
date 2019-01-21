package net.geforcemods.teleportals.network.packets;

import java.util.Iterator;
import java.util.List;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.geforcemods.teleportals.Teleportals;
import net.geforcemods.teleportals.entity.EntityMinecartPortal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class PacketSMinecartCheck implements IMessage{
	
	private boolean isKeyDown;
	
	public PacketSMinecartCheck(){
		
	}
	
	public PacketSMinecartCheck(boolean isKeyDown){
		this.isKeyDown = isKeyDown;
	}

	public void fromBytes(ByteBuf buf) {
		this.isKeyDown = buf.readBoolean();
	}

	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(isKeyDown);
	}
	
public static class Handler extends PacketHelper implements IMessageHandler<PacketSMinecartCheck, IMessage> {

	public IMessage onMessage(PacketSMinecartCheck message, MessageContext ctx) {
		EntityPlayer player = ctx.getServerHandler().playerEntity;
		EntityMinecartPortal cart = null;
		
		if(ctx.getServerHandler().playerEntity.ridingEntity != null && ctx.getServerHandler().playerEntity.ridingEntity instanceof EntityMinecartPortal){
			cart = (EntityMinecartPortal) ctx.getServerHandler().playerEntity.ridingEntity;
		}else{
			return null;
		}
		
		if(message.isKeyDown){
			this.checkForAnotherMinecart(player, cart, MinecraftServer.getServer().getEntityWorld());
		}

		return null;
	}
	
	private void checkForAnotherMinecart(EntityPlayer par1EntityPlayer, EntityMinecartPortal par2Minecart, World par3World) {
		for(int i = 1; i <= Teleportals.minecartTeleportationRange; i++){
			AxisAlignedBB box = null;
			String dir = par2Minecart.getMinecartDirection();
			
			if(dir.toLowerCase().matches("east")){
				box = AxisAlignedBB.getBoundingBox(par1EntityPlayer.posX, par1EntityPlayer.posY, par1EntityPlayer.posZ, par1EntityPlayer.posX + i, par1EntityPlayer.posY, par1EntityPlayer.posZ).expand(1, 1, 1);
			}else if(dir.toLowerCase().matches("west")){
				box = AxisAlignedBB.getBoundingBox(par1EntityPlayer.posX - i, par1EntityPlayer.posY, par1EntityPlayer.posZ, par1EntityPlayer.posX, par1EntityPlayer.posY, par1EntityPlayer.posZ).expand(1, 1, 1);
			}else if(dir.toLowerCase().matches("south")){
				box = AxisAlignedBB.getBoundingBox(par1EntityPlayer.posX, par1EntityPlayer.posY, par1EntityPlayer.posZ, par1EntityPlayer.posX, par1EntityPlayer.posY, par1EntityPlayer.posZ + i).expand(1, 1, 1);
			}else if(dir.toLowerCase().matches("north")){
				box = AxisAlignedBB.getBoundingBox(par1EntityPlayer.posX, par1EntityPlayer.posY, par1EntityPlayer.posZ - i, par1EntityPlayer.posX, par1EntityPlayer.posY, par1EntityPlayer.posZ).expand(1, 1, 1);
			}
			
			if(box == null || dir.toLowerCase().matches("unknown")){
				return;
			}
			
			List<?> entityList = par3World.getEntitiesWithinAABBExcludingEntity(par2Minecart, box);
			
			Iterator<?> iterator = entityList.iterator();
			
			while(iterator.hasNext()){
				Entity entity = (Entity) iterator.next();
				
				if(entity instanceof EntityMinecartPortal){
					par1EntityPlayer.dismountEntity(par2Minecart);
					par1EntityPlayer.mountEntity(entity);

					if(dir.toLowerCase().matches("east") || dir.toLowerCase().matches("west")){
						entity.motionX = par2Minecart.motionX;
					}else if(dir.toLowerCase().matches("south") || dir.toLowerCase().matches("north")){
						entity.motionZ = par2Minecart.motionZ;
					}
					
					par3World.playSoundAtEntity(entity, "mob.endermen.portal", 1.0F, 1.0F);
					par2Minecart.cooldown = 30;
					break;
				}
			}
		}
	}
	
}

}
