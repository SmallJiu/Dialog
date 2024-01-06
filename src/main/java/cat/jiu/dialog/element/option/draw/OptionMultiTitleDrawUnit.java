package cat.jiu.dialog.element.option.draw;

import cat.jiu.dialog.DialogAPI;
import cat.jiu.dialog.ModMain;
import cat.jiu.dialog.element.option.OptionMultiTitle;
import cat.jiu.dialog.event.MultiTitleEvent;
import cat.jiu.dialog.net.msg.option.MsgMultiTitle;
import cat.jiu.dialog.ui.GuiDialog;
import cat.jiu.dialog.utils.dimension.DialogDimension;
import cat.jiu.dialog.utils.dimension.OptionDimension;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import stanhebben.zenscript.annotations.ZenClass;

@ZenRegister
@ZenClass("dialog.draw.MultiTitle")
public class OptionMultiTitleDrawUnit extends GuiOptionButton {
	protected final OptionMultiTitle option;
	protected int currentTitleIndex = 0;
	public OptionMultiTitleDrawUnit(ResourceLocation dialogID, OptionMultiTitle option, int optionID, DialogDimension dialogDimension) {
		super(Minecraft.getMinecraft().player, dialogID, option, optionID, dialogDimension);
		this.option = option;
	}
	
	@Override
	public void init(GuiDialog gui, FontRenderer fr) {
		super.init(gui, fr);
		gui.setTitle(this.option.getTitles().get(this.currentTitleIndex));
	}
	
	@Override
	public void mouseClicked(GuiDialog gui, int mouseX, int mouseY, int mouseButton, OptionDimension dimension) {
		if(GuiDialog.isInRange(mouseX, mouseY, dimension)) {
			if(this.currentTitleIndex >= this.option.getTitles().size()-1) {
				if(this.option.canBackPreviousDialog()
				&& gui.getParentDialog() != null
				&& !this.option.canCloseDialog()) {
					MinecraftForge.EVENT_BUS.post(new MultiTitleEvent.BackParent(this.player, this.dialogID, this.optionID, this.option.getTitlesAsString(), this.currentTitleIndex, gui.getParentDialog().getDialog().getID()));
					ModMain.NETWORK.sendMessageToServer(new MsgMultiTitle.BackParent(this.dialogID, this.optionID, this.option.getTitlesAsString(), this.currentTitleIndex, gui.getParentDialog().getDialog().getID()));
					
					DialogAPI.displayDialog(this.player, gui.getParentDialog().getDialog());
					return;
				}
				if(this.option.canCloseDialog()) {
					MinecraftForge.EVENT_BUS.post(new MultiTitleEvent.Close(this.player, gui.getParentDialogID(), this.dialogID, this.optionID, this.option.getTitlesAsString(), this.currentTitleIndex));
					ModMain.NETWORK.sendMessageToServer(new MsgMultiTitle.Close(gui.getParentDialogID(), this.dialogID, this.optionID, this.option.getTitlesAsString(), this.currentTitleIndex));
					
					this.player.closeScreen();
				}
				return;
			}
			
			this.currentTitleIndex++;
			if(this.currentTitleIndex == this.option.getTitles().size() - 1) {
				if(this.option.canBackPreviousDialog()) {
					this.option.setDisplayText(OptionMultiTitle.BACK);
					super.text.clear();
					super.text.addAll(super.getOptionText(option, dialogDimension, Minecraft.getMinecraft().fontRenderer));
				}
				if(this.option.canCloseDialog()) {
					this.option.setDisplayText(OptionMultiTitle.CLOSE);
					super.text.clear();
					super.text.addAll(super.getOptionText(option, dialogDimension, Minecraft.getMinecraft().fontRenderer));
				}
			}
			
			try {
				gui.setTitle(this.option.getTitles().get(this.currentTitleIndex));
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			MinecraftForge.EVENT_BUS.post(new MultiTitleEvent.Change(this.player, gui.getParentDialogID(), this.dialogID, this.optionID, this.option.getTitlesAsString(), this.currentTitleIndex, this.currentTitleIndex-1));
			ModMain.NETWORK.sendMessageToServer(new MsgMultiTitle.Change(gui.getParentDialogID(), this.dialogID, this.optionID, this.option.getTitlesAsString(), this.currentTitleIndex, this.currentTitleIndex-1));
			
			playClickSound();
		}
	}
}
