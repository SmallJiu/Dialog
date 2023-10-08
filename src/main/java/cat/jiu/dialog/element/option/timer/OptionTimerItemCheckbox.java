package cat.jiu.dialog.element.option.timer;

import com.google.gson.JsonObject;

import cat.jiu.core.api.ITimer;
import cat.jiu.dialog.api.IDialogOption;
import cat.jiu.dialog.element.DialogOption;
import cat.jiu.dialog.element.OptionDrawUnit;
import cat.jiu.dialog.element.option.OptionItemCheckbox;
import cat.jiu.dialog.element.option.OptionTimerBase;
import cat.jiu.dialog.element.option.draw.timer.OptionTimerItemCheckboxDrawUnit;
import cat.jiu.dialog.utils.dimension.DialogDimension;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stanhebben.zenscript.annotations.ZenClass;

@ZenRegister
@ZenClass("dialog.option.timer.ItemCheckbox")
public class OptionTimerItemCheckbox extends OptionTimerBase<OptionItemCheckbox> {
	public OptionTimerItemCheckbox() {
		super(new OptionItemCheckbox());
	}
	public OptionTimerItemCheckbox(ITimer timer, boolean timeOutAutoConfirm, OptionItemCheckbox option) {
		super(timer, timeOutAutoConfirm, option);
		if(this.getOption().getColumn() != 4) {
			this.getOption().setColumn(4);
		}
	}

	@Override
	public void readFromJson(JsonObject json) {
		super.readFromJson(json);
		if(this.getOption().getColumn() != 4) {
			this.getOption().setColumn(4);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		if(this.getOption().getColumn() != 4) {
			this.getOption().setColumn(4);
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public OptionDrawUnit getDrawUnit(ResourceLocation dialogID, int optionID, IDialogOption option, DialogDimension dialogDimension) {
		return new OptionTimerItemCheckboxDrawUnit(dialogID, (OptionTimerItemCheckbox) option, optionID, dialogDimension);
	}

	@Override
	public ResourceLocation getTypeID() {
		return DialogOption.timer_item_checkbox().getTypeID();
	}

	@Override
	public OptionTimerItemCheckbox copy() {
		return new OptionTimerItemCheckbox(this.getTimer().copy(), this.isTimeoutAutoConfirm(), this.getOption().copy());
	}
}
