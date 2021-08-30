package com.bob.at.dto.help;

public class FileExportDTO {

    /**
     * 本地文件全路径
     */
    private String localFullFileName;

    /**
     * 实际文件名
     */
    private String realFileName;

    public FileExportDTO() {}

    public FileExportDTO(String localFullFileName, String realFileName) {
        this.localFullFileName = localFullFileName;
        this.realFileName = realFileName;
    }

    public String getLocalFullFileName() {
        return localFullFileName;
    }

    public void setLocalFullFileName(String localFullFileName) {
        this.localFullFileName = localFullFileName;
    }

    public String getRealFileName() {
        return realFileName;
    }

    public void setRealFileName(String realFileName) {
        this.realFileName = realFileName;
    }
}
