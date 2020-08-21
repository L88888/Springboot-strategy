package com.sailing.jdbc.setter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;

/**
 * 实现{@linkplain BatchPreparedStatementSetter}的方法，并提供模板方法简化PreparedStatement参数注入
 *
 * @param <T>
 * @author YaoWei
 */
public class BatchUpdateSetter implements BatchPreparedStatementSetter {
    /**
     * 批量保存/修改的对象
     */
    private List<?> batchSaveRecords;

    String sql = new String();

    List<String> sqlParamNames = new ArrayList<String>();

    public BatchUpdateSetter(String sql) {
        batchSaveRecords = new ArrayList<Object>();
        this.sql = sql;
        if (this.sql.contains("INSERT") || this.sql.contains("insert")) {
            this.sqlParamNames = SqlPsUtils.getInsertSqlParamNames(sql);
        } else if (this.sql.contains("UPDATE") || this.sql.contains("update")) {
            this.sqlParamNames = SqlPsUtils.getUpdateSqlParamNames(sql);
        } else {
            this.sqlParamNames = SqlPsUtils.getDeleteSqlParamNames(sql);
        }
    }

    public BatchUpdateSetter(String sql, List<?> batchSaveRecords) {
        this.batchSaveRecords = batchSaveRecords;
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
    public void setValues(PreparedStatement ps, int index) throws SQLException {
        setValue(ps, batchSaveRecords.get(index));
    }

    @Override
    public int getBatchSize() {
        return batchSaveRecords.size();
    }

    /**
     * 将java.uitl.Date转换为java.sql.Date，如果参数为null 则返回null
     *
     * @param date java.uitl.Date
     * @return
     */
    public java.sql.Date paresDate(Date date) {
        return date == null ? null : new java.sql.Date(date.getTime());
    }

    /**
     * 设置单个对象的参数注入
     *
     * @param ps
     * @param param
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
