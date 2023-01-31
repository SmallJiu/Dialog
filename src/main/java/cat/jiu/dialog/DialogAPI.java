package cat.jiu.dialog;

import java.util.HashMap;

import com.google.gson.JsonObject;

import cat.jiu.dialog.api.DialogBuilder;
import cat.jiu.dialog.api.DialogDimension;
import cat.jiu.dialog.element.Dialog;
import cat.jiu.dialog.element.option.DialogOptionDrawUnit;
import cat.jiu.dialog.iface.IDialogOptionDataUnit;
import cat.jiu.dialog.iface.IDialogOptionType;
import cat.jiu.dialog.net.MsgDialog;
import cat.jiu.dialog.ui.ContainerDialog;
import cat.jiu.dialog.ui.GuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DialogAPI {
	/**
	 * 展示一个对话框<p>
	 * 建议在服务端调用此方法，此方法可使玩家在服务端的对话框Gui容器({@link net.minecraft.inventory.Container})也含有对话框({@code dialog})
	 * @param player 需要展示对话框的玩家
	 * @param dialog 对话框对象
	 */
	public static void displayDialog(EntityPlayer player, DialogBuilder dialog) {
		displayDialog(player, dialog.build());
	}
	/**
	 * 展示一个对话框<p>
	 * 在服务端调用此方法时，可使玩家在服务端的对话框Gui容器({@link net.minecraft.inventory.Container})也含有对话框({@code dialog})
	 * @param player 需要展示对话框的玩家
	 * @param dialog 对话框对象
	 */
	public static void displayDialog(EntityPlayer player, Dialog dialog) {
		BlockPos pos = player.getPosition();
		player.openGui(ModMain.MODID, GuiHandler.DIALOG, player.world, pos.getX(), pos.getY(), pos.getZ());
		
		Container container = player.openContainer;
		if(container instanceof ContainerDialog) {
			((ContainerDialog) container).setDialog(dialog);
		}
		
		new Thread(()->{
			// 对网络延迟的妥协处理
			try {Thread.sleep(100);}catch(InterruptedException e) { e.printStackTrace();}
			
			if(player.world.isRemote) {
				ModMain.network.sendMessageToServer(new MsgDialog(dialog));
			}else {
				ModMain.network.sendMessageToPlayer(new MsgDialog(dialog), (EntityPlayerMP) player);
			}
		}).start();
	}
	
	private static final HashMap<ResourceLocation, IDialogOptionType> registry = new HashMap<>();
	/**
	 * 注册一个自定义的对话框选项到系统<p>
	 * 注：需要在客户端与服务端同时注册，否则一端会接收不到自定义的选项
	 * @param type
	 */
	public static void registerOption(IDialogOptionType type) {
		if(!registry.containsKey(type.getTypeID())) {
			registry.put(type.getTypeID(), type);
		}
	}
	
	public static boolean hasRegisterOption(ResourceLocation id) {
		return registry.containsKey(id);
	}
	
	public static IDialogOptionDataUnit newInstance(ResourceLocation id, NBTTagCompound nbt) throws Exception {
		if(registry.containsKey(id)) {
			return registry.get(id).getDataUnit(nbt);
		}
		ModMain.log.warn("Dialog Option is not register, option type id: {}", id);
		return null;
	}
	public static IDialogOptionDataUnit newInstance(ResourceLocation id, JsonObject json) throws Exception {
		if(registry.containsKey(id)) {
			return registry.get(id).getDataUnit(json);
		}
		ModMain.log.warn("Dialog Option is not register, option type id: {}", id);
		return null;
	}
	
	@SideOnly(Side.CLIENT)
	public static DialogOptionDrawUnit getDrawUnit(ResourceLocation id, ResourceLocation dialogID, int optionID, IDialogOptionDataUnit option, DialogDimension dim) {
		if(registry.containsKey(id)) {
			return registry.get(id).getDrawUnit(dialogID, optionID, option, dim);
		}
		ModMain.log.warn("Dialog Option is not register, option type id: {}", id);
		return null;
	}
}
