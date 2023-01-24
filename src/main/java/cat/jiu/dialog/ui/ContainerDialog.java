package cat.jiu.dialog.ui;

import cat.jiu.dialog.element.Dialog;
import cat.jiu.dialog.iface.IDialogOptionDataUnit;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerDialog extends Container {
	private Dialog dialog;
	
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
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}
}
