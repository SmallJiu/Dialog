package cat.jiu.dialog.event;

import java.util.Map;

import cat.jiu.dialog.event.OptionEvent;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class CheckboxEvent extends OptionEvent {
	
	protected CheckboxEvent(EntityPlayer player, ResourceLocation parent, ResourceLocation dialogID, int optionIndex) {
		super(player, parent, dialogID, optionIndex);
	}

	public static class Check extends CheckboxEvent {
		public final String optionString;
		public final int selectIndex;
		public final boolean isRemove;
		public Check(EntityPlayer player, ResourceLocation parent, ResourceLocation dialogID, int optionID, String options, int selectIndex, boolean isRemove) {
			super(player, parent, dialogID, optionID);
			this.optionString = options;
			this.selectIndex = selectIndex;
			this.isRemove = isRemove;
		}
	}
	
	public static class Confirm extends CheckboxEvent {
		public final Map<Integer, String> selects;
		public Confirm(EntityPlayer player, ResourceLocation parent, ResourceLocation dialogID, int optionID, Map<Integer, String> selects) {
			super(player, parent, dialogID, optionID);
			this.selects = selects;
		}
	}
}
