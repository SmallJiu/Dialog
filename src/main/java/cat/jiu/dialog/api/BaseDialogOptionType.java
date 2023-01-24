package cat.jiu.dialog.api;

import com.google.gson.JsonObject;

import cat.jiu.dialog.DialogAPI;
import cat.jiu.dialog.iface.IDialogOptionDataUnit;
import cat.jiu.dialog.iface.IDialogOptionType;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public abstract class BaseDialogOptionType implements IDialogOptionType {
	protected final ResourceLocation id;
	protected final Class<? extends IDialogOptionDataUnit> clazz;
	public BaseDialogOptionType(ResourceLocation id, Class<? extends IDialogOptionDataUnit> clazz) {
		this.id = id;
		this.clazz = clazz;
		DialogAPI.registerOption(this);
	}

	@Override
	public ResourceLocation getTypeID() {
		return this.id;
	}
	
	@Override
	public IDialogOptionDataUnit getDataUnit(NBTTagCompound nbt) throws Exception {
		IDialogOptionDataUnit option = this.clazz.newInstance();
		option.readFromNBT(nbt);
		return option;
	}
	
	@Override
	public IDialogOptionDataUnit getDataUnit(JsonObject json) throws Exception {
		IDialogOptionDataUnit option = this.clazz.newInstance();
		option.readFromJson(json);
		return option;
	}
}
