package ${packageName}.dto.help;

/**
 * 文件压缩时改名DTO
 * @author Bob
 */
public class CompressChangeFileDTO {

    private String changeFileName;      // 修改后的文件名（包括后缀）

    private String fullFileName;        // 文件全路径

    public CompressChangeFileDTO() {}

    public CompressChangeFileDTO(String changeFileName, String fullFileName) {
        this.changeFileName = changeFileName;
        this.fullFileName = fullFileName;
    }

    public String getChangeFileName() {
        return changeFileName;
    }

    public void setChangeFileName(String changeFileName) {
        this.changeFileName = changeFileName;
    }

    public String getFullFileName() {
        return fullFileName;
    }

    public void setFullFileName(String fullFileName) {
        this.fullFileName = fullFileName;
    }
}
