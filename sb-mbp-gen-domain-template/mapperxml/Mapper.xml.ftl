<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="${packageName}.mapper.${eentityName}Mapper">
	<#if eentityName == 'SystemPermission'>

    <select id="findPermissionIdentifyByLogin" resultType="string">
        SELECT spep.permission_identify FROM system_page_element_permission spep
        LEFT JOIN system_role_page_element srpe ON srpe.page_element_code = spep.page_element_code
        LEFT JOIN system_user_role sur ON sur.system_role_id = srpe.role_id
        LEFT JOIN system_user su ON su.id = sur.system_user_id
        WHERE su.login = ${r'#{'}login}
    </select>
	</#if>

    <!-- 自定义SQL语句写在这里 -->

</mapper>