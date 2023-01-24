package cat.jiu.dialog.api;

/**
 * 绘制单元的位置与宽高信息
 * @author small_jiu
 */
public class OptionDimension {
	/**
	 * 组件X轴
	 */
	public int x;
	/**
	 * 组件Y轴
	 */
	public int y;
	/**
	 * 组件宽度
	 */
	public int width;
	/**
	 * 组件高度
	 */
	public int height;
	/**
	 * 如果不需要把基本信息设置为默认，请改为false
	 */
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
