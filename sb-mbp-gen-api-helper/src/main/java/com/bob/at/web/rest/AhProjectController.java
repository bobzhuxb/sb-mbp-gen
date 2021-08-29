package com.bob.at.web.rest;

import com.bob.at.dto.AhProjectDTO;
import com.bob.at.dto.criteria.AhProjectCriteria;
import com.bob.at.dto.help.ReturnCommonDTO;
import com.bob.at.service.AhProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 项目
 * @author Bob
 */
@RestController
@RequestMapping("/api")
public class AhProjectController {

    @Autowired
    private AhProjectService ahProjectService;

    /**
     * 新增
     * @param ahProjectDTO 待新增的实体
     * @return 结果返回码和消息
     */
    @PostMapping("/ah-project")
    public ResponseEntity<ReturnCommonDTO> createAhProject(@Valid @RequestBody AhProjectDTO ahProjectDTO) {
		ReturnCommonDTO resultDTO = ahProjectService.createAhProject(ahProjectDTO);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 修改
     * @param ahProjectDTO 待修改的实体
     * @return 结果返回码和消息
     */
    @PutMapping("/ah-project")
    public ResponseEntity<ReturnCommonDTO> updateAhProject(@Valid @RequestBody AhProjectDTO ahProjectDTO) {
		ReturnCommonDTO resultDTO = ahProjectService.updateAhProject(ahProjectDTO);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 单个删除
     * @param id 主键ID
     * @return 结果返回码和消息
     */
    @DeleteMapping("/ah-project/{id}")
    public ResponseEntity<ReturnCommonDTO> deleteAhProject(@PathVariable String id) {
        ReturnCommonDTO resultDTO = ahProjectService.deleteAhProject(id);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 批量删除
     * @param idList 主键ID列表
     * @return 结果返回码和消息
     */
    @DeleteMapping("/ah-project")
    public ResponseEntity<ReturnCommonDTO> deleteAhProjects(@RequestBody List<String> idList) {
        ReturnCommonDTO resultDTO = ahProjectService.deleteAhProjects(idList);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 查询单条
	 * @param primaryId 主键ID
     * @return 使用ResponseEntity封装的单条项目数据
     */
    @GetMapping("/ah-project/{primaryId}")
    public ResponseEntity<ReturnCommonDTO<AhProjectDTO>> getAhProject(@PathVariable String primaryId) {
        ReturnCommonDTO<AhProjectDTO> resultDTO = ahProjectService.getAhProject(primaryId);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 查询所有
	 * @param criteria 查询条件
     * @return 使用ResponseEntity封装的多条项目数据
     */
    @GetMapping("/ah-project-all")
    public ResponseEntity<ReturnCommonDTO<List<AhProjectDTO>>> getAllAhProjects(AhProjectCriteria criteria) {
        ReturnCommonDTO<List<AhProjectDTO>> resultDTO = ahProjectService.getAllAhProjects(criteria);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

}
