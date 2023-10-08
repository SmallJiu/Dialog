package cat.jiu.dialog.net.msg.option;

import cat.jiu.dialog.event.OptionEvent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MsgButtonClick extends OptionMessage {
	protected int mouseButton;
	public MsgButtonClick() {}
	public MsgButtonClick(ResourceLocation parent, ResourceLocation dialogID, int optionID, int mouseButton) {
		super(parent, dialogID, optionID);
		this.mouseButton = mouseButton;
	}

	@Override
	public IMessage handler(MessageContext ctx) {
		if(ctx.side.isServer()) {
			MinecraftForge.EVENT_BUS.post(new OptionEvent.ButtonClick(ctx.getServerHandler().player, parent, this.dialogID, this.optionID, this.mouseButton));
		}
		return null;
	}

	@Override
	protected NBTTagCompound writeTo(NBTTagCompound nbt) {
		nbt.setInteger("btn", this.mouseButton);
		return nbt;
	}

	@Override
	protected void readFrom(NBTTagCompound nbt) {
		this.mouseButton = nbt.getInteger("btn");
	}
}
