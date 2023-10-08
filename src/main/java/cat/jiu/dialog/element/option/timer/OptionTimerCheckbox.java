package cat.jiu.dialog.element.option.timer;

import com.google.gson.JsonObject;

import cat.jiu.core.api.ITimer;
import cat.jiu.dialog.api.IDialogOption;
import cat.jiu.dialog.element.DialogOption;
import cat.jiu.dialog.element.OptionDrawUnit;
import cat.jiu.dialog.element.option.OptionCheckbox;
import cat.jiu.dialog.element.option.OptionTimerBase;
import cat.jiu.dialog.element.option.draw.timer.OptionTimerCheckboxDrawUnit;
import cat.jiu.dialog.utils.dimension.DialogDimension;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stanhebben.zenscript.annotations.ZenClass;

@ZenRegister
@ZenClass("dialog.option.timer.Checkbox")
public class OptionTimerCheckbox extends OptionTimerBase<OptionCheckbox> {
	public OptionTimerCheckbox() {
		super(new OptionCheckbox());
	}

	public OptionTimerCheckbox(ITimer timer, boolean timeOutAutoConfirm, OptionCheckbox option) {
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
		return new OptionTimerCheckboxDrawUnit(dialogID, (OptionTimerCheckbox) option, optionID, dialogDimension);
	}

	@Override
	public ResourceLocation getTypeID() {
		return DialogOption.timer_checkbox().getTypeID();
	}

	@Override
	public OptionTimerCheckbox copy() {
		return new OptionTimerCheckbox(this.getTimer().copy(), this.isTimeoutAutoConfirm(), this.getOption().copy());
	}
}
