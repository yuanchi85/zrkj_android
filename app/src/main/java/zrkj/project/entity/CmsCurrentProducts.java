package zrkj.project.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;


/**
 * @Description: cms_current_products
 * @Author: jeecg-boot
 * @Date:   2021-01-15
 * @Version: V1.0
 */
public class CmsCurrentProducts implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键id*/
    private String id;
	/**产线编码*/
    private String productionLineCode;
	/**产品编码*/
    private String productCode;
	/**批次号*/
    private String batch;
	/**设置人*/

    private String setter;
	/**设置时间*/

    private Date setterTime;
	/**当前数量*/

    private BigDecimal currentQuantity;
	/**总数量*/

    private BigDecimal totalQuantity;
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

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
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

    public BigDecimal getCurrentQuantity() {
        return currentQuantity;
    }

    public void setCurrentQuantity(BigDecimal currentQuantity) {
        this.currentQuantity = currentQuantity;
    }

    public BigDecimal getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(BigDecimal totalQuantity) {
        this.totalQuantity = totalQuantity;
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
}
