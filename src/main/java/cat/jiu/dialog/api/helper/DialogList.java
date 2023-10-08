package cat.jiu.dialog.api.helper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import cat.jiu.dialog.DialogAPI;
import cat.jiu.dialog.api.IOptionTask;
import cat.jiu.dialog.api.helper.DialogList;
import cat.jiu.dialog.api.helper.DialogOperation;
import cat.jiu.dialog.api.task.*;
import cat.jiu.dialog.event.*;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@SuppressWarnings("all")
@ZenRegister
@ZenClass("dialog.api.List")
@EventBusSubscriber
public class DialogList {
	public final boolean subscriber;
	protected final LinkedHashMap<ResourceLocation, DialogOperation> dialogs = new LinkedHashMap<>();
	
	public DialogList(boolean autoSubscriberEvent) {
		if(autoSubscriberEvent) {
			MinecraftForge.EVENT_BUS.register(this);
		}
		this.subscriber = autoSubscriberEvent;
	}
	
	public boolean hasDialogOperation(ResourceLocation id) {
		return this.dialogs.containsKey(id);
	}
	@ZenMethod("hasDialog")
	public boolean hasDialogOperation(String id) {
		return this.dialogs.containsKey(new ResourceLocation(id));
	}
	
	public DialogOperation getDialogOperation(ResourceLocation id) {
		return this.dialogs.get(id);
	}
	@ZenMethod("getDialog")
	public DialogOperation getDialogOperation(String id) {
		return this.dialogs.get(new ResourceLocation(id));
	}
	
	public DialogList addDialogOperation(ResourceLocation id, DialogOperation dialog) {
		dialog.getOriginalDialog().setID(id);
		this.dialogs.put(id, new DialogOperation(dialog));
		return this;
	}
	@ZenMethod("addDialog")
	public DialogList addDialogOperation(String id, DialogOperation dialog) {
		return this.addDialogOperation(new ResourceLocation(id), dialog);
	}
	
	public void display(EntityPlayer player, ResourceLocation id) {
		if(this.hasDialogOperation(id)) {
			DialogAPI.displayDialog(player, this.getDialogOperation(id).copyDialog());
		}
	}
	@ZenMethod
	public void display(EntityPlayer player, String id) {
		this.display(player, new ResourceLocation(id));
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onDialogOptionButtonClick(OptionEvent.ButtonClick event) {
		if(this.dialogs.containsKey(event.dialogID)) {
			DialogOperation operation = this.getDialogOperation(event.dialogID);
			if(operation.hasTask(event.optionID)) {
				List<IOptionTask> tasks = operation.getTask(event.optionID);
				tasks.forEach(task->{
					if(task instanceof IButtonTask) {
						((IButtonTask) task).run(event.parent, event.dialogID, event.optionID, event.player, event.mouseButton);
					}
				});
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onDialogOptionEditTextConfirm(OptionEvent.TextConfirm event) {
		if(this.dialogs.containsKey(event.dialogID)) {
			DialogOperation operation = this.getDialogOperation(event.dialogID);
			if(operation.hasTask(event.optionID)) {
				List<IOptionTask> tasks = operation.getTask(event.optionID);
				tasks.forEach(task->{
					if(task instanceof IEditTextTask) {
						((IEditTextTask) task).run(event.parent, event.dialogID, event.optionID, event.player, event.text);
					}
				});
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onDialogOptionCheckboxConfirm(CheckboxEvent.Confirm event) {
		if(this.dialogs.containsKey(event.dialogID)) {
			DialogOperation operation = this.getDialogOperation(event.dialogID);
			if(operation.hasTask(event.optionID)) {
				List<IOptionTask> tasks = operation.getTask(event.optionID);
				tasks.forEach(task->{
					if(task instanceof ICheckboxConfirmTask) {
						((ICheckboxConfirmTask) task).run(event.parent, event.dialogID, event.optionID, event.player, event.selects);
					}
				});
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onDialogOptionCheckboxCheck(CheckboxEvent.Check event) {
		if(this.dialogs.containsKey(event.dialogID)) {
			DialogOperation operation = this.getDialogOperation(event.dialogID);
			if(operation.hasTask(event.optionID)) {
				List<IOptionTask> tasks = operation.getTask(event.optionID);
				tasks.forEach(task->{
					if(task instanceof ICheckboxCheckTask) {
						((ICheckboxCheckTask) task).run(event.parent, event.dialogID, event.optionID, event.player, event.selectIndex, event.optionString, event.isRemove);
					}
				});
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onDialogOptionRadioButtonEvent(RadioButtonEvent event) {
		if(this.dialogs.containsKey(event.dialogID)) {
			DialogOperation operation = this.getDialogOperation(event.dialogID);
			if(operation.hasTask(event.optionID)) {
				List<IOptionTask> tasks = operation.getTask(event.optionID);
				tasks.forEach(task->{
					if(task instanceof IRadioButtonTask) {
						((IRadioButtonTask) task).run(event.parent, event.dialogID, event.optionID, event.player, event.selectIndex, event.optionString, event.confirm);
					}
				});
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onDialogOptionItemRadioButtonSelectEvent(ItemChooseEvent.Single event) {
		if(this.dialogs.containsKey(event.dialogID)) {
			DialogOperation operation = this.getDialogOperation(event.dialogID);
			if(operation.hasTask(event.optionID)) {
				List<IOptionTask> tasks = operation.getTask(event.optionID);
				tasks.forEach(task->{
					if(task instanceof IItemRadioButtonTask) {
						((IItemRadioButtonTask) task).run(event.parent, event.dialogID, event.optionID, event.player, event.selectIndex, event.stack, event.confirm);
					}
				});
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onDialogOptionItemCheckboxConfirm(ItemChooseEvent.Multi.Confirm event) {
		if(this.dialogs.containsKey(event.dialogID)) {
			DialogOperation operation = this.getDialogOperation(event.dialogID);
			if(operation.hasTask(event.optionID)) {
				List<IOptionTask> tasks = operation.getTask(event.optionID);
				tasks.forEach(task->{
					if(task instanceof IItemCheckboxConfirmTask) {
						((IItemCheckboxConfirmTask) task).run(event.parent, event.dialogID, event.optionID, event.player, event.selects);
					}
				});
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onDialogOptionItemCheckboxCheck(ItemChooseEvent.Multi.Select event) {
		if(this.dialogs.containsKey(event.dialogID)) {
			DialogOperation operation = this.getDialogOperation(event.dialogID);
			if(operation.hasTask(event.optionID)) {
				List<IOptionTask> tasks = operation.getTask(event.optionID);
				tasks.forEach(task->{
					if(task instanceof IItemCheckboxCheckTask) {
						((IItemCheckboxCheckTask) task).run(event.parent, event.dialogID, event.optionID, event.player, event.selectIndex, event.stack, event.remove);
					}
				});
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onDialogMultiTitleChange(MultiTitleEvent.Change event) {
		if(this.dialogs.containsKey(event.dialogID)) {
			DialogOperation operation = this.getDialogOperation(event.dialogID);
			if(operation.hasTask(event.optionID)) {
				List<IOptionTask> tasks = operation.getTask(event.optionID);
				tasks.forEach(task->{
					if(task instanceof IMultiTitleChangeTask) {
						((IMultiTitleChangeTask) task).run(event.parent, event.dialogID, event.optionID, event.player, event.newTitleIndex, event.oldTitlendex);
					}
				});
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onDialogMultiTitleClose(MultiTitleEvent.Close event) {
		if(this.dialogs.containsKey(event.dialogID)) {
			DialogOperation operation = this.getDialogOperation(event.dialogID);
			if(operation.hasTask(event.optionID)) {
				List<IOptionTask> tasks = operation.getTask(event.optionID);
				tasks.forEach(task->{
					if(task instanceof IMultiTitleCloseTask) {
						((IMultiTitleCloseTask) task).run(event.parent, event.dialogID, event.optionID, event.player, event.newTitleIndex);
					}
				});
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onDialogMultiTitleToParent(MultiTitleEvent.BackParent event) {
		if(this.dialogs.containsKey(event.dialogID)) {
			DialogOperation operation = this.getDialogOperation(event.dialogID);
			if(operation.hasTask(event.optionID)) {
				List<IOptionTask> tasks = operation.getTask(event.optionID);
				tasks.forEach(task->{
					if(task instanceof IMultiTitleToParentTask) {
						((IMultiTitleToParentTask) task).run(event.dialogID, event.optionID, event.player, event.newTitleIndex, event.parent);
					}
				});
			}
		}
	}

	
	@ZenMethod
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
	@ZenMethod
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
	@ZenMethod("fromJson")
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
