package cat.jiu.dialog.net.msg.option;

import cat.jiu.dialog.event.ItemChooseEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MsgItemRadioButtonChoose extends OptionMessage {
	protected ItemStack stack;
	protected int index;
	protected boolean confirm;
	
	public MsgItemRadioButtonChoose() {}
	public MsgItemRadioButtonChoose(ResourceLocation parent, ResourceLocation dialogID, int optionID, ItemStack stack, int index, boolean confirm) {
		super(parent, dialogID, optionID);
		this.stack = stack;
		this.index = index;
		this.confirm = confirm;
	}

	@Override
	public IMessage handler(MessageContext ctx) {
		if(ctx.side.isServer()) {
			MinecraftForge.EVENT_BUS.post(new ItemChooseEvent.Single(ctx.getServerHandler().player, parent, super.dialogID, super.optionID, this.stack, this.index, this.confirm));
		}
		return null;
	}
	
	@Override
	protected NBTTagCompound writeTo(NBTTagCompound nbt) {
		nbt.setTag("stack", this.stack.serializeNBT());
		nbt.setInteger("index", this.index);
		nbt.setBoolean("confirm", this.confirm);
		return nbt;
	}
	@Override
	protected void readFrom(NBTTagCompound nbt) {
		this.stack = new ItemStack(nbt.getCompoundTag("stack"));
		this.index = nbt.getInteger("index");
		this.confirm = nbt.getBoolean("confirm");
	}
}
