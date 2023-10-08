package cat.jiu.dialog.api.task;

import cat.jiu.dialog.api.IOptionTask;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;

@ZenRegister
@ZenClass("dialog.option.task.multi_title.Change")
public interface IMultiTitleChangeTask extends IOptionTask {
	@Deprecated
	default void run(ResourceLocation parent, ResourceLocation dialog, int taskid, EntityPlayer player, int newTitleIndex, int oldTitleIndex) {
		this.onMultiTitleChange(parent, dialog, taskid, player, newTitleIndex, oldTitleIndex);
	}
	
	void onMultiTitleChange(ResourceLocation parent, ResourceLocation dialog, int taskid, EntityPlayer player, int newTitleIndex, int oldTitleIndex);
}
