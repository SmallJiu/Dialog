package cat.jiu.dialog.net;

import cat.jiu.dialog.ModMain;

import net.minecraft.entity.player.EntityPlayerMP;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class DialogNetworkHandler {
	private SimpleNetworkWrapper channel;
	private static int ID = 0;
	private static int nextID() {
		return ID++;
	}
	
	public DialogNetworkHandler() {
		this.channel = NetworkRegistry.INSTANCE.newSimpleChannel(ModMain.MODID);
		
		this.channel.registerMessage(MsgDialog::handler, MsgDialog.class, nextID(), Side.SERVER);
		this.channel.registerMessage(MsgOptionEvent::handler, MsgOptionEvent.class, nextID(), Side.SERVER);
		
		this.channel.registerMessage(MsgDialog::handler, MsgDialog.class, nextID(), Side.CLIENT);
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
