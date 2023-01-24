package cat.jiu.dialog.iface;

import com.google.gson.JsonObject;

import cat.jiu.dialog.api.DialogDimension;
import cat.jiu.dialog.element.option.DialogOptionDrawUnit;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IDialogOptionType {
	/**
	 * 获取选项的ID<p>
	 * @see cat.jiu.dialog.iface.IDialogOptionDataUnit#getTypeID()
	 * @return 选项的类型ID
	 */
	ResourceLocation getTypeID();
	
	/**
	 * 从NBT反序列化为一个选项数据单元
	 * @param nbt NBT
	 * @return
	 * @throws Exception
	 */
	IDialogOptionDataUnit getDataUnit(NBTTagCompound nbt) throws Exception;
	/**
	 * 从Json反序列化为一个选项数据单元
	 * @param json JsonObJect
	 * @return
	 * @throws Exception
	 */
	IDialogOptionDataUnit getDataUnit(JsonObject json) throws Exception;
	
	/**
	 * 获取对话框选项的gui绘制单元
	 * @param dialogID 对话框ID
	 * @param optionID 选项ID
	 * @param dialogDimension
	 * @param drawUnit 选项对象
	 * @return
	 */
	@SideOnly(Side.CLIENT)
	DialogOptionDrawUnit getDrawUnit(ResourceLocation dialogID, int optionID, IDialogOptionDataUnit option, DialogDimension dialogDimension);
}
