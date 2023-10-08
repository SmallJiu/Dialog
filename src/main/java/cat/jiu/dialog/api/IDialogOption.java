package cat.jiu.dialog.api;

import com.google.gson.JsonObject;

import cat.jiu.core.api.element.IText;
import cat.jiu.core.util.element.Text;
import cat.jiu.dialog.element.OptionDrawUnit;
import cat.jiu.dialog.utils.dimension.DialogDimension;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("dialog.Data")
public interface IDialogOption {
	/**
	 * 获取选项ID
	 */
	ResourceLocation getTypeID();
	@ZenGetter("id")
	default String getTypeIDAsString() {
		return String.valueOf(this.getTypeID()).toLowerCase();
	}
	/**
	 * 获取选项显示的内容
	 */
	@ZenGetter("text")
	default IText getOptionText() {
		return Text.empty;
	}
	/**
	 * 是否可以关闭对话框
	 */
	@ZenGetter("close")
	boolean canCloseDialog();
	
	JsonObject writeToJson(JsonObject json);
	void readFromJson(JsonObject json);
	
	NBTTagCompound writeToNBT();
	default NBTTagCompound writeNBT() {
		NBTTagCompound nbt = this.writeToNBT();
		if(nbt==null) nbt = new NBTTagCompound();
		if(!nbt.hasKey("type")) nbt.setString("type", this.getTypeID().toString());
		return nbt;
	}
	void readFromNBT(NBTTagCompound nbt);
	
	@ZenGetter("copy")
	IDialogOption copy();
	
	@ZenMethod("draw")
	@SideOnly(Side.CLIENT)
	OptionDrawUnit getDrawUnit(ResourceLocation dialogID, int optionID, IDialogOption option, DialogDimension dialogDimension);
}
