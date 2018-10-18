/**
 * 
 */
package fr.interiale.portlet.siteavisclient.bean;

import java.io.Serializable;

import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portlet.asset.model.AssetCategory;

import fr.interiale.portlet.siteavisclient.util.SiteAvisClientUtil;

/**
 * @author antoine.comble
 *
 */
public class CategorieNoteGlobaleFO implements Serializable {

	private static final long serialVersionUID = -140824234015823479L;

	private AssetCategory category;
	
	private CategorieNoteGlobale noteGlobale = new CategorieNoteGlobale();

	/**
	 * @return the category
	 */
	public AssetCategory getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(AssetCategory category) {
		this.category = category;
	}

	/**
	 * @return the noteGlobale
	 */
	public CategorieNoteGlobale getNoteGlobale() {
		return noteGlobale;
	}

	/**
	 * @param note the noteGlobale to set
	 */
	public void setNoteGlobale(CategorieNoteGlobale noteGlobale) {
		this.noteGlobale = noteGlobale;
	}
	
	public String getFormattedNote() {
		try {
			return SiteAvisClientUtil.DECIMAL_FORMAT_FRANCE.format(new Double(noteGlobale.getNote()) / noteGlobale.getNbAvis());
		} catch (NumberFormatException | NullPointerException e) {
			return StringPool.BLANK;
		}

	}
}
