package cat.jiu.dialog.element;

import java.util.List;

import com.google.common.collect.Lists;

import cat.jiu.dialog.api.IDialogOption;
import cat.jiu.dialog.ui.GuiDialog;
import cat.jiu.dialog.utils.dimension.DialogDimension;
import cat.jiu.dialog.utils.dimension.OptionDimension;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

/**
 * 对话框选项的gui绘制单元，主要用于在对话框配合数据单元绘制在对话框上
 * @author small_jiu
 */
@ZenRegister
@ZenClass("dialog.Draw")
public abstract class OptionDrawUnit {
	protected final IDialogOption dataUnit;
	
	/**
	 * 已根据对话框Gui宽度进行切割换行的选项提示
	 */
	protected final List<String> text;
	
	/** 对话框ID */
	protected final ResourceLocation dialogID;
	
	/** 组件ID */
	protected final int optionID;
	
	/**
	 * 对话框Gui的长宽<p>
	 * 注：这在新建绘制单元时，高度始终为0，但在{@link #init(GuiDialog, FontRenderer)}时则初始化完成
	 * */
	protected final DialogDimension dialogDimension;

	@SideOnly(Side.CLIENT)
	public OptionDrawUnit(ResourceLocation dialogID, IDialogOption option, int id, DialogDimension dialogDimension) {
		this.dialogID = dialogID;
		this.dataUnit = option;
		this.optionID = id;
		this.dialogDimension = dialogDimension;
		this.text = this.getOptionText(this.dataUnit, dialogDimension, Minecraft.getMinecraft().fontRenderer);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends IDialogOption> T getOptionAs() {
		return (T) dataUnit;
	}

	/**
	 * 获取选项已切割换行的提示列表
	 * @param option {@link #dataUnit}
	 * @param dialogDimension {@link #dialogDimension}
	 * @return 已切割换行的提示列表
	 */
	@ZenMethod
	@SideOnly(Side.CLIENT)
	protected List<String> getOptionText(IDialogOption option, DialogDimension dialogDimension, FontRenderer fr) {
		String titleStr = option.getOptionText().format();
		if(option.getOptionText().isVanillaWrap()) {
			return Lists.newArrayList(fr.listFormattedStringToWidth(titleStr, dialogDimension.width - 25));
		}else {
			return GuiDialog.splitString(titleStr, dialogDimension.width - 25);
		}
	}
	
	@ZenMethod
	@SideOnly(Side.CLIENT)
	public void init(GuiDialog gui, FontRenderer fr) {
		this.init(fr);
	}
	@Deprecated
	@SideOnly(Side.CLIENT)
	public void init(FontRenderer fr) {}

	/**
	 * 绘制组件
	 * @param gui gui对象
	 * @param mouseX 鼠标的X轴
	 * @param mouseY 鼠标的Y轴
	 * @param dimension 当前组件的显示信息
	 * @param mc mc
	 * @param fr FontRenderer
	 */
	@ZenMethod
	@SideOnly(Side.CLIENT)
	public abstract void draw(GuiDialog gui, int mouseX, int mouseY, OptionDimension dimension, Minecraft mc, FontRenderer fr);
	
	/**
	 * 绘制鼠标悬停在组件上时的提示
	 * @param gui GuiDialog
	 * @param mc Minecraft
	 * @param fr FontRenderer
	 * @param mouseX 鼠标的X轴
	 * @param mouseY 鼠标的Y轴
	 * @param sr ScaledResolution
	 * @param dimension 当前组件的显示信息
	 */
	@ZenMethod
	@SideOnly(Side.CLIENT)
	public void drawHoveringText(GuiDialog gui, Minecraft mc, FontRenderer fr, int mouseX, int mouseY, ScaledResolution sr, OptionDimension dimension) {
		
	}

	@Deprecated
	@SideOnly(Side.CLIENT)
	public void mouseClicked(int mouseX, int mouseY, int mouseButton, OptionDimension dimension) {}
	
	/**
	 * 当按下鼠标后调用
	 * @param mouseX 鼠标点击的X轴
	 * @param mouseY 鼠标点击的Y轴
	 * @param mouseButton 鼠标点击时使用的按键
	 * @param dimension 组件的显示信息
	 * @see net.minecraft.client.gui.inventory.GuiContainer#mouseClicked
	 */
	@ZenMethod
	@SideOnly(Side.CLIENT)
	public void mouseClicked(GuiDialog gui, int mouseX, int mouseY, int mouseButton, OptionDimension dimension) {
		this.mouseClicked(mouseX, mouseY, mouseButton, dimension);
	}
	
	@Deprecated
	@SideOnly(Side.CLIENT)
	public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		
	}
	
	/**
	 * 当按下鼠标并移动时调用
	 * @param mouseX 鼠标所在的X轴
	 * @param mouseY 鼠标所在的Y轴
	 * @param clickedMouseButton 点击时的按键
	 * @param timeSinceLastClick 点击时长
	 * @see net.minecraft.client.gui.inventory.GuiContainer#mouseClickMove
	 */
	@ZenMethod
	@SideOnly(Side.CLIENT)
	public void mouseClickMove(GuiDialog gui, int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		this.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
	}
	
	@Deprecated
	@SideOnly(Side.CLIENT)
	public void handleInput(FontRenderer fr) {}
	@ZenMethod
	@SideOnly(Side.CLIENT)
	public void handleInput(GuiDialog gui, FontRenderer fr) {
		this.handleInput(fr);
	}
	
	@Deprecated
	@SideOnly(Side.CLIENT)
	public void handleMouseInput(FontRenderer fr) {}
	/**
	 * @see net.minecraft.client.gui.GuiScreen#handleMouseInput
	 */
	@ZenMethod
	@SideOnly(Side.CLIENT)
	public void handleMouseInput(GuiDialog gui, FontRenderer fr) {
		this.handleMouseInput(fr);
	}
	
	@Deprecated
	@SideOnly(Side.CLIENT)
	public void handleKeyboardInput(FontRenderer fr) {}
	/**
	 * @see net.minecraft.client.gui.GuiScreen#handleKeyboardInput
	 */
	@ZenMethod
	@SideOnly(Side.CLIENT)
	public void handleKeyboardInput(GuiDialog gui, FontRenderer fr) {
		this.handleKeyboardInput(fr);
	}

	@Deprecated
	@SideOnly(Side.CLIENT)
	public boolean keyTyped(char typedChar, int keyCode) {
		return false;
	}
	/**
	 * 输入字符时调用
	 * @param typedChar 字符
	 * @param keyCode lwjgl的键盘按键ID, {@link org.lwjgl.input.Keyboard}
	 * @return 返回 true 可阻止后续组件响应输入字符
	 * @see net.minecraft.client.gui.inventory.GuiContainer#keyTyped
	 */
	@ZenMethod
	@SideOnly(Side.CLIENT)
	public boolean keyTyped(GuiDialog gui, char typedChar, int keyCode) {
		return this.keyTyped(typedChar, keyCode);
	}
	/**
	 * 获取选项的总体高度
	 */
	@ZenMethod
	@SideOnly(Side.CLIENT)
	public int getHeight(FontRenderer fr) {
		if(this.text.size() > 1) {
			return this.text.size() * fr.FONT_HEIGHT + 6;
		}
		return fr.FONT_HEIGHT + 6;
	}
	
	/**
	 * 播放被点击后的声音
	 */
	@ZenMethod
	@SideOnly(Side.CLIENT)
	public static void playClickSound() {
		Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
	}

	@SideOnly(Side.CLIENT)
	public void onClose(GuiDialog gui) {
		
	}
}
