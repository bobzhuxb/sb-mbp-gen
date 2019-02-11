package ${packageName}.dto.help;

public class ExcelTitleDTO {

    private String titleName;       // 标题名（字段或Key）
    private String titleContent;    // 标题内容

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getTitleContent() {
        return titleContent;
    }

    public void setTitleContent(String titleContent) {
        this.titleContent = titleContent;
    }
}
