package cat.jiu.dialog.event;

import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * 用于主动向对话框发送模拟的玩家各种输入行为的事件<p>
 * 该类事件均会被对话框接收并处理
 * @author small_jiu
 */
public class DialogInputEvent extends Event {
	/**
	 * 选项ID
	 */
	public final int optionID;
	public DialogInputEvent(int optionID) {
		this.optionID = optionID;
	}
	/**
	 * 用于主动向对话框发送模拟的玩家的鼠标输入行为
	 * @author small_jiu
	 */
	public static class MouseInput extends DialogInputEvent {
		/**
		 * 鼠标按键ID
		 */
		public final int mouseButton;
		/**
		 * 
		 * @param optionID 选项ID
		 * @param mouseButton 鼠标按键ID
		 */
		public MouseInput(int optionID, int mouseButton) {
			super(optionID);
			this.mouseButton = mouseButton;
		}
		/**
		 * 用于主动向对话框发送模拟的玩家鼠标点击事件
		 * @see cat.jiu.dialog.ui.GuiDialog#receiveMouseClicked(cat.jiu.dialog.event.DialogInputEvent.MouseInput.Clicked)
		 * @author small_jiu
		 */
		public static class Clicked extends MouseInput {
			/** 鼠标点击选项时的X轴 */
			public final int x;
			/** 鼠标点击选项时的Y轴 */
			public final int y;
			/**
			 * 鼠标将点击选项的X:5,Y:5位置
			 * @param optionID 选项ID
			 * @param mouseButton 鼠标按键ID
			 */
			public Clicked(int optionID, int mouseButton) {
				this(optionID, mouseButton, 5, 5);
			}
			/**
			 * @param optionID 选项ID
			 * @param mouseButton 鼠标按键ID
			 * @param x 鼠标点击选项时的X轴, 将从选项的左上角(0,0)开始
			 * @param y 鼠标点击选项时的Y轴, 将从选项的左上角(0,0)开始
			 */
			public Clicked(int optionID, int mouseButton, int x, int y) {
				super(optionID, mouseButton);
				this.x = x;
				this.y = y;
			}
		}
		/**
		 * 用于主动向对话框发送模拟的玩家鼠标长按并移动事件
		 * @see cat.jiu.dialog.ui.GuiDialog#receiveMouseMove(cat.jiu.dialog.event.DialogInputEvent.MouseInput.Move)
		 * @author small_jiu
		 */
		public static class Move extends MouseInput {
			/** 鼠标点击时长 */
			public final long timeSinceLastClick;
			/**
			 * 鼠标将按住1000毫秒 (1秒)
			 * @param optionID 选项ID
			 * @param mouseButton 鼠标按键ID
			 */
			public Move(int optionID, int mouseButton) {
				this(optionID, mouseButton, 1000);
			}
			/**
			 * 
			 * @param optionID 选项ID
			 * @param mouseButton 鼠标按键ID
			 * @param timeSinceLastClick 鼠标点击时长
			 */
			public Move(int optionID, int mouseButton, long timeSinceLastClick) {
				super(optionID, mouseButton);
				this.timeSinceLastClick = timeSinceLastClick;
			}
		}
	}
	
	/**
	 * 用于主动向对话框发送模拟的玩家敲击键盘事件
	 * @see cat.jiu.dialog.ui.GuiDialog#receiveKeyTyped(cat.jiu.dialog.event.DialogInputEvent.KeyboardInput)
	 * @author small_jiu
	 */
	public static class KeyboardInput extends DialogInputEvent {
		/**
		 * 输入的字符
		 */
		public final char typedChar;
		/**
		 * 键盘按键ID
		 */
		public final int keyCode;
		/**
		 * @param optionID 选项ID
		 * @param typedChar 输入的字符
		 * @param keyCode 键盘按键ID
		 */
		public KeyboardInput(int optionID, char typedChar, int keyCode) {
			super(optionID);
			this.typedChar = typedChar;
			this.keyCode = keyCode;
		}
	}
}
