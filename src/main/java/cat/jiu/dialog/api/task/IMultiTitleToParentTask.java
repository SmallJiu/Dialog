package cat.jiu.dialog.api.task;

import cat.jiu.dialog.api.IOptionTask;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;

@ZenRegister
@ZenClass("dialog.option.task.multi_title.Parent")
public interface IMultiTitleToParentTask extends IOptionTask {
	@Deprecated
	default void run(ResourceLocation dialog, int taskid, EntityPlayer player, int title, ResourceLocation parent) {
		this.run(dialog, taskid, player, title, parent);
	}
	void onMultiTitleToParent(ResourceLocation dialog, int taskid, EntityPlayer player, int title, ResourceLocation parent);
}
