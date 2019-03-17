package com.bob.sm.config;

import com.bob.sm.dto.help.BaseEntityConfigDTO;
import com.bob.sm.dto.help.BaseEntityConfigDicDTO;
import com.bob.sm.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GlobalCache {

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
        serviceMap.put("BaseDictionary", baseDictionaryService);
        serviceMap.put("SfstCondimentShow", sfstCondimentShowService);
        serviceMap.put("SfstDish", sfstDishService);
        serviceMap.put("SfstDishMaterial", sfstDishMaterialService);
        serviceMap.put("SfstDishPic", sfstDishPicService);
        serviceMap.put("SfstExcelImportHis", sfstExcelImportHisService);
        serviceMap.put("SfstFinancialBill", sfstFinancialBillService);
        serviceMap.put("SfstMaterial", sfstMaterialService);
        serviceMap.put("SfstMaterialPic", sfstMaterialPicService);
        serviceMap.put("SfstMaterialPrice", sfstMaterialPriceService);
        serviceMap.put("SfstMenu", sfstMenuService);
        serviceMap.put("SfstOrderBill", sfstOrderBillService);
        serviceMap.put("SfstSchool", sfstSchoolService);
        serviceMap.put("SfstSchoolBatch", sfstSchoolBatchService);
        serviceMap.put("SfstSchoolCertificate", sfstSchoolCertificateService);
        serviceMap.put("SfstSchoolMaterialAttr", sfstSchoolMaterialAttrService);
        serviceMap.put("SfstSchoolMaterialStockRecord", sfstSchoolMaterialStockRecordService);
        serviceMap.put("SfstSchoolOrder", sfstSchoolOrderService);
        serviceMap.put("SfstSchoolOutboundRecord", sfstSchoolOutboundRecordService);
        serviceMap.put("SfstSchoolSupplier", sfstSchoolSupplierService);
        serviceMap.put("SfstSchoolType", sfstSchoolTypeService);
        serviceMap.put("SfstSubMenu", sfstSubMenuService);
        serviceMap.put("SfstSubMenuDish", sfstSubMenuDishService);
        serviceMap.put("SfstSubMenuDishSchoolBatch", sfstSubMenuDishSchoolBatchService);
        serviceMap.put("SfstSupplier", sfstSupplierService);
        serviceMap.put("SfstSupplierInboundBatch", sfstSupplierInboundBatchService);
        serviceMap.put("SfstSupplierInboundOrder", sfstSupplierInboundOrderService);
        serviceMap.put("SfstSupplierLicense", sfstSupplierLicenseService);
        serviceMap.put("SfstTestRecord", sfstTestRecordService);
        serviceMap.put("SfstTestRecordBatch", sfstTestRecordBatchService);
        serviceMap.put("SfstTestRecordReport", sfstTestRecordReportService);
        serviceMap.put("SystemLog", systemLogService);
        serviceMap.put("SystemOrganization", systemOrganizationService);
        serviceMap.put("SystemPermission", systemPermissionService);
        serviceMap.put("SystemResource", systemResourceService);
        serviceMap.put("SystemResourcePermission", systemResourcePermissionService);
        serviceMap.put("SystemRole", systemRoleService);
        serviceMap.put("SystemRoleResource", systemRoleResourceService);
        serviceMap.put("SystemUser", systemUserService);
        serviceMap.put("SystemUserResource", systemUserResourceService);
        serviceMap.put("SystemUserRole", systemUserRoleService);
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

    private static Map<String, BaseService> serviceMap = new HashMap<>();

    // 只在启动的时候初始化一次
    private static Map<String, BaseEntityConfigDTO> entityConfigMap = new HashMap<>();

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
}
