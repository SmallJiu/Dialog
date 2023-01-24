package cat.jiu.dialog.iface;

import com.google.gson.JsonObject;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public interface IDialogOptionDataUnit {
	/**
	 * 获取选项ID
	 */
	ResourceLocation getTypeID();
	/**
	 * 获取选项显示的内容
	 */
	IDialogText getOptionText();
	/**
	 * 是否可以关闭对话框
	 */
	boolean canCloseDialog();
	
	JsonObject writeToJson(JsonObject json);
	void readFromJson(JsonObject json);
	
	NBTTagCompound writeToNBT();
	default NBTTagCompound writeNBT() {
		NBTTagCompound nbt = this.writeToNBT();
		if(nbt==null) nbt = new NBTTagCompound();
		if(!nbt.hasKey("type")) nbt.setString("type", this.getTypeID().toString());
		return nbt;
	}
	void readFromNBT(NBTTagCompound nbt);
}
