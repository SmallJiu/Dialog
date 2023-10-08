package cat.jiu.dialog.element.option;

import com.google.gson.JsonObject;

import cat.jiu.core.api.ITimer;
import cat.jiu.core.api.element.IText;
import cat.jiu.core.util.timer.Timer;
import cat.jiu.dialog.api.IDialogOption;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.nbt.NBTTagCompound;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenSetter;

@ZenRegister
@ZenClass("dialog.option.Timer")
public abstract class OptionTimerBase<T extends IDialogOption> implements IDialogOption {
	protected T option;
	protected ITimer timer;
	protected boolean autoConfirm;

	protected OptionTimerBase(T instance) {
		this.option = instance;
		this.timer = new Timer(0);
	}

	protected OptionTimerBase(ITimer timer, boolean timeOutAutoConfirm, T option) {
		this.timer = timer;
		this.autoConfirm = timeOutAutoConfirm;
		this.option = option;
	}

	@Override
	public boolean canCloseDialog() {
		return this.option.canCloseDialog();
	}

	@Override
	public IText getOptionText() {
		return this.option.getOptionText();
	}
	
	@ZenGetter("timer")
	public ITimer getTimer() {
		return timer;
	}
	
	@ZenGetter("option")
	public T getOption() {
		return option;
	}
	
	@ZenGetter("auto")
	public boolean isTimeoutAutoConfirm() {
		return autoConfirm;
	}
	@ZenSetter("auto")
	public void setTimeoutAutoConfirm(boolean autoConfirm) {
		this.autoConfirm = autoConfirm;
	}

	@Override
	public JsonObject writeToJson(JsonObject json) {
		if(json == null)
			json = new JsonObject();

		json.add("timer", this.timer.writeTo(JsonObject.class));
		json.addProperty("auto", this.autoConfirm);
		json.add("value", this.option.writeToJson(new JsonObject()));

		return json;
	}

	@Override
	public void readFromJson(JsonObject json) {
		this.timer.readFrom(json.get("timer").getAsJsonObject());
		this.autoConfirm = json.has("auto") && json.get("auto").getAsBoolean();
		this.option.readFromJson(json.getAsJsonObject("value"));
	}

	@Override
	public NBTTagCompound writeToNBT() {
		NBTTagCompound nbt = new NBTTagCompound();

		nbt.setTag("timer", this.timer.writeTo(NBTTagCompound.class));
		nbt.setBoolean("auto", this.autoConfirm);
		nbt.setTag("value", this.option.writeToNBT());

		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		this.timer.readFrom(nbt.getTag("timer"));
		this.autoConfirm = nbt.hasKey("auto") && nbt.getBoolean("auto");
		this.option.readFromNBT(nbt.getCompoundTag("value"));
	}
}
