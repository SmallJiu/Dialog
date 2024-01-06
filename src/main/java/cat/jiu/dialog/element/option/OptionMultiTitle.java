package cat.jiu.dialog.element.option;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import cat.jiu.core.api.element.IText;
import cat.jiu.core.util.element.Text;
import cat.jiu.core.util.mc.SimpleNBTTagList;
import cat.jiu.dialog.api.IDialogOption;
import cat.jiu.dialog.element.DialogOption;
import cat.jiu.dialog.element.OptionDrawUnit;
import cat.jiu.dialog.element.option.draw.OptionMultiTitleDrawUnit;
import cat.jiu.dialog.utils.dimension.DialogDimension;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenProperty;

/**
 * 用于多段剧情展示，可替代需要打开多个对话框来展示不同的title，但相同的'确定'按钮的情况
 * @author small_jiu
 */
@ZenRegister
@ZenClass("dialog.option.MultiTitle")
public class OptionMultiTitle extends OptionButton implements IDialogOption {
	@ZenProperty(getter = "continue")
	public static final IText CONTINUE = new Text("dialog.text.continue") {
		public Text setText(String key) {return this;}
	};
	@ZenProperty(getter = "back")
	public static final IText BACK = new Text("dialog.text.back") {
		public Text setText(String key) {return this;}
	};
	@ZenProperty(getter = "close")
	public static final IText CLOSE = new Text("dialog.text.close") {
		public Text setText(String key) {return this;}
	};
	
	protected List<IText> titles;
	protected boolean canBackPreviousDialog;
	
	public OptionMultiTitle() {}
	public OptionMultiTitle(boolean closeDialog, boolean canBackPreviousDialog, IText... titles) {
		this(closeDialog, canBackPreviousDialog, Lists.newArrayList(titles));
	}
	public OptionMultiTitle(boolean closeDialog, IText... titles) {
		this(closeDialog, false, titles);
	}
	public OptionMultiTitle(boolean closeDialog, List<IText> titles) {
		this(closeDialog, false, titles);
	}
	public OptionMultiTitle(boolean closeDialog, boolean canBackPreviousDialog, List<IText> titles) {
		super(CONTINUE, closeDialog);
		this.titles = titles;
		this.canBackPreviousDialog = canBackPreviousDialog;
	}
	
	@ZenGetter("titles")
	public List<IText> getTitles() {
		return titles;
	}
	@ZenMethod("titles")
	public OptionMultiTitle setTitles(List<IText> titles) {
		this.titles = titles;
		return this;
	}
	@ZenMethod
	public OptionMultiTitle addTitles(IText title) {
		if(this.titles==null) this.titles = Lists.newArrayList();
		this.titles.add(title);
		return this;
	}
	
	@SideOnly(Side.CLIENT)
	public List<String> getTitlesAsString(){
		List<String> titles = Lists.newArrayList();
		for (IText title : this.titles) {
			titles.add(title.format());
		}
		return titles;
	}
	
	@ZenGetter("canBack")
	public boolean canBackPreviousDialog() {
		return canBackPreviousDialog;
	}
	@ZenMethod("canBack")
	public OptionMultiTitle setCanBackPreviousDialog(boolean canBackPreviousDialog) {
		this.canBackPreviousDialog = canBackPreviousDialog;
		return this;
	}
	
	@ZenMethod("text")
	public OptionMultiTitle setDisplayText(IText text) {
		super.text = text;
		return this;
	}
	
	@Override
	public void readFromJson(JsonObject json) {
		super.readFromJson(json);
		
		JsonArray tileJson = json.getAsJsonArray("tiles");
		for(int i = 0; i < tileJson.size(); i++) {
			this.addTitles(new Text(tileJson.get(i).getAsJsonObject()));
		}
		this.setCanBackPreviousDialog(json.has("canBackParentDialog") && json.get("canBackParentDialog").getAsBoolean());
	}
	
	@Override
	public JsonObject writeToJson(JsonObject json) {
		json = super.writeToJson(json);
		
		JsonArray tileJson = new JsonArray();
		for(int i = 0; i < this.getTitles().size(); i++) {
			tileJson.add(this.getTitles().get(i).writeTo(JsonObject.class));
		}
		json.add("tiles", tileJson);
		json.addProperty("canBackParentDialog", this.canBackPreviousDialog());
		
		return json;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);

		NBTTagList tileNBT = nbt.getTagList("tiles", 10);
		for(int i = 0; i < tileNBT.tagCount(); i++) {
			this.addTitles(new Text(tileNBT.getCompoundTagAt(i)));
		}
		this.setCanBackPreviousDialog(nbt.hasKey("canBackParentDialog") && nbt.getBoolean("canBackParentDialog"));
	}
	@Override
	public NBTTagCompound writeToNBT() {
		NBTTagCompound nbt = super.writeToNBT();
		
		SimpleNBTTagList tileNBT = new SimpleNBTTagList();
		for(int i = 0; i < this.getTitles().size(); i++) {
			tileNBT.append(this.getTitles().get(i).writeTo(NBTTagCompound.class));
		}
		nbt.setTag("tiles", tileNBT);
		nbt.setBoolean("canBackParentDialog", this.canBackPreviousDialog());
		
		return nbt;
	}
	
	@Override
	public ResourceLocation getTypeID() {
		return DialogOption.multi_tile().getTypeID();
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public OptionDrawUnit getDrawUnit(ResourceLocation dialogID, int optionID, IDialogOption option, DialogDimension dialogDimension) {
		return new OptionMultiTitleDrawUnit(dialogID, (OptionMultiTitle) option, optionID, dialogDimension);
	}
	
	@Override
	public OptionMultiTitle copy() {
		List<IText> titles = this.getTitles() == null ? Collections.emptyList() : this.getTitles().stream().map(IText::copy).collect(Collectors.toList());
		return new OptionMultiTitle(canCloseDialog(), canBackPreviousDialog(), titles);
	}
}
