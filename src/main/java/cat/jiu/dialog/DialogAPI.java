package cat.jiu.dialog;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;

import cat.jiu.dialog.api.IDialogOption;
import cat.jiu.dialog.api.IDialogOptionType;
import cat.jiu.dialog.api.IOptionTask;
import cat.jiu.dialog.api.helper.DialogList;
import cat.jiu.dialog.element.Dialog;
import cat.jiu.dialog.element.OptionDrawUnit;
import cat.jiu.dialog.net.msg.MsgDialog;
import cat.jiu.dialog.ui.ContainerDialog;
import cat.jiu.dialog.ui.GuiHandler;
import cat.jiu.dialog.utils.DialogBuilder;
import cat.jiu.dialog.utils.dimension.DialogDimension;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("dialog.API")
public class DialogAPI {
	/**
	 * 展示一个对话框<p>
	 * 建议在服务端调用此方法，此方法可使玩家在服务端的对话框Gui容器({@link net.minecraft.inventory.Container})也含有对话框({@code dialog})
	 * @param player 需要展示对话框的玩家
	 * @param dialog 对话框对象
	 */
	@ZenMethod("display")
	public static void displayDialog(EntityPlayer player, DialogBuilder dialog) {
		displayDialog(player, dialog.build());
	}
	/**
	 * 展示一个对话框<p>
	 * 在服务端调用此方法时，可使玩家在服务端的对话框Gui容器({@link ContainerDialog})也含有对话框({@code dialog})
	 * @param player 需要展示对话框的玩家
	 * @param dialog 对话框对象
	 */
	@ZenMethod("display")
	public static void displayDialog(EntityPlayer player, Dialog dialog) {
		BlockPos pos = player.getPosition();
		player.openGui(ModMain.MODID, GuiHandler.DIALOG, player.world, pos.getX(), pos.getY(), pos.getZ());
		
		Container container = player.openContainer;
		if(container instanceof ContainerDialog) {
			((ContainerDialog) container).setDialog(dialog);
		}
		
		new Thread(()->{
			// 对网络延迟的妥协处理
			try {Thread.sleep(100);}catch(InterruptedException e) {}
			
			if(player.world.isRemote) {
				ModMain.NETWORK.sendMessageToServer(new MsgDialog(dialog));
			}else {
				ModMain.NETWORK.sendMessageToPlayer(new MsgDialog(dialog), (EntityPlayerMP) player);
			}
		}).start();
	}
	
	private static final HashMap<ResourceLocation, IDialogOptionType> registry = new HashMap<>();
	/**
	 * 注册一个自定义的对话框选项到系统<p>
	 * 注：需要在客户端与服务端同时注册，否则一端会接收不到自定义的选项
	 * 
	 * @param type
	 * @see {@link cat.jiu.dialog.element.DialogOption}
	 */
	public static void registerOption(IDialogOptionType type) {
		if(!hasRegisterOption(type.getTypeID())) {
			registry.put(type.getTypeID(), type);
			ModMain.log.info("Register Option: {} success.", type.getTypeID());
		}
	}
	
	public static boolean hasRegisterOption(ResourceLocation id) {
		return registry.containsKey(id);
	}
	
	public static <T extends IDialogOption> T newInstance(ResourceLocation id, NBTTagCompound nbt) throws Exception {
		if(hasRegisterOption(id)) {
			return registry.get(id).getDataUnit(nbt);
		}
		ModMain.log.error("Dialog Option is not register, option type id: {}", id);
		return null;
	}
	public static <T extends IDialogOption> T newInstance(ResourceLocation id, JsonObject json) throws Exception {
		if(hasRegisterOption(id)) {
			return registry.get(id).getDataUnit(json);
		}
		ModMain.log.error("Dialog Option is not register, option type id: {}", id);
		return null;
	}
	
	@SideOnly(Side.CLIENT)
	public static OptionDrawUnit getDrawUnit(ResourceLocation id, ResourceLocation dialogID, int optionID, IDialogOption option, DialogDimension dim) {
		if(hasRegisterOption(id)) {
			return option.getDrawUnit(dialogID, optionID, option, dim);
		}
		ModMain.log.error("Dialog Option is not register, option type id: {}", id);
		return null;
	}
	
	static final Map<Class<? extends IOptionTask>, IOptionTaskSerializable> SerializableRegistry = Maps.newHashMap();
	
	public static void registerTask(Class<? extends IOptionTask> clazz, IOptionTaskSerializable serializable) {
		SerializableRegistry.put(clazz, serializable);
	}
	
	public static IOptionTask getTask(Class<? extends IOptionTask> clazz, DialogList list, JsonObject data) {
		if(SerializableRegistry.containsKey(clazz)) {
			return SerializableRegistry.get(clazz).read(list, data);
		}
		return null;
	}
	
	@FunctionalInterface
	public static interface IOptionTaskSerializable {
		IOptionTask read(DialogList list, JsonObject data);
	}
}
