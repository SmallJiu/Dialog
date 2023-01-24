package cat.jiu.dialog.api.helper;

import java.util.LinkedHashMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import cat.jiu.dialog.DialogAPI;
import cat.jiu.dialog.api.ButtonDisplayDialog;
import cat.jiu.dialog.api.DialogBuilder;
import cat.jiu.dialog.element.Dialog;
import cat.jiu.dialog.element.DialogOptionType;
import cat.jiu.dialog.element.DialogText;
import cat.jiu.dialog.element.option.button.DialogButton;
import cat.jiu.dialog.iface.*;

import net.minecraft.util.ResourceLocation;

/**
 * 对对话框的包装，可以在这里设置选项点击后的处理方式
 * @author small_jiu
 */
public class DialogOperation {
	protected final Dialog dialog;
	protected final LinkedHashMap<Integer, IOptionTask> optionTasks = new LinkedHashMap<>();
	public DialogOperation(DialogBuilder dialog) {
		this(dialog.build());
	}
	public DialogOperation(Dialog dialog) {
		this.dialog = dialog;
	}
	
	/**
	 * 获取对话框
	 */
	public Dialog getDialog() {
		return dialog;
	}
	/**
	 * 是否含有选项的处理对象
	 * @param index 选项ID
	 */
	public boolean hasTask(int index) {
		return this.optionTasks.containsKey(index);
	}
	/**
	 * 获取选项的处理对象
	 * @param index 选项ID
	 */
	public IOptionTask getTask(int index){
		if(this.hasTask(index)) {
			return this.optionTasks.get(index);
		}
		return null;
	}
	
	/**
	 * 设置按钮选项被点击时的处理<p>
	 * 注：这在序列化为Json时不会序列化
	 * @param index 选项ID
	 * @param task 处理对象
	 */
	public DialogOperation setButtonTask(int index, IOptionButtonTask task) {
		return this.setTask(index, task);
	}
	/**
	 * 设置文本框选项被点击时的处理<p>
	 * 注：这在序列化为Json时不会序列化
	 * @param index 选项ID
	 * @param task 处理对象
	 */
	public DialogOperation setEditTextTask(int index, IOptionEditTextTask task) {
		return this.setTask(index, task);
	}
	
	/**
	 * 设置选项被点击时的处理<p>
	 * 注：这在序列化为Json时不会序列化
	 * @param index 选项ID
	 * @param task 处理对象
	 */
	public DialogOperation setTask(int index, IOptionTask task) {
		this.optionTasks.put(index, task);
		return this;
	}
	
	protected JsonObject writeToJson(JsonObject json) {
		if(json==null) json = new JsonObject();
		json.addProperty("id", this.dialog.getID().toString());
		json.addProperty("text", this.dialog.getTitle().getText());
		if(!this.dialog.getOptions().isEmpty()) {
			JsonArray options = new JsonArray();
			for(int i = 0; i < this.dialog.getOptions().size(); i++) {
				IDialogOptionDataUnit option = this.dialog.getOptions().get(i);
				
				if(option != null) {
					JsonObject optionJson = new JsonObject();
					
					optionJson.addProperty("type", option.getTypeID().toString());
					
					if(option instanceof DialogButton
					&& this.optionTasks.containsKey(i)
					&& this.optionTasks.get(i) instanceof ButtonDisplayDialog) {
						optionJson.addProperty("next", ((ButtonDisplayDialog)this.optionTasks.get(i)).id.toString());
					}
					
					option.writeToJson(optionJson);
					options.add(optionJson);
				}
			}
			json.add("option", options);
		}
		
		return json;
	}
	
	static DialogOperation get(DialogList list, JsonObject json) {
		if(json==null) return null;
		IDialogText title = DialogText.get(json.get("text"));
		
		DialogBuilder builder = new DialogBuilder(title);
		LinkedHashMap<Integer, IOptionTask> optionTasks = new LinkedHashMap<>();
		
		JsonArray options = json.getAsJsonArray("option");
		for(int i = 0; i < options.size(); i++) {
			JsonObject option = options.get(i).getAsJsonObject();
			ResourceLocation type = new ResourceLocation(option.get("type").getAsString());
			if(DialogOptionType.BUTTON.getTypeID().equals(type) && option.has("next")) {
				int id = i;
				if(option.has("id")) {
					id = option.get("id").getAsInt();
				}
				optionTasks.put(id, new ButtonDisplayDialog(list, new ResourceLocation(option.get("next").getAsString())));
				option.addProperty("close", false);
			}
			try {
				IDialogOptionDataUnit o = DialogAPI.newInstance(type, option);
				if(o!=null) builder.addOption(o);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		DialogOperation operation = new DialogOperation(builder);
		operation.optionTasks.putAll(optionTasks);
		return operation;
	}
}
