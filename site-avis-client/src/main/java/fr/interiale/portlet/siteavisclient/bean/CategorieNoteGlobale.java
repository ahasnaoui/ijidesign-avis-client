/**
 * 
 */
package fr.interiale.portlet.siteavisclient.bean;

import java.io.Serializable;

/**
 * @author antoine.comble
 *
 */
public class CategorieNoteGlobale implements Serializable {

	private static final long serialVersionUID = -140824234015823479L;

	private int nbAvis = 0;
	
	private long note = 0;

	/**
	 * @return the nbAvis
	 */
	public int getNbAvis() {
		return nbAvis;
	}

	/**
	 * @param nbAvis the nbAvis to set
	 */
	public void setNbAvis(int nbAvis) {
		this.nbAvis = nbAvis;
	}

	/**
	 * @return the note
	 */
	public long getNote() {
		return note;
	}

	/**
	 * @param note the note to set
	 */
	public void setNote(long note) {
		this.note = note;
	}

	
}
