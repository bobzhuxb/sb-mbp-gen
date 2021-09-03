package ${packageName}.dto.help;

import ${packageName}.annotation.GenComment;

/**
 * 文件上传
 * @author Bob
 */
public class ReturnFileUploadDTO {

    @GenComment("文件原始名称")
    private String originalFileName;

    @GenComment("文件相对路径")
    private String relativePath;

    @GenComment("压缩后文件相对路径")
    private String compressedRelativePath;

    @GenComment("文件上传时间")
    private String uploadTime;

    @GenComment("文件后缀")
    private String extension;

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
