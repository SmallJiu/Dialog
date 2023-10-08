package cat.jiu.dialog.api;

import com.google.gson.JsonObject;

import cat.jiu.core.api.handler.IJsonSerializable;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;

@ZenRegister
@ZenClass("dialog.option.Task")
public interface IOptionTask extends IJsonSerializable {
	@Override
	default void read(JsonObject json) {}
	@Override
	default JsonObject write(JsonObject json) {
		return json;
	}
}
