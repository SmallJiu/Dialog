package cat.jiu.dialog.utils.dimension;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenProperty;

@ZenRegister
@ZenClass("dialog.dimension.Range")
public class Range {
	@ZenProperty
	public int x, y, width, height = 0;
	public Range() {}
	public Range(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	@ZenMethod
	public Range setRange(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		return this;
	}
	@ZenMethod
	public Range setPos(int x, int y) {
		this.x = x;
		this.y = y;
		return this;
	}
	@ZenMethod
	public Range setDimension(int width, int height) {
		this.width = width;
		this.height = height;
		return this;
	}
	@ZenMethod
	public boolean isInRange(int mouseX, int mouseY) {
		return (mouseX >= this.x && mouseY >= this.y) && (mouseX <= this.x + this.width && mouseY <= this.y + this.height);
	}
}
