package com.sailing.dataextraction.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author Liufei Yang
 * @version 1.0
 * @className IndexJsonData
 * @description 索引数据实体
 * @date 2020/8/25 15:36
 **/
@Data
public class IndexJsonData implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 资源标识符Id */
    private String resourceId;
    /** 数据文件 */
    private List<String> dataFiles;
    /** 数据标识符 */
    private List<String> dataItems;

    /**
     * 单条数据对象集合,前期先保留这块数据结构,后期会进行更换掉
     */
    private List<Map<String, String>> data;

    /**
     * 同行对象数据集合
     */
    private List<Peers> peers;
}
