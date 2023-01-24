package cat.jiu.dialog.iface;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import cat.jiu.dialog.utils.DialogConfig;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.text.TextComponentTranslation;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IDialogText {
	String getText();
	void setText(String text);
	
	Object[] getParameters();
	void setParameters(Object... parameters);
	
	boolean isCenter();
	void setCenter(boolean isCenter);
	
	/**
	 * 使用原版的长行回绕，false则无视语序单字回绕<p>
	 * use {@link FontRenderer#listFormattedStringToWidth(String, int)} to format text if true, <p>
	 * else use {@link cat.jiu.dialog.ui.GuiDialog#splitString(String, int)} to format text.
	 * @see net.minecraft.client.gui.FontRenderer#listFormattedStringToWidth(String, int)
	 * @see cat.jiu.dialog.ui.GuiDialog#splitString(String, int)
	 */
	default boolean isVanillaWrap() {
		return DialogConfig.Enable_Vanilla_Wrap_Text;
	}
	
	@SideOnly(Side.CLIENT)
	default String format() {
		return I18n.format(this.getText(), this.getParameters());
	}
	@SideOnly(Side.CLIENT)
	default int getStringWidth(FontRenderer fr) {
		return fr.getStringWidth(this.format());
	}
	default TextComponentTranslation toTextComponent() {
		return new TextComponentTranslation(this.getText(), this.getParameters());
	}
	
	public static final NBTTagString emptyNBT = new NBTTagString();
	
	default NBTBase writeToNBT() {
		if(!"".equals(this.getText())) {
			if(this.isCenter()) {
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setString("text", this.getText());
				nbt.setBoolean("isCenter", this.isCenter());
				if(this.getParameters()!=null && this.getParameters().length>0) {
					NBTTagList args = new NBTTagList();
					for(int i = 0; i < this.getParameters().length; i++) {
						args.appendTag(new NBTTagString(String.valueOf(this.getParameters()[i])));
					}
					nbt.setTag("args", args);
				}
				return nbt;
			}else {
				return new NBTTagString(this.getText());
			}
		}
		return emptyNBT;
	}
	
	default void readFromNBT(NBTBase base) {
		if(base instanceof NBTTagCompound) {
			NBTTagCompound nbt = (NBTTagCompound) base;
			if(!nbt.hasKey("text"))
				return;
			
			String text = nbt.getString("text");
			if(text.isEmpty())
				return;
			
			this.setText(text);
			if(nbt.hasKey("isCenter")) this.setCenter(nbt.getBoolean("isCenter"));
			if(nbt.hasKey("args")) {
				NBTTagList argsNBT = nbt.getTagList("args", 8);
				Object[] args = new Object[argsNBT.tagCount()];
				for(int i = 0; i < args.length; i++) {
					args[i] = argsNBT.getStringTagAt(i);
				}
				this.setParameters(args);
			}
		}else if(base instanceof NBTTagString) {
			this.setText(((NBTTagString) base).getString());
		}
	}
	
	public static final JsonElement emptyJson = new JsonPrimitive("");
	
	default JsonElement writeToJson() {
		if(!"".equals(this.getText())) {
			if(this.isCenter()) {
				JsonObject json = new JsonObject();
				
				json.addProperty("text", this.getText());
				json.addProperty("isCenter", this.isCenter());
				Object[] args = this.getParameters();
				if(args!=null && args.length > 0) {
					JsonArray argsJson = new JsonArray();
					for(int i = 0; i < args.length; i++) {
						argsJson.add(String.valueOf(args[i]));
					}
					json.add("args", argsJson);
				}
				return json;
			}else {
				return new JsonPrimitive(this.getText());
			}
		}
		
		return emptyJson;
	}
	
	default void readFromJson(JsonElement e) {
		if(e instanceof JsonObject) {
			JsonObject json = e.getAsJsonObject();
			if(!json.has("text"))
				return;
			
			String text = json.get("text").getAsString();
			if(text.isEmpty())
				return;
			
			this.setText(text);
			if(json.has("isCenter")) this.setCenter(json.get("isCenter").getAsBoolean());
			if(json.has("args")) {
				JsonArray argsJson = json.getAsJsonArray("args");
				Object[] args = new Object[argsJson.size()];
				for(int i = 0; i < args.length; i++) {
					args[i] = argsJson.get(i).getAsString();
				}
				this.setParameters(args);
			}
		}else if(e instanceof JsonPrimitive) {
			this.setText(e.getAsString());
		}
	}
	
	IDialogText copy();
}
