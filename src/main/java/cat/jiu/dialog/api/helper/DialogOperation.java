package cat.jiu.dialog.api.helper;

import java.util.LinkedHashMap;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import cat.jiu.core.api.element.IText;
import cat.jiu.core.util.element.Text;
import cat.jiu.dialog.DialogAPI;
import cat.jiu.dialog.api.IDialogOption;
import cat.jiu.dialog.api.IOptionTask;
import cat.jiu.dialog.api.task.*;
import cat.jiu.dialog.element.Dialog;
import cat.jiu.dialog.utils.DialogBuilder;

import net.minecraft.util.ResourceLocation;

import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import crafttweaker.annotations.ZenRegister;

/**
 * 对对话框的包装，可以在这里设置选项点击后的处理方式
 * @author small_jiu
 */
@ZenRegister
@ZenClass("dialog.api.Operation")
public class DialogOperation {
	public final boolean saveTaskToJson;
	protected final Dialog dialog;
	protected final LinkedHashMap<Integer, List<IOptionTask>> optionTasks = new LinkedHashMap<>();
	public DialogOperation(DialogBuilder dialog, boolean saveTaskToJson) {
		this(dialog.build(), saveTaskToJson);
	}
	public DialogOperation(Dialog dialog, boolean saveTaskToJson) {
		this.dialog = dialog;
		this.saveTaskToJson = saveTaskToJson;
	}
	public DialogOperation(DialogOperation other) {
		this(other.copyDialog(), other.isSaveTaskToJson());
		this.optionTasks.putAll(other.optionTasks);
	}
	
	/**
	 * @return 获取原始对话框，仅用在需要调整参数时调用
	 */
	@ZenMethod("getOriginal")
	public Dialog getOriginalDialog() {
		return dialog;
	}
	/**
	 * @return 获取复制后的对话框，用于展示出来
	 */
	@ZenMethod("copy")
	public Dialog copyDialog() {
		return dialog.clone();
	}
	public boolean isSaveTaskToJson() {
		return saveTaskToJson;
	}
	/**
	 * 是否含有选项的处理对象
	 * @param index 选项ID
	 */
	@ZenMethod
	public boolean hasTask(int index) {
		return this.optionTasks.containsKey(index);
	}
	/**
	 * 获取选项的处理对象
	 * @param index 选项ID
	 */
	@ZenMethod
	public List<IOptionTask> getTask(int index){
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
	@ZenMethod
	public DialogOperation setButtonTask(int index, IButtonTask task) {
		return this.setTask(index, task);
	}
	/**
	 * 设置文本框选项被点击时的处理<p>
	 * 注：这在序列化为Json时不会序列化
	 * @param index 选项ID
	 * @param task 处理对象
	 */
	@ZenMethod
	public DialogOperation setEditTextTask(int index, IEditTextTask task) {
		return this.setTask(index, task);
	}

	@ZenMethod
	public DialogOperation setCheckBoxCheckTask(int index, ICheckboxCheckTask task) {
		this.setTask(index, task);
		return this;
	}

	@ZenMethod
	public DialogOperation setCheckBoxConfirmTask(int index, ICheckboxConfirmTask task) {
		this.setTask(index, task);
		return this;
	}

	@ZenMethod
	public DialogOperation setItemCheckboxConfirmTask(int index, IItemCheckboxConfirmTask task) {
		this.setTask(index, task);
		return this;
	}

	@ZenMethod
	public DialogOperation setItemCheckboxSelectTask(int index, IItemCheckboxCheckTask task) {
		this.setTask(index, task);
		return this;
	}

	@ZenMethod
	public DialogOperation setRadioButtonTask(int index, IRadioButtonTask task) {
		this.setTask(index, task);
		return this;
	}

	@ZenMethod
	public DialogOperation setItemRadioButtonTask(int index, IItemRadioButtonTask task) {
		this.setTask(index, task);
		return this;
	}

	@ZenMethod
	public DialogOperation setMultiTitleCloseTask(int index, IMultiTitleCloseTask task) {
		this.setTask(index, task);
		return this;
	}
	@ZenMethod
	public DialogOperation setMultiTitleChangeTask(int index, IMultiTitleChangeTask task) {
		this.setTask(index, task);
		return this;
	}
	@ZenMethod
	public DialogOperation setMultiTitleBackParentTask(int index, IMultiTitleToParentTask task) {
		this.setTask(index, task);
		return this;
	}
	
	/**
	 * 设置选项被点击时的处理<p>
	 * 注：这在序列化为Json时不会序列化
	 * @param index 选项ID
	 * @param task 处理对象
	 */
	@ZenMethod
	public DialogOperation setTask(int index, IOptionTask task) {
		if(!this.optionTasks.containsKey(index)) {
			this.optionTasks.put(index, Lists.newArrayList());
		}
		boolean hasTask = false;
		for(IOptionTask t : this.optionTasks.get(index)) {
			if(task.getClass().isAssignableFrom(t.getClass())) {
				hasTask = true;
				break;
			}
		}
		if(!hasTask) this.optionTasks.get(index).add(task);
		return this;
	}
	
	protected JsonObject writeToJson(JsonObject json) {
		if(json==null) json = new JsonObject();
		json.addProperty("id", this.dialog.getID().toString());
		json.addProperty("text", this.dialog.getTitle().getText());
		json.addProperty("saveTask", this.saveTaskToJson);
		if(!this.dialog.getOptions().isEmpty()) {
			JsonArray options = new JsonArray();
			for(int i = 0; i < this.dialog.getOptions().size(); i++) {
				IDialogOption option = this.dialog.getOptions().get(i);
				
				if(option != null) {
					JsonObject optionJson = new JsonObject();
					
					optionJson.addProperty("type", option.getTypeID().toString());
					
					if(this.optionTasks.containsKey(i) && this.saveTaskToJson) {
						JsonArray taskJson = new JsonArray();
						for(IOptionTask task : this.optionTasks.get(i)) {
							JsonObject object = new JsonObject();
							object.addProperty("clazz", task.getClass().getName());
							object.add("data", task.write(new JsonObject()));
							taskJson.add(object);
						}
						optionJson.add("tasks", taskJson);
					}
					
					optionJson.add("data", option.writeToJson(new JsonObject()));
					options.add(optionJson);
				}
			}
			json.add("option", options);
		}
		
		return json;
	}
	
	static DialogOperation get(DialogList list, JsonObject json) {
		if(json==null) return null;
		IText title;
		
		if(json.get("text").isJsonObject()) {
			title = new Text(json.getAsJsonObject("text"));
		}else {
			title = new Text(json.get("text").getAsString());
		}
		
		DialogBuilder builder = new DialogBuilder(title);
		LinkedHashMap<Integer, List<IOptionTask>> optionTasks = new LinkedHashMap<>();
		
		JsonArray options = json.getAsJsonArray("option");
		for(int i = 0; i < options.size(); i++) {
			JsonObject option = options.get(i).getAsJsonObject();
			ResourceLocation type = new ResourceLocation(option.get("type").getAsString());
			if(option.has("tasks") ) {
				JsonArray tasks = option.getAsJsonArray("tasks");
				for(int j = 0; j < tasks.size(); j++) {
					if(!optionTasks.containsKey(i)) optionTasks.put(i, Lists.newArrayList());
					JsonObject taskJson = tasks.get(j).getAsJsonObject();
					try {
						String taskClazz = taskJson.get("clazz").getAsString();
						if(!taskClazz.contains("$Lambda$")) {
							@SuppressWarnings("unchecked")
							Class<? extends IOptionTask> clazz = (Class<? extends IOptionTask>) Class.forName(taskClazz);
							IOptionTask task = DialogAPI.getTask(clazz, list, taskJson.getAsJsonObject("data"));
							optionTasks.get(i).add(task != null ? task : (IOptionTask) clazz.newInstance());
						}
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
			try {
				IDialogOption o = DialogAPI.newInstance(type, option.getAsJsonObject("data"));
				if(o!=null) builder.addOption(o);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		DialogOperation operation = new DialogOperation(builder, json.has("saveTask") && json.get("saveTask").getAsBoolean());
		operation.optionTasks.putAll(optionTasks);
		return operation;
	}
}
