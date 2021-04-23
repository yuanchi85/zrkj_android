package zrkj.project.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;

/**
 * @Description: 销售销货表
 * @Author: jeecg-boot
 * @Date:   2021-02-04
 * @Version: V1.0
 */
public class CmsSaleDetailed implements Serializable {
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
	/**销货日期*/
    private Date saleTime;
	/**销货人*/
    private String saleBy;
	/**销货数量*/
    private BigDecimal quantity;
	/**仓库*/
    private String warehouseCode;
	/**货位*/
    private String locationCode;
	/**托盘条码*/
    private String palletCode;
	/**销货单单号*/
    private String salesNumber;
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

    /**销货明细id*/
    private String salesId;

    public String getSalesId() {
        return salesId;
    }

    public void setSalesId(String salesId) {
        this.salesId = salesId;
    }

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

    public Date getSaleTime() {
        return saleTime;
    }

    public void setSaleTime(Date saleTime) {
        this.saleTime = saleTime;
    }

    public String getSaleBy() {
        return saleBy;
    }

    public void setSaleBy(String saleBy) {
        this.saleBy = saleBy;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getPalletCode() {
        return palletCode;
    }

    public void setPalletCode(String palletCode) {
        this.palletCode = palletCode;
    }

    public String getSalesNumber() {
        return salesNumber;
    }

    public void setSalesNumber(String salesNumber) {
        this.salesNumber = salesNumber;
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
