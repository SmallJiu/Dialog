package cat.jiu.dialog.element.option;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import cat.jiu.core.api.element.IText;
import cat.jiu.core.util.element.Text;
import cat.jiu.dialog.api.IDialogOption;
import cat.jiu.dialog.element.DialogOption;
import cat.jiu.dialog.element.OptionDrawUnit;
import cat.jiu.dialog.element.option.draw.OptionCheckboxDrawUnit;
import cat.jiu.dialog.utils.dimension.DialogDimension;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenSetter;

@ZenRegister
@ZenClass("dialog.option.Checkbox")
public class OptionCheckbox implements IDialogOption {
	public static final int[] EMPTY_DEFAULT_SELECT = new int[0];
	protected boolean canCloseDialog;
	protected List<IText> options;
	protected int[] defaultSelect;
	protected int column;

	public OptionCheckbox() {}

	/**
	 * Recommended for single option use
	 * 
	 * @param canCloseDialog
	 *            true if choose one to close dialog
	 * @param options
	 *            options
	 * @param column
	 *            columns num
	 */
	public OptionCheckbox(boolean canCloseDialog, int column, int[] defaultSelect, List<IText> options) {
		this.canCloseDialog = canCloseDialog;
		this.options = options;
		this.defaultSelect = defaultSelect == null ? new int[0] : defaultSelect;
		this.column = column;
	}

	public OptionCheckbox(boolean canCloseDialog, int column, int[] defaultSelect, IText... options) {
		this(canCloseDialog, column, defaultSelect, Arrays.asList(options));
	}

	@Override
	public ResourceLocation getTypeID() {
		return DialogOption.checkbox().getTypeID();
	}

	@Override
	public boolean canCloseDialog() {
		return this.canCloseDialog;
	}

	@ZenMethod
	public List<IText> getOptions() {
		return options == null ? Collections.emptyList() : options;
	}
	@ZenMethod
	@SideOnly(Side.CLIENT)
	public List<String> getOptionsAsString() {
		return options == null ? Collections.emptyList() : options.stream().map(e->e.format()).collect(Collectors.toList());
	}

	@ZenGetter("column")
	public int getColumn() {
		return column;
	}
	
	@ZenSetter("column")
	public void setColumn(int column) {
		this.column = column;
	}
	
	@ZenMethod("default")
	public int[] getDefaultSelect() {
		return defaultSelect;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public OptionDrawUnit getDrawUnit(ResourceLocation dialogID, int optionID, IDialogOption option, DialogDimension dialogDimension) {
		return new OptionCheckboxDrawUnit(dialogID, (OptionCheckbox) option, optionID, dialogDimension);
	}

	@Override
	public JsonObject writeToJson(JsonObject json) {
		if(json == null) {
			json = new JsonObject();
		}
		JsonArray options = new JsonArray();
		for(int i = 0; i < this.options.size(); i++) {
			options.add(this.options.get(i).writeTo(JsonObject.class));
		}
		json.add("options", options);

		if(this.defaultSelect.length > 0) {
			JsonArray defaultSelect = new JsonArray();
			for(int i = 0; i < this.defaultSelect.length; i++) {
				defaultSelect.add(this.defaultSelect[i]);
			}
			json.add("default", defaultSelect);
		}

		json.addProperty("close", this.canCloseDialog);
		json.addProperty("column", this.column);
		return json;
	}

	@Override
	public void readFromJson(JsonObject json) {
		if(json.has("options")) {
			this.options = Lists.newArrayList();
			JsonArray options = json.getAsJsonArray("options");
			for(int i = 0; i < options.size(); i++) {
				this.options.add(new Text(options.get(i).getAsJsonObject()));
			}
		}

		if(json.has("default")) {
			JsonArray defaultSelect = json.getAsJsonArray("default");
			this.defaultSelect = new int[defaultSelect.size()];
			for(int i = 0; i < this.defaultSelect.length; i++) {
				this.defaultSelect[i] = defaultSelect.get(i).getAsInt();
			}
		}

		this.canCloseDialog = json.has("close") ? json.get("close").getAsBoolean() : true;
		this.column = json.has("column") ? json.get("column").getAsInt() : 2;
	}

	@Override
	public NBTTagCompound writeToNBT() {
		NBTTagCompound nbt = new NBTTagCompound();

		NBTTagList options = new NBTTagList();
		for(int i = 0; i < this.options.size(); i++) {
			options.appendTag(this.options.get(i).writeTo(NBTTagCompound.class));
		}
		nbt.setTag("options", options);

		if(this.defaultSelect.length > 0) {
			NBTTagList defaultSelect = new NBTTagList();
			for(int i = 0; i < this.defaultSelect.length; i++) {
				defaultSelect.appendTag(new NBTTagInt(this.defaultSelect[i]));
			}
			nbt.setTag("default", defaultSelect);
		}

		nbt.setBoolean("close", this.canCloseDialog);
		nbt.setInteger("column", this.column);

		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		if(nbt.hasKey("options")) {
			NBTTagList options = nbt.getTagList("options", 10);
			this.options = Lists.newArrayList();
			for(int i = 0; i < options.tagCount(); i++) {
				this.options.add(new Text(options.getCompoundTagAt(i)));
			}
		}
		if(nbt.hasKey("default")) {
			NBTTagList defaultSelect = nbt.getTagList("default", 3);
			this.defaultSelect = new int[defaultSelect.tagCount()];
			for(int i = 0; i < this.defaultSelect.length; i++) {
				this.defaultSelect[i] = ((NBTTagInt) defaultSelect.get(i)).getInt();
			}
		}
		this.canCloseDialog = nbt.hasKey("close") ? nbt.getBoolean("close") : true;
		this.column = nbt.hasKey("column") ? nbt.getInteger("column") : 2;
	}

	@Override
	public OptionCheckbox copy() {
		List<IText> options = this.getOptions().stream().map(option -> option.copy()).collect(Collectors.toList());
		int[] defaultSleect = new int[this.getDefaultSelect().length];
		for(int i = 0; i < this.getDefaultSelect().length; i++) {
			defaultSleect[i] = this.getDefaultSelect()[i];
		}
		return new OptionCheckbox(this.canCloseDialog(), this.getColumn(), defaultSleect, options);
	}
}
