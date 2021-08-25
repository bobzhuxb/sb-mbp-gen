package com.bob.at.web.rest;

import com.bob.at.dto.AhClassCodeDTO;
import com.bob.at.dto.criteria.AhClassCodeCriteria;
import com.bob.at.dto.help.ReturnCommonDTO;
import com.bob.at.service.AhClassCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 可用实体类
 * @author Bob
 */
@RestController
@RequestMapping("/api")
public class AhClassCodeController {

    @Autowired
    private AhClassCodeService ahClassCodeService;

    /**
     * 新增
     * @param ahClassCodeDTO 待新增的实体
     * @return 结果返回码和消息
     */
    @PostMapping("/ah-class-code")
    public ResponseEntity<ReturnCommonDTO> createAhClassCode(@RequestBody AhClassCodeDTO ahClassCodeDTO) {
        ReturnCommonDTO resultDTO = ahClassCodeService.createAhClassCode(ahClassCodeDTO);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 修改
     * @param ahClassCodeDTO 待修改的实体
     * @return 结果返回码和消息
     */
    @PutMapping("/ah-class-code")
    public ResponseEntity<ReturnCommonDTO> updateAhClassCode(@RequestBody AhClassCodeDTO ahClassCodeDTO) {
        ReturnCommonDTO resultDTO = ahClassCodeService.updateAhClassCode(ahClassCodeDTO);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 单个删除
     * @param id 主键ID
     * @return 结果返回码和消息
     */
    @DeleteMapping("/ah-class-code/{id}")
    public ResponseEntity<ReturnCommonDTO> deleteAhClassCode(@PathVariable String id) {
        ReturnCommonDTO resultDTO = ahClassCodeService.deleteAhClassCode(id);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 批量删除
     * @param idList 主键ID列表
     * @return 结果返回码和消息
     */
    @DeleteMapping("/ah-class-code")
    public ResponseEntity<ReturnCommonDTO> deleteAhClassCodes(@RequestBody List<String> idList) {
        ReturnCommonDTO resultDTO = ahClassCodeService.deleteAhClassCodes(idList);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 查询单条
     * @param primaryId 主键ID
     * @return 使用ResponseEntity封装的单条项目数据
     */
    @GetMapping("/ah-class-code/{primaryId}")
    public ResponseEntity<ReturnCommonDTO<AhClassCodeDTO>> getAhClassCode(@PathVariable String primaryId) {
        ReturnCommonDTO<AhClassCodeDTO> resultDTO = ahClassCodeService.getAhClassCode(primaryId);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 查询所有
     * @param criteria 查询条件
     * @return 使用ResponseEntity封装的多条项目数据
     */
    @GetMapping("/ah-class-code-all")
    public ResponseEntity<ReturnCommonDTO<List<AhClassCodeDTO>>> getAllAhClassCodes(AhClassCodeCriteria criteria) {
        ReturnCommonDTO<List<AhClassCodeDTO>> resultDTO = ahClassCodeService.getAllAhClassCodes(criteria);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 上传类所在文件夹
     * @param projectId 所属项目ID
     * @param fileType 文件类型（JAVA或CLASS）
     * @param overwrite yes全覆盖  no增量更新
     * @param folder
     * @return
     */
    @PostMapping("/ah-class-code-folder-upload")
    public ResponseEntity<ReturnCommonDTO> uploadClassFolder(String projectId, String fileType, String overwrite,
                                                             @RequestParam("folder") MultipartFile[] folder) {
        ReturnCommonDTO resultDTO = ahClassCodeService.uploadClassFiles(projectId, fileType, overwrite, folder);
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

    /**
     * 上传类所在文件夹
     * @param projectId 所属项目ID
     * @param fileType 文件类型（JAVA或CLASS）
     * @param file
     * @return
     */
    @PostMapping("/ah-class-code-file-upload")
    public ResponseEntity<ReturnCommonDTO> uploadClassFile(String projectId, String fileType, MultipartFile file) {
        // 单文件只增量更新
        ReturnCommonDTO resultDTO = ahClassCodeService.uploadClassFiles(projectId, fileType, "no", new MultipartFile[] {file});
        return ResponseEntity.ok().headers(null).body(resultDTO);
    }

}
