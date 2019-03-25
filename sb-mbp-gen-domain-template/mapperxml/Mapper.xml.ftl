<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="${packageName}.mapper.${eentityName}Mapper">

    <update id="cascadeToNull">
        UPDATE ${tableName} SET ${r'${relationColumnName}'} = null WHERE ${r'${relationColumnName}'} = ${r'#{'}relationId}
    </update>

    <select id="joinSelectList" resultType="${packageName}.domain.${eentityName}">
        ${r'${queryMain}'} ${r'${ew.customSqlSegment}'}
    </select>

    <select id="joinSelectPage" resultType="${packageName}.domain.${eentityName}">
        ${r'${queryMain}'} ${r'${ew.customSqlSegment}'}
    </select>

    <select id="joinSelectCount" resultType="int">
        ${r'${queryMain}'} ${r'${ew.customSqlSegment}'}
    </select>

    <!-- 自定义SQL语句写在这里 -->

</mapper>