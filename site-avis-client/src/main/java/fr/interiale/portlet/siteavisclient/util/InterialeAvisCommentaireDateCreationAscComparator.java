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
public class InterialeAvisCommentaireDateCreationAscComparator implements Comparator<InterialeAvisCommentaireFO> {

	@Override
	public int compare(InterialeAvisCommentaireFO o1, InterialeAvisCommentaireFO o2) {

		if (Validator.isNotNull(o1.getDateCreation()) && Validator.isNotNull(o2.getDateCreation())) {
            return (o1.getDateCreation().compareTo(o2.getDateCreation()));
        }

        return 0;
        
	}

}
