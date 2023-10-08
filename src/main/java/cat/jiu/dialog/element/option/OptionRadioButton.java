package cat.jiu.dialog.element.option;

import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;

import cat.jiu.core.api.element.IText;
import cat.jiu.dialog.api.IDialogOption;
import cat.jiu.dialog.element.DialogOption;
import cat.jiu.dialog.element.OptionDrawUnit;
import cat.jiu.dialog.element.option.draw.OptionRadioButtonDrawUnit;
import cat.jiu.dialog.utils.dimension.DialogDimension;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("dialog.option.RadioButton")
public class OptionRadioButton extends OptionCheckbox {
	protected int defaultSelect;

	public OptionRadioButton() {
	}

	/**
	 * Recommended for single option use
	 * 
	 * @param canCloseDialog
	 *            true if choose one to close dialog
	 * @param options
	 *            options
	 * @param column
	 *            columns num
	 * @param defaultSelect
	 *            default select option
	 */
	public OptionRadioButton(boolean canCloseDialog, int column, int defaultSelect, List<IText> options) {
		super(canCloseDialog, column, OptionCheckbox.EMPTY_DEFAULT_SELECT, options);
		this.defaultSelect = defaultSelect;
	}

	public OptionRadioButton(boolean canCloseDialog, int column, int defaultSelect, IText... options) {
		super(canCloseDialog, column, OptionCheckbox.EMPTY_DEFAULT_SELECT, options);
		this.defaultSelect = defaultSelect;
	}

	@Override
	public int[] getDefaultSelect() {
		return new int[] { this.getDefaultSelectSingle() };
	}
	
	@ZenMethod("default")
	public int getDefaultSelectSingle() {
		return this.defaultSelect;
	}

	@Override
	public ResourceLocation getTypeID() {
		return DialogOption.radio_button().getTypeID();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public OptionDrawUnit getDrawUnit(ResourceLocation dialogID, int optionID, IDialogOption option, DialogDimension dialogDimension) {
		return new OptionRadioButtonDrawUnit(dialogID, (OptionRadioButton) option, optionID, dialogDimension);
	}

	@Override
	public JsonObject writeToJson(JsonObject j) {
		JsonObject json = super.writeToJson(j);
		json.addProperty("select", this.getDefaultSelectSingle());
		return json;
	}

	@Override
	public void readFromJson(JsonObject json) {
		super.readFromJson(json);
		this.defaultSelect = json.get("select").getAsInt();
	}

	@Override
	public NBTTagCompound writeToNBT() {
		NBTTagCompound nbt = super.writeToNBT();
		nbt.setInteger("select", this.getDefaultSelectSingle());
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.defaultSelect = nbt.getInteger("select");
	}

	@Override
	public OptionRadioButton copy() {
		List<IText> options = this.getOptions().stream().map(option -> option.copy()).collect(Collectors.toList());
		return new OptionRadioButton(this.canCloseDialog(), this.getColumn(), this.getDefaultSelectSingle(), options);
	}
}
