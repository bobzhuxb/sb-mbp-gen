package com.bob.freemarker.module;

import com.bob.freemarker.dto.*;
import com.bob.freemarker.init.AfterInitRunner;
import com.bob.freemarker.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据库操作模块
 */
public class DbModule {

    private final static Logger log = LoggerFactory.getLogger(AfterInitRunner.class);

    private static String dbIp = null;

    private static String dbPort = null;

    private static String dbName = null;

    private static String dbUsername = null;

    private static String dbPassword = null;

    private static String allowChangeTable = null;

    private static List<String> deleteTableNameStartList = null;

    private static Connection connection = null;

    private static Statement statement = null;

    /**
     * 初始化DB参数
     * @param dbIp
     * @param dbPort
     * @param dbName
     * @param dbUsername
     * @param dbPassword
     * @param allowChangeTable
     */
    public static void initDbParam(String dbIp, String dbPort, String dbName,
                                   String dbUsername, String dbPassword, String allowChangeTable,
                                   List<String> deleteTableNameStartList) {
        DbModule.dbIp = dbIp;
        DbModule.dbPort = dbPort;
        DbModule.dbName = dbName;
        DbModule.dbUsername = dbUsername;
        DbModule.dbPassword = dbPassword;
        DbModule.allowChangeTable = allowChangeTable;
        DbModule.deleteTableNameStartList = deleteTableNameStartList;
    }

    /**
     * 获取数据库连接
     * @return
     * @throws Exception
     */
    public static Connection getConnection() throws Exception {
        if (connection != null) {
            return connection;
        }
        Class.forName("com.mysql.cj.jdbc.Driver");
        String connectionUrl = "jdbc:mysql://" + dbIp + ":" + dbPort + "/" + dbName
                + "?serverTimezone=UTC&useUnicode=true&characterEncoding=utf8&useSSL=false&useLegacyDatetimeCode=false"
                + "&allowPublicKeyRetrieval=true";
        connection = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword);
        return connection;
    }

    /**
     * 关闭数据库连接
     * @throws Exception
     */
    public static void closeConnection() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }

    /**
     * 根据实体创建或更新表（同时处理数据库连接资源）
     * @param erdto
     * @param defaultColumnValue 数据库列的默认值的设定方式
     * @param changeColumnSingle 数据库是否一列一列修改（yes：是 no：否）
     * @throws Exception
     */
    public static void createEntityTables(ERDTO erdto, String defaultColumnValue, String changeColumnSingle) throws Exception {
        try {
            getConnection();
            createEntityTablesDirect(erdto, defaultColumnValue, changeColumnSingle);
            initDbData();
        } finally {
            if (connection != null) {
                connection.close();
                connection = null;
            }
        }
    }

    /**
     * 根据实体创建或更新表
     * @param erdto
     * @param defaultColumnValue 数据库列的默认值的设定方式
     * @param changeColumnSingle 数据库是否一列一列修改（yes：是 no：否）
     * @throws Exception
     */
    public static void createEntityTablesDirect(ERDTO erdto, String defaultColumnValue, String changeColumnSingle) throws Exception {
        List<EntityDTO> entityDTOList = erdto.getEntityDTOList();
        List<RelationshipDTO> relationshipDTOList = erdto.getRelationshipDTOList();
        Statement statement = connection.createStatement();
        List<DbTableDTO> tableExistList = getAllTables();
        // 删除已经不用的表
        if ("yes".equalsIgnoreCase(allowChangeTable) && deleteTableNameStartList != null
                && deleteTableNameStartList.size() > 0) {
            // 若允许修改表，则可进行删除表操作
            // 不删除允许范围（指定表名的开头）外的表
            List<String> toDeleteTableNameList = new ArrayList<>();
            for (DbTableDTO tableDTOExist : tableExistList) {
                // 表名开头是否符合要求，不符合要求的表不要删除
                boolean tableNameStartFix = false;
                for (String deleteTableNameStart : deleteTableNameStartList) {
                    if (tableDTOExist.getTableName().toLowerCase().startsWith(deleteTableNameStart.toLowerCase())) {
                        tableNameStartFix = true;
                        break;
                    }
                }
                if (tableNameStartFix) {
                    // 本次仍然存在的表就不用删除了
                    boolean tableStillExist = false;
                    for (EntityDTO entityDTO: entityDTOList) {
                        if (entityDTO.getTableName().equalsIgnoreCase(tableDTOExist.getTableName())) {
                            tableStillExist = true;
                            break;
                        }
                    }
                    if (!tableStillExist) {
                        // 本次不存在的表要删除
                        toDeleteTableNameList.add(tableDTOExist.getTableName());
                    }
                }
            }
            for (String toDeleteTableName : toDeleteTableNameList) {
                statement.executeUpdate("drop table `" + toDeleteTableName + "`");
            }
        }
        // 附加的关联字段
        Map<String, List<RelationshipDTO>> relationColumnMap = new HashMap<>();
        for (RelationshipDTO relationshipDTO : relationshipDTOList) {
            String fromToEntityType = relationshipDTO.getFromToEntityType();
            List<RelationshipDTO> relationColumnList = relationColumnMap.get(fromToEntityType);
            if (relationColumnList == null) {
                relationColumnList = new ArrayList<>();
                relationColumnMap.put(fromToEntityType, relationColumnList);
            }
            relationColumnList.add(relationshipDTO);
        }
        for (EntityDTO entityDTO : entityDTOList) {
            String tableName = entityDTO.getTableName();
            // 判断是否已经存在表
            DbTableDTO dbTableExist = null;
            for (DbTableDTO tableExist : tableExistList) {
                if (tableName.equals(tableExist.getTableName())) {
                    dbTableExist = tableExist;
                    break;
                }
            }
            if (dbTableExist == null) {
                // 表不存在，新建
                String createSql = "create table " + tableName + "(";
                createSql += "`id` char(50) not null primary key comment '主键', ";
                for (int i = 0; i < entityDTO.getFieldList().size(); i++) {
                    EntityFieldDTO fieldDTO = entityDTO.getFieldList().get(i);
                    String columnName = StringUtil.camelToUnderline(fieldDTO.getCamelName());
                    if ("id".equals(fieldDTO.getCamelName())) {
                        continue;
                    } else {
                        createSql += "`" + columnName + "` " + fieldDTO.getColumnType() + " comment '" + fieldDTO.getComment() + "'";
                        // 数据库列的默认值设定
                        String defaultAppend = appendDefaultValue(defaultColumnValue, fieldDTO);
                        if (defaultAppend != null) {
                            createSql += " default " + defaultAppend;
                        }
                    }
                    if (i < entityDTO.getFieldList().size() - 1) {
                        createSql += ", ";
                    }
                }
                // 附加的关联字段
                List<RelationshipDTO> relationColumnList = relationColumnMap.get(entityDTO.getEentityName());
                if (relationColumnList != null && relationColumnList.size() > 0) {
                    for (RelationshipDTO relationColumn : relationColumnList) {
                        String relationColumnName = StringUtil.camelToUnderline(relationColumn.getToFromEntityName()) + "_id";
                        String relationComment = relationColumn.getToFromComment() + "ID";
                        createSql += ", `" + relationColumnName + "` char(50) comment '" + relationComment + "'";
                        // 数据库列的默认值设定
                        if ("notnull".equals(defaultColumnValue)) {
                            createSql += "not null default ''";
                        }
                    }
                }
                createSql += ") COMMENT='" + entityDTO.getEntityComment() + "'";
                int infectLines = statement.executeUpdate(createSql);
                log.info("表" + entityDTO.getTableName() + "创建完成。");
            } else {
                // 表存在，修改
                if (!"yes".equalsIgnoreCase(allowChangeTable)) {
                    // 若禁止程序修改表，则跳过
                    continue;
                }
                // 获取现有的字段
                List<String> toDeleteColumnNameList = dbTableExist.getColumnList().stream()
                        .map(DbColumnDTO::getColumnName).collect(Collectors.toList());
                // 主键ID不允许变更
                toDeleteColumnNameList.remove("id");
                // 共通的alter table
                String alterTablePrefix = "alter table `" + tableName + "`";
                StringJoiner alterColumnSb = new StringJoiner(",");
                // entity自带字段
                for (EntityFieldDTO fieldDTO : entityDTO.getFieldList()) {
                    String newColumnName = StringUtil.camelToUnderline(fieldDTO.getCamelName());
                    String newColumnType = fieldDTO.getColumnType();
                    String newColumnComment = fieldDTO.getComment();
                    if (!toDeleteColumnNameList.contains(newColumnName)) {
                        // 列不存在，新增列（类型、注释、是否允许空、默认值）
                        String alterColumnStr = " add `" + newColumnName + "` "
                                + newColumnType + " comment '" + newColumnComment + "'";
                        // 数据库列的默认值设定
                        String defaultAppend = appendDefaultValue(defaultColumnValue, fieldDTO);
                        if (defaultAppend != null) {
                            alterColumnStr += " not null default " + defaultAppend;
                        }
                        // 为整表改表做准备
                        alterColumnSb.add(alterColumnStr);
                        // 单列改表
                        if ("yes".equals(changeColumnSingle)) {
                            statement.executeUpdate(alterTablePrefix + alterColumnStr);
                        }
                    } else {
                        // 列已经存在
                        for (DbColumnDTO dbColumnExist : dbTableExist.getColumnList()) {
                            if (dbColumnExist.getColumnName().equals(newColumnName)) {
                                // 直接更新列（类型、注释、是否允许空、默认值）
                                String alterColumnStr = " modify column `"
                                        + newColumnName + "` " + newColumnType + " comment '" + newColumnComment + "'";
                                // 数据库列的默认值设定
                                String defaultAppend = appendDefaultValue(defaultColumnValue, fieldDTO);
                                if (defaultAppend != null) {
                                    alterColumnStr += " not null default " + defaultAppend;
                                }
                                // 为整表改表做准备
                                alterColumnSb.add(alterColumnStr);
                                if ("yes".equals(changeColumnSingle)) {
                                    try {
                                        // 单列改表
                                        statement.executeUpdate(alterTablePrefix + alterColumnStr);
                                    } catch (Exception e) {
                                        System.out.println("【SQL改库错误】：" + alterTablePrefix + alterColumnStr);
                                        throw e;
                                    }
                                }
                                break;
                            }
                        }
                        // 从待删除字段列表中移除
                        toDeleteColumnNameList.remove(newColumnName);
                    }
                }
                // 附加的关联字段
                List<RelationshipDTO> relationColumnList = relationColumnMap.get(entityDTO.getEentityName());
                if (relationColumnList != null && relationColumnList.size() > 0) {
                    for (RelationshipDTO relationColumn : relationColumnList) {
                        String relationColumnName = StringUtil.camelToUnderline(relationColumn.getToFromEntityName()) + "_id";
                        String relationComment = relationColumn.getToFromComment() + "ID";
                        if (!toDeleteColumnNameList.contains(relationColumnName)) {
                            // 列不存在，新增列
                            String alterColumnStr = " add `" + relationColumnName
                                    + "` char(50) comment '" + relationComment + "'";
                            // 数据库列的默认值设定
                            if ("notnull".equals(defaultColumnValue)) {
                                alterColumnStr += " default ''";
                            }
                            // 为整表改表做准备
                            alterColumnSb.add(alterColumnStr);
                            // 单列改表
                            if ("yes".equals(changeColumnSingle)) {
                                statement.executeUpdate(alterTablePrefix + alterColumnStr);
                            }
                        } else {
                            // 列已经存在
                            for (DbColumnDTO dbColumnExist : dbTableExist.getColumnList()) {
                                if (dbColumnExist.getColumnName().equals(relationColumnName)) {
                                    // 直接更新列类型和注释
                                    String alterColumnStr = " modify column `"
                                            + relationColumnName + "` char(50) comment '" + relationComment + "'";
                                    // 数据库列的默认值设定
                                    if ("notnull".equals(defaultColumnValue)) {
                                        alterColumnStr += " default ''";
                                    }
                                    // 为整表改表做准备
                                    alterColumnSb.add(alterColumnStr);
                                    // 单列改表
                                    if ("yes".equals(changeColumnSingle)) {
                                        statement.executeUpdate(alterTablePrefix + alterColumnStr);
                                    }
                                    break;
                                }
                            }
                            // 从列表中移除
                            toDeleteColumnNameList.remove(relationColumnName);
                        }
                    }
                }
                for (String toDeleteColumn : toDeleteColumnNameList) {
                    // 移除已经去掉的列
                    String alterColumnStr = " drop column `" + toDeleteColumn + "`";
                    // 为整表改表做准备
                    alterColumnSb.add(alterColumnStr);
                    // 单列改表
                    if ("yes".equals(changeColumnSingle)) {
                        statement.executeUpdate(alterTablePrefix + alterColumnStr);
                    }
                }
                if ("no".equals(changeColumnSingle)) {
                    // 整体改表
                    if (alterColumnSb.length() > 0) {
                        statement.executeUpdate(alterTablePrefix + alterColumnSb.toString());
                    }
                }
                log.info("表" + entityDTO.getTableName() + "修改完成。");
            }
        }
    }

    /**
     * 初始化用户表、角色表、用户角色关系表数据（只加入admin账号及ROLE_ADMIN角色）
     * @throws Exception
     */
    private static void initDbData() throws Exception {
        String nowTimeStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select count(0) from system_user");
        while (resultSet.next()) {
            int count = resultSet.getInt(1);
            if (count == 0) {
                // 数据表尚未初始化，开始初始化
                statement.execute("truncate table system_user");
                statement.execute("truncate table system_role");
                statement.execute("truncate table system_user_role");
                statement.execute("INSERT INTO system_user(id, login, password, name, cell, " +
                        "identify_no, memo, insert_time) VALUES ('1', 'admin', " +
                        "'$2a$10$PQkBezu.nvPOSenQXu/WxOMQtKj1j5ybjELKRfxr8uLeU8NCRBhDq', '超级管理员', '12345678901', " +
                        "'123456789012345678', '仅供开发人员使用', '" + nowTimeStr + "')");
                statement.execute("INSERT INTO system_role(id, name, chinese_name, insert_user_id, " +
                        "operate_user_id, insert_time) VALUES ('1', 'ROLE_ADMIN', '管理员', " +
                        "'1', '1', '" + nowTimeStr + "')");
                statement.execute("INSERT INTO system_user_role(id, insert_user_id, operate_user_id, insert_time, " +
                        "system_user_id, system_role_id) VALUES ('1', '1', '1', '" + nowTimeStr +
                        "', '1', '1')");
            }
            break;
        }
    }

    /**
     * 获取数据库列的默认值
     * @param defaultColumnValue
     * @param fieldDTO
     * @return
     */
    private static String appendDefaultValue(String defaultColumnValue, EntityFieldDTO fieldDTO) {
        if ("notnull".equals(defaultColumnValue)) {
            // 配置项：数据库列要使用默认值
            // 不允许有默认值的字段类型
            List<String> columnTypeNoDefaultList = new ArrayList<>(Arrays.asList("blob", "text", "longtext", "geometry", "json"));
            if (columnTypeNoDefaultList.contains(fieldDTO.getColumnType().toLowerCase())) {
                return null;
            }
            if (fieldDTO.getColumnDefaultValue() != null) {
                // 字段填写了默认值的
                String columnDefaultValue = fieldDTO.getColumnDefaultValue();
                if ("String".equals(fieldDTO.getJavaType())) {
                    // 字符串需加上引号
                    columnDefaultValue = "'" + columnDefaultValue + "'";
                }
                return columnDefaultValue;
            } else {
                // 字段没有填写默认值的
                if ("Integer".equals(fieldDTO.getJavaType()) || "Long".equals(fieldDTO.getJavaType())) {
                    return "0";
                } else if ("Double".equals(fieldDTO.getJavaType())) {
                    return "0.0";
                } else {
                    // String或其他
                    return "''";
                }
            }
        } else {
            return null;
        }
    }

    /**
     * 将Java类型转换为数据库列的类型
     * @param camelName 字段名称
     * @param javaType Java类型
     * @param columnType 列类型
     * @return
     */
    public static String convertJavaTypeToColumnType(String camelName, String javaType, String columnType) {
        if (columnType != null) {
            return columnType;
        }
        // 系统添加的默认的时间字段
        if ("insertTime".equals(camelName) || "updateTime".equals(camelName)) {
            return "varchar(20)";
        }
        columnType = "varchar(255)";
        if ("Integer".equals(javaType)) {
            columnType = "int(11)";
        } else if ("Long".equals(javaType)) {
            columnType = "bigint(20)";
        } else if ("Double".equals(javaType)) {
            columnType = "double(11, 2)";
        }
        return columnType;
    }

    /**
     * 获取数据库的所有表
     * @return
     * @throws Exception
     */
    public static List<DbTableDTO> getAllTables() throws Exception {
        Connection connection = getConnection();
        List<DbTableDTO> tableList = new ArrayList<>();
        Statement statementTable = connection.createStatement();
        ResultSet resultSetTable = statementTable.executeQuery("select table_name, table_comment from " +
                "information_schema.tables where table_schema = '" + dbName + "'");
        while (resultSetTable.next()) {
            DbTableDTO tableDTO = new DbTableDTO();
            String tableName = resultSetTable.getString("table_name");
            String tableComment = resultSetTable.getString("table_comment");
            tableDTO.setTableName(tableName);
            tableDTO.setTableComment(tableComment);
            // 获取列信息
            List<DbColumnDTO> columnList = new ArrayList<>();
            Statement statementColumn = connection.createStatement();
            ResultSet resultSetColumn = statementColumn.executeQuery("show full columns from " + tableName);
            while (resultSetColumn.next()) {
                DbColumnDTO dbColumnDTO = new DbColumnDTO();
                dbColumnDTO.setColumnName(resultSetColumn.getString("Field"));
                dbColumnDTO.setColumnType(resultSetColumn.getString("Type"));
                dbColumnDTO.setColumnComment(resultSetColumn.getString("Comment"));
                columnList.add(dbColumnDTO);
            }
            tableDTO.setColumnList(columnList);

            tableList.add(tableDTO);
        }
        return tableList;
    }

}
