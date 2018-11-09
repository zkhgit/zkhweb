package zkh.tool.list.demo;

import java.util.Date;

/**
 * CourseTeacher entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CourseTeacher implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// Fields
	private String teacherId;
	private String teacherName;
	private String sex;
	private String idCard;
	private String introduction;
	private String photoUrl;
	private String title;
	private String teacherType;
	private String mobile;
	private String qq;
	private String email;
	private String workUnit;
	private String note;
	private String baseId;
	private Date birthDay;

	private String baseName;
	private String nationalType;
	private String nationalTypeName;
	private String fundType;
	private String fundTypeName;
	private String educationType;
	private String educationName;
	private String economicType;
	private String economicName;
	private String updateUser;
	private Date updateDate;
	
	private Long dd;

	// Constructors
	private String status;

	/** default constructor */
	public CourseTeacher() {
	}

	
	// Property accessors
	public String getTeacherId() {
		return this.teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getTeacherName() {
		return this.teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getSex() {
		return this.sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Date getBirthDay() {
		return this.birthDay;
	}

	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
	}

	public String getIdCard() {
		return this.idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getIntroduction() {
		return this.introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getPhotoUrl() {
		return this.photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTeacherType() {
		return this.teacherType;
	}

	public void setTeacherType(String teacherType) {
		this.teacherType = teacherType;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getQq() {
		return this.qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWorkUnit() {
		return this.workUnit;
	}

	public void setWorkUnit(String workUnit) {
		this.workUnit = workUnit;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getBaseId() {
		return this.baseId;
	}

	public void setBaseId(String baseId) {
		this.baseId = baseId;
	}

	public String getBaseName() {
		return this.baseName;
	}

	public void setBaseName(String baseName) {
		this.baseName = baseName;
	}

	public String getNationalType() {
		return this.nationalType;
	}

	public void setNationalType(String nationalType) {
		this.nationalType = nationalType;
	}

	public String getFundType() {
		return this.fundType;
	}

	public void setFundType(String fundType) {
		this.fundType = fundType;
	}

	public String getEducationType() {
		return this.educationType;
	}

	public void setEducationType(String educationType) {
		this.educationType = educationType;
	}

	public String getEconomicType() {
		return this.economicType;
	}

	public void setEconomicType(String economicType) {
		this.economicType = economicType;
	}

	public String getUpdateUser() {
		return this.updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNationalTypeName() {
		return nationalTypeName;
	}

	public void setNationalTypeName(String nationalTypeName) {
		this.nationalTypeName = nationalTypeName;
	}

	public String getFundTypeName() {
		return fundTypeName;
	}

	public void setFundTypeName(String fundTypeName) {
		this.fundTypeName = fundTypeName;
	}

	public String getEducationName() {
		return educationName;
	}

	public void setEducationName(String educationName) {
		this.educationName = educationName;
	}

	public String getEconomicName() {
		return economicName;
	}

	public void setEconomicName(String economicName) {
		this.economicName = economicName;
	}


	public Long getDd() {
		return dd;
	}


	public void setDd(Long dd) {
		this.dd = dd;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
}