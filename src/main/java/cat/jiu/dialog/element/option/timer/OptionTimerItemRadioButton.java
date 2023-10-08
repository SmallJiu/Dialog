package cat.jiu.dialog.element.option.timer;

import com.google.gson.JsonObject;

import cat.jiu.core.api.ITimer;
import cat.jiu.dialog.api.IDialogOption;
import cat.jiu.dialog.element.DialogOption;
import cat.jiu.dialog.element.OptionDrawUnit;
import cat.jiu.dialog.element.option.OptionItemRadioButton;
import cat.jiu.dialog.element.option.OptionTimerBase;
import cat.jiu.dialog.element.option.draw.timer.OptionTimerItemRadioButtonDrawUnit;
import cat.jiu.dialog.utils.dimension.DialogDimension;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stanhebben.zenscript.annotations.ZenClass;

@ZenRegister
@ZenClass("dialog.option.timer.ItemRadioButton")
public class OptionTimerItemRadioButton extends OptionTimerBase<OptionItemRadioButton> {
	public OptionTimerItemRadioButton() {
		super(new OptionItemRadioButton());
	}
	public OptionTimerItemRadioButton(ITimer timer, boolean timeOutAutoConfirm, OptionItemRadioButton option) {
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
		return new OptionTimerItemRadioButtonDrawUnit(dialogID, (OptionTimerItemRadioButton) option, optionID, dialogDimension);
	}

	@Override
	public ResourceLocation getTypeID() {
		return DialogOption.timer_item_radio_button().getTypeID();
	}

	@Override
	public OptionTimerItemRadioButton copy() {
		return new OptionTimerItemRadioButton(this.getTimer().copy(), this.isTimeoutAutoConfirm(), this.getOption().copy());
	}
}
