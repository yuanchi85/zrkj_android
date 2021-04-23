package zrkj.project.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;

/**
 * @Description: SA_SaleDelivery_b
 * @Author: jeecg-boot
 * @Date:   2021-02-03
 * @Version: V1.0
 */
public class SaSaleDeliveryDetail implements Serializable {
    private static final long serialVersionUID = 1L;

	/**编号*/
    private String code;
	/**数量*/
    private BigDecimal quantity;
	/**批号*/
    private String batch;
	/**updatedby*/
    private String updatedby;
	/**id*/
    private String id;
	/**存货id*/
    private String idinventory;
	/**deliverydate*/
    private Date deliverydate;
	/**createdtime*/
    private Date createdtime;
	/**updated*/
    private Date updated;
	/**productiondate*/
    private Date productiondate;
    /**销货单id*/
    private String idsaledeliverydto;

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

    public String getUpdatedby() {
        return updatedby;
    }

    public void setUpdatedby(String updatedby) {
        this.updatedby = updatedby;
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

    public Date getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(Date createdtime) {
        this.createdtime = createdtime;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
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
}
