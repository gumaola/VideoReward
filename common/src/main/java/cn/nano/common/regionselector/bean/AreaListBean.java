package cn.nano.common.regionselector.bean;

public class AreaListBean {
    /**
     * code : 110101
     * name : 东城区
     */

    private String code;
    private String name;

    //是否是最后一个区域
    private boolean hasChildArea = true;
    private boolean isDirectCity;//直辖市或者特别行政区

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHasChildArea() {
        return hasChildArea;
    }

    public void setHasChildArea(boolean hasChildArea) {
        this.hasChildArea = hasChildArea;
    }

    public boolean isDirectCity() {
        return isDirectCity;
    }

    public void setDirectCity(boolean directCity) {
        isDirectCity = directCity;
    }
}
