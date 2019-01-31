package ${packageName}.dto.criteria.filter;

import java.io.Serializable;

public class NothingFilter<FIELD_TYPE> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nothing;    // 什么条件都没有

    public String getNothing() {
        return nothing;
    }

    public void setNothing(String nothing) {
        this.nothing = nothing;
    }

    protected String getFilterName() {
        return this.getClass().getSimpleName();
    }

}
