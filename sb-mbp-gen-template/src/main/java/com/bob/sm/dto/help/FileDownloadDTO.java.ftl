package ${packageName}.dto.help;

import ${packageName}.annotation.GenComment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 文件下载DTO
 * @author Bob
 */
public class FileDownloadDTO {

    @GenComment("待下载的文件的相对路径")
    @NotBlank
    @Size(min = 1)
    private String relativePath;

    @GenComment("修改后的文件名")
    private String changeFileName;

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public String getChangeFileName() {
        return changeFileName;
    }

    public void setChangeFileName(String changeFileName) {
        this.changeFileName = changeFileName;
    }
}
