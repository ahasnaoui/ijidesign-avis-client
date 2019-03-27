package fr.interiale.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class InterialeWSProperties {
	
	private final static Logger LOGGER = Logger.getLogger(InterialeWSProperties.class);

	private final static Properties PROPERTIES = new Properties();
	
	private static final String WEBSERVICES_PROPERTIES_CONFIG_FILE_PATH = "/opt/jboss-4.2.2.GA/server/default/deploy/ite-ws-configuration.properties";
	
	static {
		readConfigurations();
	}
	
	/**
	 * Charge les configurations.
	 */
	private static void readConfigurations() {
		try {
			PROPERTIES.load(new FileInputStream(WEBSERVICES_PROPERTIES_CONFIG_FILE_PATH));
		} catch (IOException ioe) {
			LOGGER.error(ioe);
		}
	}
	
	/** Accès au webservice EDeal via la SOA */
	private static final String EDEAL_SOA_WEBSERVICE_URL_KEY = "ite.ws.edeal.soa.wsdl";
	public static final String EDEAL_SOA_WEBSERVICE_URL_VALUE = PROPERTIES.getProperty(EDEAL_SOA_WEBSERVICE_URL_KEY);
	
	/** Accès au webservice EDeal en direct */
	private static final String EDEAL_WEBSERVICE_URL_KEY = "ite.ws.edeal.wsdl";
	public static final String EDEAL_WEBSERVICE_URL_VALUE = PROPERTIES.getProperty(EDEAL_WEBSERVICE_URL_KEY);
}
