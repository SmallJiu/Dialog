package cat.jiu.dialog.test;

import com.google.gson.JsonObject;

import cat.jiu.core.api.element.IText;
import cat.jiu.core.util.element.Text;
import cat.jiu.dialog.api.IDialogOption;
import cat.jiu.dialog.element.OptionDrawUnit;
import cat.jiu.dialog.utils.dimension.DialogDimension;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TestDataUnit implements IDialogOption {
	@Override
	public IText getOptionText() {
		return new Text("dialog.text.custom_option").setCenter(false);
	}
	
	protected static final NBTTagCompound nbt = new NBTTagCompound();
	@Override
	public NBTTagCompound writeToNBT() {
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {}
	@Override
	public JsonObject writeToJson(JsonObject json) {
		if(json==null) json = new JsonObject();
		json.addProperty("text", "dialog.text.custom_option");
		return json;
	}
	
	@Override
	public void readFromJson(JsonObject json) { }

	@Override
	public ResourceLocation getTypeID() {
		return TestDialog.getTestOption().getTypeID();
	}

	@Override
	public boolean canCloseDialog() {
		return false;
	}
	@Override
	public TestDataUnit copy() {
		return new TestDataUnit();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public OptionDrawUnit getDrawUnit(ResourceLocation dialogID, int optionID, IDialogOption option, DialogDimension dialogDimension) {
		return new TestDrawUnit(dialogID, option, optionID, dialogDimension);
	}
}
