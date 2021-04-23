package zrkj.project.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;

/**
 * @Description: cms_assembly
 * @Author: jeecg-boot
 * @Date:   2021-01-16
 * @Version: V1.0
 */
public class CmsAssembly implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键id*/

    private String id;

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

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
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

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getQualifiedQuantity() {
        return qualifiedQuantity;
    }

    public void setQualifiedQuantity(BigDecimal qualifiedQuantity) {
        this.qualifiedQuantity = qualifiedQuantity;
    }

    public BigDecimal getUnqualifiedQuantity() {
        return unqualifiedQuantity;
    }

    public void setUnqualifiedQuantity(BigDecimal unqualifiedQuantity) {
        this.unqualifiedQuantity = unqualifiedQuantity;
    }

    public String getProductionLineCode() {
        return productionLineCode;
    }

    public void setProductionLineCode(String productionLineCode) {
        this.productionLineCode = productionLineCode;
    }

    public String getPointsStands() {
        return pointsStands;
    }

    public void setPointsStands(String pointsStands) {
        this.pointsStands = pointsStands;
    }

    public Date getPartingTime() {
        return partingTime;
    }

    public void setPartingTime(Date partingTime) {
        this.partingTime = partingTime;
    }

    public Date getStackingTime() {
        return stackingTime;
    }

    public void setStackingTime(Date stackingTime) {
        this.stackingTime = stackingTime;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public Date getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(Date reviewTime) {
        this.reviewTime = reviewTime;
    }

    public String getIsReview() {
        return isReview;
    }

    public void setIsReview(String isReview) {
        this.isReview = isReview;
    }

    public String getQualityInspector() {
        return qualityInspector;
    }

    public void setQualityInspector(String qualityInspector) {
        this.qualityInspector = qualityInspector;
    }

    public Date getQualityTime() {
        return qualityTime;
    }

    public void setQualityTime(Date qualityTime) {
        this.qualityTime = qualityTime;
    }

    public String getIsQuality() {
        return isQuality;
    }

    public void setIsQuality(String isQuality) {
        this.isQuality = isQuality;
    }

    public String getInboundNumber() {
        return inboundNumber;
    }

    public void setInboundNumber(String inboundNumber) {
        this.inboundNumber = inboundNumber;
    }

    public String getCancellingNumber() {
        return cancellingNumber;
    }

    public void setCancellingNumber(String cancellingNumber) {
        this.cancellingNumber = cancellingNumber;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
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

    public BigDecimal getInventoryQuantity() {
        return inventoryQuantity;
    }

    public void setInventoryQuantity(BigDecimal inventoryQuantity) {
        this.inventoryQuantity = inventoryQuantity;
    }

    public BigDecimal getActualWarehouseQuantity() {
        return actualWarehouseQuantity;
    }

    public void setActualWarehouseQuantity(BigDecimal actualWarehouseQuantity) {
        this.actualWarehouseQuantity = actualWarehouseQuantity;
    }

    public BigDecimal getReviewQuantity() {
        return reviewQuantity;
    }

    public void setReviewQuantity(BigDecimal reviewQuantity) {
        this.reviewQuantity = reviewQuantity;
    }

    public String getQualityInspectionType() {
        return qualityInspectionType;
    }

    public void setQualityInspectionType(String qualityInspectionType) {
        this.qualityInspectionType = qualityInspectionType;
    }

    public String getReviewerBatch() {
        return reviewerBatch;
    }

    public void setReviewerBatch(String reviewerBatch) {
        this.reviewerBatch = reviewerBatch;
    }

    /**产品编码*/

    private String productCode;
    /**产品名称*/
    private String productName;
    /**规格型号*/
    private String specificationModel;
    /**计量单位*/
    private String unit;
	/**批次号*/
    private String batch;
	/**仓库编码*/
    private String warehouseCode;
	/**货位编码*/
    private String locationCode;
	/**托盘编码*/

    private String palletCode;
	/**条码*/

    private String barCode;
	/**数量*/

    private BigDecimal quantity;
	/**合格数量*/

    private BigDecimal qualifiedQuantity;
	/**不合格数量*/

    private BigDecimal unqualifiedQuantity;
	/**产线编码*/
    private String productionLineCode;
	/**分托人*/

    private String pointsStands;
	/**分托时间*/

    private Date partingTime;
	/**码垛时间*/

    private Date stackingTime;
	/**复核人*/

    private String reviewer;
	/**复核时间*/

    private Date reviewTime;
	/**是否复核*/

    private String isReview;
	/**质检人*/

    private String qualityInspector;
	/**质检时间*/

    private Date qualityTime;
	/**是否质检*/

    private String isQuality;
	/**入库单号*/

    private String inboundNumber;
	/**退库单号*/

    private String cancellingNumber;
	/**备注*/

    private String remarks;
	/**状态*/

    private String state;
	/**类型*/

    private String type;
	/**设备id*/

    private String equipmentId;
	/**创建人*/

    private String createBy;
	/**创建时间*/

    private Date createTime;
	/**更新人*/

    private String updateBy;
	/**更新时间*/

    private Date updateTime;
    /**入库数量*/

    private BigDecimal inventoryQuantity;
    /**实际入库数量*/

    private BigDecimal actualWarehouseQuantity;
    /**复核数量*/

    private BigDecimal reviewQuantity;
    /**质检类型*/

    private String qualityInspectionType;
    /**复核批号*/

    private String reviewerBatch;

    /**仓库名称*/
    private String warehouseName;

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    private BigDecimal zongshu;
    private BigDecimal yilingshuliang;

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

    public BigDecimal getYilingshuliang() {
        return yilingshuliang;
    }

    public void setYilingshuliang(BigDecimal yilingshuliang) {
        this.yilingshuliang = yilingshuliang;
    }
}
