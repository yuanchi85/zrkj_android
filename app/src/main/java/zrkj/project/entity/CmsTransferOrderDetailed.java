package zrkj.project.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;

/**
 * @Description: cms_transfer_order_detailed
 * @Author: jeecg-boot
 * @Date:   2021-02-01
 * @Version: V1.0
 */
public class CmsTransferOrderDetailed implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键id*/
    private String id;
	/**产品编码*/
    private String productCode;
	/**产品名称*/
    private String productName;
	/**规格型号*/
    private String specificationModel;
	/**计量单位*/
    private String unit;
	/**条码*/
    private String barCode;
	/**产品批次*/
    private String batch;
	/**调拨日期*/
    private Date transferTime;
	/**调拨人*/
    private String transferBy;
	/**调拨数量*/
    private BigDecimal quantity;
	/**原仓库*/
    private String originalWarehouse;
	/**原货位*/
    private String originalLocation;
	/**原托盘条码*/
    private String originalPalletCode;
	/**新仓库*/
    private String newWarehouse;
	/**新货位*/
    private String newLocation;
	/**新托盘条码*/
    private String newPalletCode;
	/**调拨单单号*/
    private String transferOrderNumber;
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

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public Date getTransferTime() {
        return transferTime;
    }

    public void setTransferTime(Date transferTime) {
        this.transferTime = transferTime;
    }

    public String getTransferBy() {
        return transferBy;
    }

    public void setTransferBy(String transferBy) {
        this.transferBy = transferBy;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getOriginalWarehouse() {
        return originalWarehouse;
    }

    public void setOriginalWarehouse(String originalWarehouse) {
        this.originalWarehouse = originalWarehouse;
    }

    public String getOriginalLocation() {
        return originalLocation;
    }

    public void setOriginalLocation(String originalLocation) {
        this.originalLocation = originalLocation;
    }

    public String getOriginalPalletCode() {
        return originalPalletCode;
    }

    public void setOriginalPalletCode(String originalPalletCode) {
        this.originalPalletCode = originalPalletCode;
    }

    public String getNewWarehouse() {
        return newWarehouse;
    }

    public void setNewWarehouse(String newWarehouse) {
        this.newWarehouse = newWarehouse;
    }

    public String getNewLocation() {
        return newLocation;
    }

    public void setNewLocation(String newLocation) {
        this.newLocation = newLocation;
    }

    public String getNewPalletCode() {
        return newPalletCode;
    }

    public void setNewPalletCode(String newPalletCode) {
        this.newPalletCode = newPalletCode;
    }

    public String getTransferOrderNumber() {
        return transferOrderNumber;
    }

    public void setTransferOrderNumber(String transferOrderNumber) {
        this.transferOrderNumber = transferOrderNumber;
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
