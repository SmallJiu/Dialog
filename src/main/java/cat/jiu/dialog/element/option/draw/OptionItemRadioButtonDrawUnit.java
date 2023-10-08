package cat.jiu.dialog.element.option.draw;

import java.awt.Color;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.Maps;

import cat.jiu.dialog.ModMain;
import cat.jiu.dialog.element.OptionDrawUnit;
import cat.jiu.dialog.element.option.OptionItemRadioButton;
import cat.jiu.dialog.event.ItemChooseEvent;
import cat.jiu.dialog.net.msg.option.MsgItemRadioButtonChoose;
import cat.jiu.dialog.ui.GuiDialog;
import cat.jiu.dialog.utils.GuiUtils;
import cat.jiu.dialog.utils.dimension.DialogDimension;
import cat.jiu.dialog.utils.dimension.OptionDimension;
import cat.jiu.dialog.utils.dimension.Range;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag.TooltipFlags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("dialog.draw.ItemRadioButton")
public class OptionItemRadioButtonDrawUnit extends OptionDrawUnit {
	protected final OptionItemRadioButton option;
	protected int select = 0;
	protected GuiButton confirm;
	protected final Map<Integer, Range> inRange = Maps.newHashMap();
	
	@SideOnly(Side.CLIENT)
	public OptionItemRadioButtonDrawUnit(ResourceLocation dialogID, OptionItemRadioButton option, int id, DialogDimension dim) {
		super(dialogID, option, id, dim);
		this.option = option;
		this.select = option.getDefaultSelectSingle();
	}
	
	@Override
	public void init(GuiDialog gui, FontRenderer fr) {
		super.init(gui, fr);
		this.confirm = this.getConfirmButtom(gui, this.dialogDimension);
	}
	
	@ZenMethod
	@SideOnly(Side.CLIENT)
	protected GuiButton getConfirmButtom(GuiDialog gui, DialogDimension dim) {
		return new GuiButton(0, 0, 0, dim.width, Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 3, I18n.format("dialog.text.confirm")) {
			public void mouseReleased(int mouseX, int mouseY) {
				confirmButtonClick(gui, mouseX, mouseY);
			}
		};
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public int getHeight(FontRenderer fr) {
		return this.getHeight(fr, this.option.getColumn());
	}
	
	@ZenMethod
	@SideOnly(Side.CLIENT)
	protected int getHeight(FontRenderer fr, int column) {
		int col = this.option
				.getStacks()
				.size() / column;
		if(this.option.getStacks().size() % column > 0) {
			col++;
		}
		return col * 16 + this.confirm.height;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void draw(GuiDialog gui, int mouseX, int mouseY, OptionDimension dim, Minecraft mc, FontRenderer fr) {
		int x = dim.x,
			y = dim.y+2,
			col = 0;
		
		for(int i = 0; i < this.option.getStacks().size(); i++) {
			if(col >= this.option.getColumn()) {
				x = dim.x;
				y += 16;
				col = 0;
			}
			
			fr.drawString(TextFormatting.RED.toString() + (i+1)+":", x+11, y, Color.BLACK.getRGB(), false);
			
			mc.getRenderItem().renderItemIntoGUI(this.option.getStacks().get(i), x + 10 + fr.getStringWidth((i+1)+":"), y - 4);
			fr.drawString("x" + this.option.getStacks().get(i).getCount(), x + 2 + fr.FONT_HEIGHT + fr.getStringWidth((i+1)+":") + 15, y+1, Color.BLACK.getRGB());
			
			if(!this.inRange.containsKey(i)) {
				this.inRange.put(i, new Range(x, y, this.dialogDimension.width/this.option.getColumn()-2, 12));
			}else {
				this.inRange.get(i).setRange(x, y, this.dialogDimension.width/this.option.getColumn()-2, 12);
			}
			
			GuiUtils.drawHollowSquare(gui, x, y, fr.FONT_HEIGHT, fr.FONT_HEIGHT, (GuiUtils.isInRange(mouseX, mouseY, x, y, this.dialogDimension.width/this.option.getColumn()-2, 16) ? Color.RED : Color.BLACK).getRGB());
			if(this.select == i) {
				gui.drawGradientRect(x+2, y+2, x+8, y+8, Color.BLACK.getRGB(), Color.BLACK.getRGB());
			}
			
			col++;
			x += this.dialogDimension.width/this.option.getColumn();
		}
		
		if(col>0) y += 13;
		
		this.confirm.x = dim.x - 2;
		this.confirm.y = y;
		this.confirm.drawButton(mc, mouseX, mouseY, 0);
		gui.drawHorizontalLine(this.confirm.x, this.confirm.x+this.confirm.width-2, this.confirm.y+this.confirm.height, Color.BLACK.getRGB());
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void drawHoveringText(GuiDialog gui, Minecraft mc, FontRenderer fr, int mouseX, int mouseY, ScaledResolution sr, OptionDimension dimension) {
		for(Entry<Integer, Range> range : this.inRange.entrySet()) {
			if(range.getValue().isInRange(mouseX, mouseY)) {
				gui.drawHoveringText(this.option.getStacks().get(range.getKey()).getTooltip(mc.player, TooltipFlags.ADVANCED), mouseX, mouseY);
				break;
			}
		}
	}
	
	@Override
	public boolean keyTyped(GuiDialog gui, char typedChar, int keyCode) {
		if(keyCode == Keyboard.KEY_UP
		|| keyCode == Keyboard.KEY_DOWN
		|| keyCode == Keyboard.KEY_RIGHT
		|| keyCode == Keyboard.KEY_LEFT) {
			int old = this.select;
			int New = this.select;
			
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
			
			if(New < 0 || New > this.option.getStacks().size()-1) {
				New = this.select;
			}
			if(old != New) {
				this.select = New;
				ModMain.NETWORK.sendMessageToServer(new MsgItemRadioButtonChoose(gui.getParentDialogID(), this.dialogID, this.optionID, this.option.getStacks().get(this.select), this.select, false));
				MinecraftForge.EVENT_BUS.post(new ItemChooseEvent.Single(Minecraft.getMinecraft().player, gui.getParentDialogID(), this.dialogID, this.optionID, this.option.getStacks().get(this.select), this.select, false));
			}
			return true;
		}else if(keyCode == Keyboard.KEY_RETURN) {
			this.confirm.mouseReleased(0, 0);
			return true;
		}
		
		return false;
	}
	
	@Override
	public void mouseClicked(GuiDialog gui, int mouseX, int mouseY, int mouseButton, OptionDimension dimension) {
		if(mouseButton!=0) return;
		
		if(this.confirm.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY)) {
			this.confirm.mouseReleased(mouseX, mouseY);
		}else {
			for(Entry<Integer, Range> option : this.inRange.entrySet()) {
				if(option.getValue().isInRange(mouseX, mouseY)
				&& this.select != option.getKey()) {
					this.select = option.getKey();
					
					ModMain.NETWORK.sendMessageToServer(new MsgItemRadioButtonChoose(gui.getParentDialogID(), this.dialogID, this.optionID, this.option.getStacks().get(this.select), option.getKey(), false));
					MinecraftForge.EVENT_BUS.post(new ItemChooseEvent.Single(Minecraft.getMinecraft().player, gui.getParentDialogID(), this.dialogID, this.optionID, this.option.getStacks().get(this.select), option.getKey(), false));
					playClickSound();
					break;
				}
			}
		}
	}
	
	@ZenMethod
	protected void confirmButtonClick(GuiDialog gui, int mouseX, int mouseY) {
		ModMain.NETWORK.sendMessageToServer(new MsgItemRadioButtonChoose(gui.getParentDialogID(), this.dialogID, this.optionID, this.option.getStacks().get(this.select), this.select, true));
		MinecraftForge.EVENT_BUS.post(new ItemChooseEvent.Single(Minecraft.getMinecraft().player, gui.getParentDialogID(), this.dialogID, this.optionID, this.option.getStacks().get(this.select), this.select, true));
		if(this.option.canCloseDialog()) {
			Minecraft.getMinecraft().player.closeScreen();
		}
		playClickSound();
	}
}
