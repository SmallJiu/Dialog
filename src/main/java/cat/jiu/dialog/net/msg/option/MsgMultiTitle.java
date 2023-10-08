package cat.jiu.dialog.net.msg.option;

import java.util.List;

import com.google.common.collect.Lists;

import cat.jiu.core.util.mc.SimpleNBTTagList;
import cat.jiu.dialog.event.MultiTitleEvent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public abstract class MsgMultiTitle extends OptionMessage {
	protected List<String> titles;
	protected int titleIndex;
	public MsgMultiTitle() {}
	protected MsgMultiTitle(ResourceLocation parent, ResourceLocation dialogID, int optionID, List<String> titles, int titleIndex) {
		super(parent, dialogID, optionID);
		this.titles = titles;
		this.titleIndex = titleIndex;
	}
	
	@Override
	protected NBTTagCompound writeTo(NBTTagCompound nbt) {
		if(nbt==null) nbt = new NBTTagCompound();
		
		SimpleNBTTagList titles = new SimpleNBTTagList();
		for(int i = 0; i < this.titles.size(); i++) {
			titles.append(this.titles.get(i));
		}
		nbt.setTag("titles", titles);
		
		nbt.setInteger("index", this.titleIndex);
		
		return nbt;
	}
	
	@Override
	protected void readFrom(NBTTagCompound nbt) {
		this.titles = Lists.newArrayList();
		NBTTagList titles = nbt.getTagList("titles", 8);
		for(int i = 0; i < titles.tagCount(); i++) {
			this.titles.add(titles.getStringTagAt(i));
		}
		this.titleIndex = nbt.getInteger("index");
	}
	
	public static class Change extends MsgMultiTitle {
		protected int old;
		public Change() {}
		public Change(ResourceLocation parent, ResourceLocation dialogID, int optionID, List<String> titles, int newTitle, int oldTitle) {
			super(parent, dialogID, optionID, titles, newTitle);
			this.old = oldTitle;
		}
		@Override
		protected void readFrom(NBTTagCompound nbt) {
			super.readFrom(nbt);
			this.old = nbt.getInteger("old");
		}
		@Override
		protected NBTTagCompound writeTo(NBTTagCompound nbt) {
			nbt = super.writeTo(nbt);
			nbt.setInteger("old", this.old);
			return nbt;
		}

		@Override
		public IMessage handler(MessageContext ctx) {
			if(ctx.side.isServer()) {
				MinecraftForge.EVENT_BUS.post(new MultiTitleEvent.Change(ctx.getServerHandler().player, parent, super.dialogID, super.optionID, super.titles, super.titleIndex, this.old));
			}
			return null;
		}
	}
	
	public static class Close extends MsgMultiTitle {
		public Close() {}
		public Close(ResourceLocation parent, ResourceLocation dialogID, int optionID, List<String> titles, int titleIndex) {
			super(parent, dialogID, optionID, titles, titleIndex);
		}

		@Override
		public IMessage handler(MessageContext ctx) {
			if(ctx.side.isServer()) {
				MinecraftForge.EVENT_BUS.post(new MultiTitleEvent.Close(ctx.getServerHandler().player, parent, super.dialogID, super.optionID, super.titles, super.titleIndex));
			}
			return null;
		}
	}
	
	public static class BackParent extends MsgMultiTitle {
		public BackParent() {}
		public BackParent(ResourceLocation dialogID, int optionID, List<String> titles, int newTitle, ResourceLocation parent) {
			super(parent, dialogID, optionID, titles, newTitle);
		}

		@Override
		public IMessage handler(MessageContext ctx) {
			if(ctx.side.isServer()) {
				MinecraftForge.EVENT_BUS.post(new MultiTitleEvent.BackParent(ctx.getServerHandler().player, super.dialogID, super.optionID, super.titles, super.titleIndex, this.parent));
			}
			return null;
		}
	}
}
