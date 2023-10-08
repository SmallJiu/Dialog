package cat.jiu.dialog.element.option.timer;

import com.google.gson.JsonObject;

import cat.jiu.core.api.ITimer;
import cat.jiu.dialog.api.IDialogOption;
import cat.jiu.dialog.element.DialogOption;
import cat.jiu.dialog.element.OptionDrawUnit;
import cat.jiu.dialog.element.option.OptionRadioButton;
import cat.jiu.dialog.element.option.OptionTimerBase;
import cat.jiu.dialog.element.option.draw.timer.OptionTimerRadioButtonDrawUnit;
import cat.jiu.dialog.utils.dimension.DialogDimension;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stanhebben.zenscript.annotations.ZenClass;

@ZenRegister
@ZenClass("dialog.option.timer.RadioButton")
public class OptionTimerRadioButton extends OptionTimerBase<OptionRadioButton> {
	public OptionTimerRadioButton() {
		super(new OptionRadioButton());
	}
	public OptionTimerRadioButton(ITimer timer, boolean timeOutAutoConfirm, OptionRadioButton option) {
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
		if(this.getOption().getColumn() > 4 || this.getOption().getColumn() < 4) {
			this.getOption().setColumn(4);
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public OptionDrawUnit getDrawUnit(ResourceLocation dialogID, int optionID, IDialogOption option, DialogDimension dialogDimension) {
		return new OptionTimerRadioButtonDrawUnit(dialogID, (OptionTimerRadioButton) option, optionID, dialogDimension);
	}

	@Override
	public ResourceLocation getTypeID() {
		return DialogOption.timer_radio_button().getTypeID();
	}

	@Override
	public OptionTimerRadioButton copy() {
		return new OptionTimerRadioButton(this.getTimer().copy(), this.isTimeoutAutoConfirm(), this.getOption().copy());
	}
}
