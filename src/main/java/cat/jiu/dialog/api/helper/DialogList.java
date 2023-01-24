package cat.jiu.dialog.api.helper;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import cat.jiu.dialog.event.DialogOptionEvent;
import cat.jiu.dialog.iface.*;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * 对话框的列表，可以自动订阅事件并处理<p>
 * 注意：如果你创建对象时是在static时创建的，那你可能需要使用类似于Class.forName的方法初始化该类
 * @author small_jiu
 */
@EventBusSubscriber
public class DialogList {
	protected final boolean subscriber;
	protected final LinkedHashMap<ResourceLocation, DialogOperation> dialogs = new LinkedHashMap<>();
	/**
	 * @param autoSubscriberEvent 是否自动处理对话框选项的点击事件
	 */
	public DialogList(boolean autoSubscriberEvent) {
		if(autoSubscriberEvent) {
			MinecraftForge.EVENT_BUS.register(this);
		}
		this.subscriber = autoSubscriberEvent;
	}
	/**
	 * 是否有对应ID的对话框
	 * @param id 对话框的ID
	 * @return 
	 */
	public boolean hasDialogOperation(ResourceLocation id) {
		return this.dialogs.containsKey(id);
	}
	/**
	 * 获取对应ID的对话框
	 * @param id 对话框ID
	 * @return 对应的对话框
	 */
	public DialogOperation getDialogOperation(ResourceLocation id) {
		return this.dialogs.get(id);
	}
	/**
	 * 添加一个对话框到列表
	 * @param id 
	 * @param dialog
	 * @return
	 */
	public DialogList addDialogOperation(ResourceLocation id, DialogOperation dialog) {
		dialog.getDialog().setID(id);
		this.dialogs.put(id, dialog);
		return this;
	}
	/**
	 * 处理一个对话框按钮选项的点击事件
	 * @param player Player
	 * @param dialog 对话框ID
	 * @param taskid 选项ID
	 * @param mouseButton 点击时使用的按键
	 */
	public void execute(EntityPlayer player, ResourceLocation dialog, int taskid, int mouseButton) {
		if(this.dialogs.containsKey(dialog)) {
			DialogOperation operation = this.getDialogOperation(dialog);
			if(operation.hasTask(taskid)) {
				IOptionTask task = operation.getTask(taskid);
				if(task instanceof IOptionButtonTask) {
					((IOptionButtonTask) task).run(player, mouseButton);
				}
			}
		}
	}
	
	/**
	 * 处理一个对话框文本框选项的点击事件
	 * @param playerPlayer
	 * @param text 文本框的文本
	 * @param dialog 对话框ID
	 * @param taskid 选项ID
	 */
	public void execute(EntityPlayer player, String text, ResourceLocation dialog, int taskid) {
		if(this.dialogs.containsKey(dialog)) {
			DialogOperation operation = this.getDialogOperation(dialog);
			if(operation.hasTask(taskid)) {
				IOptionTask task = operation.getTask(taskid);
				if(task instanceof IOptionEditTextTask) {
					((IOptionEditTextTask) task).run(player, text);
				}
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onDialogOptionButtonClick(DialogOptionEvent.ButtonClick event) {
		if(!event.isCanceled()) {
			this.execute(event.player, event.dialogID, event.optionID, event.mouseButton);
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onDialogOptionEditTextConfirm(DialogOptionEvent.TextConfirm event) {
		if(!event.isCanceled()) {
			this.execute(event.player, event.getText(), event.dialogID, event.optionID);
		}
	}
	
	@Override
	public String toString() {
		return this.writeToJson(new JsonObject()).toString();
	}
	
	/**
	 * 把对话框列表序列化为Json<p>
	 * 注意：这不会把选项点击时的处理序列化
	 * @param json
	 * @return
	 */
	public JsonObject writeToJson(JsonObject json) {
		if(json==null) json = new JsonObject();
		json.addProperty("subscriber", this.subscriber);
		JsonArray dialogs = new JsonArray();
		for(Entry<ResourceLocation, DialogOperation> dialog : this.dialogs.entrySet()) {
			dialogs.add(dialog.getValue().writeToJson(new JsonObject()));
		}
		json.add("dialog", dialogs);
		return json;
	}
	
	/**
	 * 从一个Json文件获取对话框列表
	 */
	public static DialogList get(JsonObject json) {
		DialogList list = new DialogList(json.has("subscriber") ? json.get("subscriber").getAsBoolean() : false);
		if(json.has("dialog") && json.get("dialog").isJsonArray()) {
			JsonArray dialogs = json.getAsJsonArray("dialog");
			for(int i = 0; i < dialogs.size(); i++) {
				JsonObject dialog = dialogs.get(i).getAsJsonObject();
				list.addDialogOperation(new ResourceLocation(dialog.get("id").getAsString()), DialogOperation.get(list, dialog));
			}
		}
		return list;
	}
}
