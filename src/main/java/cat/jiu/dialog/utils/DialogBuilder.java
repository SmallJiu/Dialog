package cat.jiu.dialog.utils;

import java.util.List;

import com.google.common.collect.Lists;

import cat.jiu.core.api.element.IText;
import cat.jiu.dialog.api.IDialogOption;
import cat.jiu.dialog.element.Dialog;
import cat.jiu.dialog.element.option.OptionButton;
import cat.jiu.dialog.element.option.OptionTextField;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

/**
 * 用于方便的创建一个对话框
 * @author small_jiu
 */
@ZenRegister
@ZenClass("dialog.api.Builder")
public class DialogBuilder {
	protected ResourceLocation id;
	protected final IText title;
	protected final List<IDialogOption> options = Lists.newArrayList();
	/**
	 * use for {@link cat.jiu.dialog.api.helper.DialogList#addDialogOperation(int, cat.jiu.dialog.api.helper.DialogList.DialogOperation)}
	 * @param text
	 */
	public DialogBuilder(IText title) {
		this.title = title;
	}
	public DialogBuilder(ResourceLocation id, IText title) {
		this.id = id;
		this.title = title;
	}
	/**
	 * 添加选项到对话框
	 * @param drawUnit drawUnit
	 */
	@ZenMethod("option")
	public DialogBuilder addOption(IDialogOption option) {
		this.options.add(option);
		return this;
	}
	/**
	 * 设置对话框的ID
	 * @param id id
	 */
	public DialogBuilder setDialogID(ResourceLocation id) {
		this.id = id;
		return this;
	}
	@ZenMethod("id")
	public DialogBuilder setDialogID(String id) {
		this.id = new ResourceLocation(id);
		return this;
	}
	/**
	 * 添加一个按钮选项
	 * @param text 显示的内容
	 * @return
	 */
	@ZenMethod("button")
	public DialogBuilder addButton(IText text) {
		return this.addOption(new OptionButton(text, false));
	}
	/**
	 * 添加一个按钮选项
	 * @param text 显示的内容
	 * @param closeDialog 是否可以关闭正在显示的对话框
	 * @return
	 */
	@ZenMethod("button")
	public DialogBuilder addButton(IText text, boolean closeDialog) {
		return this.addOption(new OptionButton(text, closeDialog));
	}
	/**
	  * 添加一个按钮选项
	 * @param text 显示的内容
	 * @param closeDialog 是否可以关闭正在显示的对话框
	 * @param tooltips 鼠标移动到按钮上时显示的提示
	 */
	@ZenMethod("button")
	public DialogBuilder addButton(IText text, boolean closeDialog, IText... tooltips) {
		return this.addOption(new OptionButton(text, closeDialog, tooltips));
	}
	/**
	  * 添加一个按钮选项
	 * @param text 显示的内容
	 * @param closeDialog 是否可以关闭正在显示的对话框
	 * @param tooltips 鼠标移动到按钮上时显示的提示
	 */
	@ZenMethod("button")
	public DialogBuilder addButton(IText text, boolean closeDialog, List<IText> tooltips) {
		return this.addOption(new OptionButton(text, closeDialog, tooltips));
	}
	/**
	 * 添加一个文本输入框选项
	 * @param text 显示的提示
	 * @param closeDialog 是否可以关闭正在显示的对话框
	 * @return
	 */
	@ZenMethod("text")
	public DialogBuilder addTextField(IText info, boolean closeDialog) {
		return this.addOption(new OptionTextField(info, closeDialog));
	}
	
	@ZenMethod("button_tooltip")
	public DialogBuilder addButtonTooltip(int index, IText... tooltip) {
		if(this.options.size() > index && index >= 0 && this.options.get(index) instanceof OptionButton) {
			for(IText text : tooltip) {
				((OptionButton)this.options.get(index)).addTooltips(text);
			}
		}
		return this;
	}
	
	@ZenMethod("text_tooltip")
	public DialogBuilder addTextFieldTooltip(int index, IText... tooltip) {
		if(this.options.size() > index && index >= 0 && this.options.get(index) instanceof OptionTextField) {
			for(IText text : tooltip) {
				((OptionTextField)this.options.get(index)).addTooltips(text);
			}
		}
		return this;
	}
	
	/**
	 * 创建对话框
	 */
	@ZenMethod
	public Dialog build() {
		return new Dialog(id, title, options);
	}
}
