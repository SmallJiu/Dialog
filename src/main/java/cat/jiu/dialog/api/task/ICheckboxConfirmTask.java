package cat.jiu.dialog.api.task;

import java.util.Map;

import cat.jiu.dialog.api.IOptionTask;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;

@ZenRegister
@ZenClass("dialog.option.task.checkbox.Confirm")
public interface ICheckboxConfirmTask extends IOptionTask {
	@Deprecated
	default void run(ResourceLocation parent, ResourceLocation dialog, int option, EntityPlayer player, Map<Integer, String> selects) {
		this.onCheckboxConfirm(parent, dialog, option, player, selects);
	}
	
	void onCheckboxConfirm(ResourceLocation parent, ResourceLocation dialog, int option, EntityPlayer player, Map<Integer, String> selects);
}
