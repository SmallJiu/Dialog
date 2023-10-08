package cat.jiu.dialog.net.msg;

import java.io.IOException;

import cat.jiu.dialog.api.IBaseMessage;
import cat.jiu.dialog.event.DialogEvent;
import io.netty.buffer.ByteBuf;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MsgDialogEvent implements IBaseMessage {
	private ResourceLocation id;
	private boolean isOpen;
	public MsgDialogEvent() {}
	public MsgDialogEvent(ResourceLocation id, boolean isOpen) {
		this.id = id;
		this.isOpen = isOpen;
	}
	
	public void fromBytes(ByteBuf buf) {
		try {
			NBTTagCompound nbt = new PacketBuffer(buf).readCompoundTag();
			
			this.id = new ResourceLocation(nbt.getString("id"));
			this.isOpen = nbt.getBoolean("isOpen");
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void toBytes(ByteBuf buf) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("id", this.id.toString());
		nbt.setBoolean("isOpen", this.isOpen);
		new PacketBuffer(buf).writeCompoundTag(nbt);
	}
	
	public IMessage handler(MessageContext ctx) {
		if(ctx.side.isServer() && this.id != null) {
			if(this.isOpen) {
				MinecraftForge.EVENT_BUS.post(new DialogEvent.Open(ctx.getServerHandler().player, this.id));
			}else {
				MinecraftForge.EVENT_BUS.post(new DialogEvent.Close(ctx.getServerHandler().player, this.id));
			}
		}
		return this;
	}
}
