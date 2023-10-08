package cat.jiu.dialog.net.msg.option;

import cat.jiu.dialog.event.CheckboxEvent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MsgCheckboxCheck extends OptionMessage {
	protected String option;
	protected int selectIndex;
	protected boolean isRemove;

	public MsgCheckboxCheck() {}
	public MsgCheckboxCheck(ResourceLocation parent, ResourceLocation dialogID, int optionID, String option, int selectIndex, boolean isRemove) {
		super(parent, dialogID, optionID);
		this.dialogID = dialogID;
		this.optionID = optionID;
		this.option = option;
		this.selectIndex = selectIndex;
		this.isRemove = isRemove;
	}

	@Override
	protected NBTTagCompound writeTo(NBTTagCompound nbt) {
		nbt.setString("option", option);
		nbt.setInteger("select", this.selectIndex);
		nbt.setBoolean("remove", this.isRemove);
		return nbt;
	}

	@Override
	protected void readFrom(NBTTagCompound nbt) {
		this.option = nbt.getString("option");
		this.selectIndex = nbt.getInteger("select");
		this.isRemove = nbt.getBoolean("remove");
	}

	@Override
	public IMessage handler(MessageContext ctx) {
		if(ctx.side.isServer()) {
			MinecraftForge.EVENT_BUS.post(new CheckboxEvent.Check(ctx.getServerHandler().player, parent, super.dialogID, super.optionID, this.option, this.selectIndex, this.isRemove));
		}
		return null;
	}
}
