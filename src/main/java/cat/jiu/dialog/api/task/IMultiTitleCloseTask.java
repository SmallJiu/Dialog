package cat.jiu.dialog.api.task;

import cat.jiu.dialog.api.IOptionTask;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;

@ZenRegister
@ZenClass("dialog.option.task.multi_title.Close")
public interface IMultiTitleCloseTask extends IOptionTask {
	@Deprecated
	default void run(ResourceLocation parent, ResourceLocation dialog, int taskid, EntityPlayer player, int title) {
		this.onMultiTitleClose(parent, dialog, taskid, player, title);
	}
	
	void onMultiTitleClose(ResourceLocation parent, ResourceLocation dialog, int taskid, EntityPlayer player, int title);
}
