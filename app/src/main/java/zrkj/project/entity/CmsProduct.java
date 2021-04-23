package zrkj.project.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;

/**
 * @Description: cms_product
 * @Author: jeecg-boot
 * @Date:   2020-12-14
 * @Version: V1.0
 */
public class CmsProduct implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键id*/
    private String id;
	/**产品编号*/
    private String productCode;
	/**产品名称*/
    private String productName;
	/**规格型号*/
    private String specificationModel;
	/**计量单位*/
    private String unit;
	/**条码*/
    private String barCode;
	/**托盘数量*/
    private BigDecimal palletsNumber;
	/**备注*/
    private String remarks;
	/**创建人*/
    private String createBy;
	/**创建时间*/
    private Date createTime;
	/**更新人*/
    private String updateBy;
	/**更新时间*/
    private Date updateTime;

    private BigDecimal zongshu;

    public BigDecimal getZongshu() {
        return zongshu;
    }

    public void setZongshu(BigDecimal zongshu) {
        this.zongshu = zongshu;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSpecificationModel() {
        return specificationModel;
    }

    public void setSpecificationModel(String specificationModel) {
        this.specificationModel = specificationModel;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public BigDecimal getPalletsNumber() {
        return palletsNumber;
    }

    public void setPalletsNumber(BigDecimal palletsNumber) {
        this.palletsNumber = palletsNumber;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
