package com.software5000.base.entity;

import com.software5000.base.NotDatabaseField;

/**
 * 系统代码表
 */
public class SystemCode extends BaseEntity {
	
	public enum Daos {
		/**
		 * 查询系统参数
		 */
		selectByPrimaryKey("SystemCode.selectByPrimaryKey"),

		/**
		 * 根据CodeType查询系统参数
		 */
		selectCodeListByCodeType("t_sys_code.selectCodeListByCodeType"),

		/**
		 * 查询系统参数
		 */
		selectTsysCodeByProperty("t_sys_code.selectTsysCodeByProperty");
		public String sqlMapname;

		private Daos(String name) {
			this.sqlMapname = name;
		}

		public String toString() {
			return this.sqlMapname;
		}
	}

	/**
	 * 代码类型
	 */
	private String codeType;
	/**
	 * 代码名称
	 */
	private String codeName;
	/**
	 * 代码值
	 */
	private String codeValue;
	/**
	 * 代码状态
	 */
	private String codeStatus;
	/**
	 * 代码对应中文名称（显示）
	 */
	private String codeShowName;
	/**
	 * 代码过滤规则
	 */
	private Integer codeFiter;
	/**
	 * 代码排序
	 */
	private Integer codeSort;
	/**
	 * 代码父级代码值
	 */
	private String codeParentValue;
	/**
	 * 代码描述
	 */
	private String codeDesc;

	@NotDatabaseField
	private String testString;

	public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	public String getCodeValue() {
		return codeValue;
	}

	public void setCodeValue(String codeValue) {
		this.codeValue = codeValue;
	}

	public String getCodeStatus() {
		return codeStatus;
	}

	public void setCodeStatus(String codeStatus) {
		this.codeStatus = codeStatus;
	}

	public String getCodeShowName() {
		return codeShowName;
	}

	public void setCodeShowName(String codeShowName) {
		this.codeShowName = codeShowName;
	}

	public Integer getCodeFiter() {
		return codeFiter;
	}

	public void setCodeFiter(Integer codeFiter) {
		this.codeFiter = codeFiter;
	}

	public Integer getCodeSort() {
		return codeSort;
	}

	public void setCodeSort(Integer codeSort) {
		this.codeSort = codeSort;
	}

	public String getCodeParentValue() {
		return codeParentValue;
	}

	public void setCodeParentValue(String codeParentValue) {
		this.codeParentValue = codeParentValue;
	}

	public String getCodeDesc() {
		return codeDesc;
	}

	public void setCodeDesc(String codeDesc) {
		this.codeDesc = codeDesc;
	}
}