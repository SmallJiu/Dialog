package cat.jiu.dialog.element.option.draw;

import java.awt.Color;
import java.util.List;

import com.google.common.collect.Lists;

import cat.jiu.dialog.ModMain;
import cat.jiu.dialog.element.OptionDrawUnit;
import cat.jiu.dialog.element.option.OptionButton;
import cat.jiu.dialog.event.OptionEvent;
import cat.jiu.dialog.net.msg.option.MsgButtonClick;
import cat.jiu.dialog.ui.GuiDialog;
import cat.jiu.dialog.ui.GuiHandler;
import cat.jiu.dialog.utils.dimension.DialogDimension;
import cat.jiu.dialog.utils.dimension.OptionDimension;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("dialog.draw.Button")
@SideOnly(Side.CLIENT)
public class GuiOptionButton extends OptionDrawUnit {
	protected final OptionButton btn;
	protected final EntityPlayer player;
	protected List<String> tooltip;
	public GuiOptionButton(EntityPlayer player, ResourceLocation dialogID, OptionButton option, int optionID, DialogDimension dialogDimension) {
		super(dialogID, option, optionID, dialogDimension);
		this.btn = option;
		this.player = player;
		if(option.hasTooltips()) {
			this.tooltip = Lists.newArrayList();
			for(int i = 0; i < option.getTooltips().size(); i++) {
				this.tooltip.add(option.getTooltips().get(i).format());
			}
		}
	}

	@Override
	public void mouseClicked(GuiDialog gui, int mouseX, int mouseY, int mouseButton, OptionDimension dimension) {
		if(GuiDialog.isInRange(mouseX, mouseY, dimension)) {
			if(this.dataUnit.canCloseDialog()) {
				this.player.closeScreen();
			}
			MinecraftForge.EVENT_BUS.post(new OptionEvent.ButtonClick(this.player, gui.getParentDialogID(), this.dialogID, this.optionID, mouseButton));
			ModMain.NETWORK.sendMessageToServer(new MsgButtonClick(gui.getParentDialogID(), this.dialogID, this.optionID, mouseButton));
			
			playClickSound();
		}
	}

	@Override
	public void draw(GuiDialog gui, int mouseX, int mouseY, OptionDimension dim, Minecraft mc, FontRenderer fr) {
		GlStateManager.pushMatrix();
        GlStateManager.color(1, 1, 1, 1);
        
		this.drawBackground(gui, mc, GuiDialog.isInRange(mouseX, mouseY, dim), dim);
		
		GlStateManager.popMatrix();
		
		int y = dim.y;
		for(int i = 0; i < this.text.size(); i++) {
			if(this.btn.getOptionText().isCenter()) {
				gui.drawCenteredString(fr, this.text.get(i), dim.x+ dim.width/2, y+3 + (this.text.size()>1 ? 1 : 0), Color.WHITE.getRGB());
			}else {
				fr.drawString(this.text.get(i), dim.x+8, y+3 + (this.text.size()>1 ? 1 : 0), Color.WHITE.getRGB());
			}
			y += fr.FONT_HEIGHT;
		}
	}

	/**
	 * 绘制背景
	 * @param gui gui对象
	 * @param mc 
	 * @param hover 是否悬停在当前组件上
	 * @param dim 组件的显示信息
	 */
	@ZenMethod
	protected void drawBackground(GuiDialog gui, Minecraft mc, boolean hover, OptionDimension dim) {
		mc.getTextureManager().bindTexture(GuiHandler.dialog_texture);
		int width = dim.width+4;
		if(hover) {
			// 中间大背景
//			for(int x = dim.x + 2; x < dim.x+dim.width - 5; x++) {
//				for(int y = dim.y + 2; y < dim.y+dim.height - 2; y++) {
//					gui.drawTexturedModalRect(x, y, 17, 4, 1, 1);
//				}
//			}
			Gui.drawScaledCustomSizeModalRect(dim.x + 2, dim.y + 2, 17, 4, 1, 1, (dim.x+width - 5) - (dim.x + 2), (dim.y+dim.height - 2) - (dim.y + 2), 256, 256);
			
			// 顶部边
//			for(int x = dim.x+2; x < dim.x+2 + dim.width-8; x++) {
//				gui.drawTexturedModalRect(x, dim.y, 15, 0, 1, 2);
//			}
			Gui.drawScaledCustomSizeModalRect(dim.x + 2, dim.y, 15, 0, 1, 2, (dim.x+2 + width-8) - (dim.x+2), 2, 256, 256);
			
			// 底部边
//			for(int x = dim.x+3; x < dim.x+2 + dim.width-7; x++) {
//				gui.drawTexturedModalRect(x, dim.y + dim.height - 2, 15, 7, 1, 2);
//			}
			Gui.drawScaledCustomSizeModalRect(dim.x + 3, dim.y + dim.height - 2, 15, 7, 1, 2, (dim.x+2 + width-7) - (dim.x+3), 2, 256, 256);
			
			// 左边边
//			for(int y = dim.y+3; y < dim.y+2 + dim.height - 6; y++) {
//				gui.drawTexturedModalRect(dim.x, y, 13, 2, 3, 1);
//			}
			Gui.drawScaledCustomSizeModalRect(dim.x, dim.y + 2, 13, 2, 3, 1, 3, (dim.y+2 + dim.height - 6) - (dim.y+2), 256, 256);
			
			// 右边边
//			for(int y = dim.y+3; y < dim.y+2 + dim.height - 5; y++) {
//				gui.drawTexturedModalRect(dim.x+dim.width-6, y, 19, 2, 3, 1);
//			}
			Gui.drawScaledCustomSizeModalRect(dim.x+width-6, dim.y+3, 19, 2, 3, 1, 3, (dim.y+2 + dim.height - 5) - (dim.y+3), 256, 256);
			
			// 左上角拐弯
			gui.drawTexturedModalRect(dim.x, dim.y, 13, 0, 5, 5);
			// 右上角拐弯
			gui.drawTexturedModalRect(dim.x + width-8, dim.y, 17, 0, 5, 5);
			// 左下角拐弯
			gui.drawTexturedModalRect(dim.x, dim.y+dim.height-5, 13, 4, 5, 5);
			// 右下角拐弯
			gui.drawTexturedModalRect(dim.x+width-8, dim.y+dim.height-5, 17, 4, 5, 5);
		}else {
			// 中间大背景
//			for(int x = dim.x + 2; x < dim.x+dim.width - 4; x++) {
//				for(int y = dim.y + 2; y < dim.y+dim.height - 2; y++) {
//					gui.drawTexturedModalRect(x, y, 8, 4, 1, 1);
//				}
//			}
			Gui.drawScaledCustomSizeModalRect(dim.x + 2, dim.y + 2, 8, 4, 1, 1, (dim.x+width - 5) - (dim.x + 2), (dim.y+dim.height - 2) - (dim.y + 2), 256, 256);
			
			// 顶部边
//			for(int x = dim.x+2; x < dim.x+2 + dim.width-8; x++) {
//				gui.drawTexturedModalRect(x, dim.y, 6, 0, 1, 2);
//			}
			Gui.drawScaledCustomSizeModalRect(dim.x + 2, dim.y, 6, 0, 1, 2, (dim.x+2 + width-8) - (dim.x+2), 2, 256, 256);
			
			// 底部边
//			for(int x = dim.x+3; x < dim.x+2 + dim.width-7; x++) {
//				gui.drawTexturedModalRect(x, dim.y + dim.height - 2, 7, 7, 1, 2);
//			}
			Gui.drawScaledCustomSizeModalRect(dim.x + 3, dim.y + dim.height - 2, 7, 7, 1, 2, (dim.x+2 + width-7) - (dim.x+3), 2, 256, 256);
			
			// 左边边
//			for(int y = dim.y+2; y < dim.y+2 + dim.height - 6; y++) {
//				gui.drawTexturedModalRect(dim.x, y, 4, 2, 2, 1);
//			}
			Gui.drawScaledCustomSizeModalRect(dim.x, dim.y + 2, 4, 2, 2, 1, 2, (dim.y+2 + dim.height - 6) - (dim.y+2), 256, 256);
			
			// 右边边
//			for(int y = dim.y+3; y < dim.y+2 + dim.height - 5; y++) {
//				gui.drawTexturedModalRect(dim.x+dim.width-5, y, 11, 3, 2, 1);
//			}
			Gui.drawScaledCustomSizeModalRect(dim.x+ width-5, dim.y + 3, 11, 3, 2, 1, 2, (dim.y+2 + dim.height - 5) - (dim.y+3), 256, 256);

			// 左上角拐弯
			gui.drawTexturedModalRect(dim.x, dim.y, 4, 0, 5, 5);
			// 右上角拐弯
			gui.drawTexturedModalRect(dim.x + width-8, dim.y, 8, 0, 5, 5);
			// 左下角拐弯
			gui.drawTexturedModalRect(dim.x, dim.y+dim.height-5, 4, 4, 5, 5);
			// 右下角拐弯
			gui.drawTexturedModalRect(dim.x+width-8, dim.y+dim.height-5, 8, 4, 5, 5);
		}
	}
	
	@Override
	public void drawHoveringText(GuiDialog gui, Minecraft mc, FontRenderer fr, int mouseX, int mouseY, ScaledResolution sr, OptionDimension dim) {
		if(this.btn.hasTooltips() && GuiDialog.isInRange(mouseX, mouseY, dim)) {
			gui.drawHoveringText(this.tooltip, mouseX, mouseY);
		}
	}
}
