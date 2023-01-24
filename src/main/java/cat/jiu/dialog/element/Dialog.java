package cat.jiu.dialog.element;

import java.util.List;

import com.google.common.collect.Lists;

import cat.jiu.dialog.DialogAPI;
import cat.jiu.dialog.iface.IDialogOptionDataUnit;
import cat.jiu.dialog.iface.IDialogText;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 一个对话框，使用{@link cat.jiu.dialog.DialogAPI#displayDialog(network.minecraft.entity.player.EntityPlayer, Dialog)} 打开
 * @author small_jiu
 */
public class Dialog {
	protected ResourceLocation id;
	protected final IDialogText title;
	protected final List<IDialogOptionDataUnit> options;
	/**
	 * @param id 对话框的ID，用于识别
	 * @param text 对话框显示的内容
	 * @param options 对话框的选项
	 */
	public Dialog(ResourceLocation id, IDialogText title, List<IDialogOptionDataUnit> options) {
		this.id = id;
		this.title = title;
		this.options = options;
	}
	
	/**
	 * 获取对话框的选项列表
	 * @return 选项列表
	 */
	public List<IDialogOptionDataUnit> getOptions() {
		return options;
	}
	/**
	 * 获取对话框显示的内容
	 * @return 显示的内容
	 */
	public IDialogText getTitle() {
		return title;
	}
	/**
	 * 获取对话框ID
	 * @return 对话框ID
	 */
	public ResourceLocation getID() {
		return this.id;
	}
	/**
	 * 设置对话框的ID
	 * @param id id
	 */
	public void setID(ResourceLocation id) {
		this.id = id;
	}
	
	/**
	 * 获取未经切割换行下对话框的最长长度<P>
	 * 这取决于对话框标题与各个选项标题的最长所占长度
	 * @return Dialog Width
	 */
	@SideOnly(Side.CLIENT)
	public int getWidth() {
		FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
		int width = this.title.getStringWidth(fr);
		for(int i = 0; i < this.options.size(); i++) {
			width = Math.max(width, this.options.get(i).getOptionText().getStringWidth(fr));
		}
		return width;
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		if(nbt==null) nbt = new NBTTagCompound();
		
		nbt.setTag("text", this.title.writeToNBT());
		NBTTagList options = new NBTTagList();
		for(int i = 0; i < this.options.size(); i++) {
			IDialogOptionDataUnit option = this.options.get(i);
			if(option!=null) options.appendTag(option.writeNBT());
		}
		nbt.setTag("options", options);
		nbt.setString("dialogID", this.id.toString());
		
		return nbt;
	}
	
	public static Dialog readFromNBT(NBTTagCompound nbt) {
		IDialogText title = DialogText.get(nbt.getTag("text"));
		
		List<IDialogOptionDataUnit> options = Lists.newArrayList();
		NBTTagList optionList = nbt.getTagList("options", 10);
		for(int i = 0; i < optionList.tagCount(); i++) {
			try {
				NBTTagCompound optionNBT = optionList.getCompoundTagAt(i);
				IDialogOptionDataUnit option = DialogAPI.newInstance(new ResourceLocation(optionNBT.getString("type")), optionNBT);
				
				if(option!=null) options.add(option);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return new Dialog(new ResourceLocation(nbt.getString("dialogID")), title, options);
	}
}
