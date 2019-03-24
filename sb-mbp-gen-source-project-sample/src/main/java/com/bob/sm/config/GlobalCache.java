package com.bob.sm.config;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bob.sm.domain.BaseDomain;
import com.bob.sm.dto.BaseDTO;
import com.bob.sm.dto.criteria.BaseCriteria;
import com.bob.sm.dto.help.BaseEntityConfigDTO;
import com.bob.sm.dto.help.BaseEntityConfigDicDTO;
import com.bob.sm.dto.help.BaseEntityConfigRelationDTO;
import com.bob.sm.service.*;
import com.bob.sm.mapper.*;
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

    private static Map<String, BaseMapper> mapperMap = new HashMap<>();

    private static CommonUserService commonUserService;

    private static Map<String, Class<? extends BaseDomain>> domainClassMap = new HashMap<>();

    private static Map<String, Class<? extends BaseCriteria>> criteriaClassMap = new HashMap<>();

    private static Map<String, Class<? extends BaseDTO>> dtoClassMap = new HashMap<>();

    private static CommonUserService commonUserService;

    @Autowired
    public GlobalCache(
            SystemUserService systemUserService,
            SystemRoleService systemRoleService,
            SystemUserRoleService systemUserRoleService,
            SystemPermissionService systemPermissionService,
            SystemResourceService systemResourceService,
            SystemResourcePermissionService systemResourcePermissionService,
            SystemRoleResourceService systemRoleResourceService,
            SystemUserResourceService systemUserResourceService,
            SystemOrganizationService systemOrganizationService,
            SystemLogService systemLogService,
            BaseDictionaryService baseDictionaryService,
            SfstSupplierService sfstSupplierService,
            SfstSupplierLicenseService sfstSupplierLicenseService,
            SfstSchoolService sfstSchoolService,
            SfstSchoolCertificateService sfstSchoolCertificateService,
            SfstSchoolTypeService sfstSchoolTypeService,
            SfstSchoolSupplierService sfstSchoolSupplierService,
            SfstMaterialService sfstMaterialService,
            SfstMaterialPicService sfstMaterialPicService,
            SfstMaterialPriceService sfstMaterialPriceService,
            SfstSchoolMaterialAttrService sfstSchoolMaterialAttrService,
            SfstSupplierInboundOrderService sfstSupplierInboundOrderService,
            SfstSupplierInboundBatchService sfstSupplierInboundBatchService,
            SfstTestRecordService sfstTestRecordService,
            SfstTestRecordReportService sfstTestRecordReportService,
            SfstTestRecordBatchService sfstTestRecordBatchService,
            SfstSchoolBatchService sfstSchoolBatchService,
            SfstSchoolMaterialStockRecordService sfstSchoolMaterialStockRecordService,
            SfstSchoolOrderService sfstSchoolOrderService,
            SfstSchoolOutboundRecordService sfstSchoolOutboundRecordService,
            SfstDishService sfstDishService,
            SfstDishPicService sfstDishPicService,
            SfstDishMaterialService sfstDishMaterialService,
            SfstMenuService sfstMenuService,
            SfstSubMenuService sfstSubMenuService,
            SfstSubMenuDishService sfstSubMenuDishService,
            SfstSubMenuDishSchoolBatchService sfstSubMenuDishSchoolBatchService,
            SfstCondimentShowService sfstCondimentShowService,
            SfstFinancialBillService sfstFinancialBillService,
            SfstOrderBillService sfstOrderBillService,
            SfstExcelImportHisService sfstExcelImportHisService,
            CommonUserService commonUserService
    ) throws Exception {
        serviceMap = new HashMap<String, BaseService>() {{
            put("SystemUser", systemUserService);
            put("SystemRole", systemRoleService);
            put("SystemUserRole", systemUserRoleService);
            put("SystemPermission", systemPermissionService);
            put("SystemResource", systemResourceService);
            put("SystemResourcePermission", systemResourcePermissionService);
            put("SystemRoleResource", systemRoleResourceService);
            put("SystemUserResource", systemUserResourceService);
            put("SystemOrganization", systemOrganizationService);
            put("SystemLog", systemLogService);
            put("BaseDictionary", baseDictionaryService);
            put("SfstSupplier", sfstSupplierService);
            put("SfstSupplierLicense", sfstSupplierLicenseService);
            put("SfstSchool", sfstSchoolService);
            put("SfstSchoolCertificate", sfstSchoolCertificateService);
            put("SfstSchoolType", sfstSchoolTypeService);
            put("SfstSchoolSupplier", sfstSchoolSupplierService);
            put("SfstMaterial", sfstMaterialService);
            put("SfstMaterialPic", sfstMaterialPicService);
            put("SfstMaterialPrice", sfstMaterialPriceService);
            put("SfstSchoolMaterialAttr", sfstSchoolMaterialAttrService);
            put("SfstSupplierInboundOrder", sfstSupplierInboundOrderService);
            put("SfstSupplierInboundBatch", sfstSupplierInboundBatchService);
            put("SfstTestRecord", sfstTestRecordService);
            put("SfstTestRecordReport", sfstTestRecordReportService);
            put("SfstTestRecordBatch", sfstTestRecordBatchService);
            put("SfstSchoolBatch", sfstSchoolBatchService);
            put("SfstSchoolMaterialStockRecord", sfstSchoolMaterialStockRecordService);
            put("SfstSchoolOrder", sfstSchoolOrderService);
            put("SfstSchoolOutboundRecord", sfstSchoolOutboundRecordService);
            put("SfstDish", sfstDishService);
            put("SfstDishPic", sfstDishPicService);
            put("SfstDishMaterial", sfstDishMaterialService);
            put("SfstMenu", sfstMenuService);
            put("SfstSubMenu", sfstSubMenuService);
            put("SfstSubMenuDish", sfstSubMenuDishService);
            put("SfstSubMenuDishSchoolBatch", sfstSubMenuDishSchoolBatchService);
            put("SfstCondimentShow", sfstCondimentShowService);
            put("SfstFinancialBill", sfstFinancialBillService);
            put("SfstOrderBill", sfstOrderBillService);
            put("SfstExcelImportHis", sfstExcelImportHisService);
        }};
        this.commonUserService = commonUserService;
        for (String entityName : entityNames) {
            Class domainClass = Class.forName("com.bob.sm.domain." + entityName);
            domainClassMap.put(entityName, domainClass);
            Class criteriaClass = Class.forName("com.bob.sm.dto.criteria." + entityName + "Criteria");
            criteriaClassMap.put(entityName, criteriaClass);
            Class dtoClass = Class.forName("com.bob.sm.dto." + entityName + "DTO");
            dtoClassMap.put(entityName, dtoClass);
        }
    }

    private static List<String> entityNames = Arrays.asList(
            "SystemUser",
            "SystemRole",
            "SystemUserRole",
            "SystemPermission",
            "SystemResource",
            "SystemResourcePermission",
            "SystemRoleResource",
            "SystemUserResource",
            "SystemOrganization",
            "SystemLog",
            "BaseDictionary",
            "SfstSupplier",
            "SfstSupplierLicense",
            "SfstSchool",
            "SfstSchoolCertificate",
            "SfstSchoolType",
            "SfstSchoolSupplier",
            "SfstMaterial",
            "SfstMaterialPic",
            "SfstMaterialPrice",
            "SfstSchoolMaterialAttr",
            "SfstSupplierInboundOrder",
            "SfstSupplierInboundBatch",
            "SfstTestRecord",
            "SfstTestRecordReport",
            "SfstTestRecordBatch",
            "SfstSchoolBatch",
            "SfstSchoolMaterialStockRecord",
            "SfstSchoolOrder",
            "SfstSchoolOutboundRecord",
            "SfstDish",
            "SfstDishPic",
            "SfstDishMaterial",
            "SfstMenu",
            "SfstSubMenu",
            "SfstSubMenuDish",
            "SfstSubMenuDishSchoolBatch",
            "SfstCondimentShow",
            "SfstFinancialBill",
            "SfstOrderBill",
            "SfstExcelImportHis"
    );

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
        put("SystemUser", Arrays.asList(
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SystemUser", "systemUserRole", "SystemUserRole", "systemUser"),
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SystemUser", "systemUserResource", "SystemUserResource", "systemUser"),
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SystemUser", "schoolOrderAsShipPerson", "SfstSchoolOrder", "shipPerson"),
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SystemUser", "schoolOrderAsReceiptPerson", "SfstSchoolOrder", "receiptPerson"),
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SystemOrganization", "systemUser", "SystemUser", "systemOrganization"),
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SfstSchool", "systemUser", "SystemUser", "sfstSchool"),
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SfstSupplier", "systemUser", "SystemUser", "sfstSupplier")
        ));
        put("SystemRole", Arrays.asList(
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SystemRole", "systemUserRole", "SystemUserRole", "systemRole"),
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SystemRole", "systemRoleResource", "SystemRoleResource", "systemRole")
        ));
        put("SystemUserRole", Arrays.asList(
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SystemUser", "systemUserRole", "SystemUserRole", "systemUser"),
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SystemRole", "systemUserRole", "SystemUserRole", "systemRole")
        ));
        put("SystemPermission", Arrays.asList(
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SystemPermission", "child", "SystemPermission", "parent"),
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SystemPermission", "systemResourcePermission", "SystemResourcePermission", "systemPermission"),
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SystemPermission", "child", "SystemPermission", "parent")
        ));
        put("SystemResource", Arrays.asList(
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SystemResource", "child", "SystemResource", "parent"),
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SystemResource", "systemResourcePermission", "SystemResourcePermission", "systemResource"),
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SystemResource", "systemRoleResource", "SystemRoleResource", "systemResource"),
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SystemResource", "systemUserResource", "SystemUserResource", "systemResource"),
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SystemResource", "child", "SystemResource", "parent")
        ));
        put("SystemResourcePermission", Arrays.asList(
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SystemResource", "systemResourcePermission", "SystemResourcePermission", "systemResource"),
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SystemPermission", "systemResourcePermission", "SystemResourcePermission", "systemPermission")
        ));
        put("SystemRoleResource", Arrays.asList(
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SystemRole", "systemRoleResource", "SystemRoleResource", "systemRole"),
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SystemResource", "systemRoleResource", "SystemRoleResource", "systemResource")
        ));
        put("SystemUserResource", Arrays.asList(
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SystemUser", "systemUserResource", "SystemUserResource", "systemUser"),
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SystemResource", "systemUserResource", "SystemUserResource", "systemResource")
        ));
        put("SystemOrganization", Arrays.asList(
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SystemOrganization", "child", "SystemOrganization", "parent"),
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SystemOrganization", "systemUser", "SystemUser", "systemOrganization"),
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SystemOrganization", "child", "SystemOrganization", "parent")
        ));
        put("BaseDictionary", Arrays.asList(
                new BaseEntityConfigRelationDTO("OneToMany", "from", "BaseDictionary", "child", "BaseDictionary", "parent"),
                new BaseEntityConfigRelationDTO("OneToMany", "to", "BaseDictionary", "child", "BaseDictionary", "parent")
        ));
        put("SfstSupplier", Arrays.asList(
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SfstSupplier", "upSfstSupplier", "SfstSupplier", "downSfstSupplier"),
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SfstSupplier", "sfstSupplierLicense", "SfstSupplierLicense", "sfstSupplier"),
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SfstSupplier", "systemUser", "SystemUser", "sfstSupplier"),
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SfstSupplier", "sfstSchoolSupplier", "SfstSchoolSupplier", "sfstSupplier"),
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SfstSupplier", "sfstMaterial", "SfstMaterial", "sfstSupplier"),
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SfstSupplier", "upSupplierInboundOrder", "SfstSupplierInboundOrder", "downSupplier"),
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SfstSupplier", "downSupplierInboundOrder", "SfstSupplierInboundOrder", "upSupplier"),
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SfstSupplier", "sfstTestRecord", "SfstTestRecord", "sfstSupplier"),
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SfstSupplier", "sfstSchoolOrder", "SfstSchoolOrder", "sfstSupplier"),
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SfstSupplier", "sfstFinancialBill", "SfstFinancialBill", "sfstSupplier"),
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SfstSupplier", "upSfstSupplier", "SfstSupplier", "downSfstSupplier")
        ));
        put("SfstSupplierLicense", Arrays.asList(
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SfstSupplier", "sfstSupplierLicense", "SfstSupplierLicense", "sfstSupplier")
        ));
        put("SfstSchool", Arrays.asList(
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SfstSchool", "sfstSchoolType", "SfstSchoolType", "sfstSchool"),
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SfstSchool", "sfstSchoolCertificate", "SfstSchoolCertificate", "sfstSchool"),
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SfstSchool", "systemUser", "SystemUser", "sfstSchool"),
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SfstSchool", "sfstSchoolSupplier", "SfstSchoolSupplier", "sfstSchool"),
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SfstSchool", "sfstSchoolMaterialAttr", "SfstSchoolMaterialAttr", "sfstSchool"),
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SfstSchool", "sfstSchoolOrder", "SfstSchoolOrder", "sfstSchool"),
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SfstSchool", "sfstSchoolMaterialStockRecord", "SfstSchoolMaterialStockRecord", "sfstSchool"),
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SfstSchool", "sfstDish", "SfstDish", "sfstSchool"),
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SfstSchool", "sfstMenu", "SfstMenu", "sfstSchool"),
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SfstSchool", "sfstFinancialBill", "SfstFinancialBill", "sfstSchool")
        ));
        put("SfstSchoolCertificate", Arrays.asList(
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SfstSchool", "sfstSchoolCertificate", "SfstSchoolCertificate", "sfstSchool")
        ));
        put("SfstSchoolType", Arrays.asList(
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SfstSchool", "sfstSchoolType", "SfstSchoolType", "sfstSchool")
        ));
        put("SfstSchoolSupplier", Arrays.asList(
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SfstSchool", "sfstSchoolSupplier", "SfstSchoolSupplier", "sfstSchool"),
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SfstSupplier", "sfstSchoolSupplier", "SfstSchoolSupplier", "sfstSupplier")
        ));
        put("SfstMaterial", Arrays.asList(
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SfstMaterial", "sfstMaterialPic", "SfstMaterialPic", "sfstMaterial"),
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SfstMaterial", "sfstMaterialPrice", "SfstMaterialPrice", "sfstMaterial"),
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SfstMaterial", "sfstSchoolMaterialAttr", "SfstSchoolMaterialAttr", "sfstMaterial"),
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SfstMaterial", "sfstSupplierInboundBatch", "SfstSupplierInboundBatch", "sfstMaterial"),
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SfstMaterial", "sfstSchoolBatch", "SfstSchoolBatch", "sfstMaterial"),
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SfstMaterial", "sfstSchoolMaterialStockRecord", "SfstSchoolMaterialStockRecord", "sfstMaterial"),
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SfstMaterial", "sfstDishMaterial", "SfstDishMaterial", "sfstMaterial"),
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SfstSupplier", "sfstMaterial", "SfstMaterial", "sfstSupplier")
        ));
        put("SfstMaterialPic", Arrays.asList(
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SfstMaterial", "sfstMaterialPic", "SfstMaterialPic", "sfstMaterial")
        ));
        put("SfstMaterialPrice", Arrays.asList(
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SfstMaterial", "sfstMaterialPrice", "SfstMaterialPrice", "sfstMaterial")
        ));
        put("SfstSchoolMaterialAttr", Arrays.asList(
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SfstSchool", "sfstSchoolMaterialAttr", "SfstSchoolMaterialAttr", "sfstSchool"),
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SfstMaterial", "sfstSchoolMaterialAttr", "SfstSchoolMaterialAttr", "sfstMaterial")
        ));
        put("SfstSupplierInboundOrder", Arrays.asList(
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SfstSupplierInboundOrder", "sfstSupplierInboundBatch", "SfstSupplierInboundBatch", "sfstSupplierInboundOrder"),
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SfstSupplier", "upSupplierInboundOrder", "SfstSupplierInboundOrder", "downSupplier"),
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SfstSupplier", "downSupplierInboundOrder", "SfstSupplierInboundOrder", "upSupplier")
        ));
        put("SfstSupplierInboundBatch", Arrays.asList(
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SfstSupplierInboundBatch", "sfstTestRecordBatch", "SfstTestRecordBatch", "sfstSupplierInboundBatch"),
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SfstSupplierInboundBatch", "sfstSchoolBatch", "SfstSchoolBatch", "sfstSupplierInboundBatch"),
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SfstSupplierInboundOrder", "sfstSupplierInboundBatch", "SfstSupplierInboundBatch", "sfstSupplierInboundOrder"),
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SfstMaterial", "sfstSupplierInboundBatch", "SfstSupplierInboundBatch", "sfstMaterial")
        ));
        put("SfstTestRecord", Arrays.asList(
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SfstTestRecord", "sfstTestRecordReport", "SfstTestRecordReport", "sfstTestRecord"),
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SfstTestRecord", "sfstTestRecordBatch", "SfstTestRecordBatch", "sfstTestRecord"),
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SfstSupplier", "sfstTestRecord", "SfstTestRecord", "sfstSupplier")
        ));
        put("SfstTestRecordReport", Arrays.asList(
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SfstTestRecord", "sfstTestRecordReport", "SfstTestRecordReport", "sfstTestRecord")
        ));
        put("SfstTestRecordBatch", Arrays.asList(
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SfstTestRecord", "sfstTestRecordBatch", "SfstTestRecordBatch", "sfstTestRecord"),
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SfstSupplierInboundBatch", "sfstTestRecordBatch", "SfstTestRecordBatch", "sfstSupplierInboundBatch")
        ));
        put("SfstSchoolBatch", Arrays.asList(
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SfstSchoolBatch", "sfstSchoolOutboundRecord", "SfstSchoolOutboundRecord", "sfstSchoolBatch"),
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SfstSchoolBatch", "sfstSubMenuDishSchoolBatch", "SfstSubMenuDishSchoolBatch", "sfstSchoolBatch"),
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SfstSchoolBatch", "sfstCondimentShow", "SfstCondimentShow", "sfstSchoolBatch"),
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SfstSupplierInboundBatch", "sfstSchoolBatch", "SfstSchoolBatch", "sfstSupplierInboundBatch"),
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SfstSchoolOrder", "sfstSchoolBatch", "SfstSchoolBatch", "sfstSchoolOrder"),
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SfstMaterial", "sfstSchoolBatch", "SfstSchoolBatch", "sfstMaterial")
        ));
        put("SfstSchoolMaterialStockRecord", Arrays.asList(
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SfstMaterial", "sfstSchoolMaterialStockRecord", "SfstSchoolMaterialStockRecord", "sfstMaterial"),
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SfstSchool", "sfstSchoolMaterialStockRecord", "SfstSchoolMaterialStockRecord", "sfstSchool")
        ));
        put("SfstSchoolOrder", Arrays.asList(
                new BaseEntityConfigRelationDTO("OneToOne", "from", "SfstSchoolOrder", "originalOrder", "SfstSchoolOrder", "newestOrder"),
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SfstSchoolOrder", "sfstSchoolBatch", "SfstSchoolBatch", "sfstSchoolOrder"),
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SfstSchoolOrder", "sfstOrderBill", "SfstOrderBill", "sfstSchoolOrder"),
                new BaseEntityConfigRelationDTO("OneToOne", "to", "SfstSchoolOrder", "originalOrder", "SfstSchoolOrder", "newestOrder"),
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SfstSupplier", "sfstSchoolOrder", "SfstSchoolOrder", "sfstSupplier"),
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SfstSchool", "sfstSchoolOrder", "SfstSchoolOrder", "sfstSchool"),
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SystemUser", "schoolOrderAsShipPerson", "SfstSchoolOrder", "shipPerson"),
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SystemUser", "schoolOrderAsReceiptPerson", "SfstSchoolOrder", "receiptPerson")
        ));
        put("SfstSchoolOutboundRecord", Arrays.asList(
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SfstSchoolBatch", "sfstSchoolOutboundRecord", "SfstSchoolOutboundRecord", "sfstSchoolBatch")
        ));
        put("SfstDish", Arrays.asList(
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SfstDish", "sfstDishPic", "SfstDishPic", "sfstDish"),
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SfstDish", "sfstDishMaterial", "SfstDishMaterial", "sfstDish"),
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SfstDish", "sfstSubMenuDish", "SfstSubMenuDish", "sfstDish"),
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SfstSchool", "sfstDish", "SfstDish", "sfstSchool")
        ));
        put("SfstDishPic", Arrays.asList(
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SfstDish", "sfstDishPic", "SfstDishPic", "sfstDish")
        ));
        put("SfstDishMaterial", Arrays.asList(
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SfstDish", "sfstDishMaterial", "SfstDishMaterial", "sfstDish"),
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SfstMaterial", "sfstDishMaterial", "SfstDishMaterial", "sfstMaterial")
        ));
        put("SfstMenu", Arrays.asList(
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SfstMenu", "sfstSubMenu", "SfstSubMenu", "sfstMenu"),
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SfstSchool", "sfstMenu", "SfstMenu", "sfstSchool")
        ));
        put("SfstSubMenu", Arrays.asList(
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SfstSubMenu", "sfstSubMenuDish", "SfstSubMenuDish", "sfstSubMenu"),
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SfstMenu", "sfstSubMenu", "SfstSubMenu", "sfstMenu")
        ));
        put("SfstSubMenuDish", Arrays.asList(
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SfstSubMenuDish", "sfstSubMenuDishSchoolBatch", "SfstSubMenuDishSchoolBatch", "sfstSubMenuDish"),
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SfstSubMenu", "sfstSubMenuDish", "SfstSubMenuDish", "sfstSubMenu"),
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SfstDish", "sfstSubMenuDish", "SfstSubMenuDish", "sfstDish")
        ));
        put("SfstSubMenuDishSchoolBatch", Arrays.asList(
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SfstSubMenuDish", "sfstSubMenuDishSchoolBatch", "SfstSubMenuDishSchoolBatch", "sfstSubMenuDish"),
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SfstSchoolBatch", "sfstSubMenuDishSchoolBatch", "SfstSubMenuDishSchoolBatch", "sfstSchoolBatch")
        ));
        put("SfstCondimentShow", Arrays.asList(
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SfstSchoolBatch", "sfstCondimentShow", "SfstCondimentShow", "sfstSchoolBatch")
        ));
        put("SfstFinancialBill", Arrays.asList(
                new BaseEntityConfigRelationDTO("OneToMany", "from", "SfstFinancialBill", "sfstOrderBill", "SfstOrderBill", "sfstFinancialBill"),
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SfstSchool", "sfstFinancialBill", "SfstFinancialBill", "sfstSchool"),
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SfstSupplier", "sfstFinancialBill", "SfstFinancialBill", "sfstSupplier")
        ));
        put("SfstOrderBill", Arrays.asList(
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SfstFinancialBill", "sfstOrderBill", "SfstOrderBill", "sfstFinancialBill"),
                new BaseEntityConfigRelationDTO("OneToMany", "to", "SfstSchoolOrder", "sfstOrderBill", "SfstOrderBill", "sfstSchoolOrder")
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

    public static Map<String, BaseMapper> getMapperMap() {
        return mapperMap;
    }

    public static CommonUserService getCommonUserService() {
        return commonUserService;
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
