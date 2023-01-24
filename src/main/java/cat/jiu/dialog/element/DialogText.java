package cat.jiu.dialog.element;

import java.util.Arrays;

import com.google.gson.JsonElement;

import cat.jiu.dialog.iface.IDialogText;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public class DialogText implements IDialogText {
	public static final Object[] emptyArgs = new Object[0];
	public static final DialogText EMPTY = new DialogText("") {
		public void setText(String key) {}
	};
	
	protected String text = "";
	protected Object[] args = emptyArgs;
	protected boolean isCenter = false;
	
	/**
	 * 
	 * @param text the text, can be a translate key
	 * @param parameters translate key parameters
	 */
	public DialogText(String key, Object... parameters) {
		this.text = key;
		this.args = parameters!=null ? parameters : emptyArgs;
	}
	
	/**
	 * @param text the text, can be a translate key
	 * @param isCenter Align center
	 * @param parameters translate key parameters
	 */
	public DialogText(boolean isCenter, String key, Object... parameters) {
		this.text = key;
		this.args = parameters!=null ? parameters : emptyArgs;
		this.isCenter = isCenter;
	}
	protected DialogText(NBTTagCompound nbt) {
		this.readFromNBT(nbt);
	}
	
	public String getText() { return text; }
	public void setText(String text) { this.text = text; }
	
	public Object[] getParameters() { return args; }
	public void setParameters(Object... parameters) { this.args = parameters; }
	
	public boolean isCenter() {return this.isCenter;}
	public void setCenter(boolean isCenter) {this.isCenter = isCenter;}
	
	@Override
	public DialogText copy() {
		return new DialogText(this.isCenter(), this.getText(), this.getParameters());
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(args);
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		DialogText other = (DialogText) obj;
		if(!Arrays.equals(args, other.args))
			return false;
		if(text == null) {
			if(other.text != null)
				return false;
		}else if(!text.equals(other.text))
			return false;
		return true;
	}
	
	public static DialogText get(NBTBase nbt) {
		if(nbt==null) return EMPTY;
		DialogText text = EMPTY.copy();
		text.readFromNBT(nbt);
		return text;
	}
	public static DialogText get(JsonElement json) {
		if(json==null || json.isJsonNull()) return EMPTY;
		DialogText text = EMPTY.copy();
		text.readFromJson(json);
		return text;
	}
}
