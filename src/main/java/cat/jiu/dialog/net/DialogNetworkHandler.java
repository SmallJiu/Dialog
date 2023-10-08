package cat.jiu.dialog.net;

import cat.jiu.dialog.ModMain;
import cat.jiu.dialog.api.IBaseMessage;
import cat.jiu.dialog.net.msg.*;
import cat.jiu.dialog.net.msg.option.*;
import net.minecraft.entity.player.EntityPlayerMP;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class DialogNetworkHandler {
	private static SimpleNetworkWrapper channel;
	private static int ID = 0;
	private static int nextID() {
		return ID++;
	}
	
	public DialogNetworkHandler() {
		if(channel==null) {
			ID=0;
			channel = NetworkRegistry.INSTANCE.newSimpleChannel(ModMain.MODID);
			this.register(MsgDialog.class, Side.SERVER);
			this.register(MsgDialog.class, Side.CLIENT);
			this.register(MsgDialogEvent.class, Side.SERVER);
			
			this.register(MsgButtonClick.class, Side.SERVER);
			this.register(MsgTextConfirm.class, Side.SERVER);
			
			this.register(MsgCheckboxConfirm.class, Side.SERVER);
			this.register(MsgCheckboxCheck.class, Side.SERVER);
			
			this.register(MsgRadioButtom.class, Side.SERVER);
			
			this.register(MsgItemRadioButtonChoose.class, Side.SERVER);
			
			this.register(MsgItemCheckboxConfirm.class, Side.SERVER);
			this.register(MsgItemCheckboxSelect.class, Side.SERVER);
			
			this.register(MsgMultiTitle.Change.class, Side.SERVER);
			this.register(MsgMultiTitle.Close.class, Side.SERVER);
			this.register(MsgMultiTitle.BackParent.class, Side.SERVER);
		}
	}
	
	private <T extends IBaseMessage> void register(Class<T> clazz, Side sendTo) {
		channel.registerMessage(T::handler, clazz, nextID(), sendTo);
	}
	
	/** server to client */
	public void sendMessageToPlayer(IMessage msg, EntityPlayerMP player) {
		channel.sendTo(msg, player);
	}
	
	/** client to server */
	public void sendMessageToServer(IMessage msg) {
		channel.sendToServer(msg);
	}
}
