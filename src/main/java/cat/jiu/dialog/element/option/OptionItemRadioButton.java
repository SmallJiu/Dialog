package cat.jiu.dialog.element.option;

import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;

import cat.jiu.dialog.api.IDialogOption;
import cat.jiu.dialog.element.DialogOption;
import cat.jiu.dialog.element.OptionDrawUnit;
import cat.jiu.dialog.element.option.draw.OptionItemRadioButtonDrawUnit;
import cat.jiu.dialog.utils.dimension.DialogDimension;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("dialog.option.ItemRadioButton")
public class OptionItemRadioButton extends OptionItemCheckbox {
	protected int defaultSelect = 0;

	public OptionItemRadioButton() {
	}

	/**
	 * Recommended for single option use
	 * 
	 * @param canCloseDialog
	 *            true if choose one to close dialog
	 * @param stacks
	 *            items
	 * @param defaultSelect
	 * 
	 * @param column
	 *            columns num, range 1 ~ 5
	 */
	public OptionItemRadioButton(boolean canCloseDialog, int column, int defaultSelect, ItemStack... stacks) {
		super(canCloseDialog, column, null, stacks);
		this.defaultSelect = defaultSelect;
	}

	/**
	 * Recommended for single option use
	 * 
	 * @param canCloseDialog
	 *            true if choose one to close dialog
	 * @param stacks
	 *            items
	 * @param defaultSelect
	 * 
	 * @param column
	 *            columns num, range 1 ~ 5
	 */
	public OptionItemRadioButton(boolean canCloseDialog, int column, int defaultSelect, List<ItemStack> stacks) {
		super(canCloseDialog, column, null, stacks);
		this.defaultSelect = defaultSelect;
	}

	@Override
	public ResourceLocation getTypeID() {
		return DialogOption.item_radio_button().getTypeID();
	}

	public int[] getDefaultSelect() {
		return new int[] { defaultSelect };
	}
	
	@ZenMethod("default")
	public int getDefaultSelectSingle() {
		return defaultSelect;
	}

	@Override
	public JsonObject writeToJson(JsonObject json) {
		json = super.writeToJson(json);
		json.addProperty("default", this.defaultSelect);
		return json;
	}

	@Override
	public void readFromJson(JsonObject json) {
		super.readFromJson(json);
		if(json.has("default")) {
			this.defaultSelect = json.get("default").getAsInt();
		}
	}

	@Override
	public NBTTagCompound writeToNBT() {
		NBTTagCompound nbt = super.writeToNBT();
		nbt.setInteger("default", this.defaultSelect);
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		if(nbt.hasKey("default")) {
			this.defaultSelect = nbt.getInteger("default");
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public OptionDrawUnit getDrawUnit(ResourceLocation dialogID, int optionID, IDialogOption option, DialogDimension dialogDimension) {
		return new OptionItemRadioButtonDrawUnit(dialogID, (OptionItemRadioButton) option, optionID, dialogDimension);
	}

	@Override
	public OptionItemRadioButton copy() {
		List<ItemStack> options = this.getStacks().stream().map(option -> option.copy()).collect(Collectors.toList());
		return new OptionItemRadioButton(this.canCloseDialog(), this.getColumn(), this.getDefaultSelectSingle(), options);
	}
}
