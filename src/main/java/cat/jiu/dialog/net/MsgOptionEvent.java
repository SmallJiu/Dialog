package cat.jiu.dialog.net;

import cat.jiu.dialog.element.DialogOptionType;
import cat.jiu.dialog.event.OptionEvent;
import cat.jiu.dialog.iface.IDialogOptionType;
import io.netty.buffer.ByteBuf;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MsgOptionEvent implements IMessage {
	protected ResourceLocation dialogID;
	protected int optionID;
	protected Integer mouseButton;
	protected String text;
	protected ResourceLocation optionType;
	
	public MsgOptionEvent() {}
	public MsgOptionEvent(ResourceLocation dialogID, int optionID, IDialogOptionType type) {
		this.dialogID = dialogID;
		this.optionID = optionID;
		this.optionType = type.getTypeID();
	}
	public MsgOptionEvent(ResourceLocation dialogID, int optionID, String text) {
		this.dialogID = dialogID;
		this.optionID = optionID;
		this.text = text;
		this.optionType = DialogOptionType.TEXT.getTypeID();
	}
	public MsgOptionEvent(ResourceLocation dialogID, int optionID, int mouseButton) {
		this.dialogID = dialogID;
		this.optionID = optionID;
		this.mouseButton = mouseButton;
		this.optionType = DialogOptionType.BUTTON.getTypeID();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		try {
			NBTTagCompound nbt = new PacketBuffer(buf).readCompoundTag();
			
			this.dialogID = new ResourceLocation(nbt.getString("dialog"));
			this.optionID = nbt.getInteger("optionID");
			this.optionType = new ResourceLocation(nbt.getString("type"));
			
			if(nbt.hasKey("mouseButton")) this.mouseButton = nbt.getInteger("mouseButton");
			if(nbt.hasKey("text")) this.text = nbt.getString("text");
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound nbt = new NBTTagCompound();
		
		nbt.setString("dialog", this.dialogID.toString());
		nbt.setInteger("optionID", this.optionID);
		nbt.setString("type", this.optionType.toString());
		
		if(this.mouseButton!=null) nbt.setInteger("mouseButton", this.mouseButton);
		if(this.text!=null) nbt.setString("text", this.text);
		
		new PacketBuffer(buf).writeCompoundTag(nbt);
	}
	
	public final IMessage handler(MessageContext ctx) {
		if(ctx.side.isServer()) {
			if(DialogOptionType.BUTTON.getTypeID().equals(this.optionType)) {
				MinecraftForge.EVENT_BUS.post(new OptionEvent.ButtonClick(
						ctx.getServerHandler().player,
						this.dialogID, this.optionID, this.mouseButton));
			}else if(DialogOptionType.TEXT.getTypeID().equals(this.optionType)) {
				MinecraftForge.EVENT_BUS.post(new OptionEvent.TextConfirm(
						ctx.getServerHandler().player,
						this.dialogID, this.optionID, this.text));
			}else {
				OptionEvent event = this.customEvent(ctx);
				if(event!=null) {
					MinecraftForge.EVENT_BUS.post(event);
				}
			}
		}
		return this;
	}
	
	protected OptionEvent customEvent(MessageContext ctx) {return null;}
}
