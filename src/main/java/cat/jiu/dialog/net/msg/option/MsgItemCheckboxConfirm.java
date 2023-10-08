package cat.jiu.dialog.net.msg.option;

import java.util.Map;

import com.google.common.collect.Maps;

import cat.jiu.core.util.mc.SimpleNBTTagList;
import cat.jiu.dialog.event.ItemChooseEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MsgItemCheckboxConfirm extends OptionMessage {
	protected Map<Integer, ItemStack> selects;
	
	public MsgItemCheckboxConfirm() {}
	public MsgItemCheckboxConfirm(ResourceLocation parent, ResourceLocation dialogID, int optionID, Map<Integer, ItemStack> selects) {
		super(parent, dialogID, optionID);
		this.selects = selects;
	}

	@Override
	protected NBTTagCompound writeTo(NBTTagCompound nbt) {
		SimpleNBTTagList selects = new SimpleNBTTagList();
		this.selects.forEach((k,v)->{
			NBTTagCompound select = new NBTTagCompound();
			select.setInteger("index", k);
			select.setTag("stack", v.serializeNBT());
			selects.append(select);
		});
		nbt.setTag("selects", selects);
		return nbt;
	}

	@Override
	protected void readFrom(NBTTagCompound nbt) {
		this.selects = Maps.newHashMap();
		nbt.getTagList("selects", 10).forEach(base->{
			NBTTagCompound tag = (NBTTagCompound) base;
			this.selects.put(tag.getInteger("index"), new ItemStack(tag.getCompoundTag("stack")));
		});
	}

	@Override
	public IMessage handler(MessageContext ctx) {
		if(ctx.side.isServer()) {
			MinecraftForge.EVENT_BUS.post(new ItemChooseEvent.Multi.Confirm(ctx.getServerHandler().player, parent, this.dialogID, this.optionID, this.selects));
		}
		return null;
	}
}
