<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.bob.sm.mapper.${eentityName}Mapper">

    <select id="joinSelectList" resultType="com.bob.sm.domain.${eentityName}">
        ${r'${queryMain}'} ${r'${ew.customSqlSegment}'}
    </select>

    <select id="joinSelectPage" resultType="com.bob.sm.domain.${eentityName}">
        ${r'${queryMain}'} ${r'${ew.customSqlSegment}'}
    </select>

    <select id="joinSelectCount" resultType="int">
        ${r'${queryMain}'} ${r'${ew.customSqlSegment}'}
    </select>
	<#list toFromList as toFrom>

    <update id="${toFrom.toFromEntityName}IdCascadeToNull">
        UPDATE ${tableName} SET ${toFrom.fromColumnName} = null WHERE ${toFrom.fromColumnName} = ${r'#{'}${toFrom.toFromEntityName}Id}
    </update>
	</#list>

</mapper>