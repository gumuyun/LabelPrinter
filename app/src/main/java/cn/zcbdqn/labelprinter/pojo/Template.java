package cn.zcbdqn.labelprinter.pojo;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

/**
 * 模板 ,父级布局
 * Created by gumuyun on 2018/12/11.
 */
public class Template extends DataSupport implements Serializable {

    /**
     * id
     */
    private Integer id;
    /**
     * 布局id layoutId inflate.inflate()
     */
    private Integer layoutId;
    /**
     * 模板名称
     */
    private String templateName;
    /**
     * 模板类型
     */
    private String viewType;
    /**
     * 打印机类型
     */
    private Integer printType;
    /**
     * 纸张类型
     */
    private Integer paperType;
    /**
     * 宽
     */
    private Integer width;
    /**
     * 高
     */
    private Integer height;

    private Integer createBy;
    private String createDate;
    private Integer updateBy;
    private String updateDate;

    private String remarks;

    private List<TemplateEditView> templateEditViews;

    public Template() {
    }

    public Template(Integer layoutId, String templateName, String viewType, Integer printType, Integer paperType, Integer width, Integer height, Integer createBy, String createDate) {
        this.layoutId = layoutId;
        this.templateName = templateName;
        this.viewType = viewType;
        this.printType = printType;
        this.paperType = paperType;
        this.width = width;
        this.height = height;
        this.createBy = createBy;
        this.createDate = createDate;
    }

    public Template(Integer id, String templateName, String viewType, Integer printType, Integer paperType, Integer width, Integer height, Integer createBy, String createDate, Integer updateBy, String updateDate, String remarks, List<TemplateEditView> templateEditViews) {
        this.id = id;
        this.templateName = templateName;
        this.viewType = viewType;
        this.printType = printType;
        this.paperType = paperType;
        this.width = width;
        this.height = height;
        this.createBy = createBy;
        this.createDate = createDate;
        this.updateBy = updateBy;
        this.updateDate = updateDate;
        this.remarks = remarks;
        this.templateEditViews = templateEditViews;
    }

    public Template(Integer layoutId, String templateName, String viewType, Integer printType, Integer paperType, Integer width, Integer height, Integer createBy, String createDate, String remarks) {
        this.layoutId = layoutId;
        this.templateName = templateName;
        this.viewType = viewType;
        this.printType = printType;
        this.paperType = paperType;
        this.width = width;
        this.height = height;
        this.createBy = createBy;
        this.createDate = createDate;
        this.remarks = remarks;
    }

    public Integer getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(Integer layoutId) {
        this.layoutId = layoutId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public Integer getPrintType() {
        return printType;
    }

    public void setPrintType(Integer printType) {
        this.printType = printType;
    }

    public Integer getPaperType() {
        return paperType;
    }

    public void setPaperType(Integer paperType) {
        this.paperType = paperType;
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

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Integer getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<TemplateEditView> getTemplateEditViews() {
        return templateEditViews;
    }

    public void setTemplateEditViews(List<TemplateEditView> templateEditViews) {
        this.templateEditViews = templateEditViews;
    }

    @Override
    public String toString() {
        return "Template{" +
                "id=" + id +
                ", layoutId=" + layoutId +
                ", templateName='" + templateName + '\'' +
                ", viewType='" + viewType + '\'' +
                ", printType=" + printType +
                ", paperType=" + paperType +
                ", width=" + width +
                ", height=" + height +
                ", createBy=" + createBy +
                ", createDate='" + createDate + '\'' +
                ", updateBy=" + updateBy +
                ", updateDate='" + updateDate + '\'' +
                ", remarks='" + remarks + '\'' +
                ", templateEditViews=" + templateEditViews +
                '}';
    }
}
