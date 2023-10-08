package cat.jiu.dialog.api.task;

import cat.jiu.dialog.api.IOptionTask;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;

@ZenRegister
@ZenClass("dialog.option.task.checkbox.item.Check")
public interface IItemCheckboxCheckTask extends IOptionTask {
	@Deprecated
	default void run(ResourceLocation parent, ResourceLocation dialog, int optionID, EntityPlayer player, int setectIndex, ItemStack stack, boolean remove) {
		this.onItemCheckboxCheck(parent, dialog, optionID, player, setectIndex, stack, remove);
	}
	
	void onItemCheckboxCheck(ResourceLocation parent, ResourceLocation dialog, int optionID, EntityPlayer player, int setectIndex, ItemStack stack, boolean remove);
}
