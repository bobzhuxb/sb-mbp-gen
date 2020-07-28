package ${packageName}.dto.help;

/**
 * 文件上传
 * @author Bob
 */
public class ReturnFileUploadDTO {

    private String originalFileName;            // 文件原始名称

    private String relativePath;                // 文件相对路径

    private String compressedRelativePath;      // 压缩后文件相对路径

    private String uploadTime;                  // 文件上传时间

    private String extension;                   // 文件后缀

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public String getCompressedRelativePath() {
        return compressedRelativePath;
    }

    public void setCompressedRelativePath(String compressedRelativePath) {
        this.compressedRelativePath = compressedRelativePath;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
