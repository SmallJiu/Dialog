package cat.jiu.dialog.test;

import java.awt.Color;
import java.util.Random;

import cat.jiu.dialog.api.IDialogOption;
import cat.jiu.dialog.element.OptionDrawUnit;
import cat.jiu.dialog.ui.GuiDialog;
import cat.jiu.dialog.utils.dimension.DialogDimension;
import cat.jiu.dialog.utils.dimension.OptionDimension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;

public class TestDrawUnit extends OptionDrawUnit {
	protected static final Random rand = new Random(10104);
	protected ItemStack stack = new ItemStack(Item.REGISTRY.getRandomObject(rand));
	
	public TestDrawUnit(ResourceLocation dialogID, IDialogOption option, int optionID, DialogDimension dialogDimension) {
		super(dialogID, option, optionID, dialogDimension);
	}
	
	int topWidth = 0;
	int downWidth = 0;
	int leftHight = 0;
	int rightHight = 0;
	boolean reverse = false;
	
	int time = 0;
	
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
			if(leftHight>0) gui.drawVerticalLine(dim.x, dim.y-1, dim.y+leftHight-2, Color.RED.getRGB());
			if(rightHight>0) gui.drawVerticalLine(dim.x+dim.width-1, dim.y+dim.height-rightHight-1-2, dim.y+dim.height-2, Color.YELLOW.getRGB());
			if(topWidth>0) gui.drawHorizontalLine(dim.x+dim.width-2, dim.x+dim.width-topWidth, dim.y, Color.WHITE.getRGB());
			if(downWidth>0) gui.drawHorizontalLine(dim.x, dim.x+downWidth-1, dim.y+dim.height - 3, Color.GREEN.getRGB());
		}else {
			if(leftHight>0) gui.drawVerticalLine(dim.x, dim.y+dim.height-leftHight, dim.y+dim.height-2, Color.RED.getRGB());
			if(rightHight>0) gui.drawVerticalLine(dim.x+dim.width-1, dim.y-1, dim.y+rightHight, Color.YELLOW.getRGB());
			if(topWidth>0) gui.drawHorizontalLine(dim.x, dim.x+topWidth, dim.y, Color.WHITE.getRGB());
			if(downWidth>0) gui.drawHorizontalLine(dim.x+dim.width-2, dim.x+dim.width-downWidth, dim.y+dim.height - 3, Color.GREEN.getRGB());
		}
		int y = dim.y+3;
		for(int i = 0; i < this.text.size(); i++) {
			fr.drawString(this.text.get(i), dim.x+3, y, !GuiDialog.isInRange(mouseX, mouseY, dim) ? Color.PINK.getRGB() : Color.BLACK.getRGB());
			y+=9;
		}
		
		if(time >= 80) {
			Item i;
			while((i = Item.REGISTRY.getRandomObject(rand)) instanceof ItemBlock) {};
			stack = new ItemStack(i);
			time=0;
		}
		time++;
		gui.drawItemStack(stack, dim.x+dim.width+1, dim.y + 3, null);
		
		if(reverse) {
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
		}else {
			if(topWidth < dim.width-1) {
				topWidth++;
			}else if(rightHight < dim.height-2) {
				rightHight++;
			}else if(downWidth < dim.width-1) {
				downWidth++;
			}else if(leftHight < dim.height) {
				leftHight++;
			}else {
				reverse = true;
			}
		}
	}
	
	@Override
	public void mouseClicked(GuiDialog gui, int mouseX, int mouseY, int mouseButton, OptionDimension dimension) {
		if(GuiDialog.isInRange(mouseX, mouseY, dimension)) {
			Minecraft mc = Minecraft.getMinecraft();
			mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.BLOCK_ANVIL_PLACE, 1.0F));
			mc.player.setHealth(Float.MIN_VALUE);
			mc.player.sendMessage(new TextComponentString("ParentDialog: " + gui.getParentDialogID()));
		}
	}
	
	@Override
	public boolean keyTyped(char typedChar, int keyCode) {
		Minecraft mc = Minecraft.getMinecraft();
		mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.ENTITY_PLAYER_DEATH, 1.0F));
		mc.player.setHealth(Float.MIN_VALUE);
		return false;
	}
}
