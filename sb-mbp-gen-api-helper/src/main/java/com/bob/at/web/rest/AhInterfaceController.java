package com.bob.at.web.rest;

import com.bob.at.dto.AhInterfaceDTO;
import com.bob.at.dto.criteria.AhInterfaceCriteria;
import com.bob.at.dto.help.ReturnCommonDTO;
import com.bob.at.service.AhInterfaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 接口
 * @author Bob
 */
@RestController
@RequestMapping("/api")
public class AhInterfaceController {

    @Autowired
    private com.bob.at.service.AhInterfaceService AhInterfaceService;

    /**
     * 新增
     * @param ahInterfaceDTO 待新增的实体
     * @return 结果返回码和消息
     */
    @PostMapping("/ah-interface")
    public ResponseEntity<ReturnCommonDTO> createAhInterface(@RequestBody AhInterfaceDTO ahInterfaceDTO) {
        ReturnCommonDTO resultDTO = AhInterfaceService.createAhInterface(ahInterfaceDTO);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 修改
     * @param ahInterfaceDTO 待修改的实体
     * @return 结果返回码和消息
     */
    @PutMapping("/ah-interface")
    public ResponseEntity<ReturnCommonDTO> updateAhInterface(@RequestBody AhInterfaceDTO ahInterfaceDTO) {
        ReturnCommonDTO resultDTO = AhInterfaceService.updateAhInterface(ahInterfaceDTO);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 单个删除
     * @param id 主键ID
     * @return 结果返回码和消息
     */
    @DeleteMapping("/ah-interface/{id}")
    public ResponseEntity<ReturnCommonDTO> deleteAhInterface(@PathVariable String id) {
        ReturnCommonDTO resultDTO = AhInterfaceService.deleteAhInterface(id);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 批量删除
     * @param idList 主键ID列表
     * @return 结果返回码和消息
     */
    @DeleteMapping("/ah-interface")
    public ResponseEntity<ReturnCommonDTO> deleteAhInterfaces(@RequestBody List<String> idList) {
        ReturnCommonDTO resultDTO = AhInterfaceService.deleteAhInterfaces(idList);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 查询单条
     * @param primaryId 主键ID
     * @return 使用ResponseEntity封装的单条项目数据
     */
    @GetMapping("/ah-interface/{primaryId}")
    public ResponseEntity<ReturnCommonDTO<AhInterfaceDTO>> getAhInterface(@PathVariable String primaryId) {
        ReturnCommonDTO<AhInterfaceDTO> resultDTO = AhInterfaceService.getAhInterface(primaryId);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 查询所有
     * @param criteria 查询条件
     * @return 使用ResponseEntity封装的多条项目数据
     */
    @GetMapping("/ah-interface-all")
    public ResponseEntity<ReturnCommonDTO<List<AhInterfaceDTO>>> getAllAhInterfaces(AhInterfaceCriteria criteria) {
        ReturnCommonDTO<List<AhInterfaceDTO>> resultDTO = AhInterfaceService.getAllAhInterfaces(criteria);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

}
