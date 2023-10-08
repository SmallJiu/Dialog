package cat.jiu.dialog.api;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public interface IBaseMessage extends IMessage {
	IMessage handler(MessageContext ctx);
}
