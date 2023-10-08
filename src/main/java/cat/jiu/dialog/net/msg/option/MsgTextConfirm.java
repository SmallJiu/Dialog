package cat.jiu.dialog.net.msg.option;

import cat.jiu.dialog.event.OptionEvent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MsgTextConfirm extends OptionMessage {
	protected String text;
	public MsgTextConfirm() {}
	public MsgTextConfirm(ResourceLocation parent, ResourceLocation dialogID, int optionID, String text) {
		super(parent, dialogID, optionID);
		this.text = text;
	}

	@Override
	public IMessage handler(MessageContext ctx) {
		if(ctx.side.isServer()) {
			MinecraftForge.EVENT_BUS.post(new OptionEvent.TextConfirm(ctx.getServerHandler().player, parent, this.dialogID, this.optionID, this.text));
		}
		return null;
	}

	@Override
	protected NBTTagCompound writeTo(NBTTagCompound nbt) {
		nbt.setString("text", text);
		return nbt;
	}

	@Override
	protected void readFrom(NBTTagCompound nbt) {
		this.text = nbt.getString("text");
	}
}
