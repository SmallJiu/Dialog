package cat.jiu.dialog.utils.dimension;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenProperty;

@ZenRegister
@ZenClass("dialog.dimension.Dialog")
public class DialogDimension {
	@ZenProperty
	public int width = 0, height = 0;
	public DialogDimension(int width, int height) {
		this.width = width;
		this.height = height;
	}
}
