package zrkj.project.entity;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

/**
 * @Description: cms_current_products
 * @Author: jeecg-boot
 * @Date:   2021-01-15
 * @Version: V1.0
 */
public class CmsCurrentProductsBay implements Serializable {
    private static final long serialVersionUID = 1L;


    private String id;
	/**产线编码*/
    private String productionLineCode;
    /**产线名称*/
    private String productionLineName;
	/**产品编码*/
    private String productCode;
    /**产品名称*/
    private String productName;
    /**规格型号*/
    private String specificationModel;
	/**批次号*/
    private String batch;
	/**设置人*/
    private String setter;

	/**设置时间*/
    private Date setterTime;
	/**当前数量*/
    private BigDecimal quantity;
	/**创建人*/
    private String createBy;
	/**创建时间*/
    private Date createTime;
	/**更新人*/
    private String updateBy;
	/**更新时间*/
    private Date updateTime;
    /**设备id*/
    private String equipmentId;
    /**计量单位*/
    private String unit;
    private String basicData;

    /**探头1计数*/
    private BigDecimal quantity1;

    /**探头2计数*/
    private BigDecimal quantity2;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductionLineCode() {
        return productionLineCode;
    }

    public void setProductionLineCode(String productionLineCode) {
        this.productionLineCode = productionLineCode;
    }

    public String getProductionLineName() {
        return productionLineName;
    }

    public void setProductionLineName(String productionLineName) {
        this.productionLineName = productionLineName;
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

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getSetter() {
        return setter;
    }

    public void setSetter(String setter) {
        this.setter = setter;
    }

    public Date getSetterTime() {
        return setterTime;
    }

    public void setSetterTime(Date setterTime) {
        this.setterTime = setterTime;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
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

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getBasicData() {
        return basicData;
    }

    public void setBasicData(String basicData) {
        this.basicData = basicData;
    }

    public BigDecimal getQuantity1() {
        return quantity1;
    }

    public void setQuantity1(BigDecimal quantity1) {
        this.quantity1 = quantity1;
    }

    public BigDecimal getQuantity2() {
        return quantity2;
    }

    public void setQuantity2(BigDecimal quantity2) {
        this.quantity2 = quantity2;
    }
}
