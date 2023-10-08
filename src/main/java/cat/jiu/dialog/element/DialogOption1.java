/*
package cat.jiu.dialog.element;

import com.google.gson.JsonObject;

import cat.jiu.dialog.DialogAPI;
import cat.jiu.dialog.ModMain;
import cat.jiu.dialog.element.option.*;
import cat.jiu.dialog.element.option.timer.*;
import cat.jiu.dialog.iface.IDialogOptionDataUnit;
import cat.jiu.dialog.iface.IDialogOptionType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public enum DialogOption1 implements IDialogOptionType {
	BUTTON(new ResourceLocation(ModMain.MODID, "button"), DialogButton.class),
	TEXT  (new ResourceLocation(ModMain.MODID, "text"), DialogTextField.class),
	
	CHECKBOX(new ResourceLocation(ModMain.MODID, "checkbox"), OptionCheckbox.class),
	RADIO_BUTTON(new ResourceLocation(ModMain.MODID, "radio_btn"), OptionRadioButton.class),
	SINGLE_ITEM(new ResourceLocation(ModMain.MODID, "item_single"), OptionSingleItemChoose.class),
	MULTI_ITEM(new ResourceLocation(ModMain.MODID, "item_multi"), OptionMultiItemChoose.class),
	MULTI_TITLE(new ResourceLocation(ModMain.MODID, "multi_tile"), OptionMultiTitle.class),

	CHECKBOX_TIMER(new ResourceLocation(ModMain.MODID, "timer_checkbox"), OptionTimerCheckbox.class),
	RADIO_BUTTON_TIMER(new ResourceLocation(ModMain.MODID, "timer_radio_btn"), OptionTimerRadioButton.class),
	SINGLE_ITEM_TIMER(new ResourceLocation(ModMain.MODID, "timer_item_single"), OptionTimerSingleItemChoose.class),
	MULTI_ITEM_TIMER(new ResourceLocation(ModMain.MODID, "timer_item_multi"), OptionTimerMultiItemChoose.class);
	
	private static final DialogOption1[] types = DialogOption1.values();
	static {
		for(DialogOption1 type : types) {
			DialogAPI.registerOption(type);
		}
	}
	
	private final ResourceLocation id;
	private final Class<? extends IDialogOptionDataUnit> optionClass;
	
	private DialogOption1(ResourceLocation id, Class<? extends IDialogOptionDataUnit> clazz) {
		this.id = id;
		this.optionClass = clazz;
	}
	
	@Override
	public ResourceLocation getTypeID() {
		return this.id;
	}

	@Override
	public IDialogOptionDataUnit getDataUnit(NBTTagCompound nbt) throws Exception {
		IDialogOptionDataUnit option = this.optionClass.newInstance();
		option.readFromNBT(nbt);
		return option;
	}
	@Override
	public IDialogOptionDataUnit getDataUnit(JsonObject json) throws Exception {
		IDialogOptionDataUnit option = this.optionClass.newInstance();
		option.readFromJson(json);
		return option;
	}
	
	public static DialogOption1 getTypeFromID(ResourceLocation id) {
		for(int i = 0; i < types.length; i++) {
			if(types[i].id.equals(id)) return types[i];
		}
		return null;
	}
	public static DialogOption1 getTypeFromClass(Class<?> clazz) {
		for(int i = 0; i < types.length; i++) {
			if(types[i].optionClass == clazz) return types[i];
		}
		return null;
	}
}
*/