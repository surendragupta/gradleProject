/**
 * 
 */
package lm.com.brainhoney.model;

import java.io.Serializable;

/**
 * @author mithun.mondal
 *
 */
public class User implements Serializable, Models {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long userId;
	private String prefixName;
	private String userName;
	private String userPassword;
	
	private String baseName;
	private String omittedJsonList;
	/**
	 * 
	 */
	public User() {
		// TODO Auto-generated constructor stub
	}
	
	public long getUserId() {
		return userId;
	}
	
	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	public String getPrefixName() {
		return prefixName;
	}
	
	public void setPrefixName(String prefixName) {
		this.prefixName = prefixName;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getUserPassword() {
		return userPassword;
	}
	
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	
	public String getBaseName() {
		return baseName;
	}

	public void setBaseName(String baseName) {
		this.baseName = baseName;
	}

	public String getOmittedJsonList() {
		return omittedJsonList;
	}
	
	public void setOmittedJsonList(String omittedJsonList) {
		this.omittedJsonList = omittedJsonList;
	}

	
}
