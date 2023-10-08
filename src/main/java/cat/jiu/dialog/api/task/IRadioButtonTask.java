package cat.jiu.dialog.api.task;

import cat.jiu.dialog.api.IOptionTask;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;

@ZenRegister
@ZenClass("dialog.option.task.RadioButton")
public interface IRadioButtonTask extends IOptionTask {
	@Deprecated
	default void run(ResourceLocation parent, ResourceLocation dialog, int optionIndex, EntityPlayer player, int selectIndex, String optionString, boolean confirm) {
		this.onRadioButton(parent, dialog, optionIndex, player, selectIndex, optionString, confirm);
	}
	void onRadioButton(ResourceLocation parent, ResourceLocation dialog, int optionIndex, EntityPlayer player, int selectIndex, String optionString, boolean confirm);
}
