package ${packageName}.dto.help;

public class BaseEntityConfigRelationDTO {

    private String relationType;        // OneToOne和OneToMany

    private String fromOrTo;            // from或to

    private String fromType;            // from类型

    private String fromName;            // from名称

    private String toType;              // to类型

    private String toName;              // to名称

    private String fromToComment;       // to在from中的注释

    private String toFromComment;       // from在to中的注释

    private String cascadeDelete;       // 级联删除类型（DELETE：级联删除，FORBIDDEN：禁止删除，NULL：级联置空）

    public BaseEntityConfigRelationDTO() {}

    public BaseEntityConfigRelationDTO(String relationType, String fromOrTo, String fromType, String fromName,
                                       String toType, String toName, String fromToComment, String toFromComment,
                                       String cascadeDelete) {
        this.relationType = relationType;
        this.fromOrTo = fromOrTo;
        this.fromType = fromType;
        this.fromName = fromName;
        this.toType = toType;
        this.toName = toName;
        this.fromToComment = fromToComment;
        this.toFromComment = toFromComment;
        this.cascadeDelete = cascadeDelete;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public String getFromOrTo() {
        return fromOrTo;
    }

    public void setFromOrTo(String fromOrTo) {
        this.fromOrTo = fromOrTo;
    }

    public String getFromType() {
        return fromType;
    }

    public void setFromType(String fromType) {
        this.fromType = fromType;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getToType() {
        return toType;
    }

    public void setToType(String toType) {
        this.toType = toType;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getFromToComment() {
        return fromToComment;
    }

    public void setFromToComment(String fromToComment) {
        this.fromToComment = fromToComment;
    }

    public String getToFromComment() {
        return toFromComment;
    }

    public void setToFromComment(String toFromComment) {
        this.toFromComment = toFromComment;
    }

    public String getCascadeDelete() {
        return cascadeDelete;
    }

    public void setCascadeDelete(String cascadeDelete) {
        this.cascadeDelete = cascadeDelete;
    }
}
