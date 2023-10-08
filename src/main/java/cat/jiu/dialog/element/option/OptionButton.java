package cat.jiu.dialog.element.option;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import cat.jiu.core.api.element.IText;
import cat.jiu.core.util.element.Text;
import cat.jiu.dialog.api.IDialogOption;
import cat.jiu.dialog.element.DialogOption;
import cat.jiu.dialog.element.OptionDrawUnit;
import cat.jiu.dialog.element.option.draw.GuiOptionButton;
import cat.jiu.dialog.utils.dimension.DialogDimension;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

/**
 * 对话框的按钮组件，点击后将在服务端与客户端触发{@link cat.jiu.dialog.event.OptionEvent.ButtonClick}
 * @author small_jiu
 */
@ZenRegister
@ZenClass("dialog.option.Button")
public class OptionButton implements IDialogOption {
	public OptionButton() {}
	
	protected List<IText> tooltips;
	protected IText text = Text.empty;
	protected boolean closeDialog;
	
	/**
	 * @param text 按钮的提示，类似于{@link network.minecraft.client.gui.GuiButton#displayString}
	 * @param closeDialog 点击选项后是否可以关闭对话框
	 */
	public OptionButton(IText text, boolean closeDialog) {
		this.text = text!=null ? text : Text.empty;
		this.closeDialog = closeDialog;
	}
	
	/**
	 * @param text 按钮的提示，类似于{@link network.minecraft.client.gui.GuiButton#displayString}
	 * @param closeDialog 点击选项后是否可以关闭对话框
	 * @param tooltips 鼠标移动到按钮上时显示的提示
	 */
	public OptionButton(IText text, boolean closeDialog, IText... tooltips) {
		this.text = text!=null ? text : Text.empty;
		this.closeDialog = closeDialog;
		this.tooltips = Lists.newArrayList(tooltips);
	}
	/**
	 * @param text 按钮的提示，类似于{@link network.minecraft.client.gui.GuiButton#displayString}
	 * @param closeDialog 点击选项后是否可以关闭对话框
	 * @param tooltips 鼠标移动到按钮上时显示的提示
	 */
	public OptionButton(IText text, boolean closeDialog, List<IText> tooltips) {
		this.text = text!=null ? text : Text.empty;
		this.closeDialog = closeDialog;
		this.tooltips = tooltips;
	}
	
	@Override
	public IText getOptionText() {
		return this.text;
	}
	
	public boolean canCloseDialog() {
		return closeDialog;
	}
	/**
	 * @return 鼠标移动到按钮上时显示的提示
	 */
	@ZenMethod
	public List<IText> getTooltips() {
		return tooltips;
	}
	/**
	 * @return 从{@link cat.jiu.dialog.element.Text} 转换为 {@link java.lang.String} 的提示
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
	@ZenMethod
	public void setTooltips(List<IText> tooltips) {
		this.tooltips = tooltips;
	}
	/**
	 * 添加鼠标移动到按钮上时显示的提示
	 * @param tooltip 鼠标移动到按钮上时显示的提示
	 */
	@ZenMethod
	public void addTooltips(IText tooltip) {
		if(this.tooltips==null) this.tooltips = Lists.newArrayList();
		this.tooltips.add(tooltip);
	}
	/*
	 * 是否含有鼠标移动到按钮上时显示的提示
	 */
	@ZenMethod
	public boolean hasTooltips() {
		return this.tooltips!=null && !this.tooltips.isEmpty();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		this.closeDialog = nbt.getBoolean("close");
		if(nbt.hasKey("text")) {
			this.text = new Text(nbt.getCompoundTag("text"));
		}
		
		if(nbt.hasKey("tooltips")) {
			NBTTagList tooltips = nbt.getTagList("tooltips", 10);
			this.tooltips = Lists.newArrayList();
			for(int i = 0; i < tooltips.tagCount(); i++) {
				this.tooltips.add(new Text(tooltips.getCompoundTagAt(i)));
			}
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setBoolean("close", this.closeDialog);
		if(this.text!=null) 
			nbt.setTag("text", this.text.writeTo(NBTTagCompound.class));
		if(this.hasTooltips()) {
			NBTTagList tooltips = new NBTTagList();
			for(int i = 0; i < this.tooltips.size(); i++) {
				tooltips.appendTag(this.tooltips.get(i).writeTo(NBTTagCompound.class));
			}
			nbt.setTag("tooltips", tooltips);
		}
		return nbt;
	}

	@Override
	public void readFromJson(JsonObject json) {
		List<IText> tooltips = null;
		if(json.has("tooltip")) {
			tooltips = Lists.newArrayList();
			JsonArray infos = json.getAsJsonArray("tooltip");
			for(int j = 0; j < infos.size(); j++) {
				tooltips.add(new Text(infos.get(j).getAsString()));
			}
		}
		boolean close = false;
		if(json.has("close")) {
			close = json.get("close").getAsBoolean();
		}
		if(json.has("text")) {
			if(json.get("text").isJsonObject()) {
				this.text = new Text(json.getAsJsonObject("text"));
			}else if(json.get("text").isJsonPrimitive()) {
				this.text = new Text(json.get("text").getAsString());
			}
		}
		
		this.closeDialog = close;
		this.tooltips = tooltips;
	}

	@Override
	public JsonObject writeToJson(JsonObject json) {
		if(json==null) json = new JsonObject();
		if(this.text!=null) json.add("text", this.getOptionText().writeTo(JsonObject.class));
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
		return DialogOption.button().getTypeID();
	}
	@Override
	public OptionButton copy() {
		List<IText> tooltips = this.tooltips == null ? null : this.tooltips.stream().map(tooltip -> tooltip.copy()).collect(Collectors.toList());
		return new OptionButton(this.text.copy(), canCloseDialog(), tooltips);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public OptionDrawUnit getDrawUnit(ResourceLocation dialogID, int optionID, IDialogOption option, DialogDimension dialogDimension) {
		return new GuiOptionButton(Minecraft.getMinecraft().player, dialogID, (OptionButton) option, optionID, dialogDimension);
	}
}
