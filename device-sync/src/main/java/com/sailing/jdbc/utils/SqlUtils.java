package com.sailing.jdbc.utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

/**
 * 完成动态sql查询条件拼接，sql分页处理，普通sql转换为select count(*) count from xx
 *
 * @author YaoWei
 * @version V1.0
 * @ClassName: SqlUtils
 * @date 2018年1月6日
 */
public class SqlUtils {
    /**
     * sql查询条件-- and xxx=?
     */
    private StringBuilder queryCondition = new StringBuilder();
    /**
     * group by 信息
     */
    private StringBuilder groupby = new StringBuilder();


    public SqlUtils like(String key, String value) {
        if (StringUtils.isNotEmpty(value)) {
            queryCondition.append(" and " + key + " like '%" + value + "%'");
        }
        return this;
    }

    public SqlUtils eq(String key, String value) {
        if (StringUtils.isNotEmpty(value)) {
            queryCondition.append(" and " + key + " = '" + value + "'");
        }
        return this;
    }

    public SqlUtils in(String key, String valueStrs) {
        if (StringUtils.isNotEmpty(valueStrs)) {
            String[] values = valueStrs.split(",");
            return this.in(key, values);
        }
        return this;
    }

    public SqlUtils and(String key, String value) {
        if (StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(value)) {
            queryCondition.append(" and " + key + " ='" + value + "'");
        }
        return this;
    }

    public SqlUtils and(String key, Integer value) {
        if (StringUtils.isNotEmpty(key)) {
            queryCondition.append(" and " + key + " =" + value + "");
        }
        return this;
    }

    public SqlUtils in(String key, String... values) {
        if (values.length > 0) {
            if (values.length == 1) {
                queryCondition.append(" and " + key + "='" + values[0] + "'");
                return this;
            }
            queryCondition.append(" and " + key + " in (");
            for (int i = 0; i < values.length; i++) {
                queryCondition.append("'" + values[i] + "'");
                if (i != values.length - 1) {
                    queryCondition.append(",");
                }
            }
            queryCondition.append(")");
        }
        return this;
    }

    public SqlUtils notIn(String key, String valueStrs) {
        if (StringUtils.isNotEmpty(valueStrs)) {
            queryCondition.append(" and " + key + " not in (");
            String[] values = valueStrs.split(",");
            for (int i = 0; i < values.length; i++) {
                queryCondition.append("'" + values[i] + "'");
                if (i != values.length - 1) {
                    queryCondition.append(",");
                }
            }
            queryCondition.append(")");
        }
        return this;
    }

    public SqlUtils in(String key, List<String> values) {
        if (values.size() > 0) {
            queryCondition.append(" and " + key + " in (");
            for (int i = 0; i < values.size(); i++) {
                queryCondition.append("'" + values.get(i) + "'");
                if (i != values.size() - 1) {
                    queryCondition.append(",");
                }
            }
            queryCondition.append(")");
        }
        return this;
    }

    /**
     * 在sql末尾添加group by
     *
     * @param groupby --> group by a,b having count(a) > 0
     * @return
     */
    public SqlUtils groupby(String groupby) {
        Assert.notNull(groupby, "group by 不能为空");
        this.groupby.append(" ").append(groupby);
        return this;
    }

    /**
     * 针对时间查询条件进行比较查询<p>
     * 2、startTime不为空且endTime为空，返回  and key >= startTime<p>
     * 3、endTime不为空且startTime为空，返回  and key <= endTime
     *
     * @param key
     * @param startTime
     * @param endTime
     * @return
     */
    public SqlUtils between(String key, String startTime, String endTime) {
        if (StringUtils.isNotBlank(startTime)) {
            queryCondition.append(" and ").append(key).append(" >=")
                    .append(" to_date('" + startTime + "','yyyy-mm-dd hh24:mi:ss')");
        }
        if (StringUtils.isNotBlank(endTime)) {
            queryCondition.append(" and ").append(key).append(" <=")
                    .append(" to_date('" + endTime + "','yyyy-mm-dd hh24:mi:ss')");
        }
        return this;
    }

    @Override
    public String toString() {
        return queryCondition.toString();
    }

    /**
     * 将select * from xxx 替换为  select count(1) from xxx
     *
     * @param sql sql查询语句，如：select * from xxx
     * @return
     */
    public String getCountSql(String sql) {
        if (!containsGroupBy(sql)) {
            StringBuilder regex = new StringBuilder("^select (?:(?!select|from)[\\s\\S])");
            regex.append("*(\\(select (?:(?!from)[\\s\\S])* from [^\\)]*\\)(?:(?!select|from)[^\\(])*)*from");
            Matcher m = Pattern.compile(regex.toString(), Pattern.CASE_INSENSITIVE).matcher(sql);
            while (m.find()) {
                String rg = m.group().trim().replaceAll("\\*", "\\\\\\*").replaceAll("\\(", "\\\\\\(")
                        .replaceAll("\\)", "\\\\\\)").replaceAll("\\|", "\\\\\\|");
                sql = sql.replaceFirst(rg, "select count(1) count from ");
                break;
            }
            return sql;
        } else {
            //包含group by的sql语句 直接包装一层统计sql语句
            return "select count(1) count from (" + sql + ")";
        }
    }

    /**
     * 将sql语句转换为分页的sql
     *
     * @param sqlStr   sql查询语句，如：select * from xxx
     * @param pageable 分页参数
     * @return
     */
    public String getPageSql(String sqlStr, int page, int size) {
        Integer startRow = page * size + 1;
        Integer endRow = (page + 1) * size;
        StringBuilder sql = new StringBuilder("SELECT * FROM (SELECT tt_.*, ROWNUM AS rowno FROM ( ");
        sql.append(sqlStr);
        sql.append(" ) tt_ WHERE ROWNUM <= " + endRow + ") ttt_ WHERE ttt_.rowno >= " + startRow);
        return sql.toString();
    }

    /**
     * 判断sql语句中是否包含group by 语句
     *
     * @param sql sql查询语句
     * @return true 包含group by 反之不包含
     */
    private static boolean containsGroupBy(String sql) {
        String patternStr = ".*group\\s{1,}by\\s{1,}.*";
        return Pattern.matches(patternStr, sql.toLowerCase());
    }


    /**
     * 车辆品牌获取查询条件-字段未按照国标命名-与国标命名相比 品牌字段少了gb_
     *
     * @param tableAis 表别名
     * @param brandStr 数据格式：车辆品牌-车辆子品牌，多个以“,”分割
     * @return //满足条件返回：and (vehicle_brand in('') or  vehicle_sub_brand in('')),不满足返回""
     */
    public String getBrandQueryCondition(String tableAis, String brandStr) {
        String condition = "";
        String gb_vehicle_brand = getVehicleBrand(brandStr, false);//获取父品牌
        String gb_vehicle_sub_brand = getVehicleBrand(brandStr, true); //获取子品牌

        if (StringUtils.isNotEmpty(gb_vehicle_brand) && StringUtils.isNotEmpty(gb_vehicle_sub_brand)) {
            //形成 and ( gb_vehicle_brand in('') or  gb_vehicle_sub_brand in('')) 查询条件
            condition += " and ( _tttt.vehicle_brand in ('" + gb_vehicle_brand.replaceAll(",", "','") + "')";
            condition += " or _tttt.vehicle_sub_brand in ('" + gb_vehicle_sub_brand.replaceAll(",", "','") + "') )";
        } else if (StringUtils.isNotEmpty(gb_vehicle_brand)) {
            condition += new SqlUtils().in("_tttt.vehicle_brand", gb_vehicle_brand);
        } else if (StringUtils.isNotEmpty(gb_vehicle_sub_brand)) {
            condition += new SqlUtils().in("_tttt.vehicle_sub_brand", gb_vehicle_sub_brand);
        }
        if (StringUtils.isNotEmpty(tableAis)) {
            return condition.replaceAll("_tttt", tableAis);
        } else {
            return condition.replaceAll("_tttt.", "");
        }
    }

    /**
     * 车辆品牌获取查询条件-字段按照国标命名gb_vehicle_brand，与非国标比起来 字段多了gb_
     *
     * @param tableAis 表别名
     * @param brandStr 数据格式：车辆品牌-车辆子品牌，多个以“,”分割
     * @return //满足条件返回：and ( gb_vehicle_brand in('') or  gb_vehicle_sub_brand in('')),不满足返回""
     */
    public String getGbBrandQueryCondition(String tableAis, String brandStr) {
        String condition = "";
        String gb_vehicle_brand = getVehicleBrand(brandStr, false);//获取父品牌
        String gb_vehicle_sub_brand = getVehicleBrand(brandStr, true); //获取子品牌

        if (StringUtils.isNotEmpty(gb_vehicle_brand) && StringUtils.isNotEmpty(gb_vehicle_sub_brand)) {
            //形成 and ( gb_vehicle_brand in('') or  gb_vehicle_sub_brand in('')) 查询条件
            condition += " and ( _tttt.gb_vehicle_brand in ('" + gb_vehicle_brand.replaceAll(",", "','") + "')";
            condition += " or _tttt.gb_vehicle_sub_brand in ('" + gb_vehicle_sub_brand.replaceAll(",", "','") + "') )";
        } else if (StringUtils.isNotEmpty(gb_vehicle_brand)) {
            condition += new SqlUtils().in("_tttt.gb_vehicle_brand", gb_vehicle_brand);
        } else if (StringUtils.isNotEmpty(gb_vehicle_sub_brand)) {
            condition += new SqlUtils().in("_tttt.gb_vehicle_sub_brand", gb_vehicle_sub_brand);
        }
        if (StringUtils.isNotEmpty(tableAis)) {
            return condition.replaceAll("_tttt", tableAis);
        } else {
            return condition.replaceAll("_tttt.", "");
        }
    }

    /**
     * 根据前端的车辆品牌插件传来的车辆品牌信息提取车辆品牌和子品牌
     *
     * @param brandStr 车辆品牌-格式：车辆父品牌_车辆子品牌 4,2_872,783 得到父品牌[4,2]子品牌[872,783]
     * @param subBrand 是否提取子品牌信息 true 提取子品牌 flase 提取父品牌
     * @return
     */
    public String getVehicleBrand(String brandStr, boolean subBrand) {
        String condtion = "";
        if (StringUtils.isEmpty(brandStr) || brandStr.equals("_")) {
            return condtion;
        }
        String[] brands = brandStr.split("_");//数组中第一个元素是父品牌,第二个元素是子品牌
        if (brands.length == 0 || (subBrand && brands.length == 1)) {
            return condtion;
        }
        condtion = brands[subBrand ? 1 : 0];
        return condtion;
    }
}
