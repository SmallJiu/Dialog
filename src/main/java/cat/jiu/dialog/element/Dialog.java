package cat.jiu.dialog.element;

import java.util.List;
import com.google.common.collect.Lists;

import cat.jiu.core.api.element.IText;
import cat.jiu.core.util.element.Text;
import cat.jiu.dialog.DialogAPI;
import cat.jiu.dialog.api.IDialogOption;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenSetter;
import crafttweaker.annotations.ZenRegister;

/**
 * 一个对话框，使用{@link cat.jiu.dialog.DialogAPI#displayDialog(network.minecraft.entity.player.EntityPlayer, Dialog)} 打开
 * @author small_jiu
 */
@ZenRegister
@ZenClass("dialog.Dialog")
public class Dialog {
	protected ResourceLocation id;
	protected final IText title;
	protected final List<IDialogOption> options;
	/**
	 * @param id 对话框的ID，用于识别
	 * @param text 对话框显示的内容
	 * @param options 对话框的选项
	 */
	public Dialog(ResourceLocation id, IText title) {
		this(id, title, Lists.newArrayList());
	}
	public Dialog(ResourceLocation id, IText title, List<IDialogOption> options) {
		this.id = id;
		this.title = title;
		this.options = options;
	}
	
	/**
	 * 获取对话框的选项列表
	 * @return 选项列表
	 */
	@ZenGetter("options")
	public List<IDialogOption> getOptions() {
		return options;
	}
	/**
	 * 获取对话框显示的内容
	 * @return 显示的内容
	 */
	@ZenGetter("title")
	public IText getTitle() {
		return title;
	}
	/**
	 * 获取对话框ID
	 * @return 对话框ID
	 */
	public ResourceLocation getID() {
		return this.id;
	}
	@ZenGetter("id")
	public String getIDAsStrting() {
		return String.valueOf(this.getID()).toLowerCase();
	}
	/**
	 * 设置对话框的ID
	 * @param id id
	 */
	public void setID(ResourceLocation id) {
		this.id = id;
	}
	@ZenSetter("id")
	public void setID(String id) {
		this.id = new ResourceLocation(id);
	}
	
	/**
	 * 获取未经切割换行下对话框的最长长度<P>
	 * 这取决于对话框标题与各个选项标题的最长所占长度
	 * @return Dialog Width
	 */
	@ZenGetter("width")
	@SideOnly(Side.CLIENT)
	public int getWidth() {
		int width = this.title.getStringWidth();
		for(int i = 0; i < this.options.size(); i++) {
			width = Math.max(width, this.options.get(i).getOptionText().getStringWidth());
		}
		return width;
	}
	
	@ZenGetter
	@Override
	public Dialog clone() {
		List<IDialogOption> options = Lists.newArrayList();
		for(IDialogOption option : this.options) {
			if(option!=null) {
				options.add(option.copy());
			}
		}
		return new Dialog(this.id, this.title.copy(), options);
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		if(nbt==null) nbt = new NBTTagCompound();
		
		nbt.setTag("text", this.title.writeTo(NBTTagCompound.class));
		NBTTagList options = new NBTTagList();
		for(int i = 0; i < this.options.size(); i++) {
			IDialogOption option = this.options.get(i);
			if(option!=null) options.appendTag(option.writeNBT());
		}
		nbt.setTag("options", options);
		nbt.setString("dialogID", this.id.toString());
		
		return nbt;
	}
	
	public static Dialog readFromNBT(NBTTagCompound nbt) {
		IText title = new Text(nbt.getCompoundTag("text"));
		
		List<IDialogOption> options = Lists.newArrayList();
		NBTTagList optionList = nbt.getTagList("options", 10);
		for(int i = 0; i < optionList.tagCount(); i++) {
			try {
				NBTTagCompound optionNBT = optionList.getCompoundTagAt(i);
				IDialogOption option = DialogAPI.newInstance(new ResourceLocation(optionNBT.getString("type")), optionNBT);
				
				if(option!=null) options.add(option);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return new Dialog(new ResourceLocation(nbt.getString("dialogID")), title, options);
	}
}
