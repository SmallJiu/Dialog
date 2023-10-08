package cat.jiu.dialog.net.msg.option;

import cat.jiu.dialog.event.RadioButtonEvent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MsgRadioButtom extends OptionMessage {
	protected String option;
	protected int selectIndex;
	protected boolean confirm;

	public MsgRadioButtom() {}
	public MsgRadioButtom(ResourceLocation parent, ResourceLocation dialogID, int optionID, String options, int selectIndex, boolean confirm) {
		super(parent, dialogID, optionID);
		this.option = options;
		this.selectIndex = selectIndex;
		this.confirm = confirm;
	}

	@Override
	protected NBTTagCompound writeTo(NBTTagCompound nbt) {
		nbt.setInteger("select", this.selectIndex);
		nbt.setBoolean("confirm", this.confirm);
		nbt.setString("option", this.option);
		return nbt;
	}

	@Override
	protected void readFrom(NBTTagCompound nbt) {
		this.selectIndex = nbt.getInteger("select");
		this.confirm = nbt.getBoolean("confirm");
		this.option = nbt.getString("option");
	}

	@Override
	public IMessage handler(MessageContext ctx) {
		if(ctx.side.isServer()) {
			MinecraftForge.EVENT_BUS.post(new RadioButtonEvent(ctx.getServerHandler().player, parent, super.dialogID, super.optionID, this.option, this.selectIndex, this.confirm));
		}
		return null;
	}
}
