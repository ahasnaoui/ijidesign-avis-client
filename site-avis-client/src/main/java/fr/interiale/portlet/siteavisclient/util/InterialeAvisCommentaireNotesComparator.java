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
public class InterialeAvisCommentaireNotesComparator implements Comparator<InterialeAvisCommentaireFO> {

	@Override
	public int compare(InterialeAvisCommentaireFO o1, InterialeAvisCommentaireFO o2) {

		if (Validator.isNotNull(o1.getNotes()) && Validator.isNotNull(o2.getNotes())) {
            final float notes1 = Float.parseFloat(o1.getNotes());
            final float notes2 = Float.parseFloat(o2.getNotes());
			final int result = Float.compare(notes1, notes2);
			if (result == 0) {
				if (Validator.isNotNull(o1.getDateCreation()) && Validator.isNotNull(o2.getDateCreation())) {
		            return (o1.getDateCreation().compareTo(o2.getDateCreation())) * -1;
		        }
			}
			return result;
        }

        return 0;
        
	}

}
