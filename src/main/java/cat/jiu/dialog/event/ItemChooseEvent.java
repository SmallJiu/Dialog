package cat.jiu.dialog.event;

import java.util.Map;

import cat.jiu.dialog.event.OptionEvent;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ItemChooseEvent extends OptionEvent {
	protected ItemChooseEvent(EntityPlayer player, ResourceLocation parent, ResourceLocation dialogID, int optionIndex) {
		super(player, parent, dialogID, optionIndex);
	}

	public static class Multi extends ItemChooseEvent {
		protected Multi(EntityPlayer player, ResourceLocation parent, ResourceLocation dialogID, int optionIndex) {
			super(player, parent, dialogID, optionIndex);
		}
		public static class Select extends Multi {
			public final boolean remove;
			public final ItemStack stack;
			public final int selectIndex;
			public Select(EntityPlayer player, ResourceLocation parent, ResourceLocation dialogID, int optionIndex, int index, ItemStack stack, boolean remove) {
				super(player, parent, dialogID, optionIndex);
				this.remove = remove;
				this.stack = stack;
				this.selectIndex = index;
			}
		}
		public static class Confirm extends Multi {
			public final Map<Integer, ItemStack> selects;
			public Confirm(EntityPlayer player, ResourceLocation parent, ResourceLocation dialogID, int optionIndex, Map<Integer, ItemStack> selects) {
				super(player, parent, dialogID, optionIndex);
				this.selects = selects;
			}
		}
	}
	
	public static class Single extends ItemChooseEvent {
		public final ItemStack stack;
		public final int selectIndex;
		public final boolean confirm;
		
		public Single(EntityPlayer player, ResourceLocation parent, ResourceLocation dialogID, int optionIndex, ItemStack stack, int select, boolean remove) {
			super(player, parent, dialogID, optionIndex);
			this.stack = stack;
			this.selectIndex = select;
			this.confirm = remove;
		}
	}
}
