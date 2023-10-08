package cat.jiu.dialog.net.msg.option;

import cat.jiu.dialog.event.ItemChooseEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MsgItemCheckboxSelect extends OptionMessage {
	protected int index;
	protected ItemStack stack;
	protected boolean remove;
	
	public MsgItemCheckboxSelect() {}
	public MsgItemCheckboxSelect(ResourceLocation parent, ResourceLocation dialogID, int optionID, int index, ItemStack stack, boolean remove) {
		super(parent, dialogID, optionID);
		this.index = index;
		this.stack = stack;
		this.remove = remove;
	}
	
	@Override
	protected NBTTagCompound writeTo(NBTTagCompound nbt) {
		nbt.setTag("stack", this.stack.serializeNBT());
		nbt.setInteger("index", this.index);
		nbt.setBoolean("remove", this.remove);
		return nbt;
	}
	@Override
	protected void readFrom(NBTTagCompound nbt) {
		this.stack = new ItemStack(nbt.getCompoundTag("stack"));
		this.index = nbt.getInteger("index");
		this.remove = nbt.getBoolean("remove");
	}
	
	@Override
	public IMessage handler(MessageContext ctx) {
		if(ctx.side.isServer()) {
			MinecraftForge.EVENT_BUS.post(new ItemChooseEvent.Multi.Select(ctx.getServerHandler().player, parent, this.dialogID, this.optionID, this.index, this.stack, this.remove));
		}
		return null;
	}
}
