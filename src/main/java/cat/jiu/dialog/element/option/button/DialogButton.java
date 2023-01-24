package cat.jiu.dialog.element.option.button;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import cat.jiu.dialog.element.DialogOptionType;
import cat.jiu.dialog.element.DialogText;
import cat.jiu.dialog.iface.IDialogOptionDataUnit;
import cat.jiu.dialog.iface.IDialogText;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 对话框的按钮组件，点击后将在服务端与客户端触发{@link cat.jiu.dialog.event.DialogOptionEvent.ButtonClick}
 * @author small_jiu
 */
public class DialogButton implements IDialogOptionDataUnit {
	public DialogButton() {}
	
	protected List<IDialogText> tooltips;
	protected IDialogText text = DialogText.EMPTY;
	protected boolean closeDialog;
	
	/**
	 * @param text 按钮的提示，类似于{@link network.minecraft.client.gui.GuiButton#displayString}
	 * @param closeDialog 点击选项后是否可以关闭对话框
	 */
	public DialogButton(IDialogText text, boolean closeDialog) {
		this.text = text!=null ? text : DialogText.EMPTY;
		this.closeDialog = closeDialog;
	}
	
	/**
	 * @param text 按钮的提示，类似于{@link network.minecraft.client.gui.GuiButton#displayString}
	 * @param closeDialog 点击选项后是否可以关闭对话框
	 * @param tooltips 鼠标移动到按钮上时显示的提示
	 */
	public DialogButton(IDialogText text, boolean closeDialog, IDialogText... tooltips) {
		this.text = text!=null ? text : DialogText.EMPTY;
		this.closeDialog = closeDialog;
		this.tooltips = Lists.newArrayList(tooltips);
	}
	/**
	 * @param text 按钮的提示，类似于{@link network.minecraft.client.gui.GuiButton#displayString}
	 * @param closeDialog 点击选项后是否可以关闭对话框
	 * @param tooltips 鼠标移动到按钮上时显示的提示
	 */
	public DialogButton(IDialogText text, boolean closeDialog, List<IDialogText> tooltips) {
		this.text = text!=null ? text : DialogText.EMPTY;
		this.closeDialog = closeDialog;
		this.tooltips = tooltips;
	}
	
	@Override
	public IDialogText getOptionText() {
		return this.text;
	}
	
	public boolean canCloseDialog() {
		return closeDialog;
	}
	/**
	 * @return 鼠标移动到按钮上时显示的提示
	 */
	public List<IDialogText> getTooltips() {
		return tooltips;
	}
	/**
	 * @return 从{@link cat.jiu.dialog.element.DialogText} 转换为 {@link java.lang.String} 的提示
	 */
	@SideOnly(Side.CLIENT)
	public List<String> getTooltipsString() {
		if(!this.hasTooltips()) return null;
		List<String> tips = Lists.newArrayList();
		for(int i = 0; i < this.tooltips.size(); i++) {
			tips.add(this.tooltips.get(i).format());
		}
		return tips;
	}
	/**
	 * 设置鼠标移动到按钮上时显示的提示
	 * @param tooltips 鼠标移动到按钮上时显示的提示
	 */
	public void setTooltips(List<IDialogText> tooltips) {
		this.tooltips = tooltips;
	}
	/**
	 * 添加鼠标移动到按钮上时显示的提示
	 * @param tooltip 鼠标移动到按钮上时显示的提示
	 */
	public void addTooltips(IDialogText tooltip) {
		if(this.tooltips==null) this.tooltips = Lists.newArrayList();
		this.tooltips.add(tooltip);
	}
	/*
	 * 是否含有鼠标移动到按钮上时显示的提示
	 */
	public boolean hasTooltips() {
		return this.tooltips!=null && !this.tooltips.isEmpty();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		this.closeDialog = nbt.getBoolean("close");
		if(nbt.hasKey("text")) {
			this.text = DialogText.get(nbt.getTag("text"));
		}
		
		if(nbt.hasKey("tooltips")) {
			NBTTagList tooltips = nbt.getTagList("tooltips", 10);
			this.tooltips = Lists.newArrayList();
			for(int i = 0; i < tooltips.tagCount(); i++) {
				this.tooltips.add(DialogText.get(tooltips.getCompoundTagAt(i)));
			}
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setBoolean("close", this.closeDialog);
		if(this.text!=null) 
			nbt.setTag("text", this.text.writeToNBT());
		if(this.hasTooltips()) {
			NBTTagList tooltips = new NBTTagList();
			for(int i = 0; i < this.tooltips.size(); i++) {
				tooltips.appendTag(this.tooltips.get(i).writeToNBT());
			}
			nbt.setTag("tooltips", tooltips);
		}
		return nbt;
	}

	@Override
	public void readFromJson(JsonObject json) {
		List<IDialogText> tooltips = null;
		if(json.has("tooltip")) {
			tooltips = Lists.newArrayList();
			JsonArray infos = json.getAsJsonArray("tooltip");
			for(int j = 0; j < infos.size(); j++) {
				tooltips.add(new DialogText(infos.get(j).getAsString()));
			}
		}
		boolean close = false;
		if(json.has("close")) {
			close = json.get("close").getAsBoolean();
		}
		if(json.has("text")) {
			this.text = DialogText.get(json.get("text"));
		}
		
		this.closeDialog = close;
		this.tooltips = tooltips;
	}

	@Override
	public JsonObject writeToJson(JsonObject json) {
		if(json==null) json = new JsonObject();
		if(this.text!=null) json.add("text", this.getOptionText().writeToJson());
		json.addProperty("close", this.canCloseDialog());
		
		if(this.hasTooltips()) {
			JsonArray infos = new JsonArray();
			for(int j = 0; j < this.getTooltips().size(); j++) {
				infos.add(this.getTooltips().get(j).getText());
			}
			json.add("tooltip", infos);
		}
		return json;
	}
	
	@Override
	public ResourceLocation getTypeID() {
		return DialogOptionType.BUTTON.getTypeID();
	}
}
