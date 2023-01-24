package cat.jiu.dialog.element;

import com.google.gson.JsonObject;

import cat.jiu.dialog.DialogAPI;
import cat.jiu.dialog.ModMain;
import cat.jiu.dialog.api.DialogDimension;
import cat.jiu.dialog.element.option.DialogOptionDrawUnit;
import cat.jiu.dialog.element.option.button.DialogButton;
import cat.jiu.dialog.element.option.button.GuiOptionButton;
import cat.jiu.dialog.element.option.text.DialogTextField;
import cat.jiu.dialog.element.option.text.GuiOptionTextField;
import cat.jiu.dialog.iface.IDialogOptionDataUnit;
import cat.jiu.dialog.iface.IDialogOptionType;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public enum DialogOptionType implements IDialogOptionType {
	/**
	 * 对话框 按钮 选项
	 */
	BUTTON(new ResourceLocation(ModMain.MODID, "button"), DialogButton.class),
	/**
	 * 对话框 文本输入框 选项
	 */
	TEXT  (new ResourceLocation(ModMain.MODID, "text"), DialogTextField.class);
	
	private static final DialogOptionType[] types = DialogOptionType.values();
	
	private final ResourceLocation id;
	private final Class<? extends IDialogOptionDataUnit> optionClass;
	
	private DialogOptionType(ResourceLocation id, Class<? extends IDialogOptionDataUnit> clazz) {
		this.id = id;
		this.optionClass = clazz;
		DialogAPI.registerOption(this);
	}

	@Override
	public ResourceLocation getTypeID() {
		return this.id;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public DialogOptionDrawUnit getDrawUnit(ResourceLocation dialogID, int optionID, IDialogOptionDataUnit option, DialogDimension dialogDimension) {
		Minecraft mc = Minecraft.getMinecraft();
		if(BUTTON.getTypeID().equals(option.getTypeID())) {
			return new GuiOptionButton(mc.player, dialogID, (DialogButton) option, optionID, dialogDimension);
		}else if(TEXT.getTypeID().equals(option.getTypeID())) {
			return new GuiOptionTextField(mc.player, dialogID, (DialogTextField) option, optionID, dialogDimension);
		}
		return null;
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
	
	public static DialogOptionType getTypeFromID(ResourceLocation id) {
		for(int i = 0; i < types.length; i++) {
			if(types[i].id.equals(id)) return types[i];
		}
		return null;
	}
	public static DialogOptionType getTypeFromClass(Class<?> clazz) {
		for(int i = 0; i < types.length; i++) {
			if(types[i].optionClass == clazz) return types[i];
		}
		return null;
	}
}
