package com.huacheng.huiservers.property.bean;

import com.huacheng.huiservers.house.HouseBean;

import java.io.Serializable;
import java.util.List;

/**
 * 类描述：
 * 时间：2018/8/11 17:04
 * created by DFF
 */
public class ModelPropertyWyInfo {

    /**
     * shuifei : {"type":"36864","info":{"Total":"0.00","C_time":"2018-08-12 01:03:04.557","Saccount":"32.00","SMay_acc":"32.00"},"upper_limit":300}
     * dianfei : {"type":"36865","info":{"Total":"0.00","C_time":"2018-08-12 01:02:55.0","Daccount":"242.00","DMay_acc":"242.00"},"upper_limit":300}
     * room_info : {"building_name":null,"unit":null,"community_name":null,"address":"号楼单元","room_id":null,"code":null,"community_id":null,"fullname":null}
     * is_available : 0
     * is_available_cn :
     * wuye : {"list":[[{"startdate":"2017-07-01","enddate":"2018-01-01","sumvalue":"685.32","charge_type_cn":"物业费","charge_type":"1","bill_id":"354354"},{"startdate":"2018-01-01","enddate":"2019-01-01","sumvalue":"1370.64","charge_type_cn":"物业费","charge_type":"1","bill_id":"385022"},{"startdate":"2019-01-01","enddate":"2020-01-01","sumvalue":"1370.64","charge_type_cn":"物业费","charge_type":"1","bill_id":"635055"}],[{"startdate":"2017-07-01","enddate":"2018-01-01","sumvalue":"239.86","charge_type_cn":"电梯费,按建筑面积计算","charge_type":"10","bill_id":"354516"},{"startdate":"2018-01-01","enddate":"2019-01-01","sumvalue":"479.72","charge_type_cn":"电梯费,按建筑面积计算","charge_type":"10","bill_id":"384920"},{"startdate":"2019-01-01","enddate":"2020-01-01","sumvalue":"479.72","charge_type_cn":"电梯费,按建筑面积计算","charge_type":"10","bill_id":"634953"}]],"tot_sumvalue":4625.9}
     */

    private RoomInfoBean room_info;
    private int is_available;
    private String is_available_cn;
    private List<WuyeBean> wuye;

    private List<SFaddDFBean> shuifei;
    private List<SFaddDFBean> dianfei;

    public List<SFaddDFBean> getShuifei() {
        return shuifei;
    }

    public void setShuifei(List<SFaddDFBean> shuifei) {
        this.shuifei = shuifei;
    }

    public List<SFaddDFBean> getDianfei() {
        return dianfei;
    }

    public void setDianfei(List<SFaddDFBean> dianfei) {
        this.dianfei = dianfei;
    }

    public RoomInfoBean getRoom_info() {
        return room_info;
    }

    public void setRoom_info(RoomInfoBean room_info) {
        this.room_info = room_info;
    }

    public int getIs_available() {
        return is_available;
    }

    public void setIs_available(int is_available) {
        this.is_available = is_available;
    }

    public String getIs_available_cn() {
        return is_available_cn;
    }

    public void setIs_available_cn(String is_available_cn) {
        this.is_available_cn = is_available_cn;
    }

    public List<WuyeBean> getWuye() {
        return wuye;
    }

    public void setWuye(List<WuyeBean> wuye) {
        this.wuye = wuye;
    }


    public static class RoomInfoBean implements Serializable {
        /**
         * building_name : null
         * unit : null
         * community_name : null
         * address : 号楼单元
         * room_id : null
         * code : null
         * community_id : null
         * fullname : null
         */

        private String building_name;
        private String unit;
        private String community_name;
        private String address;
        private String room_id;
        private String code;
        private String community_id;
        private String fullname;
        private String name;
        private String order_num;
        private String addtime;
        private String tot_sumvalue;
        private String pay_time;
        private String count;//

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getPay_time() {
            return pay_time;
        }

        public void setPay_time(String pay_time) {
            this.pay_time = pay_time;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTot_sumvalue() {
            return tot_sumvalue;
        }

        public void setTot_sumvalue(String tot_sumvalue) {
            this.tot_sumvalue = tot_sumvalue;
        }

        public String getOrder_num() {
            return order_num;
        }

        public void setOrder_num(String order_num) {
            this.order_num = order_num;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getBuilding_name() {
            return building_name;
        }

        public void setBuilding_name(String building_name) {
            this.building_name = building_name;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getCommunity_name() {
            return community_name;
        }

        public void setCommunity_name(String community_name) {
            this.community_name = community_name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getRoom_id() {
            return room_id;
        }

        public void setRoom_id(String room_id) {
            this.room_id = room_id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getCommunity_id() {
            return community_id;
        }

        public void setCommunity_id(String community_id) {
            this.community_id = community_id;
        }

        public String getFullname() {
            return fullname;
        }

        public void setFullname(String fullname) {
            this.fullname = fullname;
        }
    }


    public static class WuyeBean {

        /**
         * id : 20
         * chargeFrom : 0
         * chargeEnd : 0
         * category_name : 装修保证金
         * realPrice : 1000.00
         * createAt : 1539679408
         */

        private String id;
        private String chargeFrom;
        private String chargeEnd;
        private String category_name;
        private String realPrice;
        private String createAt;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getChargeFrom() {
            return chargeFrom;
        }

        public void setChargeFrom(String chargeFrom) {
            this.chargeFrom = chargeFrom;
        }

        public String getChargeEnd() {
            return chargeEnd;
        }

        public void setChargeEnd(String chargeEnd) {
            this.chargeEnd = chargeEnd;
        }

        public String getCategory_name() {
            return category_name;
        }

        public void setCategory_name(String category_name) {
            this.category_name = category_name;
        }

        public String getRealPrice() {
            return realPrice;
        }

        public void setRealPrice(String realPrice) {
            this.realPrice = realPrice;
        }

        public String getCreateAt() {
            return createAt;
        }

        public void setCreateAt(String createAt) {
            this.createAt = createAt;
        }
    }


    public static class SFaddDFBean {
        /**
         * type : 36864
         * type_cn
         * info : {"Total":"0.00","C_time":"2018-08-12 01:03:04.557","Saccount":"32.00","SMay_acc":"32.00"}
         * upper_limit : 300
         */

        private String type;
        private String type_cn;
        private HouseBean info;
        private Double upper_limit;

        public String getType_cn() {
            return type_cn;
        }

        public void setType_cn(String type_cn) {
            this.type_cn = type_cn;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public HouseBean getInfo() {
            return info;
        }

        public void setInfo(HouseBean info) {
            this.info = info;
        }

        public Double getUpper_limit() {
            return upper_limit;
        }

        public void setUpper_limit(Double upper_limit) {
            this.upper_limit = upper_limit;
        }
    }

}
