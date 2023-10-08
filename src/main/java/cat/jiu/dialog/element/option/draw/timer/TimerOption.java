package cat.jiu.dialog.element.option.draw.timer;

import cat.jiu.core.api.ITimer;
import cat.jiu.core.util.timer.MillisTimer;
import cat.jiu.dialog.ui.GuiDialog;
import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TimerOption {
	@SideOnly(Side.CLIENT)
	public static void startTimer(ITimer timer, GuiDialog gui, boolean autoConfirm, GuiButton confirm) {
		new Thread(()->{
			try {Thread.sleep(1000);}catch(Exception e) {}
			
			boolean startd = false;
			if(timer instanceof MillisTimer) {
				timer.start();
				startd = true;
			}
			while(!gui.isClose()) {
				try {
					Thread.sleep(50);
					if(timer.isDone()) break;
					if(!startd) timer.update();
				}catch(InterruptedException e) {}
			}
			if(autoConfirm) {
				confirm.mouseReleased(0, 0);
			}
		}, "Timer Option Check Thread").start();
	}
}
