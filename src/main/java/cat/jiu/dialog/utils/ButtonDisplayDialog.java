package cat.jiu.dialog.utils;

import com.google.gson.JsonObject;

import cat.jiu.dialog.api.helper.DialogList;
import cat.jiu.dialog.api.task.IButtonTask;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import crafttweaker.annotations.ZenRegister;

/**
 * 按下按钮选项后将展示一个对话框
 * @author small_jiu
 */
@ZenRegister
@ZenClass("dialog.option.task.button.DisplayDialog")
public class ButtonDisplayDialog implements IButtonTask {
	/**
	 * 对话框ID
	 */
	public ResourceLocation id;
	protected DialogList list;
	public ButtonDisplayDialog() {}
	/**
	 * @param list 对话框列表
	 * @param id 需要展示的对话框ID
	 */
	public ButtonDisplayDialog(DialogList list, ResourceLocation id) {
		this.list = list;
		this.id = id;
	}
	@Override
	public void read(JsonObject json) {
		this.id = new ResourceLocation(json.get("next").getAsString());
	}
	@Override
	public JsonObject write(JsonObject json) {
		if(json==null) json = new JsonObject();
		json.addProperty("next", this.id.toString());
		return json;
	}
	@ZenMethod
	public void setList(DialogList list) {
		this.list = list;
	}
	@Override
	public void onButtonClick(ResourceLocation parent, ResourceLocation dialog, int option, EntityPlayer player, int mouseButton) {
		if(player.world.isRemote
		&& mouseButton == 0
		&& this.list!=null && this.list.hasDialogOperation(this.id)) {
			this.list.display(player, this.id);
		}
	}
}
