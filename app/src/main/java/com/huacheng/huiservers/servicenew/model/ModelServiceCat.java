package com.huacheng.huiservers.servicenew.model;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * created by wangxiaotao
 * 2018/9/7 0007 下午 6:17
 */
public class ModelServiceCat implements Serializable {
    private String id;
    private String p_id;
    private String name;
    private List<GridBean> list;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GridBean> getList() {
        return list;
    }

    public void setList(List<GridBean> list) {
        this.list = list;
    }

    public static class GridBean {
        /**
         * id : 10
         * p_id : 1
         * name : 清洗厨房
         */

        private String id;
        private String p_id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getP_id() {
            return p_id;
        }

        public void setP_id(String p_id) {
            this.p_id = p_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
