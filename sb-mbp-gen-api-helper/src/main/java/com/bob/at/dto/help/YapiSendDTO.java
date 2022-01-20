package com.bob.at.dto.help;

public class YapiSendDTO {

    private String ahProjectId;     // 项目ID

    private String dataJson;        // 待发送的数据

    public String getAhProjectId() {
        return ahProjectId;
    }

    public void setAhProjectId(String ahProjectId) {
        this.ahProjectId = ahProjectId;
    }

    public String getDataJson() {
        return dataJson;
    }

    public void setDataJson(String dataJson) {
        this.dataJson = dataJson;
    }
}
