package cat.jiu.dialog.api;

import com.google.gson.JsonObject;

import crafttweaker.annotations.ZenRegister;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("dialog.Type")
public interface IDialogOptionType {
	/**
	 * 获取选项的ID<p>
	 * @see cat.jiu.dialog.api.IDialogOption#getTypeID()
	 * @return 选项的类型ID
	 */
	ResourceLocation getTypeID();
	@ZenGetter("id")
	default String getTypeIDAsString() {
		return String.valueOf(this.getTypeID()).toLowerCase();
	}
	
	/**
	 * 从NBT反序列化为一个选项数据单元
	 * @param nbt NBT
	 * @return
	 * @throws Exception
	 */
	@ZenMethod
	<T extends IDialogOption> T getDataUnit(NBTTagCompound nbt) throws Exception;
	/**
	 * 从Json反序列化为一个选项数据单元
	 * @param json JsonObJect
	 * @return
	 * @throws Exception
	 */
	@ZenMethod
	<T extends IDialogOption> T getDataUnit(JsonObject json) throws Exception;
}
