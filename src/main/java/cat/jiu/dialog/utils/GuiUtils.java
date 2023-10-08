package cat.jiu.dialog.utils;

import java.util.List;

import com.google.common.collect.Lists;

import cat.jiu.core.api.element.IText;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

public class GuiUtils {
	public static List<String> formatText(List<IText> texts, int maxLength, boolean useVanillaFormat) {
		int width = maxLength;
		FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
		
		if(useVanillaFormat) {
			List<String> str = Lists.newArrayList();
			for(int row = 0; row < texts.size(); row++) {
				String msg = texts.get(row).format();
				if(msg.startsWith("&il")) msg = "    " + msg.substring(3);
				
				str.addAll(fr.listFormattedStringToWidth(msg, width));
			}
			return str;
		}else {
			List<String> str = Lists.newArrayList();
			for(int row = 0; row < texts.size(); row++) {
				String msg = texts.get(row).format();
				if(msg.startsWith("&il")) msg = "    " + msg.substring(3);
				if(fr.getStringWidth(msg) >= width) {
					char[] chs = msg.toCharArray();
					StringBuilder s = new StringBuilder();
					for(int k = 0; k < chs.length; k++) {
						s.append(chs[k]);
						String formatStr = s.toString();
						if(fr.getStringWidth(formatStr) >= width) {
							str.add(formatStr);
							s.setLength(0);
						}
					}
					if(s.length() > 0) {
						str.add(s.toString());
					}
				}else {
					str.add(msg);
				}
			}
			return str;
		}
	}
	
	public static boolean isInRange(int mouseX, int mouseY, int x, int y, int width, int height) {
		return (mouseX >= x && mouseY >= y) && (mouseX <= x + width && mouseY <= y + height);
	}
	
	public static void drawHollowSquare(Gui gui, int x, int y, int width, int height, int color) {
		gui.drawHorizontalLine(x, x+width, y, color);
		gui.drawHorizontalLine(x, x+width, y+height, color);
		
		gui.drawVerticalLine(x, y, y+height, color);
		gui.drawVerticalLine(x+width, y, y+height, color);
	}
	
	public static void drawCircle(Gui gui, int x, int y, int radius, int color, boolean solid) {
		
	}
}
