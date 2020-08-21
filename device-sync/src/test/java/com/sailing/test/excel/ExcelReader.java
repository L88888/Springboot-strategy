//package com.sailing.test.excel;
//
//import java.io.File;
//import java.util.List;
//
////import org.jeecgframework.poi.excel.ExcelImportUtil;
////import org.jeecgframework.poi.excel.entity.ImportParams;
//
//public class ExcelReader {
//
//	public static void main(String[] args) throws Exception {
//		List<ExcelField> excelFields = excelFields();
////		getEntityClassContent(excelFields);
////		getSqlForYjyd(excelFields);
////		getUpdateSql(excelFields);
//		getInsertSql(excelFields);
//	}
//
//	/**
//	 * 生成查询一机一档sql
//	 *
//	 * @return
//	 */
//	public static void getSqlForYjyd(List<ExcelField> excelFields) {
//		StringBuffer content = new StringBuffer("select ");
//		excelFields.forEach(e -> {
//			if (e.getYjyd() != null) {
//				content.append(e.getYjyd().toLowerCase()).append(" as ").append(e.getXsz().toLowerCase()).append(",");
//			}
//		});
//		content.append("from CAMERA_INFO");
//		System.out.println(content);
//	}
//
//	public static void getInsertSql(List<ExcelField> excelFields) {
//		StringBuffer content = new StringBuffer("insert into DEVICE_INFO(");
//		excelFields.stream().filter(e->e.getYjyd() != null)
//		.forEach(e -> {
//			content.append(e.getXsz().toLowerCase()).append(",");
//		});
//		content.append(") values()");
//		System.out.println(content);
//	}
//
//	public static void getUpdateSql(List<ExcelField> excelFields) {
//		StringBuffer content = new StringBuffer("update DEVICE_INFO set ");
//		excelFields.forEach(e -> {
//			if (e.getYjyd() != null) {
//				content.append(e.getXsz().toLowerCase()).append("=?,");
//			}
//		});
//		content.append(" where device_id=?");
//		System.out.println(content);
//	}
//
//	/**
//	 * 根据表生成实体和注释-新视综
//	 */
//	public static void getEntityClassContent(List<ExcelField> excelFields) {
//		StringBuffer content = new StringBuffer();
//		excelFields.forEach(e -> {
//			content.append("// ").append(e.getXszzs()).append("\n");
//			content.append("private String ").append(e.getXsz().toLowerCase()).append(";").append("\n");
//		});
//		System.out.println(content);
//	}
//
//	private static List<ExcelField> excelFields() {
//		String filePath = "D:\\workspace_springboot\\device-sync\\src\\test\\java\\dbfiledrelations.xlsx";
//		File excelFile = new File(filePath);
//		return null;//ExcelImportUtil.importExcel(excelFile, ExcelField.class, new ImportParams());
//	}
//
//}
