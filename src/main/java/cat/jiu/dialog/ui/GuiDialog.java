package cat.jiu.dialog.ui;

import java.awt.Color;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;

import cat.jiu.core.api.element.IText;
import cat.jiu.dialog.DialogAPI;
import cat.jiu.dialog.ModMain;
import cat.jiu.dialog.api.IDialogOption;
import cat.jiu.dialog.element.Dialog;
import cat.jiu.dialog.element.OptionDrawUnit;
import cat.jiu.dialog.event.DialogEvent;
import cat.jiu.dialog.event.DialogInputEvent;
import cat.jiu.dialog.net.msg.MsgDialogEvent;
import cat.jiu.dialog.utils.DialogConfig;
import cat.jiu.dialog.utils.dimension.DialogDimension;
import cat.jiu.dialog.utils.dimension.OptionDimension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@EventBusSubscriber(Side.CLIENT)
public class GuiDialog extends GuiContainer {
	protected boolean isClose = false;;
	public final EntityPlayer player;
	protected final List<Unit> options = Lists.newArrayList();
	public final ContainerDialog contaniner;
	public final DialogDimension dim = new DialogDimension(this.getTextLength()+5, 0);
	protected GuiDialog parentDialog;
	
	public GuiDialog(GuiScreen parent, EntityPlayer player) {
		super(new ContainerDialog(player));
		this.player = player;
		this.contaniner = (ContainerDialog) super.inventorySlots;
		this.xSize = this.getTextLength()+5;
		
		if(parent instanceof GuiScreen) {
			this.parentDialog = (GuiDialog) parent;
		}
		
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	public void setParentDialog(GuiDialog parent) {
		this.parentDialog = parent;
	}
	public GuiDialog getParentDialog() {
		return parentDialog;
	}
	public ResourceLocation getParentDialogID() {
		if(this.parentDialog!=null && this.parentDialog.getDialog()!=null) {
			return this.parentDialog.getDialog().getID();
		}
		return null;
	}
	
	public Dialog getDialog() {
		if(this.contaniner.getDialog() != null) {
			return this.contaniner.getDialog().clone();
		}
		return null;
	}
	
	// option function implement
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		boolean flag = false;
		for(int i = 0; i < this.options.size(); i++) {
			flag = this.options.get(i).drawUnit.keyTyped(this, typedChar, keyCode);
			if(flag) break;
		}
		if(!flag) super.keyTyped(typedChar, keyCode);
	}
	@SubscribeEvent
	public void receiveKeyTyped(DialogInputEvent.KeyboardInput event) {
		if(event.optionID < this.options.size() && event.optionID >= 0) {
			this.options.get(event.optionID).drawUnit.keyTyped(this, event.typedChar, event.keyCode);
		}
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if(this.contaniner.getDialog()!=null && this.init) {
			for(int i = 0; i < this.options.size(); i++) {
				this.options.get(i).drawUnit.mouseClicked(this, mouseX, mouseY, mouseButton, this.options.get(i).dimension);
			}
		}
	}
	@SubscribeEvent
	public void receiveMouseClicked(DialogInputEvent.MouseInput.Clicked event) {
		if(event.optionID < this.options.size() && event.optionID >= 0) {
			OptionDimension dim = this.options.get(event.optionID).dimension;
			try {
				this.mouseClicked(dim.x + event.x, dim.y + event.y, event.mouseButton);
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
		for(int i = 0; i < this.options.size(); i++) {
			this.options.get(i).drawUnit.mouseClickMove(this, mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
		}
	}
	@SubscribeEvent
	public void receiveMouseMove(DialogInputEvent.MouseInput.Move event) {
		if(event.optionID < this.options.size() && event.optionID >= 0) {
			OptionDimension dim = this.options.get(event.optionID).dimension;
			this.mouseClickMove(dim.x + 5, dim.y + 5, event.mouseButton, event.timeSinceLastClick);
		}
	}
	
	@Override
	public void handleInput() throws IOException {
		super.handleInput();
		for(int i = 0; i < this.options.size(); i++) {
			this.options.get(i).drawUnit.handleInput(this, this.fontRenderer);
		}
	}
	
	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		for(int i = 0; i < this.options.size(); i++) {
			this.options.get(i).drawUnit.handleMouseInput(this, this.fontRenderer);
		}
	}
	
	@Override
	public void handleKeyboardInput() throws IOException {
		super.handleKeyboardInput();
		for(int i = 0; i < this.options.size(); i++) {
			this.options.get(i).drawUnit.handleKeyboardInput(this, this.fontRenderer);
		}
	}
	
	// draw dialog gui
	
	private boolean init = false;
	private List<String> title;
	
	public void setTitle(IText tile) {
		if(tile.isVanillaWrap()) {
			this.title = this.fontRenderer.listFormattedStringToWidth(tile.format(), this.getTextLength()+6);
		}else {
			this.title = splitString(tile.format(), this.getTextLength()+6);
		}
	}
	
	protected int getTextLength() {
		return DialogConfig.Dialog_Gui_Width;
	}
	protected int getDrawX() {
		return -(this.getTextLength()/2);
	}
	protected int getDrawY() {
		return 21;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}
	
	protected void drawDialog(int mouseX, int mouseY) {
		GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        
		ScaledResolution sr = new ScaledResolution(this.mc);
        int x = sr.getScaledWidth() / 2 + this.getDrawX();
        
        int height = this.title.size() * 9 + 2 + 8;
        for(int i = 0; i < this.options.size(); i++) {
        	height += this.options.get(i).drawUnit.getHeight(this.fontRenderer);
        }
        int y = (int) sr.getScaledHeight() / 2 + 30;
        if(y+height > sr.getScaledHeight()) {
        	y = sr.getScaledHeight() - height;
        }
        
        this.drawBackground(x-3 - 5 - 2, y - 3, this.getTextLength()+5, height);
		
		for(int i = 0; i < this.title.size(); i++) {
			if(this.contaniner.getDialog().getTitle().isCenter()) {
				this.drawCenteredString(fontRenderer, this.title.get(i), x-5 + this.getTextLength()/2, y+2, Color.BLACK.getRGB());
			}else {
				this.fontRenderer.drawString(this.title.get(i), x-5, y+2, Color.BLACK.getRGB());
			}
			y += 9;
		}
		
		y+=3;
		for(int i = 0; i < this.options.size(); i++) {
			Unit unit = this.options.get(i);
			if(unit.dimension.defaultDimension) {
				unit.dimension.x = x;
				unit.dimension.y = y;
			}
			
			unit.drawUnit.draw(this, mouseX, mouseY, unit.dimension, this.mc, this.fontRenderer);
			if(isInRange(mouseX, mouseY, unit.dimension)) {
		        GlStateManager.pushMatrix();
				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
				
				this.mc.getTextureManager().bindTexture(GuiHandler.dialog_texture);
				this.drawTexturedModalRect(unit.dimension.x - 4 - 2, y + unit.drawUnit.getHeight(this.fontRenderer) / 2 - 2, 0, 0, 4, 4);
				
				GlStateManager.popMatrix();
			}
			
			y += unit.drawUnit.getHeight(this.fontRenderer);
		}
		
		for(int i = 0; i < this.options.size(); i++) {
			this.options.get(i).drawUnit.drawHoveringText(this, this.mc, this.fontRenderer, mouseX, mouseY, sr, this.options.get(i).dimension);
		}
		
		GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableRescaleNormal();
	}
	
	protected void drawBackground(int startX, int startY, int width, int height) {
		GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        
		this.dim.width = width;
		this.dim.height = height;
		
    	int endX = startX + width + 12 - 3;
    	int endY = startY + height - 4;
    	int u = DialogConfig.Enable_Transparent_Background ? 22 : 32;
    	
        this.mc.getTextureManager().bindTexture(GuiHandler.dialog_texture);
        // 中间大背景
        Gui.drawScaledCustomSizeModalRect(startX + 4, startY + 4, u+3, 4, 2, 2, (endX + 1) - (startX + 4), (endY + 1) - (startY + 4), 256, 256);
        
        // 中间大背景顶部与边边中间的一小条
        Gui.drawScaledCustomSizeModalRect(startX + 4, startY + 3, u+3, 4, 2, 2, endX - (startX + 3), 1, 256, 256);
        
        // 中间大背景左边与边边中间的一小条
        Gui.drawScaledCustomSizeModalRect(startX + 3, startY + 4, u+3, 4, 2, 2, 1, (endY + 1) - (startY + 4), 256, 256);
        
        // 中间大背景底部与边边中间的一小条
        Gui.drawScaledCustomSizeModalRect(startX + 4, endY + 1, u+3, 4, 2, 2, (endX + 1) - (startX + 4), 1, 256, 256);
        
        // 中间大背景右边与边边中间的一小条
        Gui.drawScaledCustomSizeModalRect(endX+1, startY + 4, u+3, 4, 2, 2, 1, (endY + 1) - (startY + 4), 256, 256);
        
        // 中间大背景顶部的边
        Gui.drawScaledCustomSizeModalRect(startX + 4, startY, u+2, 0, 1, 3, (endX + 1) - (startX + 4), 3, 256, 256);
        
        // 中间大背景底部的边
        Gui.drawScaledCustomSizeModalRect(startX + 4, endY + 2, u+3, 7, 1, 3, (endX + 1) - (startX + 4), 3, 256, 256);
        
        // 中间大背景左边的边
        Gui.drawScaledCustomSizeModalRect(startX, startY + 4, u, 2, 3, 1, 3, (endY + 1) - (startY + 4), 256, 256);
        
        // 中间大背景右边的边
        Gui.drawScaledCustomSizeModalRect(endX+2, startY + 4, u+7, 3, 3, 1, 3, (endY + 1) - (startY + 4), 256, 256);
        
        // 中间大背景左上的拐弯
        this.drawTexturedModalRect(startX, startY, u, 0, 4, 4);
        // 中间大背景右上的拐弯
        this.drawTexturedModalRect(endX+1, startY, u+6, 0, 4, 4);
        
        // 中间大背景左下的拐弯
        this.drawTexturedModalRect(startX, endY+1, u, 6, 4, 4);
        // 中间大背景右下的拐弯
        this.drawTexturedModalRect(endX+1, endY+1, u+6, 6, 4, 4);
        
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	}
	
	private void findTopParentDialog() {
		if(this.parentDialog != null
		&& this.parentDialog.getDialog().getID().equals(this.getDialog().getID())) {
			this.parentDialog = this.parentDialog.parentDialog;
			this.findTopParentDialog();
		}
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		if(this.init) {
			this.drawDialog(mouseX, mouseY);
		}else {
			this.loadingBar();
		}
		
		if(this.contaniner.getDialog()!=null && !this.init) {
			this.findTopParentDialog();
			
			this.setTitle(this.contaniner.getDialog().getTitle());
			
			ScaledResolution sr = new ScaledResolution(this.mc);
			int centerX = sr.getScaledWidth() / 2 -1;
	        int centerY = sr.getScaledHeight() / 2 - 4;
	        
	        int x = centerX + this.getDrawX();
	        int y = centerY + this.getDrawY() + (this.title.size()*11);
	        ResourceLocation dialogID = this.contaniner.getDialog().getID();
			
			for(int optionID = 0; optionID < this.contaniner.getDialog().getOptions().size(); optionID++) {
				IDialogOption data = this.contaniner.getDialog().getOptions().get(optionID);
				OptionDrawUnit draw = DialogAPI.getDrawUnit(data.getTypeID(), dialogID, optionID, data, this.dim);
				if(draw == null) continue;
				
				draw.init(this, this.fontRenderer);
				this.options.add(new Unit(draw, new OptionDimension(x, y, this.getTextLength()-5, draw.getHeight(this.fontRenderer))));
	        	
				y += draw.getHeight(this.fontRenderer);
			}
			
			int height = this.title.size() * 9 + 2 + 7 ;
	        for(int i = 0; i < this.options.size(); i++) {
	        	height += this.options.get(i).drawUnit.getHeight(this.fontRenderer);
	        }
	        int dialogY = (int) sr.getScaledHeight() / 2 + 30;
	        if(dialogY+height > sr.getScaledHeight()) {
	        	dialogY = sr.getScaledHeight() - height;
	        }
	        this.dim.height = height;
	        this.ySize = height;
	        
	        MinecraftForge.EVENT_BUS.post(new DialogEvent.Open(this.player, this.contaniner.getDialog().getID()));
			ModMain.NETWORK.sendMessageToServer(new MsgDialogEvent(this.contaniner.getDialog().getID(), true));
	        this.init = true;
		}
	}
	
	protected void loadingBar() {
		this.drawLoadingBar();
		if(!this.startLoading) {
			this.startLoading = true;
			new Thread(()->{
				boolean reverseLoadingIndex = false;
				int i = 0;
				while(!this.init) {
					try {
						Thread.sleep(100);
						i++;
						if(this.loadingIndex >= 4) {
							reverseLoadingIndex = true;
						}else if(this.loadingIndex <= 0) {
							reverseLoadingIndex = false;
						}
						if(reverseLoadingIndex) {
							this.loadingIndex--;
						}else {
							this.loadingIndex++;
						}
						if(i>9) {
							loadingTime++;
							i=0;
						}
					}catch(Exception e) {}
				}
			}).start();
		}
	}
	
	protected int loadingIndex = 0;
	protected int loadingTime=0;
	private boolean startLoading;
	protected void drawLoadingBar() {
		int x = this.guiLeft + this.xSize / 2 - 5;
		int y = this.guiTop + this.ySize / 2 + 5;
		
		StringBuilder loadTime = new StringBuilder();
		long time = this.loadingTime;
		if(this.loadingTime>=60) {
			loadTime.append(time/60)
					.append(I18n.format("dialog.text.minutes"))
					.append(',')
					.append(' ');
			time %= 60;
		}
		
		loadTime.append(time)
				.append(I18n.format("dialog.text.seconds"));
		
		int width = Math.max(this.fontRenderer.getStringWidth(I18n.format("dialog.text.loading")), this.fontRenderer.getStringWidth(loadTime.toString()) - this.fontRenderer.getStringWidth("   ")) + 4;
		int height = this.fontRenderer.FONT_HEIGHT + 1;
		x = x - width/2;
		y = y - height/2;
		
		this.drawHoveringText(Arrays.asList(I18n.format("dialog.text.loading") + "   ", loadTime.toString()), x - 10, y+3);
		
		GlStateManager.pushMatrix();
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		this.mc.getTextureManager().bindTexture(GuiHandler.dialog_texture);
		this.drawTexturedModalRect(x + width, y - 8, 0, 9 + (8 * this.loadingIndex), 10, 7);
		GlStateManager.popMatrix();
	}
	
	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		MinecraftForge.EVENT_BUS.unregister(this);
		this.isClose = true;
		if(this.contaniner.getDialog()!=null) {
			MinecraftForge.EVENT_BUS.post(new DialogEvent.Close(this.player, this.contaniner.getDialog().getID()));
			ModMain.NETWORK.sendMessageToServer(new MsgDialogEvent(this.contaniner.getDialog().getID(), false));
		}
		this.options.forEach(unit -> unit.drawUnit.onClose(this));
	}
	
	// public method
	
	public boolean isClose() {
		return isClose;
	}
	
	@Override
	public void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
		super.drawGradientRect(left, top, right, bottom, startColor, endColor);
	}
	@Override
	public void drawHorizontalLine(int startX, int endX, int y, int color) {
		super.drawHorizontalLine(startX, endX, y, color);
	}
	@Override
	public void drawVerticalLine(int x, int startY, int endY, int color) {
		super.drawVerticalLine(x, startY, endY, color);
	}
	
	@Override
	public void drawCenteredString(FontRenderer fr, String text, int x, int y, int color) {
		fr.drawString(text, (float)(x - fr.getStringWidth(text) / 2), (float)y, color, false);
	}
	public void drawCenteredStringWithShadow(FontRenderer fr, String text, float x, float y, int color) {
		fr.drawString(text, (float)(x - fr.getStringWidth(text) / 2), (float)y, color, true);
	}
	
	public Slot addSlot(Slot slot) {
		return this.contaniner.addSlotToContainer(slot);
	}
	
	public Slot getSlot(int index) {
		return this.contaniner.getSlot(index);
	}
	
	@Override
	public void drawItemStack(ItemStack stack, int x, int y, String altText) {
		super.drawItemStack(stack, x, y, altText);
	}
	
	@Override
	public boolean isMouseOverSlot(Slot slot, int mouseX, int mouseY) {
		return super.isMouseOverSlot(slot, mouseX, mouseY);
	}
	
	@Override
	public Slot getSlotAtPosition(int x, int y) {
		return super.getSlotAtPosition(x, y);
	}
	
	@Override
	public void drawSlot(Slot slot) {
		if(slot.isEnabled() && GuiHandler.dialog_texture.equals(slot.getBackgroundLocation())) {
			GlStateManager.disableLighting();
			this.mc.getTextureManager().bindTexture(GuiHandler.dialog_texture);
			this.drawTexturedModalRect(slot.xPos-1, slot.yPos-1, 0,48, 18, 18);
	        GlStateManager.enableLighting();
		}
		super.drawSlot(slot);
	}
	
	// static
	
	public static boolean isInRange(int mouseX, int mouseY, OptionDimension dim) {
		return isInRange(mouseX, mouseY, dim.x, dim.y, dim.width, dim.height);
	}
	public static boolean isInRange(int mouseX, int mouseY, int x, int y, int width, int height) {
		int maxX = x + width;
		int maxY = y + height;
		return (mouseX >= x && mouseY >= y) && (mouseX <= maxX && mouseY <= maxY);
	}
	
	public static List<String> splitString(String text, int textMaxLength) {
		FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
		List<String> texts = Lists.newArrayList();
		if(fr.getStringWidth(text) >= textMaxLength) {
			StringBuilder s = new StringBuilder();
			for(int i = 0; i < text.length(); i++) {
				String str = s.toString();
				if(fr.getStringWidth(str) >= textMaxLength) {
					texts.add(str);
					s.setLength(0);
				}
				s.append(text.charAt(i));
			}
			if(s.length() > 0) {
				texts.add(s.toString());
			}
		}else {
			texts.add(text);
		}
		return texts;
	}
	
	class Unit {
		public final OptionDrawUnit drawUnit;
		public final OptionDimension dimension;
		public Unit(OptionDrawUnit option, OptionDimension dimension) {
			this.drawUnit = option;
			this.dimension = dimension;
		}
	}
}
