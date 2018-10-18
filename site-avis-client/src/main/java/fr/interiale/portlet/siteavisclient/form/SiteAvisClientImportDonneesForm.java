package fr.interiale.portlet.siteavisclient.form;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author cckc3018
 */
public class SiteAvisClientImportDonneesForm implements Serializable {

	private static final long serialVersionUID = 6624589906692014824L;
	
	private MultipartFile file;

	/**
	 * @return the file
	 */
	public MultipartFile getFile() {
		return file;
	}

	/**
	 * @param file the file to set
	 */
	public void setFile(MultipartFile file) {
		this.file = file;
	}
}
