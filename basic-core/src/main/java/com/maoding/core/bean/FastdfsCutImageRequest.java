package com.maoding.core.bean;

/**
 * Created by Wuwq on 2016/12/29.
 */
public class FastdfsCutImageRequest {

    private String group;
    private String path;
    private String fileName;
    private Integer x;
    private Integer y;
    private Integer width;
    private Integer height;
    /**
     * 缩放宽度
     */
    private Integer zoomWidth;
    /**
     * 缩放高度
     */
    private Integer zoomHeight;
    /**
     * 缩放保持纵横比
     */
    private boolean zoomKeepRatio = true;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCutPrefixName() {
        return "_cut_" + x + "_" + y + "_" + width + "x" + height;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public void setZoomWidth(Integer zoomWidth) {
        this.zoomWidth = zoomWidth;
    }

    public void setZoomHeight(Integer zoomHeight) {
        this.zoomHeight = zoomHeight;
    }

    public boolean isZoomKeepRatio() {
        return zoomKeepRatio;
    }

    public void setZoomKeepRatio(boolean zoomKeepRatio) {
        this.zoomKeepRatio = zoomKeepRatio;
    }

    public Integer getZoomWidth() {
        return zoomWidth;
    }

    public void setZoomWidth(int zoomWidth) {
        this.zoomWidth = zoomWidth;
    }

    public Integer getZoomHeight() {
        return zoomHeight;
    }

    public void setZoomHeight(int zoomHeight) {
        this.zoomHeight = zoomHeight;
    }

    public String getFileExtName() {
        int extIndex = path.lastIndexOf(".");
        if (extIndex != -1) {
            return path.substring(extIndex + 1);
        }
        return null;
    }
}
