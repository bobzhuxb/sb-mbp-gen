package ${packageName}.dto.help;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class FileDownloadDTO {

    @NotBlank
    @Size(min = 1)
    private String relativePath;        // 待下载的文件的相对路径

    private String changeFileName;      // 修改后的文件名

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
