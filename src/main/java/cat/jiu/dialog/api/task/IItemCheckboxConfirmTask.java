package cat.jiu.dialog.api.task;

import java.util.Map;

import cat.jiu.dialog.api.IOptionTask;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;

@ZenRegister
@ZenClass("dialog.option.task.checkbox.item.Confirm")
public interface IItemCheckboxConfirmTask extends IOptionTask {
	@Deprecated
	default void run(ResourceLocation parent, ResourceLocation dialog, int option, EntityPlayer player, Map<Integer, ItemStack> selects) {
		this.onItemCheckboxConfirm(parent, dialog, option, player, selects);
	}
	
	void onItemCheckboxConfirm(ResourceLocation parent, ResourceLocation dialog, int option, EntityPlayer player, Map<Integer, ItemStack> selects);
}
