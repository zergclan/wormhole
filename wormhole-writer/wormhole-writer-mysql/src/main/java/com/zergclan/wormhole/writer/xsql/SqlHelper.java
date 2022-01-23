/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zergclan.wormhole.writer.xsql;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.zergclan.wormhole.core.metadata.resource.TableMetadata;

/**
 * sql helper
 */
public class SqlHelper {

	private final TableMetadata tableMetadata;

	public SqlHelper(final TableMetadata tableMetadata) {
		this.tableMetadata = tableMetadata;
	}

	/** 为查询准备SQL语句 */
	private String prepareSqlForSelect(String table, List<String> selectColumns, Map<String,String> where, String order, Integer limit) {

		StringBuilder sql = new StringBuilder();
		sql.append(" select ");
		String d = " ";
		for (String column : selectColumns) {
			sql.append(d);
			sql.append(column);
			d = ", ";
		}

		sql.append(" from ");
		sql.append(table);

		if (where != null) {
			sql.append(" where ");
			for (Map.Entry<String, String> entry : where.entrySet()) {
				// TODO
			}
			sql.append(where);
		}

		if (order != null) {
			sql.append(" order by ");
			sql.append(order);
		}

		if (limit != null) {
			sql.append(" limit ");
			sql.append(limit);
		}

		return sql.toString();
	}


	/** 查询对象 */
	public <T> T query(Class<T> type, String table, String include, String exclude, String where, Object params) throws Exception {
		String sql = prepareSqlForSelect(table, include, exclude, null, null, where);
		return xsql.query(type, sql, params);
	}


	/** 构造select子句的列名列表 */
	public String columns(String table, String alias, String field, String include, String exclude) throws Exception {
		TableInfo ti = getTableInfo(table);

		StringBuilder sb = new StringBuilder();
		for (String c : filterColumns(ti, include, exclude)) {
			if (sb.length() > 0) sb.append(", ");
			sb.append(alias).append('.').append(c);
			sb.append(' ');
			sb.append('"').append(field).append('.').append(c).append('"');
		}
		return sb.toString();
	}

	/** 插入对象 */
	public int insert(Object bean, String table, String include, String exclude) throws Exception {
		TableInfo ti = getTableInfo(table);
		ClassInfo ci = getClassInfo(bean);

		Map<String, Field> fields = ci.getFields();

		StringBuilder columns = new StringBuilder();
		StringBuilder values = new StringBuilder();

		String d = "";
		for (String column : filterColumns(ti, include, exclude)) {
			String name = XsqlUtils.sqlToJava(column);
			Field field = fields.get(name);
			if (field == null) throw new Exception("字段不存在: " + bean.getClass() + ": " + name);

			Object value = field.get(bean);
			if (value != null) {
				columns.append(d);
				columns.append(column);
				values.append(d);
				values.append("$").append(map.get(field.getType())).append("{").append(column).append("}");
				d = ", ";
			}
		}

		String sql = String.format("insert into %s(%s) values (%s)", table, columns.toString(), values.toString());

		return xsql.update(sql, bean);
	}

	/** 更新对象 */
	public int update(Object bean, String table, String include, String exclude, String where) throws Exception {
		TableInfo ti = getTableInfo(table);
		ClassInfo ci = getClassInfo(bean);

		Map<String, Field> fields = ci.getFields();

		StringBuilder sql = new StringBuilder();
		sql.append("update ");
		sql.append(table);
		sql.append(" set ");

		String d = "";
		for (String column : filterColumns(ti, include, exclude)) {
			String name = XsqlUtils.sqlToJava(column);
			Field field = fields.get(name);
			if (field == null) throw new Exception("字段不存在: " + bean.getClass() + ":" + name);

			sql.append(d);
			sql.append(column).append(" = ");
			sql.append("$").append(map.get(field.getType())).append("{").append(column).append("}");
			d = ", ";
		}

		if (where != null) {
			sql.append(" where ");
			sql.append(where);
		}

		return xsql.update(sql.toString(), bean);
	}

	/** 删除对象 */
	public int delete(String table, String where, Object... parameters) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("delete from ");
		sql.append(table);
		if (where != null) {
			sql.append(" where ");
			sql.append(where);
		}

		return xsql.update(sql.toString(), parameters);
	}

	// ==================================

	/** 为查询准备SQL语句 */
	private String prepareSqlForSelect(String table, String include, String exclude, String order, Integer limit, String where)
			throws Exception {

		List<String> columns = filterColumns(ti, include, exclude);
		StringBuilder sql = new StringBuilder();
		sql.append("select ");
		String d = "";
		for (String column : columns) {
			sql.append(d);
			sql.append(column);
			d = ", ";
		}

		sql.append(" from ");
		sql.append(table);

		if (where != null) {
			sql.append(" where ");
			sql.append(where);
		}

		if (order != null) {
			sql.append(" order by ");
			sql.append(order);
		}

		if (limit != null) {
			sql.append(" limit ");
			sql.append(limit);
		}

		return sql.toString();
	}

	/** 根据include, exclude过滤表列 */
	private static List<String> filterColumns(TableInfo ti, String include, String exclude) {
		if (include != null) include = include.trim();
		if (exclude != null) exclude = exclude.trim();

		if (include != null && !include.equals("")) {
			return Arrays.asList(include.split("[ \t]*,[ \t]*"));
		}

		if (exclude != null && !exclude.equals("")) {
			String[] ss = exclude.split("[ \t]*,[ \t]*");

			List<String> list = new ArrayList<String>();
			outter: for (String s1 : ti.getColumns()) {
				for (String s2 : ss) {
					if (s1.equals(s2)) continue outter;
				}

				list.add(s1);
			}

			return list;
		}

		return ti.getColumns();
	}

}
