/**
 * 
 */
package fr.interiale.portlet.siteavisclient.util;

import java.util.Comparator;

import com.liferay.portal.kernel.util.Validator;

import fr.interiale.portlet.siteavisclient.bean.InterialeAvisReponseFO;

/**
 * @author antoine.comble
 *
 */
public class InterialeAvisReponseDateCreationComparator implements Comparator<InterialeAvisReponseFO> {

	@Override
	public int compare(InterialeAvisReponseFO o1, InterialeAvisReponseFO o2) {

		if (Validator.isNotNull(o1.getDateCreation()) && Validator.isNotNull(o2.getDateCreation())) {
            return (o1.getDateCreation().compareTo(o2.getDateCreation())) * -1;
        }

        return 0;
        
	}

}
