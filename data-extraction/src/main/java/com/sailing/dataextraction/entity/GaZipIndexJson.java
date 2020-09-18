package com.sailing.dataextraction.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 国标Zip索引文件数据格式如下：
 * {
 *     "PACKAGE_HEAD":{
 *         "RECORD_NUM":"数据文件总记录",
 *         "SEND_UNIT_CODE":"发送单位_公安机关机构代码",
 *         "SEND_UNIT_NAME":"发送单位_公安机关名称",
 *         "VERSION":"版本号",
 *         "DATA_FILE_NUM":"文件个数",
 *         "ATTACHED_NUM":"附属文件个数",
 *         "MESSAGE_SEQUENCE":"数据包编号"
 *     },
 *     "DATA_DESCRIPTIONS":[
 *         {
 *             "DATA_RESOURCE_ID":"数据资源标识符",
 *             "DATA_FILES":[
 *                 {
 *                     "DATA_FILE_PATH":"数据文件路径",
 *                     "DATA_FILE_RECORD_NUM":"数据文件记录数",
 *                     "DATA_FILE_ATTACHED_NUM":"数据文件附属文件数量"
 *                 }
 *             ],
 *             "DATA_ITEMS":[
 *                 {
 *                     "IDENTITIER":"数据项标识符",
 *                     "FORMAT":"数据格式"
 *                 }
 *             ]
 *         }
 *     ]
 * }
 *
 * 示例数据：
 * {
 *     "PACKAGE_HEAD": {
 *         "RECORD_NUM": 14993,
 *         "SEND_UNIT_NAME": "遵义市公安局",
 *         "SEND_UNIT_CODE": "520301000000",
 *         "VERSION": "1.00",
 *         "DATA_FILE_NUM": 3,
 *         "ATTACHED_NUM": 0,
 *         "MESSAGE_SEQUENCE": "2020082418335500001"
 *     },
 *     "DATA_DESCRIPTIONS": [
 *         {
 *             "DATA_FILES": [
 *                 {
 *                     "DATA_FILE_ATTACHED_NUM": "0",
 *                     "DATA_FILE_RECORD_NUM": "3",
 *                     "DATA_FILE_PATH": "1.bcp"
 *                 }
 *             ],
 *             "DATA_RESOURCE_ID": "R-52030032010000000004",
 *             "DATA_ITEMS": [
 *                 {
 *                     "IDENTITIER": "xm",
 *                     "FORMAT": ""
 *                 },
 *                 {
 *                     "IDENTITIER": "gmsfhm",
 *                     "FORMAT": ""
 *                 }
 *             ]
 *         }
 *     ]
 * }
 * @author Liufei Yang
 * @version 1.0
 * @className GaZipIndexJson
 * @description 国标Zip索引文件实体
 * @date 2020/8/25 14:52
 **/
@Data
public class GaZipIndexJson implements Serializable {

    private static final long serialVersionUID = 1L;
    /** 包头 */
    private PackageHead PACKAGE_HEAD;

    private List<DataDescription> DATA_DESCRIPTIONS;

    @Data
    public class PackageHead implements Serializable{
        private static final long serialVersionUID = 1L;
        /** 数据文件总记录 */
        private long RECORD_NUM;
        /** 发送单位_公安机关机构代码 */
        private String SEND_UNIT_CODE;
        /** 发送单位_公安机关名称 */
        private String SEND_UNIT_NAME;
        /** 版本号 */
        private String VERSION;
        /** 文件个数 */
        private int DATA_FILE_NUM;
        /** 附属文件个数 */
        private int ATTACHED_NUM;
        /** 数据包编号 */
        private String MESSAGE_SEQUENCE;
    }

    /**
     * {
     *     "DATA_RESOURCE_ID":"数据资源标识符",
     *     "DATA_FILES":[
     *         {
     *             "DATA_FILE_PATH":"数据文件路径",
     *             "DATA_FILE_RECORD_NUM":"数据文件记录数",
     *             "DATA_FILE_ATTACHED_NUM":"数据文件附属文件数量"
     *         }
     *     ],
     *     "DATA_ITEMS":[
     *         {
     *             "IDENTITIER":"数据项标识符",
     *             "FORMAT":"数据格式"
     *         }
     *     ]
     * }
     **/
    @Data
    public class DataDescription implements Serializable {
        private static final long serialVersionUID = 1L;
        /** 数据资源标识符 */
        private String DATA_RESOURCE_ID;
        /** 数据文件 */
        private List<DataFile> DATA_FILES;
        /** 数据项列表 */
        private List<DataItem> DATA_ITEMS;

        @Data
        public class DataItem implements Serializable{
            private static final long serialVersionUID = 1L;
            /** 数据项标识符 */
            private String IDENTITIER;
            /** 数据格式 */
            private String FORMAT;
        }

        @Data
        public class DataFile implements Serializable{
            private static final long serialVersionUID = 1L;
            /** 数据文件路径 */
            private String DATA_FILE_PATH;
            /** 数据文件记录数 */
            private String DATA_FILE_RECORD_NUM;
            /** 数据文件附属文件数量 */
            private String DATA_FILE_ATTACHED_NUM;
        }
    }
}
