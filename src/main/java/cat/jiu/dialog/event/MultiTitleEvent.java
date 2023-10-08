package cat.jiu.dialog.event;

import java.util.List;

import cat.jiu.dialog.event.OptionEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class MultiTitleEvent extends OptionEvent {
	public final List<String> titles;
	public final int newTitleIndex;
	protected MultiTitleEvent(EntityPlayer player, ResourceLocation parent, ResourceLocation dialogID, int optionIndex, List<String> titles, int titleIndex) {
		super(player, parent, dialogID, optionIndex);
		this.titles = titles;
		this.newTitleIndex = titleIndex;
	}
	
	public static class Change extends MultiTitleEvent {
		public final int oldTitlendex;
		public Change(EntityPlayer player, ResourceLocation parent, ResourceLocation dialogID, int optionIndex, List<String> titles, int newTitle, int oldTitle) {
			super(player, parent, dialogID, optionIndex, titles, newTitle);
			this.oldTitlendex = oldTitle;
		}
	}
	
	public static class BackParent extends MultiTitleEvent {
		public BackParent(EntityPlayer player, ResourceLocation dialogID, int optionIndex, List<String> titles, int titleIndex, ResourceLocation parent) {
			super(player, parent, dialogID, optionIndex, titles, titleIndex);
		}
	}
	
	public static class Close extends MultiTitleEvent {
		public Close(EntityPlayer player, ResourceLocation parent, ResourceLocation dialogID, int optionIndex, List<String> titles, int titleIndex) {
			super(player, parent, dialogID, optionIndex, titles, titleIndex);
		}
	}
}
