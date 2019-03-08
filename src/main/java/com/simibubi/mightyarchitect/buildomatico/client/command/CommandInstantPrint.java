package com.simibubi.mightyarchitect.buildomatico.client.command;

import com.simibubi.mightyarchitect.buildomatico.client.BuildingProcessClient;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.IClientCommand;

public class CommandInstantPrint extends CommandBase implements IClientCommand {

	@Override
	public String getName() {
		return "print";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/print";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (sender instanceof EntityPlayer) {
			BuildingProcessClient.print();
		}
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}
	
	@Override
	public boolean allowUsageWithoutPrefix(ICommandSender sender, String message) {
		return false;
	}

}
