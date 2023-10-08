package cat.jiu.dialog.utils.dimension;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenProperty;

/**
 * 绘制单元的位置与宽高信息
 * @author small_jiu
 */
@ZenRegister
@ZenClass("dialog.dimension.Option")
public class OptionDimension {
	/**
	 * 组件X轴
	 */
	@ZenProperty
	public int x, y, width, height;
	/**
	 * 如果不需要把基本信息设置为默认，请改为false
	 */
	@ZenProperty
	public boolean defaultDimension = true;
	public OptionDimension(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	@Override
	public String toString() {
		return "Dimension [x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + "]";
	}
}
