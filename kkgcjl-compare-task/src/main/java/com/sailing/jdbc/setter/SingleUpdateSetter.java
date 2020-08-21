package com.sailing.jdbc.setter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.PreparedStatementSetter;

/**
 * 实现{@linkplain SingleUpdateSetter}的方法，并提供模板方法简化PreparedStatement参数注入
 *
 * @param <T>
 * @author YaoWei
 */
public class SingleUpdateSetter implements PreparedStatementSetter {
    /**
     * 保存/修改的对象
     */
    private Object record;

    String sql = new String();

    List<String> sqlParamNames = new ArrayList<String>();

    public SingleUpdateSetter(String sql, Object record) {
        this.record = record;
        this.sql = sql;
        if (this.sql.contains("INSERT") || this.sql.contains("insert")) {
            this.sqlParamNames = SqlPsUtils.getInsertSqlParamNames(sql);
        } else if (this.sql.contains("UPDATE") || this.sql.contains("update")) {
            this.sqlParamNames = SqlPsUtils.getUpdateSqlParamNames(sql);
        } else {
            this.sqlParamNames = SqlPsUtils.getDeleteSqlParamNames(sql);
        }
    }

    @Override
    public void setValues(PreparedStatement ps) throws SQLException {
        setValue(ps, record);
    }

    /**
     * 设置单个对象的参数注入
     *
     * @param ps
     * @param param 注入参数
     * @throws SQLException
     */
    @SuppressWarnings("rawtypes")
    public void setValue(PreparedStatement ps, Object param) throws SQLException {
        try {
            if (param instanceof Map) {
                SqlPsUtils.setValueForMap(ps, (Map) param, sqlParamNames);
            } else {
                SqlPsUtils.setValue(ps, param, sqlParamNames);
            }
        } catch (Exception e) {
            throw new SQLException("设置注入参数时报错", e);
        }
    }

    public String getSql() {
        return sql;
    }
}
