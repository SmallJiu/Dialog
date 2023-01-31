package cat.jiu.dialog.ui;

import cat.jiu.dialog.element.Dialog;
import cat.jiu.dialog.iface.IDialogOptionDataUnit;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerDialog extends Container {
	private Dialog dialog;
	protected final EntityPlayer player;
	
	public ContainerDialog(EntityPlayer player) {
		this.player = player;
	}
	
	public Dialog getDialog() {
		return dialog;
	}
	public void setDialog(Dialog dialog) {
		if(this.dialog==null) {
			this.dialog = dialog;
		}
	}
	
	@SideOnly(Side.CLIENT)
	public int getMaxTextWidth() {
		int width = 0;
		for(IDialogOptionDataUnit option : dialog.getOptions()) {
			width = Math.max(width, Minecraft.getMinecraft().fontRenderer.getStringWidth(option.getOptionText().format()));
		}
		return width;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		return ItemStack.EMPTY;
	}
	
	@Override
	protected Slot addSlotToContainer(Slot slotIn) {
        return super.addSlotToContainer(slotIn);
    }

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}
}
