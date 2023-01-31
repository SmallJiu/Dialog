package cat.jiu.dialog.test;

import com.google.gson.JsonObject;

import cat.jiu.dialog.element.DialogText;
import cat.jiu.dialog.iface.IDialogOptionDataUnit;
import cat.jiu.dialog.iface.IDialogText;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class TestDataUnit implements IDialogOptionDataUnit {
	@Override
	public IDialogText getOptionText() {
		return new DialogText(false, "dialog.text.custom_option");
	}
	
	protected static final NBTTagCompound nbt = new NBTTagCompound();
	@Override
	public NBTTagCompound writeToNBT() {
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {}
	@Override
	public JsonObject writeToJson(JsonObject json) {
		if(json==null) json = new JsonObject();
		json.addProperty("text", "dialog.text.custom_option");
		return json;
	}
	
	@Override
	public void readFromJson(JsonObject json) { }

	@Override
	public ResourceLocation getTypeID() {
		return TestDialog.getTestOption().getTypeID();
	}

	@Override
	public boolean canCloseDialog() {
		return false;
	}
}
