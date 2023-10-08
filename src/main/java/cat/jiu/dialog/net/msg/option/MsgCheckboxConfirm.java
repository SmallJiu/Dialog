package cat.jiu.dialog.net.msg.option;

import java.util.Map;

import com.google.common.collect.Maps;

import cat.jiu.core.util.mc.SimpleNBTTagList;
import cat.jiu.dialog.event.CheckboxEvent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MsgCheckboxConfirm extends OptionMessage {
	protected Map<Integer, String> selects;
	public MsgCheckboxConfirm() {}
	public MsgCheckboxConfirm(ResourceLocation parent, ResourceLocation dialogID, int optionID, Map<Integer, String> selects) {
		super(parent, dialogID, optionID);
		this.selects = selects;
	}

	@Override
	protected NBTTagCompound writeTo(NBTTagCompound nbt) {
		SimpleNBTTagList selects = new SimpleNBTTagList();
		this.selects.forEach((k,v)->{
			NBTTagCompound select = new NBTTagCompound();
			select.setInteger("index", k);
			select.setString("option", v);
			selects.append(select);
		});
		nbt.setTag("selects", selects);
		return nbt;
	}

	@Override
	protected void readFrom(NBTTagCompound nbt) {
		this.selects = Maps.newHashMap();
		nbt.getTagList("selects", 10).forEach(tag->{
			NBTTagCompound select = (NBTTagCompound) tag;
			this.selects.put(select.getInteger("index"), select.getString("option"));
		});
	}

	@Override
	public IMessage handler(MessageContext ctx) {
		if(ctx.side.isServer()) {
			MinecraftForge.EVENT_BUS.post(new CheckboxEvent.Confirm(ctx.getServerHandler().player, parent, super.dialogID, super.optionID, this.selects));
		}
		return null;
	}
}
