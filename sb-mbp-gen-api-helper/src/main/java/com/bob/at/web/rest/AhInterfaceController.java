package com.bob.at.web.rest;

import com.bob.at.dto.AhInterfaceDTO;
import com.bob.at.dto.criteria.AhInterfaceCriteria;
import com.bob.at.dto.help.ReturnCommonDTO;
import com.bob.at.service.AhInterfaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * 接口
 * @author Bob
 */
@RestController
@RequestMapping("/api")
public class AhInterfaceController {

    @Autowired
    private AhInterfaceService ahInterfaceService;

    /**
     * 新增
     * @param ahInterfaceDTO 待新增的实体
     * @return 结果返回码和消息
     */
    @PostMapping("/ah-interface")
    public ResponseEntity<ReturnCommonDTO> createAhInterface(@Valid @RequestBody AhInterfaceDTO ahInterfaceDTO) {
        ReturnCommonDTO resultDTO = ahInterfaceService.createAhInterface(ahInterfaceDTO);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 修改
     * @param ahInterfaceDTO 待修改的实体
     * @return 结果返回码和消息
     */
    @PutMapping("/ah-interface")
    public ResponseEntity<ReturnCommonDTO> updateAhInterface(@Valid @RequestBody AhInterfaceDTO ahInterfaceDTO) {
        ReturnCommonDTO resultDTO = ahInterfaceService.updateAhInterface(ahInterfaceDTO);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 单个删除
     * @param id 主键ID
     * @return 结果返回码和消息
     */
    @DeleteMapping("/ah-interface/{id}")
    public ResponseEntity<ReturnCommonDTO> deleteAhInterface(@PathVariable String id) {
        ReturnCommonDTO resultDTO = ahInterfaceService.deleteAhInterface(id);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 批量删除
     * @param idList 主键ID列表
     * @return 结果返回码和消息
     */
    @DeleteMapping("/ah-interface")
    public ResponseEntity<ReturnCommonDTO> deleteAhInterfaces(@RequestBody List<String> idList) {
        ReturnCommonDTO resultDTO = ahInterfaceService.deleteAhInterfaces(idList);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 查询单条
     * @param primaryId 主键ID
     * @return 使用ResponseEntity封装的单条项目数据
     */
    @GetMapping("/ah-interface/{primaryId}")
    public ResponseEntity<ReturnCommonDTO<AhInterfaceDTO>> getAhInterface(@PathVariable String primaryId) {
        ReturnCommonDTO<AhInterfaceDTO> resultDTO = ahInterfaceService.getAhInterface(primaryId);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 查询所有
     * @param criteria 查询条件
     * @return 使用ResponseEntity封装的多条项目数据
     */
    @GetMapping("/ah-interface-all")
    public ResponseEntity<ReturnCommonDTO<List<AhInterfaceDTO>>> getAllAhInterfaces(AhInterfaceCriteria criteria) {
        ReturnCommonDTO<List<AhInterfaceDTO>> resultDTO = ahInterfaceService.getAllAhInterfaces(criteria);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 导出JSON
     * @param interfaceId 接口ID
     * @return 使用ResponseEntity封装的多条药品库存批次数据
     */
    @GetMapping("/export-inter-json/{interfaceId}")
    public void exportInterJson(@PathVariable String interfaceId, HttpServletResponse response) {
        ahInterfaceService.exportInterJson(interfaceId, response);
    }

    /**
     * 导出工程的JSON文件并压缩
     * @param projectId 工程ID
     * @return 使用ResponseEntity封装的多条药品库存批次数据
     */
    @GetMapping("/export-project-inter-json/{projectId}")
    public void exportProjectInterJson(@PathVariable String projectId, HttpServletResponse response) {
        ahInterfaceService.exportProjectInterJson(projectId, response);
    }

}
