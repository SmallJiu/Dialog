package cat.jiu.dialog.element.option.text;

import java.awt.Color;
import java.util.List;

import com.google.common.collect.Lists;

import cat.jiu.dialog.ModMain;
import cat.jiu.dialog.api.DialogDimension;
import cat.jiu.dialog.api.OptionDimension;
import cat.jiu.dialog.element.option.DialogOptionDrawUnit;
import cat.jiu.dialog.event.DialogOptionEvent;
import cat.jiu.dialog.net.MsgOptionEvent;
import cat.jiu.dialog.ui.GuiDialog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiOptionTextField extends DialogOptionDrawUnit {
	protected final DialogTextField option;
	protected final GuiTextField field;
	protected final GuiButton confirm;
	protected List<String> tooltip;
	public GuiOptionTextField(EntityPlayer player, ResourceLocation dialogID, DialogTextField option, int optionID, DialogDimension dialogDimension) {
		super(dialogID, option, optionID, dialogDimension);
		this.option = option;
		this.field = new GuiTextField(0, Minecraft.getMinecraft().fontRenderer, 0, 0, 0, 0);
		this.field.setMaxStringLength(Integer.MAX_VALUE);
		this.confirm = this.getConfirmButtom(player, dialogID, option, optionID, Minecraft.getMinecraft().fontRenderer);
		if(option.hasTooltips()) {
			this.tooltip = Lists.newArrayList();
			for(int i = 0; i < option.getTooltips().size(); i++) {
				this.tooltip.add(option.getTooltips().get(i).format());
			}
		}
	}
	
	/**
	 * 获取文本框的'确定'按钮.
	 */
	protected GuiButton getConfirmButtom(EntityPlayer player, ResourceLocation dialogID, DialogTextField option, int optionID, FontRenderer fr) {
		return new GuiButton(0, 0, 0, I18n.format("dialog.text.confirm")) {
			@Override
			public void mouseReleased(int mouseX, int mouseY) {
				if(option.canCloseDialog()) {
					player.closeScreen();
				}
				
				DialogOptionEvent.TextConfirm event = new DialogOptionEvent.TextConfirm(player, dialogID, optionID, field.getText());
				if(MinecraftForge.EVENT_BUS.post(event)) return;
				field.setText(event.getText());
				ModMain.network.sendMessageToServer(new MsgOptionEvent(dialogID, optionID, field.getText()));
				playClickSound();
			}
			
			@Override
			public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
				super.drawButton(mc, mouseX, mouseY, partialTicks);
				this.drawHorizontalLine(this.x, this.x + this.width - 1, this.y + this.height, Color.BLACK.getRGB());
			}
		};
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton, OptionDimension dim) {
		this.field.mouseClicked(mouseX, mouseY, mouseButton);
		if (mouseButton == 0 && this.confirm.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY)) {
			this.confirm.mouseReleased(mouseX, mouseY);
		}
	}
	
	protected boolean init;
	@Override
	public void draw(GuiDialog gui, int mouseX, int mouseY, OptionDimension dim, Minecraft mc, FontRenderer fr) {
		GlStateManager.pushMatrix();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		
		this.confirm.width = fr.getStringWidth(this.confirm.displayString) + 6;
		
		this.field.x = dim.x + 1;
		this.field.y = dim.y + 2;
		this.field.width = dim.width - 2 - this.confirm.width;
		this.field.height = this.text.size() * 11 + 1;
		dim.height = this.field.height;
		
		this.field.drawTextBox();
		
		this.confirm.y = this.field.y - 1;
		this.confirm.x = dim.x + this.field.width + 3;
		this.confirm.height = fr.FONT_HEIGHT + 4;
		this.confirm.drawButton(mc, mouseX, mouseY, 0);
		
		if(!this.init) {
			this.text.clear();
			String titleStr = this.option.getOptionText().format();
			if(this.option.getOptionText().isVanillaWrap()) {
				this.text.addAll(fr.listFormattedStringToWidth(titleStr, this.field.width - 10));
			}else {
				this.text.addAll(GuiDialog.splitString(titleStr, this.field.width - 10));
			}
			this.init = true;
		}
		
		if(this.field.getText().isEmpty()) {
			int y = dim.y;
			for(int i = 0; i < this.text.size(); i++) {
				if(this.option.getOptionText().isCenter()) {
					gui.drawCenteredString(fr, this.text.get(i), dim.x+5 + dim.width/2, y + 4, 0x7B7B7B);
				}else {
					fr.drawString(this.text.get(i), dim.x+5, y + 4, 0x7B7B7B);
				}
				y += fr.FONT_HEIGHT;
			}
		}
		GlStateManager.popMatrix();
	}

	@Override
	public boolean keyTyped(char typedChar, int keyCode) {
		return this.field.textboxKeyTyped(typedChar, keyCode);
	}

	@Override
	public void drawHoveringText(GuiDialog gui, Minecraft mc, FontRenderer fr, int mouseX, int mouseY, ScaledResolution sr, OptionDimension dim) {
		if(this.option.hasTooltips() && GuiDialog.isInRange(mouseX, mouseY, this.field.x, this.field.y, this.field.width, this.field.height)) {
			gui.drawHoveringText(this.tooltip, mouseX, mouseY);
		}
	}
	
	@Override
	public int getHeight(FontRenderer fr) {
		return this.field.height + 4;
	}
}
