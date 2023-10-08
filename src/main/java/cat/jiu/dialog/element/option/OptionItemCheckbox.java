package cat.jiu.dialog.element.option;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import cat.jiu.core.util.mc.SimpleNBTTagList;
import cat.jiu.dialog.api.IDialogOption;
import cat.jiu.dialog.element.DialogOption;
import cat.jiu.dialog.element.OptionDrawUnit;
import cat.jiu.dialog.element.option.draw.OptionItemCheckboxDrawUnit;
import cat.jiu.dialog.utils.JsonToStackUtil;
import cat.jiu.dialog.utils.dimension.DialogDimension;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenSetter;

@ZenRegister
@ZenClass("dialog.option.ItemCheckbox")
public class OptionItemCheckbox implements IDialogOption {
	protected List<ItemStack> stacks;
	protected boolean canCloseDialog;
	protected int[] defaultSelect;
	protected int column;

	public OptionItemCheckbox() {}

	/**
	 * Recommended for single option use
	 * 
	 * @param canCloseDialog
	 *            true if choose one to close dialog
	 * @param stacks
	 *            items
	 * @param defaultSelect
	 * 
	 * @param column
	 *            columns num, range 1 ~ 4
	 */
	public OptionItemCheckbox(boolean canCloseDialog, int column, int[] defaultSelect, ItemStack... stacks) {
		this(canCloseDialog, column, defaultSelect, Arrays.asList(stacks));
	}

	/**
	 * Recommended for single option use
	 * 
	 * @param canCloseDialog
	 *            true if choose one to close dialog
	 * @param stacks
	 *            items
	 * @param defaultSelect
	 * 
	 * @param column
	 *            columns num, range 1 ~ 4
	 */
	public OptionItemCheckbox(boolean canCloseDialog, int column, int[] defaultSelect, List<ItemStack> stacks) {
		this.stacks = stacks;
		this.canCloseDialog = canCloseDialog;
		this.defaultSelect = defaultSelect == null ? OptionCheckbox.EMPTY_DEFAULT_SELECT : defaultSelect;
		this.column = Math.min(Math.max(column, 1), 4);
	}

	@Override
	public ResourceLocation getTypeID() {
		return DialogOption.item_checkbox().getTypeID();
	}
	
	@ZenMethod("default")
	public int[] getDefaultSelect() {
		return defaultSelect!=null ? defaultSelect : OptionCheckbox.EMPTY_DEFAULT_SELECT;
	}

	@ZenGetter("column")
	public int getColumn() {
		return column;
	}

	@ZenSetter("column")
	public void setColumn(int column) {
		this.column = column;
	}

	@ZenMethod("stacks")
	public List<ItemStack> getStacks() {
		return stacks == null ? Collections.emptyList() : stacks;
	}

	@Override
	public boolean canCloseDialog() {
		return this.canCloseDialog;
	}

	@Override
	public JsonObject writeToJson(JsonObject json) {
		if(json == null)
			json = new JsonObject();

		json.add("stacks", JsonToStackUtil.toJsonObject(this.stacks, false));
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
		if(json.has("stacks")) {
			this.stacks = JsonToStackUtil.toStacks(json.get("stacks"));
		}

		if(json.has("default") && json.get("default").isJsonArray()) {
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

		if(!this.stacks.isEmpty()) {
			SimpleNBTTagList stacks = new SimpleNBTTagList();
			for(int i = 0; i < this.stacks.size(); i++) {
				stacks.append(this.stacks.get(i).serializeNBT());
			}
			nbt.setTag("stacks", stacks);
		}
		if(this.defaultSelect.length > 0) {
			SimpleNBTTagList defaultSelect = new SimpleNBTTagList();
			for(int i = 0; i < this.defaultSelect.length; i++) {
				defaultSelect.append(this.defaultSelect[i]);
			}
			nbt.setTag("default", defaultSelect);
		}
		nbt.setBoolean("close", this.canCloseDialog);
		nbt.setInteger("column", this.column);

		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		if(nbt.hasKey("stacks")) {
			this.stacks = Lists.newArrayList();
			NBTTagList stacks = nbt.getTagList("stacks", 10);
			for(int i = 0; i < stacks.tagCount(); i++) {
				this.stacks.add(new ItemStack(stacks.getCompoundTagAt(i)));
			}
		}

		if(nbt.hasKey("default")) {
			NBTTagList defaultSelect = nbt.getTagList("default", 3);
			this.defaultSelect = new int[defaultSelect.tagCount()];
			for(int i = 0; i < this.defaultSelect.length; i++) {
				this.defaultSelect[i] = defaultSelect.getIntAt(i);
			}
		}
		this.canCloseDialog = nbt.hasKey("close") ? nbt.getBoolean("close") : true;
		this.column = nbt.hasKey("column") ? nbt.getInteger("column") : 2;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public OptionDrawUnit getDrawUnit(ResourceLocation dialogID, int optionID, IDialogOption option, DialogDimension dialogDimension) {
		return new OptionItemCheckboxDrawUnit(dialogID, (OptionItemCheckbox) option, optionID, dialogDimension);
	}

	@Override
	public OptionItemCheckbox copy() {
		List<ItemStack> options = this.getStacks().stream().map(option -> option.copy()).collect(Collectors.toList());
		int[] defaultSleect = new int[this.getDefaultSelect().length];
		for(int i = 0; i < this.getDefaultSelect().length; i++) {
			defaultSleect[i] = this.getDefaultSelect()[i];
		}
		return new OptionItemCheckbox(this.canCloseDialog(), this.getColumn(), defaultSleect, options);
	}
}
