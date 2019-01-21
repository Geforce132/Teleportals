package net.geforcemods.teleportals.misc;

import java.util.Scanner;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class HelpfulMethods {	
	
	public static void checkBlocksMetadata(World par1World, int par2, int par3, int par4){
		if(par1World.getBlockMetadata(par2, par3, par4) == 0){
			System.out.println("down (MD: 0)");
		}else if(par1World.getBlockMetadata(par2, par3, par4) == 1){
			System.out.println("up (MD: 1)");
	    }else if(par1World.getBlockMetadata(par2, par3, par4) == 2){
        	System.out.println("north (MD: 2)");
        }else if(par1World.getBlockMetadata(par2, par3, par4) == 3){
        	System.out.println("south (MD: 3)");
        }else if(par1World.getBlockMetadata(par2, par3, par4) == 4){
        	System.out.println("west (MD: 4)");
        }else if(par1World.getBlockMetadata(par2, par3, par4) == 5){
        	System.out.println("east (MD: 5)");
        }
	}
	
	public static ForgeDirection getDirection(World par1World, int par2, int par3, int par4){
		if(par1World.getBlockMetadata(par2, par3, par4) == 2){
			return ForgeDirection.NORTH;
		}else if(par1World.getBlockMetadata(par2, par3, par4) == 3){
			return ForgeDirection.SOUTH;
		}else if(par1World.getBlockMetadata(par2, par3, par4) == 4){
			return ForgeDirection.WEST;
		}else if(par1World.getBlockMetadata(par2, par3, par4) == 4){
			return ForgeDirection.EAST;
		}else{
			return ForgeDirection.UNKNOWN;
		}
	}

	public static boolean isTruelySingleplayer(){
		Side side = FMLCommonHandler.instance().getSide();
		if(side == Side.CLIENT){
			Minecraft mc = Minecraft.getMinecraft();
			if(mc.isSingleplayer() && !mc.getIntegratedServer().getPublic()){
	    		System.out.println("We are in offline singleplayer mode!");
	    		return true;
			}else{
				System.out.println("We are in online multiplayer mode! (LAN) ");
				return false;
			}
		}else if(side == Side.SERVER){
			System.out.println("We are in online multiplayer mode! (SERVER)");
    		return false;
	    }else{
			System.out.println("Unknown mode!");
	    	return false;
	    }
		
		
    }

	public static String getFormattedCoordinates(int par1, int par2, int par3){
		return " X:" + par1 + " Y:" + par2 + " Z:" + par3;
	}
	
	public static void setBlockInBox(World par1World, int par2, int par3, int par4, Block par5){
		par1World.setBlock(par2 + 1, par3 + 1, par4, par5);
    	par1World.scheduleBlockUpdate(par2 + 1, par3 + 1, par4, par5, 1200);

		par1World.setBlock(par2 + 1, par3 + 2, par4, par5);
    	par1World.scheduleBlockUpdate(par2 + 1, par3 + 2, par4, par5, 1200);

		par1World.setBlock(par2 + 1, par3 + 3, par4, par5);
    	par1World.scheduleBlockUpdate(par2 + 1, par3 + 3, par4, par5, 1200);

		par1World.setBlock(par2 + 1, par3 + 1, par4 + 1, par5);
    	par1World.scheduleBlockUpdate(par2 + 1, par3 + 1, par4 + 1, par5, 1200);

		par1World.setBlock(par2 + 1, par3 + 2, par4 + 1, par5);
    	par1World.scheduleBlockUpdate(par2 + 1, par3 + 2, par4 + 1, par5, 1200);

		par1World.setBlock(par2 + 1, par3 + 3, par4 + 1, par5);
    	par1World.scheduleBlockUpdate(par2 + 1, par3 + 3, par4 + 1, par5, 1200);

		par1World.setBlock(par2 - 1, par3 + 1, par4, par5);
    	par1World.scheduleBlockUpdate(par2 - 1, par3 + 1, par4, par5, 1200);

		par1World.setBlock(par2 - 1, par3 + 2, par4, par5);
    	par1World.scheduleBlockUpdate(par2 - 1, par3 + 2, par4, par5, 1200);

		par1World.setBlock(par2 - 1, par3 + 3, par4, par5);
    	par1World.scheduleBlockUpdate(par2 - 1, par3 + 3, par4, par5, 1200);

		par1World.setBlock(par2 - 1, par3 + 1, par4 + 1, par5);
    	par1World.scheduleBlockUpdate(par2 - 1, par3 + 1, par4 + 1, par5, 1200);

		par1World.setBlock(par2 - 1, par3 + 2, par4 + 1, par5);
    	par1World.scheduleBlockUpdate(par2 - 1, par3 + 2, par4 + 1, par5, 1200);

		par1World.setBlock(par2 - 1, par3 + 3, par4 + 1, par5);
    	par1World.scheduleBlockUpdate(par2 - 1, par3 + 3, par4 + 1, par5, 1200);

		par1World.setBlock(par2, par3 + 1, par4 + 1, par5);
    	par1World.scheduleBlockUpdate(par2, par3 + 1, par4 + 1, par5, 1200);

		par1World.setBlock(par2, par3 + 2, par4 + 1, par5);
    	par1World.scheduleBlockUpdate(par2, par3 + 2, par4 + 1, par5, 1200);

		par1World.setBlock(par2, par3 + 3, par4 + 1, par5);
    	par1World.scheduleBlockUpdate(par2, par3 + 3, par4 + 1, par5, 1200);

		par1World.setBlock(par2 + 1, par3 + 1, par4, par5);
    	par1World.scheduleBlockUpdate(par2 + 1, par3 + 1, par4, par5, 1200);

		par1World.setBlock(par2 + 1, par3 + 2, par4, par5);
    	par1World.scheduleBlockUpdate(par2 + 1, par3 + 2, par4, par5, 1200);

		par1World.setBlock(par2 + 1, par3 + 3, par4, par5);
    	par1World.scheduleBlockUpdate(par2 + 1, par3 + 3, par4, par5, 1200);

		
		par1World.setBlock(par2, par3 + 1, par4 - 1, par5);
    	par1World.scheduleBlockUpdate(par2, par3 + 1, par4 - 1, par5, 1200);

		par1World.setBlock(par2, par3 + 2, par4 - 1, par5);
    	par1World.scheduleBlockUpdate(par2, par3 + 2, par4 - 1, par5, 1200);

		par1World.setBlock(par2, par3 + 3, par4 - 1, par5);
    	par1World.scheduleBlockUpdate(par2, par3 + 3, par4 - 1, par5, 1200);

		par1World.setBlock(par2 + 1, par3 + 1, par4 - 1, par5);
    	par1World.scheduleBlockUpdate(par2 + 1, par3 + 1, par4 - 1, par5, 1200);

		par1World.setBlock(par2 + 1, par3 + 2, par4 - 1, par5);
    	par1World.scheduleBlockUpdate(par2 + 1, par3 + 2, par4 - 1, par5, 1200);

		par1World.setBlock(par2 + 1, par3 + 3, par4 - 1, par5);
    	par1World.scheduleBlockUpdate(par2 + 1, par3 + 3, par4 - 1, par5, 1200);

		
		par1World.setBlock(par2 - 1, par3 + 1, par4 - 1, par5);
    	par1World.scheduleBlockUpdate(par2 - 1, par3 + 1, par4 - 1, par5, 1200);

		par1World.setBlock(par2 - 1, par3 + 2, par4 - 1, par5);
    	par1World.scheduleBlockUpdate(par2 - 1, par3 + 2, par4 - 1, par5, 1200);

		par1World.setBlock(par2 - 1, par3 + 3, par4 - 1, par5);
    	par1World.scheduleBlockUpdate(par2 - 1, par3 + 3, par4 - 1, par5, 1200);

	}
	
	/**
	 * Updates a block and notify's neighboring blocks of a change.
	 * 
	 * Args: worldObj, x, y, z, blockID, tickRate, shouldUpdate
	 * 
	 * 
	 */
	public static void updateAndNotify(World par1World, int par2, int par3, int par4, Block par5, int par6, boolean par7){
		if(par7){
			par1World.scheduleBlockUpdate(par2, par3, par4, par5, par6);
		}
		par1World.notifyBlockOfNeighborChange(par2 + 1, par3, par4, par1World.getBlock(par2, par3, par4));
		par1World.notifyBlockOfNeighborChange(par2 - 1, par3, par4, par1World.getBlock(par2, par3, par4));
		par1World.notifyBlockOfNeighborChange(par2, par3, par4 + 1, par1World.getBlock(par2, par3, par4));
		par1World.notifyBlockOfNeighborChange(par2, par3, par4 - 1, par1World.getBlock(par2, par3, par4));
	}

	public static int getNumberOfUsernames(String usernames) {
		Scanner scanner = new Scanner(usernames);
		scanner.useDelimiter(",");
		
		int i = 0;
		while(scanner.hasNext()){
			scanner.next();
			i++;
		}

		scanner.close();
		return i;
	}

	public static Item getItemFromBlock(Block par1){
		return Item.getItemFromBlock(par1);
	}
	
	public static void sendMessage(ICommandSender par1ICommandSender, String par2, EnumChatFormatting par3){
        	ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation(par2, new Object[0]);
        	chatcomponenttranslation.getChatStyle().setColor(par3);
        	par1ICommandSender.addChatMessage(chatcomponenttranslation);
	}
	
	public static void sendMessageToPlayer(EntityPlayer par1EntityPlayer, String par2, EnumChatFormatting par3){
		ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation(par2, new Object[0]);
    	
		if(par3 != null){
    		chatcomponenttranslation.getChatStyle().setColor(par3);
    	}
    	
		par1EntityPlayer.addChatComponentMessage(chatcomponenttranslation);
	}
	
	public static void closePlayerScreen(EntityPlayer par1){
		Minecraft.getMinecraft().displayGuiScreen((GuiScreen)null);
		Minecraft.getMinecraft().setIngameFocus();
	}

	@SideOnly(Side.CLIENT)
	public static boolean isActiveBeacon(World par1World, int beaconX, int beaconY, int beaconZ){
		if(par1World.getBlock(beaconX, beaconY, beaconZ) == Blocks.beacon){
			float f = ((TileEntityBeacon) par1World.getTileEntity(beaconX, beaconY, beaconZ)).shouldBeamRender();
			
			return f > 0.0F ? true : false;
		}else{
			return false;
		}
	}

}
