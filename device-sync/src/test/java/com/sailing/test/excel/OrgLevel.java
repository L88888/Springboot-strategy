//package com.sailing.test.excel;
//
//import org.apache.commons.lang3.StringUtils;
//
///**
// * OrgLevel: 组织机构层级枚举
// */
//public enum OrgLevel {
//
//	/**
//	 * @Fields PROVINCE : 地区局
//	 */
//	PROVINCE("", "地区局"),
//	/**
//	 * @Fields COUNTY : 分县局
//	 */
//	COUNTY("", "分县局"),
//	/**
//	 * @Fields STATATION : 派出所
//	 */
//	STATATION("", "派出所"),
//	/**
//	 * @Fields REGION : 责任区
//	 */
//	REGION("", "责任区");
//
//	private String orgCodePrefix;
//
//	private String description;
//
//	public String getOrgCodePrefix() {
//		return orgCodePrefix;
//	}
//
//	public void setOrgCodePrefix(String orgCodePrefix) {
//		this.orgCodePrefix = orgCodePrefix;
//	}
//
//	public String getDescription() {
//		return description;
//	}
//
//	private OrgLevel(String orgCodePrefix, String description) {
//		this.description = description;
//	}
//
//	@Override
//	public String toString() {
//		return getOrgCodePrefix() + ":" + getDescription();
//	}
//
//	public static OrgLevel getLevel(String orgCode) {
//		if(orgCode.substring(0, 2).equals("52")){
//			if (orgCode.substring(4, 6).equals("00")) {
//				OrgLevel.PROVINCE.setOrgCodePrefix(StringUtils.substring(orgCode, 0, 4));
//				return OrgLevel.PROVINCE;
//			} else if (!orgCode.substring(4, 6).equals("00") && (orgCode.substring(6, 8).compareTo("60") < 0)) {
//				OrgLevel.COUNTY.setOrgCodePrefix(StringUtils.substring(orgCode, 0, 6));
//				return OrgLevel.COUNTY;
//			} else if (!orgCode.substring(4, 6).equals("00") && (orgCode.substring(6, 8).compareTo("60") >= 0)
//					&& orgCode.substring(8, 12).equals("0000")) {
//				OrgLevel.STATATION.setOrgCodePrefix(StringUtils.substring(orgCode, 0, 8));
//				return OrgLevel.STATATION;
//			} else if (!orgCode.substring(4, 6).equals("00") && (orgCode.substring(6, 8).compareTo("60") >= 0)) {
//				OrgLevel.REGION.setOrgCodePrefix(orgCode);
//				return OrgLevel.REGION;
//			}
//			OrgLevel.PROVINCE.setOrgCodePrefix(StringUtils.substring(orgCode, 0, 4));
//		}else{
//			if (orgCode.substring(4, 6).equals("00")) {
//				OrgLevel.PROVINCE.setOrgCodePrefix(StringUtils.substring(orgCode, 0, 4));
//				return OrgLevel.PROVINCE;
//			} else if (!orgCode.substring(4, 6).equals("00") && (orgCode.substring(6, 8).compareTo("51") < 0)) {
//				OrgLevel.COUNTY.setOrgCodePrefix(StringUtils.substring(orgCode, 0, 6));
//				return OrgLevel.COUNTY;
//			} else if (!orgCode.substring(4, 6).equals("00") && (orgCode.substring(6, 8).compareTo("51") >= 0)
//					&& orgCode.substring(8, 12).equals("0000")) {
//				OrgLevel.STATATION.setOrgCodePrefix(StringUtils.substring(orgCode, 0, 8));
//				return OrgLevel.STATATION;
//			} else if (!orgCode.substring(4, 6).equals("00") && (orgCode.substring(6, 8).compareTo("51") >= 0)) {
//				OrgLevel.REGION.setOrgCodePrefix(orgCode);
//				return OrgLevel.REGION;
//			}
//			OrgLevel.PROVINCE.setOrgCodePrefix(StringUtils.substring(orgCode, 0, 4));
//		}
//		return OrgLevel.PROVINCE;
//	}
//	/**
//	 * 根据当前组织机构获取与组织基本对应的组织机构前缀编码<p>
//	 * 以组织机构编码 653201000060为例:<p>
//	 * PROVINCE --->6532 <p>
//	 * COUNTY---->653201<p>
//	 * STATATION---->65320100<p>
//	 * 其他--->653201000060
//	 * @param orgLevel 组织机构级别
//	 * @param orgCode 组织机构编码
//	 * @return
//	 */
//	public static String getOrgLevelPrefix(OrgLevel orgLevel, String orgCode){
//		String orgPrefix = orgCode;
//		switch (orgLevel) {
//		case PROVINCE:
//			orgPrefix = orgCode.substring(0, 4);
//			break;
//		case COUNTY:
//			orgPrefix = orgCode.substring(0, 6);
//			break;
//		case STATATION:
//			orgPrefix = orgCode.substring(0, 8);
//			break;
//		default:
//			orgPrefix = orgCode;
//			break;
//		}
//		return orgPrefix;
//	}
//
//	public static void main(String[] args) {
//		System.out.println("27.541005　".trim());
//	}
//}
