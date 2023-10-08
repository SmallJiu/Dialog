package cat.jiu.dialog.api.task;

import cat.jiu.dialog.api.IOptionTask;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;

@ZenRegister
@ZenClass("dialog.option.task.radio_button.Item")
public interface IItemRadioButtonTask extends IOptionTask {
	@Deprecated
	default void run(ResourceLocation parent, ResourceLocation dialog, int optionIndex, EntityPlayer player, int selectIndex, ItemStack stack, boolean confirm) {
		this.onItemRadioButton(parent, dialog, optionIndex, player, selectIndex, stack, confirm);
	}
	
	void onItemRadioButton(ResourceLocation parent, ResourceLocation dialog, int optionIndex, EntityPlayer player, int selectIndex, ItemStack stack, boolean confirm);
}
