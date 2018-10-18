/**
 * 
 */
package fr.interiale.portlet.siteavisclient.util;

import java.util.Comparator;

import com.liferay.portal.kernel.util.Validator;

import fr.interiale.portlet.siteavisclient.bean.InterialeAvisCommentaireFO;

/**
 * @author antoine.comble
 *
 */
public class InterialeAvisCommentaireNotesDescComparator implements Comparator<InterialeAvisCommentaireFO> {

	@Override
	public int compare(InterialeAvisCommentaireFO o1, InterialeAvisCommentaireFO o2) {

		if (Validator.isNotNull(o1.getNotes()) && Validator.isNotNull(o2.getNotes())) {
            final float notes1 = Float.parseFloat(o1.getNotes());
            final float notes2 = Float.parseFloat(o2.getNotes());
			return  Float.compare(notes1, notes2) * -1;
        }

        return 0;
        
	}

}
