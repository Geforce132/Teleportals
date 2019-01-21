package net.geforcemods.teleportals.network;

import cpw.mods.fml.client.registry.RenderingRegistry;
import net.geforcemods.teleportals.entity.EntityMinecartPortal;
import net.geforcemods.teleportals.entity.RenderMinecartPortal;

public class ClientProxy extends ServerProxy{
	
	@Override
	public void registerProxy(){
		RenderingRegistry.registerEntityRenderingHandler(EntityMinecartPortal.class, new RenderMinecartPortal());
	}
	
}
