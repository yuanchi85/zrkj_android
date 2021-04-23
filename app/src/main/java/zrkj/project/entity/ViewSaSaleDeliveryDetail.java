package zrkj.project.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: VIEW_SA_SALE_Delivery_Detail
 * @Author: jeecg-boot
 * @Date:   2021-02-03
 * @Version: V1.0
 */
public class ViewSaSaleDeliveryDetail implements Serializable {
    private static final long serialVersionUID = 1L;

	/**编号*/
    private String code;
	/**数量*/
    private BigDecimal quantity;
	/**批号*/
    private String batch;
	/**更新人*/
    private String updateBy;
	/**id*/
    private String id;
	/**存货id*/
    private String idinventory;
	/**deliverydate*/
    private Date deliverydate;
	/**创建时间*/
    private Date createTime;
    private Date updateTime;
	/**productiondate*/
    private Date productiondate;
    /**销货单id*/
    private String idsaledeliverydto;
    /**产品编号*/
    private String productCode;
    /**产品名称*/
    private String productName;
    /**规格型号*/
    private String specificationModel;
    /**计量单位*/
    private String unit;
    /**
     * 实际批号
     */
    private String actualBatch;

    private String salesId;

    public String getSalesId() {
        return salesId;
    }

    public void setSalesId(String salesId) {
        this.salesId = salesId;
    }

    /**
     * 实际数量
     */
    private BigDecimal actualQuantity;
    /**备注*/
    private String remarks;
    /**销货单单号(退货、销货)*/
    private String salesNumber;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdinventory() {
        return idinventory;
    }

    public void setIdinventory(String idinventory) {
        this.idinventory = idinventory;
    }

    public Date getDeliverydate() {
        return deliverydate;
    }

    public void setDeliverydate(Date deliverydate) {
        this.deliverydate = deliverydate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getProductiondate() {
        return productiondate;
    }

    public void setProductiondate(Date productiondate) {
        this.productiondate = productiondate;
    }

    public String getIdsaledeliverydto() {
        return idsaledeliverydto;
    }

    public void setIdsaledeliverydto(String idsaledeliverydto) {
        this.idsaledeliverydto = idsaledeliverydto;
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

    public String getActualBatch() {
        return actualBatch;
    }

    public void setActualBatch(String actualBatch) {
        this.actualBatch = actualBatch;
    }

    public BigDecimal getActualQuantity() {
        return actualQuantity;
    }

    public void setActualQuantity(BigDecimal actualQuantity) {
        this.actualQuantity = actualQuantity;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getSalesNumber() {
        return salesNumber;
    }

    public void setSalesNumber(String salesNumber) {
        this.salesNumber = salesNumber;
    }
}
