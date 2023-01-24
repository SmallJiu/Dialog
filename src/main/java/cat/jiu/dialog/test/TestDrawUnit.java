package cat.jiu.dialog.test;

import java.awt.Color;

import cat.jiu.dialog.api.DialogDimension;
import cat.jiu.dialog.api.OptionDimension;
import cat.jiu.dialog.element.option.DialogOptionDrawUnit;
import cat.jiu.dialog.iface.IDialogOptionDataUnit;
import cat.jiu.dialog.ui.GuiDialog;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;

public class TestDrawUnit extends DialogOptionDrawUnit {
	public TestDrawUnit(ResourceLocation dialogID, IDialogOptionDataUnit option, int optionID, FontRenderer fr, DialogDimension dialogDimension) {
		super(dialogID, option, optionID, dialogDimension);
	}
	
	int topWidth = 0;
	int downWidth = 0;
	int leftHight = 0;
	int rightHight = 0;
	boolean reverse = false;
	
	@Override
	public void draw(GuiDialog gui, int mouseX, int mouseY, OptionDimension dim, Minecraft mc, FontRenderer fr) {
		int ik = 9;
		if(!GuiDialog.isInRange(mouseX, mouseY, dim)) {
			gui.drawGradientRect(dim.x, dim.y, dim.x+dim.width, dim.y+ik, Color.BLACK.getRGB(), Color.BLUE.getRGB());
			gui.drawGradientRect(dim.x, dim.y+ik, dim.x+dim.width, dim.y+dim.height-ik, Color.BLUE.getRGB(), Color.BLUE.getRGB());
			gui.drawGradientRect(dim.x, dim.y+dim.height-ik, dim.x+dim.width, dim.y+dim.height - 2, Color.BLUE.getRGB(), Color.BLACK.getRGB());
		}else {
			gui.drawGradientRect(dim.x, dim.y, dim.x+dim.width, dim.y+ik, Color.WHITE.getRGB(), Color.PINK.getRGB());
			gui.drawGradientRect(dim.x, dim.y+ik, dim.x+dim.width, dim.y+dim.height-ik, Color.PINK.getRGB(), Color.PINK.getRGB());
			gui.drawGradientRect(dim.x, dim.y+dim.height-ik, dim.x+dim.width, dim.y+dim.height - 2, Color.PINK.getRGB(), Color.WHITE.getRGB());
		}
		if(reverse) {
			if(leftHight>0) gui.drawVerticalLine(dim.x, dim.y, dim.y+leftHight-1, Color.RED.getRGB());
			if(rightHight>0) gui.drawVerticalLine(dim.x+dim.width, dim.y+dim.height-rightHight-1, dim.y+dim.height-1, Color.YELLOW.getRGB());
			if(topWidth>0) gui.drawHorizontalLine(dim.x+dim.width, dim.x+dim.width-topWidth, dim.y, Color.WHITE.getRGB());
			if(downWidth>0) gui.drawHorizontalLine(dim.x, dim.x+downWidth, dim.y+dim.height - 2, Color.GREEN.getRGB());
		}else {
			if(leftHight>0) gui.drawVerticalLine(dim.x, dim.y+dim.height-leftHight, dim.y+dim.height, Color.RED.getRGB());
			if(rightHight>0) gui.drawVerticalLine(dim.x+dim.width, dim.y, dim.y+rightHight, Color.YELLOW.getRGB());
			if(topWidth>0) gui.drawHorizontalLine(dim.x, dim.x+topWidth, dim.y, Color.WHITE.getRGB());
			if(downWidth>0) gui.drawHorizontalLine(dim.x+dim.width, dim.x+dim.width-downWidth, dim.y+dim.height - 2, Color.GREEN.getRGB());
		}
		
		int y = dim.y+3;
		for(int i = 0; i < this.text.size(); i++) {
			fr.drawString(this.text.get(i), dim.x+3, y, !GuiDialog.isInRange(mouseX, mouseY, dim) ? Color.PINK.getRGB() : Color.BLACK.getRGB());
			y+=9;
		}
		
		if(!reverse) {
			if(topWidth < dim.width) {
				topWidth++;
			}else if(rightHight < dim.height) {
				rightHight++;
			}else if(downWidth < dim.width) {
				downWidth++;
			}else if(leftHight < dim.height) {
				leftHight++;
			}else {
				reverse = true;
			}
		}else {
			if(topWidth>0) {
				topWidth--;
			}else if(rightHight>0) {
				rightHight--;
			}else if(downWidth>0) {
				downWidth--;
			}else if(leftHight>0) {
				leftHight--;
			}else {
				reverse = false;
			}
		}
		
//		dim.width = 10;
//		dim.height = 10;
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton, OptionDimension dimension) {
		if(GuiDialog.isInRange(mouseX, mouseY, dimension)) {
			Minecraft mc = Minecraft.getMinecraft();
			mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.BLOCK_ANVIL_PLACE, 1.0F));
			mc.player.setHealth(0.00000000000000000000001F);
		}
	}
	
	@Override
	public boolean keyTyped(char typedChar, int keyCode) {
		Minecraft mc = Minecraft.getMinecraft();
		mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.ENTITY_PLAYER_DEATH, 1.0F));
		mc.player.setHealth(0.00000000000000000000001F);
		return false;
	}
	@Override
	public int getHeight(FontRenderer fr) {
		return super.getHeight(fr);
	}
}
