package cat.jiu.dialog.element.option.draw.timer;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import cat.jiu.core.api.ITimer;
import cat.jiu.dialog.element.option.draw.OptionCheckboxDrawUnit;
import cat.jiu.dialog.element.option.timer.OptionTimerCheckbox;
import cat.jiu.dialog.ui.GuiDialog;
import cat.jiu.dialog.utils.GuiUtils;
import cat.jiu.dialog.utils.dimension.DialogDimension;
import cat.jiu.dialog.utils.dimension.OptionDimension;
import cat.jiu.dialog.utils.dimension.Range;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stanhebben.zenscript.annotations.ZenClass;

@ZenRegister
@ZenClass("dialog.draw.timer.Checkbox")
public class OptionTimerCheckboxDrawUnit extends OptionCheckboxDrawUnit {
	protected final OptionTimerCheckbox option;
	@SideOnly(Side.CLIENT)
	public OptionTimerCheckboxDrawUnit(ResourceLocation dialogID, OptionTimerCheckbox option, int id, DialogDimension dim) {
		super(dialogID, option.getOption(), id, dim);
		this.option = option;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void init(GuiDialog gui, FontRenderer fr) {
		super.init(gui, fr);
		TimerOption.startTimer(this.option.getTimer(), gui, this.option.isTimeoutAutoConfirm(), this.confirm);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public int getHeight(FontRenderer fr) {
		int height = this.getHeight(fr, 2);
		return (height >= 27 ? height : 27) - 3;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	protected int getHeight(FontRenderer fr, int column) {
		int height = 6,
			col = 0,
			textHeight = 0;
		
		for(int i = 0; i < this.option.getOption().getOptions().size(); i++) {
			List<String> format = GuiUtils.formatText(Arrays.asList(this.option.getOption().getOptions().get(i)), this.dialogDimension.width/column - 20 - 28 + 15 - (col < 1 ? 10 : 0), true);
			textHeight = Math.max(format.size()*fr.FONT_HEIGHT + 3, textHeight);
			col++;
			if(col >= column) {
				height += textHeight;
				col = 0;
				textHeight = 0;
			}
		}
		
		if(col>0) height += textHeight;
		
		return height + this.confirm.height;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void draw(GuiDialog gui, int mouseX, int mouseY, OptionDimension dim, Minecraft mc, FontRenderer fr) {
		{
			ITimer timer = this.option.getTimer();
			float progress = timer.getSurplusPart();
			int progressInt = (int) (progress*100);
			int x = dim.x + dim.width/2 - 20;
			int y = dim.y + dim.height/2 - 20;
			
			GuiUtils.drawHollowSquare(gui, x-1, y-1, 27, 27, 1347420415);
			GuiUtils.drawHollowSquare(gui, x, y, 25, 25, Color.BLACK.getRGB());
			GuiUtils.drawHollowSquare(gui, x+1, y+1, 23, 23, 1347420415);
			
			int a = Math.max(0, progressInt-75);
			int b = Math.max(0, progressInt<= 75 ? progressInt-50 : progressInt >= 50 ? 25 : 0);
			int c = Math.max(0, progressInt<= 50 ? progressInt-25 : progressInt >= 25 ? 25 : 0);
			int d = Math.max(0, progressInt<= 25 ? progressInt : progressInt >= 0 ? 25 : 0);
			
			if(progressInt>0) gui.drawHorizontalLine(x+25, x+(25-d), y, Color.RED.getRGB());// 0
			if(progressInt>=25) gui.drawVerticalLine(x, y, y+c, Color.YELLOW.getRGB());// 0.25
			if(progressInt>=50) gui.drawHorizontalLine(x, x+b, y+25, Color.GREEN.getRGB());// 0.50
			if(progressInt>=75) gui.drawVerticalLine(x+25, y+(25-a), y+25, Color.GREEN.getRGB()); // 0.75
			gui.drawCenteredString(fr, String.valueOf(timer.getTicks()/20), x+25/2+1, y+25/2-7, Color.BLACK.getRGB());
			gui.drawCenteredString(fr, I18n.format("dialog.text.seconds"), x+25/2+1, y+25/2+3, Color.BLACK.getRGB());
		}
		
		int x = dim.x,
			y = dim.y+2,
			col = 0,
			textHeight = 0;
		
		for(int i = 0; i < this.option.getOption().getOptions().size(); i++) {
			if(col >= 2) {
				x = dim.x;
				y += textHeight + 3;
				col = 0;
				textHeight = 0;
			}
			
			List<String> format = GuiUtils.formatText(Arrays.asList(this.option.getOption().getOptions().get(i)), this.dialogDimension.width/2 - 20 - 28 + 15 - (col < 1 ? 10 : 0), true);
			
			int width = 0;
			for(String s : format) {
				width = Math.max(fr.getStringWidth(s), width);
			}
			width += 16 + fr.getStringWidth((i+1)+":");
			
//			x += width/2;
			
			if(col > 0) x += 10;
			
			int texty = y;
			fr.drawString(TextFormatting.RED.toString() + (i+1)+":", x+11, texty+1, Color.BLACK.getRGB(), false);
			for(int j = 0; j < format.size(); j++) {
				fr.drawString(format.get(j), x+12+fr.getStringWidth((i+1)+":"), texty+1, Color.BLACK.getRGB(), false);	
				texty+=fr.FONT_HEIGHT;
			}
			
			textHeight = Math.max(format.size()*fr.FONT_HEIGHT, textHeight);
			if(!this.inRange.containsKey(i)) {
				this.inRange.put(i, new Range(x, y, width, textHeight));
			}else {
				this.inRange.get(i).setRange(x, y, width, textHeight);
			}
			
			GuiUtils.drawHollowSquare(gui, x, y, fr.FONT_HEIGHT, fr.FONT_HEIGHT, (this.inRange.get(i).isInRange(mouseX, mouseY) ? Color.RED : Color.BLACK).getRGB());
			if(this.selects.contains(i)) {
				GlStateManager.pushMatrix();
				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
				Minecraft.getMinecraft().getTextureManager().bindTexture(BEACON_GUI_TEXTURES);
				Gui.drawScaledCustomSizeModalRect(x+2, y+2, 91, 224, 14, 12, fr.FONT_HEIGHT-3, fr.FONT_HEIGHT-3, 256, 256);
				GlStateManager.popMatrix();
			}
			
			col++;
			x += this.dialogDimension.width/2;
		}
		
		if(col>0) y += textHeight;
		
		this.confirm.x = dim.x - 2;
		this.confirm.y = y+3;
		this.confirm.drawButton(mc, mouseX, mouseY, 0);
		gui.drawHorizontalLine(this.confirm.x, this.confirm.x+this.confirm.width-2, this.confirm.y+this.confirm.height, Color.BLACK.getRGB());
	}
	
	protected boolean clicked = false;
	@Override
	protected void confirmButtonClick(GuiDialog gui, int mouseX, int mouseY) {
		if(!this.clicked) {
			this.clicked = true;
			super.confirmButtonClick(gui, mouseX, mouseY);
		}
	}
}
