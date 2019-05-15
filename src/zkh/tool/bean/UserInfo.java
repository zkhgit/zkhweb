package zkh.tool.bean;

import java.util.List;
import java.util.Set;

/**
 * UserInfo存放在SESSION中的用户信息.
 * 
 * @author lyx
 */
public class UserInfo {

	private static final long serialVersionUID = 781831901061053384L;
	
	private String userId;
	private String userType;
	private String userName;
	private String account;
	private String password;
	private String companyId;
	private String companyName;
	private String departId;
	private String departName;
	private Set<String> permissionList;
	private String loginIp;
	private Set<String> roleList;

	/** default constructor */
	public UserInfo() {
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserType() {
		return userType;
	}
	public void setUserType(String usertype) {
		this.userType = usertype;
	}

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public String getDepartId() {
		return departId;
	}
	public void setDepartId(String departId) {
		this.departId = departId;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getDepartName() {
		return departName;
	}

	public void setDepartName(String departName) {
		this.departName = departName;
	}

	public Set<String> getPermissionList() {
		return permissionList;
	}
	public void setPermissionList(Set<String> permissionList) {
		this.permissionList = permissionList;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public Set<String> getRoleList() {
		return roleList;
	}

	public void setRoleList(Set<String> roleList) {
		this.roleList = roleList;
	}

	/**
	 * 本函数输出将作为默认的<shiro:principal/>输出.
	 */
	@Override
	public String toString() {
		return account;
	}

}
