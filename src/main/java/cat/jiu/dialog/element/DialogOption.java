package cat.jiu.dialog.element;

import java.util.Map;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;

import cat.jiu.dialog.DialogAPI;
import cat.jiu.dialog.ModMain;
import cat.jiu.dialog.api.IDialogOption;
import cat.jiu.dialog.api.IDialogOptionType;
import cat.jiu.dialog.element.option.*;
import cat.jiu.dialog.element.option.timer.*;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("dialog.Options")
@SuppressWarnings("unchecked")
public class DialogOption <T extends IDialogOption> implements IDialogOptionType {
	private static boolean register;
	private static final Map<ResourceLocation, DialogOption<?>> REGISTRY_ID = Maps.newHashMap();
	private static final Map<Class<? extends IDialogOption>, DialogOption<?>> REGISTRY_CLAZZ = Maps.newHashMap();
	
	private static DialogOption<OptionButton> BUTTON;
	@ZenGetter	public static DialogOption<OptionButton> button() {return BUTTON;}
	
	private static DialogOption<OptionTextField> TEXT;
	@ZenGetter	public static DialogOption<OptionTextField> text() {return TEXT;}
	
	private static DialogOption<OptionCheckbox> CHECKBOX;
	@ZenGetter	public static DialogOption<OptionCheckbox> checkbox() {return CHECKBOX;}
	
	private static DialogOption<OptionRadioButton> RADIO_BUTTON;
	@ZenGetter	public static DialogOption<OptionRadioButton> radio_button() {return RADIO_BUTTON;}
	
	private static DialogOption<OptionItemRadioButton> ITEM_RADIO_BUTTON;
	@ZenGetter	public static DialogOption<OptionItemRadioButton> item_radio_button() {return ITEM_RADIO_BUTTON;}
	
	private static DialogOption<OptionItemCheckbox> ITEM_CHECKBOX;
	@ZenGetter	public static DialogOption<OptionItemCheckbox> item_checkbox() {return ITEM_CHECKBOX;}
	
	private static DialogOption<OptionMultiTitle> MULTI_TITLE;
	@ZenGetter	public static DialogOption<OptionMultiTitle> multi_tile() {return MULTI_TITLE;}
	
	private static DialogOption<OptionTimerCheckbox> CHECKBOX_TIMER;
	@ZenGetter	public static DialogOption<OptionTimerCheckbox> timer_checkbox() {return CHECKBOX_TIMER;}
	
	private static DialogOption<OptionTimerRadioButton> RADIO_BUTTON_TIMER;
	@ZenGetter	public static DialogOption<OptionTimerRadioButton> timer_radio_button() {return RADIO_BUTTON_TIMER;}
	
	private static DialogOption<OptionTimerItemRadioButton> ITEM_RADIO_BUTTON_TIMER;
	@ZenGetter	public static DialogOption<OptionTimerItemRadioButton> timer_item_radio_button() {return ITEM_RADIO_BUTTON_TIMER;}
	
	private static DialogOption<OptionTimerItemCheckbox> ITEM_CHECKBOX_TIMER;
	@ZenGetter	public static DialogOption<OptionTimerItemCheckbox> timer_item_checkbox() {return ITEM_CHECKBOX_TIMER;}
	
	protected final ResourceLocation id;
	protected final Class<T> optionClass;
	
	private DialogOption(ResourceLocation id, Class<T> clazz) {
		this.id = id;
		this.optionClass = clazz;
		DialogAPI.registerOption(this);
		REGISTRY_ID.put(id, this);
		REGISTRY_CLAZZ.put(clazz, this);
	}
	
	@Override
	public ResourceLocation getTypeID() {
		return this.id;
	}
	
	@Override
	public T getDataUnit(NBTTagCompound nbt) throws Exception {
		T option = this.optionClass.newInstance();
		option.readFromNBT(nbt);
		return option;
	}
	
	@Override
	public T getDataUnit(JsonObject json) throws Exception {
		T option = this.optionClass.newInstance();
		option.readFromJson(json);
		return option;
	}

	@ZenMethod
	public static <T extends IDialogOption> DialogOption<T> getTypeFromID(String id) {
		return (DialogOption<T>) REGISTRY_ID.get(new ResourceLocation(id));
	}
	@ZenMethod
	public static <T extends IDialogOption> DialogOption<T> getTypeFromID(String domain, String id) {
		return (DialogOption<T>) REGISTRY_ID.get(new ResourceLocation(domain, id));
	}
	public static <T extends IDialogOption> DialogOption<T> getTypeFromID(ResourceLocation id) {
		return (DialogOption<T>) REGISTRY_ID.get(id);
	}
	public static <T extends IDialogOption> DialogOption<T> getTypeFromClass(Class<?> clazz) {
		return (DialogOption<T>) REGISTRY_CLAZZ.get(clazz);
	}
	
	@ZenMethod
	public static <T extends IDialogOption> DialogOption<T> register(String id, Class<T> clazz) {
		return register(new ResourceLocation(id), clazz);
	}
	@ZenMethod
	public static <T extends IDialogOption> DialogOption<T> register(String domain, String id, Class<T> clazz) {
		return register(new ResourceLocation(domain, id), clazz);
	}
	public static <T extends IDialogOption> DialogOption<T> register(ResourceLocation id, Class<T> clazz) {
		if(REGISTRY_ID.containsKey(id)) {
			return (DialogOption<T>) REGISTRY_ID.get(id);
		}
		if(REGISTRY_CLAZZ.containsKey(clazz)) {
			return (DialogOption<T>) REGISTRY_CLAZZ.get(clazz);
		}
		return new DialogOption<>(id, clazz);
	}
	
	public static void register() {
		if (register) {
			return;
		}
		BUTTON = register(ModMain.MODID, "button", OptionButton.class);
		TEXT = register(ModMain.MODID, "text", OptionTextField.class);
		
		CHECKBOX = register(ModMain.MODID, "checkbox", OptionCheckbox.class);
		RADIO_BUTTON = register(ModMain.MODID, "radio_btn", OptionRadioButton.class);
		ITEM_RADIO_BUTTON = register(ModMain.MODID, "item_radio", OptionItemRadioButton.class);
		ITEM_CHECKBOX = register(ModMain.MODID, "item_checkbox", OptionItemCheckbox.class);
		MULTI_TITLE  = register(ModMain.MODID, "multi_tile", OptionMultiTitle.class);
		
		CHECKBOX_TIMER = register(ModMain.MODID, "timer_checkbox", OptionTimerCheckbox.class);
		RADIO_BUTTON_TIMER = register(ModMain.MODID, "timer_radio_btn", OptionTimerRadioButton.class);
		ITEM_RADIO_BUTTON_TIMER = register(ModMain.MODID, "timer_item_radio", OptionTimerItemRadioButton.class);
		ITEM_CHECKBOX_TIMER = register(ModMain.MODID, "timer_item_checkbox", OptionTimerItemCheckbox.class);
		register = true;
	}
}
