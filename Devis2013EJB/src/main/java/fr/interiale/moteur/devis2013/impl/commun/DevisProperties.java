package fr.interiale.moteur.devis2013.impl.commun;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * The Class ServiceProperties.
 */
public class DevisProperties {
	
	/** nom du fichier contenant les messages */
	private static final String BUNDLE_NAME = "devis";

	/** The Constant RESOURCE_BUNDLE. */
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	/**
	 * Instantiates a new messages.
	 */
	private DevisProperties() {
	}

	/**
	 * Gets the string.
	 * 
	 * @param key
	 *            the key
	 * 
	 * @return the string
	 */
	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
