package cat.jiu.dialog.api.task;

import cat.jiu.dialog.api.IOptionTask;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;

@ZenRegister
@ZenClass("dialog.option.task.checkbox.Check")
public interface ICheckboxCheckTask extends IOptionTask {
	@Deprecated
	default void run(ResourceLocation parent, ResourceLocation dialog, int optionIndex, EntityPlayer player, int selectIndex, String optionString, boolean remove) {
		this.onCheckboxCheck(parent, dialog, optionIndex, player, selectIndex, optionString, remove);
	}
	
	void onCheckboxCheck(ResourceLocation parent, ResourceLocation dialog, int optionIndex, EntityPlayer player, int selectIndex, String optionString, boolean remove);
}
