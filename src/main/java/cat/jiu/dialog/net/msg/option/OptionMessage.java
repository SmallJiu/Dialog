package cat.jiu.dialog.net.msg.option;

import java.io.IOException;

import cat.jiu.core.api.handler.INBTSerializable;
import cat.jiu.dialog.api.IBaseMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

public abstract class OptionMessage implements IBaseMessage, INBTSerializable {
	protected ResourceLocation parent, dialogID;
	protected int optionID;

	public OptionMessage() {}
	public OptionMessage(ResourceLocation parent, ResourceLocation dialogID, int optionID) {
		this.parent = parent;
		this.dialogID = dialogID;
		this.optionID = optionID;
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		new PacketBuffer(buf).writeCompoundTag(this.write(new NBTTagCompound()));
	}
	@Override
	public void fromBytes(ByteBuf buf) {
		try {
			this.read(new PacketBuffer(buf).readCompoundTag());
		}catch(IOException e) {}
	}
	
	@Override
	public NBTTagCompound write(NBTTagCompound nbt) {
		if(nbt==null) nbt = new NBTTagCompound();
		if(this.parent!=null) nbt.setString("parent", this.parent.toString());
		nbt.setString("dialog", this.dialogID.toString());
		nbt.setInteger("option", this.optionID);
		return this.writeTo(nbt);
	}
	protected abstract NBTTagCompound writeTo(NBTTagCompound nbt);
	
	@Override
	public void read(NBTTagCompound nbt) {
		if(nbt.hasKey("parent")) this.parent = new ResourceLocation(nbt.getString("parent"));
		this.dialogID = new ResourceLocation(nbt.getString("dialog"));
		this.optionID = nbt.getInteger("option");
		this.readFrom(nbt);
	}
	
	protected abstract void readFrom(NBTTagCompound nbt);
}
