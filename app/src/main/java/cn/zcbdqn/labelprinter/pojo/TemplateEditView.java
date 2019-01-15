package cn.zcbdqn.labelprinter.pojo;

import android.graphics.Bitmap;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by gumuyun on 2018/12/11.
 */

public class TemplateEditView extends DataSupport implements Serializable{

    /**
     * id
     */
    private Integer id;
    /**
     *  viewId  findViewById()
     */
    private Integer viewId;
    /**
     * view的类型 如:android.widget.EditText
     */
    private String viewType;
    /**
     * 父级编号
     */
    private Integer parentViewId;

    /**
     * 宽
     */
    private Integer width;
    /**
     * 高
     */
    private Integer height;
    /**
     * 重力属性
     */
    private Integer gravity;


    /**
     * 上
     */
    private Integer topMargin;
    /**
     * 右
     */
    private Integer rightMargin;
    /**
     * 下
     */
    private Integer bottomMargin;
    /**
     * 左
     */
    private Integer leftMargin;
    /**
     * 填充类型ViewGroup.LayoutParams
     */
    private Integer fillParent;
    private Integer matchParent;
    private Integer wrapContent;

    /**
     * 文字
     */
    private String text;
    /**
     * 文字大小
     */
    private Float textSize=50F;
    /**
     * 文字大小
     * 当使用大布局的时候,使用此属性作为文字大小
     */
    private Integer scaleTextSize=3;
    /**
     * 文字颜色
     */
    private String textColor;

    private Integer createBy;
    private String createDate;
    private Integer updateBy;
    private Integer visibility;//是否显示
    private String updateDate;
    private String remarks;


    private Bitmap bitmap;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Integer getScaleTextSize() {
        return scaleTextSize;
    }

    public void setScaleTextSize(Integer scaleTextSize) {
        this.scaleTextSize = scaleTextSize;
    }

    public TemplateEditView() {
    }

    public TemplateEditView(Integer viewId, String viewType, Integer parentViewId, Integer width, Integer height, Integer gravity, Integer topMargin, Integer rightMargin, Integer bottomMargin, Integer leftMargin, Integer fillParent, Integer matchParent, Integer wrapContent, String text, Float textSize, String textColor, Integer createBy, String createDate, Integer visibility) {
        this.viewId = viewId;
        this.viewType = viewType;
        this.parentViewId = parentViewId;
        this.width = width;
        this.height = height;
        this.gravity = gravity;
        this.topMargin = topMargin;
        this.rightMargin = rightMargin;
        this.bottomMargin = bottomMargin;
        this.leftMargin = leftMargin;
        this.fillParent = fillParent;
        this.matchParent = matchParent;
        this.wrapContent = wrapContent;
        this.text = text;
        this.textSize = textSize;
        this.textColor = textColor;
        this.createBy = createBy;
        this.createDate = createDate;
        this.visibility = visibility;
    }

    public Integer getVisibility() {
        return visibility;
    }

    public void setVisibility(Integer visibility) {
        this.visibility = visibility;
    }

    public Integer getViewId() {
        return viewId;
    }

    public void setViewId(Integer viewId) {
        this.viewId = viewId;
    }

    public TemplateEditView(Integer id, String viewType, Integer parentViewId, Integer width, Integer height, Integer gravity, Integer topMargin, Integer rightMargin, Integer bottomMargin, Integer leftMargin, Integer fillParent, Integer matchParent, Integer wrapContent, String text, Float textSize, String textColor, Integer createBy, String createDate, Integer updateBy, String updateDate, String remarks) {
        this.id = id;
        this.viewType = viewType;
        this.parentViewId = parentViewId;
        this.width = width;
        this.height = height;
        this.gravity = gravity;
        this.topMargin = topMargin;
        this.rightMargin = rightMargin;
        this.bottomMargin = bottomMargin;
        this.leftMargin = leftMargin;
        this.fillParent = fillParent;
        this.matchParent = matchParent;
        this.wrapContent = wrapContent;
        this.text = text;
        this.textSize = textSize;
        this.textColor = textColor;
        this.createBy = createBy;
        this.createDate = createDate;
        this.updateBy = updateBy;
        this.updateDate = updateDate;
        this.remarks = remarks;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public Integer getParentViewId() {
        return parentViewId;
    }

    public void setParentViewId(Integer parentViewId) {
        this.parentViewId = parentViewId;
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

    public Integer getGravity() {
        return gravity;
    }

    public void setGravity(Integer gravity) {
        this.gravity = gravity;
    }

    public Integer getTopMargin() {
        return topMargin;
    }

    public void setTopMargin(Integer topMargin) {
        this.topMargin = topMargin;
    }

    public Integer getRightMargin() {
        return rightMargin;
    }

    public void setRightMargin(Integer rightMargin) {
        this.rightMargin = rightMargin;
    }

    public Integer getBottomMargin() {
        return bottomMargin;
    }

    public void setBottomMargin(Integer bottomMargin) {
        this.bottomMargin = bottomMargin;
    }

    public Integer getLeftMargin() {
        return leftMargin;
    }

    public void setLeftMargin(Integer leftMargin) {
        this.leftMargin = leftMargin;
    }

    public Integer getFillParent() {
        return fillParent;
    }

    public void setFillParent(Integer fillParent) {
        this.fillParent = fillParent;
    }

    public Integer getMatchParent() {
        return matchParent;
    }

    public void setMatchParent(Integer matchParent) {
        this.matchParent = matchParent;
    }

    public Integer getWrapContent() {
        return wrapContent;
    }

    public void setWrapContent(Integer wrapContent) {
        this.wrapContent = wrapContent;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Float getTextSize() {
        return textSize;
    }

    public void setTextSize(Float textSize) {
        this.textSize = textSize;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
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

    @Override
    public String toString() {
        return "TemplateEditView{" +
                "id=" + id +
                ", viewId=" + viewId +
                ", viewType='" + viewType + '\'' +
                ", parentViewId=" + parentViewId +
                ", width=" + width +
                ", height=" + height +
                ", gravity=" + gravity +
                ", topMargin=" + topMargin +
                ", rightMargin=" + rightMargin +
                ", bottomMargin=" + bottomMargin +
                ", leftMargin=" + leftMargin +
                ", fillParent=" + fillParent +
                ", matchParent=" + matchParent +
                ", wrapContent=" + wrapContent +
                ", text='" + text + '\'' +
                ", textSize=" + textSize +
                ", scaleTextSize=" + scaleTextSize +
                ", textColor='" + textColor + '\'' +
                ", createBy=" + createBy +
                ", createDate='" + createDate + '\'' +
                ", updateBy=" + updateBy +
                ", visibility=" + visibility +
                ", updateDate='" + updateDate + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
