package cat.jiu.dialog.net;

import java.io.IOException;

import cat.jiu.dialog.element.Dialog;
import cat.jiu.dialog.ui.ContainerDialog;

import io.netty.buffer.ByteBuf;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MsgDialog implements IMessage {
	private Dialog dialog;
	public MsgDialog() {}
	public MsgDialog(Dialog dialog) {
		this.dialog = dialog;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		try {
			this.dialog = Dialog.readFromNBT(new PacketBuffer(buf).readCompoundTag());
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		new PacketBuffer(buf).writeCompoundTag(this.dialog.writeToNBT(new NBTTagCompound()));
	}
	
	public IMessage handler(MessageContext ctx) {
		Container container = ctx.side.isClient() ? Minecraft.getMinecraft().player.openContainer : ctx.getServerHandler().player.openContainer;
		
		if(container instanceof ContainerDialog) {
			((ContainerDialog) container).setDialog(dialog);
		}
		return null;
	}
}
