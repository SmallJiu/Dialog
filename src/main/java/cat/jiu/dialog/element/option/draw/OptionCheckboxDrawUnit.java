package cat.jiu.dialog.element.option.draw;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cat.jiu.dialog.ModMain;
import cat.jiu.dialog.element.OptionDrawUnit;
import cat.jiu.dialog.element.option.OptionCheckbox;
import cat.jiu.dialog.event.CheckboxEvent;
import cat.jiu.dialog.net.msg.option.*;
import cat.jiu.dialog.ui.GuiDialog;
import cat.jiu.dialog.utils.GuiUtils;
import cat.jiu.dialog.utils.dimension.DialogDimension;
import cat.jiu.dialog.utils.dimension.OptionDimension;
import cat.jiu.dialog.utils.dimension.Range;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("dialog.draw.Checkbox")
public class OptionCheckboxDrawUnit extends OptionDrawUnit {
	public static final ResourceLocation BEACON_GUI_TEXTURES = new ResourceLocation("textures/gui/container/beacon.png");
	protected final List<Integer> selects = Lists.newArrayList();
	protected final Map<Integer, Range> inRange = Maps.newHashMap();
	protected final OptionCheckbox option;
	protected GuiButton confirm;
	
	@SideOnly(Side.CLIENT)
	public OptionCheckboxDrawUnit(ResourceLocation dialogID, OptionCheckbox option, int id, DialogDimension dim) {
		super(dialogID, option, id, dim);
		this.option = (OptionCheckbox) super.dataUnit;
		for(int select : option.getDefaultSelect()) {
			this.selects.add(select);
		};
	}
	
	@ZenMethod
	protected GuiButton getConfirmButtom(GuiDialog gui, DialogDimension dim) {
		return new GuiButton(0, 0, 0, dim.width, Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 3, I18n.format("dialog.text.confirm")) {
			public void mouseReleased(int mouseX, int mouseY) {
				confirmButtonClick(gui, mouseX, mouseY);
			}
		};
	}
	
	@Override
	public void init(GuiDialog gui, FontRenderer fr) {
		super.init(gui, fr);
		this.confirm = this.getConfirmButtom(gui, this.dialogDimension);
	}
	
	@Override
	public int getHeight(FontRenderer fr) {
		return this.getHeight(fr, this.option.getColumn());
	}
	
	@ZenMethod
	protected int getHeight(FontRenderer fr, int column) {
		int height = fr.FONT_HEIGHT + 6,
			col = 0,
			textHeight = 0;
		
		for(int i = 0; i < this.option.getOptions().size(); i++) {
			List<String> format = GuiUtils.formatText(Arrays.asList(this.option.getOptions().get(i)), this.dialogDimension.width/column - 20, true);
			textHeight = Math.max(format.size()*fr.FONT_HEIGHT + 3, textHeight);
			col++;
			if(col >= column) {
				height += textHeight;
				col = 0;
				textHeight = 0;
			}
		}
		
		if(col>0) height += textHeight;
		
		return height;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void draw(GuiDialog gui, int mouseX, int mouseY, OptionDimension dim, Minecraft mc, FontRenderer fr) {
		int x = dim.x,
			y = dim.y+2,
			col = 0,
			textHeight = 0;
		
		for(int i = 0; i < this.option.getOptions().size(); i++) {
			if(col >= this.option.getColumn()) {
				x = dim.x;
				y += textHeight + 3;
				col = 0;
				textHeight = 0;
			}
			
			List<String> format = GuiUtils.formatText(Arrays.asList(this.option.getOptions().get(i)), this.dialogDimension.width/this.option.getColumn() - 20, true);
			
			int texty = y;
			fr.drawString(TextFormatting.RED.toString() + (i+1)+":", x+11, texty, Color.BLACK.getRGB(), false);
			for(int j = 0; j < format.size(); j++) {
				fr.drawString(format.get(j), x+12+fr.getStringWidth((i+1)+":"), texty, Color.BLACK.getRGB(), false);	
				texty+=fr.FONT_HEIGHT;
			}
			textHeight = Math.max(format.size()*fr.FONT_HEIGHT, textHeight);
			if(!this.inRange.containsKey(i)) {
				this.inRange.put(i, new Range(x, y, this.dialogDimension.width/this.option.getColumn()-2, textHeight));
			}else {
				this.inRange.get(i).setRange(x, y, this.dialogDimension.width/this.option.getColumn()-2, textHeight);
			}
			
			GuiUtils.drawHollowSquare(gui, x, y, fr.FONT_HEIGHT, fr.FONT_HEIGHT, (GuiUtils.isInRange(mouseX, mouseY, x, y, this.dialogDimension.width/this.option.getColumn()-2, textHeight) || this.keySelect == i ? Color.RED : Color.BLACK).getRGB());
			if(this.selects.contains(i)) {
				GlStateManager.pushMatrix();
				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
				Minecraft.getMinecraft().getTextureManager().bindTexture(BEACON_GUI_TEXTURES);
				Gui.drawScaledCustomSizeModalRect(x+2, y+2, 91, 224, 14, 12, fr.FONT_HEIGHT-3, fr.FONT_HEIGHT-3, 256, 256);
				GlStateManager.popMatrix();
			}
			col++;
			x += this.dialogDimension.width/this.option.getColumn();
		}
		
		if(col>0) y += textHeight;
		
		this.confirm.x = dim.x - 2;
		this.confirm.y = y+3;
		this.confirm.drawButton(mc, mouseX, mouseY, 0);
		gui.drawHorizontalLine(this.confirm.x, this.confirm.x+this.confirm.width-2, this.confirm.y+this.confirm.height, Color.BLACK.getRGB());
	}
	
	@Override
	public void mouseClicked(GuiDialog gui, int mouseX, int mouseY, int mouseButton, OptionDimension dimension) {
		super.mouseClicked(gui, mouseX, mouseY, mouseButton, dimension);
		if(mouseButton!=0) return;
		this.keySelect = -1;
		if(this.confirm.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY)) {
			this.confirm.mouseReleased(mouseX, mouseY);
		}else {
			for(Entry<Integer, Range> option : this.inRange.entrySet()) {
				if(option.getValue().isInRange(mouseX, mouseY)) {
					this.addOrRemove(gui, option.getKey());
					playClickSound();
					break;
				}
			}
		}
	}
	
	protected int keySelect = -1;
	@Override
	public boolean keyTyped(GuiDialog gui, char typedChar, int keyCode) {
		if(keyCode == Keyboard.KEY_UP
		|| keyCode == Keyboard.KEY_DOWN
		|| keyCode == Keyboard.KEY_RIGHT
		|| keyCode == Keyboard.KEY_LEFT) {
			int old = this.keySelect;
			int New = this.keySelect;
			switch(keyCode) {
				case Keyboard.KEY_UP:
					New -= this.option.getColumn();
					break;
				case Keyboard.KEY_DOWN:
					New += this.option.getColumn();
					break;
				case Keyboard.KEY_RIGHT:
					New += 1;
					break;
				case Keyboard.KEY_LEFT:
					New -= 1;
					break;
			}
			if(New < 0 || New > this.option.getOptions().size()-1) {
				New = this.keySelect;
			}
			if(old != New) {
				this.keySelect = New;
			}
			return true;
		}else if(keyCode == Keyboard.KEY_RETURN) {
			if(GuiScreen.isCtrlKeyDown()) {
				if(this.keySelect>-1) this.addOrRemove(gui, this.keySelect);
			}else {
				this.confirm.mouseReleased(0, 0);
			}
			return true;
		}
		return super.keyTyped(gui, typedChar, keyCode);
	}
	
	@ZenMethod
	protected void addOrRemove(GuiDialog gui, int select) {
		if(select < 0 || select > this.option.getOptions().size()-1) return;
		
		if(this.selects.contains(select)) {
			this.selects.remove((Integer)select);
		}else {
			this.selects.add(select);
		}
		ModMain.NETWORK.sendMessageToServer(new MsgCheckboxCheck(gui.getParentDialogID(), this.dialogID, this.optionID, this.option.getOptions().get(select).getText(), select, !this.selects.contains(select)));
		MinecraftForge.EVENT_BUS.post(new CheckboxEvent.Check(Minecraft.getMinecraft().player, gui.getParentDialogID(), this.dialogID, this.optionID, this.option.getOptions().get(select).getText(), select, !this.selects.contains(select)));
	}
	
	@ZenMethod
	protected void confirmButtonClick(GuiDialog gui, int mouseX, int mouseY) {
		Map<Integer, String> selects = Maps.newHashMap();
		this.selects.forEach(index-> selects.put(index, this.option.getOptions().get(index).getText()));
		
		ModMain.NETWORK.sendMessageToServer(new MsgCheckboxConfirm(gui.getParentDialogID(), this.dialogID, this.optionID, selects));
		MinecraftForge.EVENT_BUS.post(new CheckboxEvent.Confirm(Minecraft.getMinecraft().player, gui.getParentDialogID(), this.dialogID, this.optionID, selects));
		if(this.option.canCloseDialog()) {
			Minecraft.getMinecraft().player.closeScreen();
		}
		playClickSound();
	}
}
