package fr.interiale.portlet.siteavisclient.bean;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * VO for the Spring porlet MVC file upload 
 *
 */
 
public class SpringFileVO {
 
    private CommonsMultipartFile fileData;
    private String successMessage;
    private String errorMessage;

	public String getSuccessMessage() {
        return successMessage;
    }
 
    public void setSuccessMessage(String message) {
        this.successMessage = message;
    }
 
    public CommonsMultipartFile getFileData() {
        return fileData;
    }
 
    public void setFileData(CommonsMultipartFile fileData) {
        this.fileData = fileData;
    }

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
     
}