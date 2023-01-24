package cat.jiu.dialog.api;

import java.util.List;

import com.google.common.collect.Lists;

import cat.jiu.dialog.element.Dialog;
import cat.jiu.dialog.element.option.button.DialogButton;
import cat.jiu.dialog.element.option.text.DialogTextField;
import cat.jiu.dialog.iface.IDialogOptionDataUnit;
import cat.jiu.dialog.iface.IDialogText;
import net.minecraft.util.ResourceLocation;

/**
 * 用于方便的创建一个对话框
 * @author small_jiu
 */
public class DialogBuilder {
	protected ResourceLocation id;
	protected final IDialogText title;
	protected final List<IDialogOptionDataUnit> options = Lists.newArrayList();
	/**
	 * use for {@link cat.jiu.dialog.api.helper.DialogList#addDialogOperation(int, cat.jiu.dialog.api.helper.DialogList.DialogOperation)}
	 * @param text
	 */
	public DialogBuilder(IDialogText title) {
		this.title = title;
	}
	public DialogBuilder(ResourceLocation id, IDialogText title) {
		this.id = id;
		this.title = title;
	}
	/**
	 * 添加选项到对话框
	 * @param drawUnit drawUnit
	 */
	public DialogBuilder addOption(IDialogOptionDataUnit option) {
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
	/**
	 * 添加一个按钮选项
	 * @param text 显示的内容
	 * @return
	 */
	public DialogBuilder addButton(IDialogText text) {
		return this.addOption(new DialogButton(text, false));
	}
	/**
	 * 添加一个按钮选项
	 * @param text 显示的内容
	 * @param closeDialog 是否可以关闭正在显示的对话框
	 * @return
	 */
	public DialogBuilder addButton(IDialogText text, boolean closeDialog) {
		return this.addOption(new DialogButton(text, closeDialog));
	}
	/**
	  * 添加一个按钮选项
	 * @param text 显示的内容
	 * @param closeDialog 是否可以关闭正在显示的对话框
	 * @param tooltips 鼠标移动到按钮上时显示的提示
	 */
	public DialogBuilder addButton(IDialogText text, boolean closeDialog, IDialogText... tooltips) {
		return this.addOption(new DialogButton(text, closeDialog, tooltips));
	}
	/**
	  * 添加一个按钮选项
	 * @param text 显示的内容
	 * @param closeDialog 是否可以关闭正在显示的对话框
	 * @param tooltips 鼠标移动到按钮上时显示的提示
	 */
	public DialogBuilder addButton(IDialogText text, boolean closeDialog, List<IDialogText> tooltips) {
		return this.addOption(new DialogButton(text, closeDialog, tooltips));
	}
	/**
	 * 添加一个文本输入框选项
	 * @param text 显示的提示
	 * @param closeDialog 是否可以关闭正在显示的对话框
	 * @return
	 */
	public DialogBuilder addTextField(IDialogText info, boolean closeDialog) {
		return this.addOption(new DialogTextField(info, closeDialog));
	}
	
	public DialogBuilder addButtonTooltip(int index, IDialogText... tooltip) {
		if(this.options.size() > index && index >= 0 && this.options.get(index) instanceof DialogButton) {
			for(IDialogText text : tooltip) {
				((DialogButton)this.options.get(index)).addTooltips(text);
			}
		}
		return this;
	}
	
	public DialogBuilder addTextFieldTooltip(int index, IDialogText... tooltip) {
		if(this.options.size() > index && index >= 0 && this.options.get(index) instanceof DialogTextField) {
			for(IDialogText text : tooltip) {
				((DialogTextField)this.options.get(index)).addTooltips(text);
			}
		}
		return this;
	}
	
	/**
	 * 创建对话框
	 */
	public Dialog build() {
		return new Dialog(id, title, options);
	}
}
