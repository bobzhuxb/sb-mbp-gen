package com.bob.sm.config;

import com.bob.sm.domain.BaseDomain;
import com.bob.sm.dto.BaseDTO;
import com.bob.sm.dto.criteria.BaseCriteria;
import com.bob.sm.dto.help.BaseEntityConfigDTO;
import com.bob.sm.dto.help.BaseEntityConfigDicDTO;
import com.bob.sm.dto.help.BaseEntityConfigRelationDTO;
import com.bob.sm.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GlobalCache {

    // 只在启动的时候初始化一次
    private static Map<String, BaseEntityConfigDTO> entityConfigMap = new HashMap<>();

    private static Map<String, BaseService> serviceMap = new HashMap<>();

    private static Map<String, Class<? extends BaseDomain>> domainClassMap = new HashMap<>();

    private static Map<String, Class<? extends BaseCriteria>> criteriaClassMap = new HashMap<>();

    private static Map<String, Class<? extends BaseDTO>> dtoClassMap = new HashMap<>();

    @Autowired
    public GlobalCache(BaseDictionaryService baseDictionaryService,
                       SfstCondimentShowService sfstCondimentShowService,
                       SfstDishService sfstDishService,
                       SfstDishMaterialService sfstDishMaterialService,
                       SfstDishPicService sfstDishPicService,
                       SfstExcelImportHisService sfstExcelImportHisService,
                       SfstFinancialBillService sfstFinancialBillService,
                       SfstMaterialService sfstMaterialService,
                       SfstMaterialPicService sfstMaterialPicService,
                       SfstMaterialPriceService sfstMaterialPriceService,
                       SfstMenuService sfstMenuService,
                       SfstOrderBillService sfstOrderBillService,
                       SfstSchoolService sfstSchoolService,
                       SfstSchoolBatchService sfstSchoolBatchService,
                       SfstSchoolCertificateService sfstSchoolCertificateService,
                       SfstSchoolMaterialAttrService sfstSchoolMaterialAttrService,
                       SfstSchoolMaterialStockRecordService sfstSchoolMaterialStockRecordService,
                       SfstSchoolOrderService sfstSchoolOrderService,
                       SfstSchoolOutboundRecordService sfstSchoolOutboundRecordService,
                       SfstSchoolSupplierService sfstSchoolSupplierService,
                       SfstSchoolTypeService sfstSchoolTypeService,
                       SfstSubMenuService sfstSubMenuService,
                       SfstSubMenuDishService sfstSubMenuDishService,
                       SfstSubMenuDishSchoolBatchService sfstSubMenuDishSchoolBatchService,
                       SfstSupplierService sfstSupplierService,
                       SfstSupplierInboundBatchService sfstSupplierInboundBatchService,
                       SfstSupplierInboundOrderService sfstSupplierInboundOrderService,
                       SfstSupplierLicenseService sfstSupplierLicenseService,
                       SfstTestRecordService sfstTestRecordService,
                       SfstTestRecordBatchService sfstTestRecordBatchService,
                       SfstTestRecordReportService sfstTestRecordReportService,
                       SystemLogService systemLogService,
                       SystemOrganizationService systemOrganizationService,
                       SystemPermissionService systemPermissionService,
                       SystemResourceService systemResourceService,
                       SystemResourcePermissionService systemResourcePermissionService,
                       SystemRoleService systemRoleService,
                       SystemRoleResourceService systemRoleResourceService,
                       SystemUserService systemUserService,
                       SystemUserResourceService systemUserResourceService,
                       SystemUserRoleService systemUserRoleService) {
        serviceMap = new HashMap<String, BaseService>() {{
			put("BaseDictionary", baseDictionaryService);
			put("SfstCondimentShow", sfstCondimentShowService);
			put("SfstDish", sfstDishService);
			put("SfstDishMaterial", sfstDishMaterialService);
			put("SfstDishPic", sfstDishPicService);
			put("SfstExcelImportHis", sfstExcelImportHisService);
			put("SfstFinancialBill", sfstFinancialBillService);
			put("SfstMaterial", sfstMaterialService);
			put("SfstMaterialPic", sfstMaterialPicService);
			put("SfstMaterialPrice", sfstMaterialPriceService);
			put("SfstMenu", sfstMenuService);
			put("SfstOrderBill", sfstOrderBillService);
			put("SfstSchool", sfstSchoolService);
			put("SfstSchoolBatch", sfstSchoolBatchService);
			put("SfstSchoolCertificate", sfstSchoolCertificateService);
			put("SfstSchoolMaterialAttr", sfstSchoolMaterialAttrService);
			put("SfstSchoolMaterialStockRecord", sfstSchoolMaterialStockRecordService);
			put("SfstSchoolOrder", sfstSchoolOrderService);
			put("SfstSchoolOutboundRecord", sfstSchoolOutboundRecordService);
			put("SfstSchoolSupplier", sfstSchoolSupplierService);
			put("SfstSchoolType", sfstSchoolTypeService);
			put("SfstSubMenu", sfstSubMenuService);
			put("SfstSubMenuDish", sfstSubMenuDishService);
			put("SfstSubMenuDishSchoolBatch", sfstSubMenuDishSchoolBatchService);
			put("SfstSupplier", sfstSupplierService);
			put("SfstSupplierInboundBatch", sfstSupplierInboundBatchService);
			put("SfstSupplierInboundOrder", sfstSupplierInboundOrderService);
			put("SfstSupplierLicense", sfstSupplierLicenseService);
			put("SfstTestRecord", sfstTestRecordService);
			put("SfstTestRecordBatch", sfstTestRecordBatchService);
			put("SfstTestRecordReport", sfstTestRecordReportService);
			put("SystemLog", systemLogService);
			put("SystemOrganization", systemOrganizationService);
			put("SystemPermission", systemPermissionService);
			put("SystemResource", systemResourceService);
			put("SystemResourcePermission", systemResourcePermissionService);
			put("SystemRole", systemRoleService);
			put("SystemRoleResource", systemRoleResourceService);
			put("SystemUser", systemUserService);
			put("SystemUserResource", systemUserResourceService);
			put("SystemUserRole", systemUserRoleService);
        }};
        for (String entityName : entityNames) {
            Class domainClass = Class.forName("com.bob.sm.domain." + entityName);
            domainClassMap.put(entityName, domainClass);
            Class criteriaClass = Class.forName("com.bob.sm.dto.criteria." + entityName + "Criteria");
            criteriaClassMap.put(entityName, criteriaClass);
            Class dtoClass = Class.forName("com.bob.sm.dto." + entityName + "DTO");
            dtoClassMap.put(entityName, dtoClass);
        }
    }

    private static List<String> entityNames = Arrays.asList("BaseDictionary", "SfstCondimentShow", "SfstDish", "SfstDishMaterial",
            "SfstDishPic", "SfstExcelImportHis", "SfstFinancialBill", "SfstMaterial", "SfstMaterialPic", "SfstMaterialPrice",
            "SfstMenu", "SfstOrderBill", "SfstSchool", "SfstSchoolBatch", "SfstSchoolCertificate", "SfstSchoolMaterialAttr",
            "SfstSchoolMaterialStockRecord", "SfstSchoolOrder", "SfstSchoolOutboundRecord", "SfstSchoolSupplier",
            "SfstSchoolType", "SfstSubMenu", "SfstSubMenuDish", "SfstSubMenuDishSchoolBatch", "SfstSupplier",
            "SfstSupplierInboundBatch", "SfstSupplierInboundOrder", "SfstSupplierLicense", "SfstTestRecord", "SfstTestRecordBatch",
            "SfstTestRecordReport", "SystemLog", "SystemOrganization", "SystemPermission", "SystemResource",
            "SystemResourcePermission", "SystemRole", "SystemRoleResource", "SystemUser", "SystemUserResource", "SystemUserRole");

    private static Map<String, List<BaseEntityConfigDicDTO>> entityDicNameMap = new HashMap<String, List<BaseEntityConfigDicDTO>>() {{
        put("SfstSchoolType", Arrays.asList(
                new BaseEntityConfigDicDTO("typeCode", "typeValue", "type_code", "type_value", "SCHOOL_TYPE")
        ));
        put("SfstMaterial", Arrays.asList(
                new BaseEntityConfigDicDTO("categoryCode", "categoryValue", "category_code", "category_value", "MATERIAL_TYPE"),
                new BaseEntityConfigDicDTO("unitCode", "unitValue", "unit_code", "unit_value", "MATERIAL_UNIT")
        ));
        put("SfstSchoolOutboundRecord", Arrays.asList(
                new BaseEntityConfigDicDTO("outboundTypeCode", "outboundTypeValue", "outbound_type_code", "outbound_type_value", "SCHOOL_OUTBOUND_TYPE")
        ));
    }};

    private static Map<String, List<BaseEntityConfigRelationDTO>> entityRelationsMap = new HashMap<String, List<BaseEntityConfigRelationDTO>>() {{
        put("SfstSchoolOrder", Arrays.asList(
                new BaseEntityConfigRelationDTO("OneToOne", "from", "SfstSchoolOrder", "originalOrder", "SfstSchoolOrder", "newestOrder"),
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SfstSchoolOrder", "sfstSchoolBatch", "SfstSchoolBatch", "sfstSchoolOrder"),
                new BaseEntityConfigRelationDTO("OneToOne", "to", "SfstSchoolOrder", "newestOrder", "SfstSchoolOrder", "originalOrder"),
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SfstSupplier", "sfstSchoolOrder", "SfstSchoolOrder", "sfstSupplier")
        ));
    }};

    public static List<String> getEntityNames() {
        return entityNames;
    }

    public static Map<String, BaseEntityConfigDTO> getEntityConfigMap() {
        return entityConfigMap;
    }

    public static Map<String, List<BaseEntityConfigDicDTO>> getEntityDicNameMap() {
        return entityDicNameMap;
    }

    public static Map<String, BaseService> getServiceMap() {
        return serviceMap;
    }

    public static Map<String, Class<? extends BaseDomain>> getDomainClassMap() {
        return domainClassMap;
    }

    public static Map<String, Class<? extends BaseCriteria>> getCriteriaClassMap() {
        return criteriaClassMap;
    }

    public static Map<String, Class<? extends BaseDTO>> getDtoClassMap() {
        return dtoClassMap;
    }

    public static Map<String, List<BaseEntityConfigRelationDTO>> getEntityRelationsMap() {
        return entityRelationsMap;
    }
}
