package cat.jiu.dialog.event;

import cat.jiu.dialog.event.OptionEvent;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class RadioButtonEvent extends OptionEvent {
	public final String optionString;
	public final int selectIndex;
	public final boolean confirm;
	public RadioButtonEvent(EntityPlayer player, ResourceLocation parent, ResourceLocation dialogID, int optionIndex, String optionStr, int selectIndex, boolean confirm) {
		super(player, parent, dialogID, optionIndex);
		this.optionString = optionStr;
		this.selectIndex = selectIndex;
		this.confirm = confirm;
	}
}
