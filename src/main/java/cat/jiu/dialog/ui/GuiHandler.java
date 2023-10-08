package cat.jiu.dialog.ui;

import cat.jiu.dialog.ModMain;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class GuiHandler implements IGuiHandler {
	public static final ResourceLocation dialog_texture = new ResourceLocation(ModMain.MODID, "textures/gui/dialog.png");
	public static final int DIALOG = 0;
	public GuiHandler() {
		NetworkRegistry.INSTANCE.registerGuiHandler(ModMain.MODID, this);
	}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == DIALOG) {
			return new ContainerDialog(player);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == DIALOG) {
			return new GuiDialog(Minecraft.getMinecraft().currentScreen, player);
		}
		return null;
	}
}
