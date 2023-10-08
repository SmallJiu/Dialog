package cat.jiu.dialog.element.option.draw.timer;

import java.awt.Color;

import cat.jiu.core.api.ITimer;
import cat.jiu.dialog.element.option.draw.OptionCheckboxDrawUnit;
import cat.jiu.dialog.element.option.draw.OptionItemCheckboxDrawUnit;
import cat.jiu.dialog.element.option.timer.OptionTimerItemCheckbox;
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
@ZenClass("dialog.draw.timer.ItemCheckbox")
public class OptionTimerItemCheckboxDrawUnit extends OptionItemCheckboxDrawUnit {
	protected final OptionTimerItemCheckbox option;
	public OptionTimerItemCheckboxDrawUnit(ResourceLocation dialogID, OptionTimerItemCheckbox option, int id, DialogDimension dim) {
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
		int height = super.getHeight(fr, 4);
		return height >= 27 ? height : 27;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	protected int getHeight(FontRenderer fr, int column) {
		return super.getHeight(fr, column);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void draw(GuiDialog gui, int mouseX, int mouseY, OptionDimension dim, Minecraft mc, FontRenderer fr) {
		{
			ITimer timer = this.option.getTimer();
			int progress = (int) (timer.getSurplusPart()*100);
			int x = dim.x + dim.width/2 - 20;
			int y = dim.y + dim.height/2 - 20;
			
			GuiUtils.drawHollowSquare(gui, x-1, y-1, 27, 27, 1347420415);
			GuiUtils.drawHollowSquare(gui, x, y, 25, 25, Color.BLACK.getRGB());
			GuiUtils.drawHollowSquare(gui, x+1, y+1, 23, 23, 1347420415);
			
			int a = Math.max(0, progress-75);
			int b = Math.max(0, progress<= 75 ? progress-50 : progress >= 50 ? 25 : 0);
			int c = Math.max(0, progress<= 50 ? progress-25 : progress >= 25 ? 25 : 0);
			int d = Math.max(0, progress<= 25 ? progress : progress >= 0 ? 25 : 0);
			
			if(progress>0) gui.drawHorizontalLine(x+25, x+(25-d), y, Color.RED.getRGB());// 0
			if(progress>=25) gui.drawVerticalLine(x, y, y+c, Color.YELLOW.getRGB());// 0.25
			if(progress>=50) gui.drawHorizontalLine(x, x+b, y+25, Color.GREEN.getRGB());// 0.50
			if(progress>=75) gui.drawVerticalLine(x+25, y+(25-a), y+25, Color.GREEN.getRGB()); // 0.75
			gui.drawCenteredString(fr, String.valueOf(timer.getTicks()/20), x+25/2+1, y+25/2-7, Color.BLACK.getRGB());
			gui.drawCenteredString(fr, I18n.format("dialog.text.seconds"), x+25/2+1, y+25/2+3, Color.BLACK.getRGB());
		}
		
		int x = dim.x,
			y = dim.y+2,
			col = 0;
		
		for(int i = 0; i < this.option.getOption().getStacks().size(); i++) {
			if(col >= 4) {
				x = dim.x;
				y += 16;
				col = 0;
			}
			
			fr.drawString(TextFormatting.RED.toString() + (i+1)+":", x+11, y, Color.BLACK.getRGB(), false);
			
			mc.getRenderItem().renderItemIntoGUI(this.option.getOption().getStacks().get(i), x + 10 + fr.getStringWidth((i+1)+":"), y - 4);
			fr.drawString("x" + this.option.getOption().getStacks().get(i).getCount(), x + 2 + fr.FONT_HEIGHT + fr.getStringWidth((i+1)+":") + 15, y+1, Color.BLACK.getRGB());
			
			if(!this.inRange.containsKey(i)) {
				this.inRange.put(i, new Range(x, y, this.dialogDimension.width/4-2, 13));
			}else {
				this.inRange.get(i).setRange(x, y, this.dialogDimension.width/4-2, 13);
			}
			
			GuiUtils.drawHollowSquare(gui, x, y, fr.FONT_HEIGHT, fr.FONT_HEIGHT, (GuiUtils.isInRange(mouseX, mouseY, x, y, this.dialogDimension.width/4-2, 16) || this.keySelect == i ? Color.RED : Color.BLACK).getRGB());
			if(this.selects.contains(i)) {
				GlStateManager.pushMatrix();
				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
				Minecraft.getMinecraft().getTextureManager().bindTexture(OptionCheckboxDrawUnit.BEACON_GUI_TEXTURES);
				Gui.drawScaledCustomSizeModalRect(x+2, y+2, 91, 224, 14, 12, fr.FONT_HEIGHT-3, fr.FONT_HEIGHT-3, 256, 256);
				GlStateManager.popMatrix();
			}
			
			col++;
			x += this.dialogDimension.width/4;
			
			if(col == 1 || col == 3) {
				x -= 8;
			}else if(col == 2) {
				x += 20;
			}
		}
		
		if(col>0) y += 13;
		
		this.confirm.x = dim.x - 2;
		this.confirm.y = y;
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
