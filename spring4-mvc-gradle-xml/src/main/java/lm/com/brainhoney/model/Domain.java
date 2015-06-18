/**
 * 
 */
package lm.com.brainhoney.model;

import java.io.Serializable;

/**
 * @author mithun.mondal
 *
 */
public class Domain implements Serializable, Models {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long domainId;
	private String reference;
	private String domainName;
	private String userSpace;
	private long parentId;
	
	private String baseName;
	private String omittedJsonList;

	/**
	 * 
	 */
	public Domain() {
		// TODO Auto-generated constructor stub
	}

	
	public long getDomainId() {
		return domainId;
	}


	public void setDomainId(long domainId) {
		this.domainId = domainId;
	}


	public String getReference() {
		return reference;
	}


	public void setReference(String reference) {
		this.reference = reference;
	}


	public String getDomainName() {
		return domainName;
	}


	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}


	public String getUserSpace() {
		return userSpace;
	}


	public void setUserSpace(String userSpace) {
		this.userSpace = userSpace;
	}


	public long getParentId() {
		return parentId;
	}


	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	
	public String getBaseName() {
		return baseName;
	}


	public void setBaseName(String baseName) {
		this.baseName = baseName;
	}


	public void setOmittedJsonList(String omittedJsonList) {
		this.omittedJsonList = omittedJsonList;
	}


	public String getOmittedJsonList() {		
		return omittedJsonList;
	}

}
