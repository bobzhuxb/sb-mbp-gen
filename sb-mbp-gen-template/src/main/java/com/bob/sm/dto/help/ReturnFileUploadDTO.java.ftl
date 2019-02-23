package ${packageName}.dto.help;

/**
 * A DTO for the ReturnFileUploadDTO entity.
 */
public class ReturnFileUploadDTO {

    private String relativePath;                // 文件相对路径

    private String compressedRelativePath;      // 压缩后文件相对路径

    private String uploadTime;                  // 文件上传时间

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

}
