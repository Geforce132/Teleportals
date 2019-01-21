package net.geforcemods.teleportals.network;

import net.geforcemods.teleportals.Teleportals;
import net.geforcemods.teleportals.block.BlockAdvancedPortalFiller;
import net.geforcemods.teleportals.block.BlockAdvancedTransportPortal;
import net.geforcemods.teleportals.block.BlockBasicTransportPortal;
import net.geforcemods.teleportals.entity.EntityMinecartPortal;
import net.geforcemods.teleportals.items.ItemMinecartPortal;
import net.geforcemods.teleportals.network.packets.PacketCBeaconTeleport;
import net.geforcemods.teleportals.network.packets.PacketCMinecartCheck;
import net.geforcemods.teleportals.network.packets.PacketSMinecartCheck;
import net.geforcemods.teleportals.network.packets.PacketSPlaySoundAtPos;
import net.geforcemods.teleportals.network.packets.PacketSetEntityHeadRotation;
import net.geforcemods.teleportals.network.packets.PacketUpdateArray;
import net.geforcemods.teleportals.tileentities.TileEntityAdvancedPortal;
import net.geforcemods.teleportals.tileentities.TileEntityBasicPortal;
import net.geforcemods.teleportals.tileentities.TileEntityOwnable;
import net.geforcemods.teleportals.tileentities.TileEntityPortalFiller;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

public class ConfigurationHandler {
	
	public void setupAdditions(FMLPreInitializationEvent event){
		this.setupBlocks(event);
		this.setupItems(event);
		this.setupGameRegistry(event);
	}
	
	private void setupBlocks(FMLPreInitializationEvent event) {
		Teleportals.portal = new BlockBasicTransportPortal().setBlockUnbreakable().setResistance(1000F).setLightLevel(0.50F).setUnlocalizedName("testPortal");
		Teleportals.advancedPortal = new BlockAdvancedTransportPortal().setBlockUnbreakable().setResistance(1000F).setLightLevel(0.50F).setUnlocalizedName("testAdvancedPortal");
		Teleportals.fillerPortal = new BlockAdvancedPortalFiller().setBlockUnbreakable().setResistance(1000F).setLightLevel(0.50F).setUnlocalizedName("fillerBlock");
	}
	
	private void setupItems(FMLPreInitializationEvent event) {
		Teleportals.portalMinecart = new ItemMinecartPortal(1337).setUnlocalizedName("portalMinecart").setCreativeTab(CreativeTabs.tabTransport).setTextureName("teleportals:minecart_portal");
	}
	
	public void setupConfiguration(FMLPreInitializationEvent event){
		Teleportals.configFile = new Configuration(event.getSuggestedConfigurationFile());
		
		Teleportals.configFile.load();
		
		Teleportals.isDebugMode = Teleportals.configFile.get("options", "Is debug mode?", false).getBoolean(false);
		Teleportals.beaconTeleportationRange = Teleportals.configFile.get("options", "Beacon teleportation range:", 75).getInt(75);
		Teleportals.minecartTeleportationRange = Teleportals.configFile.get("options", "Minecart teleportation range:", 50).getInt(50);
		
		if(Teleportals.configFile.hasChanged()){
			Teleportals.configFile.save();
		}
	}
	
	private void setupGameRegistry(FMLPreInitializationEvent event) {
		GameRegistry.registerBlock(Teleportals.portal, Teleportals.portal.getUnlocalizedName().substring(5));
		GameRegistry.registerBlock(Teleportals.advancedPortal, Teleportals.advancedPortal.getUnlocalizedName().substring(5));
		GameRegistry.registerBlock(Teleportals.fillerPortal, Teleportals.fillerPortal.getUnlocalizedName().substring(5));
		
		GameRegistry.registerItem(Teleportals.portalMinecart, Teleportals.portalMinecart.getUnlocalizedName().substring(5));

		GameRegistry.registerTileEntity(TileEntityOwnable.class, "ownableTE");
		GameRegistry.registerTileEntity(TileEntityBasicPortal.class, "basicPortalTE");
		GameRegistry.registerTileEntity(TileEntityAdvancedPortal.class, "advancedPortalTE");
		GameRegistry.registerTileEntity(TileEntityPortalFiller.class, "portalFiller");
	}
	
	public void setupEntityRegistry() {
		EntityRegistry.registerGlobalEntityID(EntityMinecartPortal.class, "minecartPortal", EntityRegistry.findGlobalUniqueEntityId());
		EntityRegistry.registerModEntity(EntityMinecartPortal.class, "minecartPortal", 0, Teleportals.instance, 128, 1, true);
	}
	
	public void setupHandlers(FMLPreInitializationEvent event){
		FMLCommonHandler.instance().bus().register(Teleportals.eventHandler);
	}

	public void setupPackets(SimpleNetworkWrapper network) {
		network.registerMessage(PacketSetEntityHeadRotation.Handler.class, PacketSetEntityHeadRotation.class, 0, Side.CLIENT);
		network.registerMessage(PacketUpdateArray.Handler.class, PacketUpdateArray.class, 1, Side.CLIENT);
		network.registerMessage(PacketCBeaconTeleport.Handler.class, PacketCBeaconTeleport.class, 2, Side.CLIENT);
		network.registerMessage(PacketSPlaySoundAtPos.Handler.class, PacketSPlaySoundAtPos.class, 3, Side.SERVER);
		network.registerMessage(PacketCMinecartCheck.Handler.class, PacketCMinecartCheck.class, 4, Side.CLIENT);
		network.registerMessage(PacketSMinecartCheck.Handler.class, PacketSMinecartCheck.class, 5, Side.SERVER);
	}


}
