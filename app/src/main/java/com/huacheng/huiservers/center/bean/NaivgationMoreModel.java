package com.huacheng.huiservers.center.bean;

import java.util.List;

public class NaivgationMoreModel {
	

    /**
     * status : 1
     * msg : 获取数据成功！
     * data : [{"id":"1","name":"生活服务","display":"1","order_num":"0","addtime":"1494062176","list":[{"id":"3","pid":"1","pname":"生活服务","menu_name":"水电暖","menu_logo":"data/upload/menu_logo/59127083b0451.png","url_type":"5","url_type_cn":"生活服务","url_id":"personal/place_order/id/1","order_num":"0","display":"1","addtime":"1494236972"},{"id":"4","pid":"1","pname":"生活服务","menu_name":"家政服务","menu_logo":"data/upload/menu_logo/5912709224663.png","url_type":"5","url_type_cn":"生活服务","url_id":"personal/place_order/id/4","order_num":"0","display":"1","addtime":"1494237181"},{"id":"5","pid":"1","pname":"生活服务","menu_name":"洗衣/干洗","menu_logo":"data/upload/menu_logo/5912709b7d2e0.png","url_type":"5","url_type_cn":"生活服务","url_id":"personal/place_order/id/3","order_num":"0","display":"1","addtime":"1494237226"},{"id":"6","pid":"1","pname":"生活服务","menu_name":"家用维修","menu_logo":"data/upload/menu_logo/591270a4d3c96.png","url_type":"5","url_type_cn":"生活服务","url_id":"personal/place_order/id/2","order_num":"0","display":"1","addtime":"1494237260"}]}]
     * dialog :
     */

    private int status;
    private String msg;
    private String dialog;
    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDialog() {
        return dialog;
    }

    public void setDialog(String dialog) {
        this.dialog = dialog;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * name : 生活服务
         * display : 1
         * order_num : 0
         * addtime : 1494062176
         * list : [{"id":"3","pid":"1","pname":"生活服务","menu_name":"水电暖","menu_logo":"data/upload/menu_logo/59127083b0451.png","url_type":"5","url_type_cn":"生活服务","url_id":"personal/place_order/id/1","order_num":"0","display":"1","addtime":"1494236972"},{"id":"4","pid":"1","pname":"生活服务","menu_name":"家政服务","menu_logo":"data/upload/menu_logo/5912709224663.png","url_type":"5","url_type_cn":"生活服务","url_id":"personal/place_order/id/4","order_num":"0","display":"1","addtime":"1494237181"},{"id":"5","pid":"1","pname":"生活服务","menu_name":"洗衣/干洗","menu_logo":"data/upload/menu_logo/5912709b7d2e0.png","url_type":"5","url_type_cn":"生活服务","url_id":"personal/place_order/id/3","order_num":"0","display":"1","addtime":"1494237226"},{"id":"6","pid":"1","pname":"生活服务","menu_name":"家用维修","menu_logo":"data/upload/menu_logo/591270a4d3c96.png","url_type":"5","url_type_cn":"生活服务","url_id":"personal/place_order/id/2","order_num":"0","display":"1","addtime":"1494237260"}]
         */

        private String id;
        private String name;
        private String display;
        private String order_num;
        private String addtime;
        private List<ListBean> list;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDisplay() {
            return display;
        }

        public void setDisplay(String display) {
            this.display = display;
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

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * id : 3
             * pid : 1
             * pname : 生活服务
             * menu_name : 水电暖
             * menu_logo : data/upload/menu_logo/59127083b0451.png
             * url_type : 5
             * url_type_cn : 生活服务
             * url_id : personal/place_order/id/1
             * order_num : 0
             * display : 1
             * addtime : 1494236972
             */

            private String id;
            private String pid;
            private String pname;
            private String menu_name;
            private String menu_logo;
            private String url_type;
            private String url_type_cn;
            private String url_id;
            private String order_num;
            private String display;
            private String addtime;
            private String is_available;

            public String getIs_available() {
                return is_available;
            }

            public void setIs_available(String is_available) {
                this.is_available = is_available;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getPid() {
                return pid;
            }

            public void setPid(String pid) {
                this.pid = pid;
            }

            public String getPname() {
                return pname;
            }

            public void setPname(String pname) {
                this.pname = pname;
            }

            public String getMenu_name() {
                return menu_name;
            }

            public void setMenu_name(String menu_name) {
                this.menu_name = menu_name;
            }

            public String getMenu_logo() {
                return menu_logo;
            }

            public void setMenu_logo(String menu_logo) {
                this.menu_logo = menu_logo;
            }

            public String getUrl_type() {
                return url_type;
            }

            public void setUrl_type(String url_type) {
                this.url_type = url_type;
            }

            public String getUrl_type_cn() {
                return url_type_cn;
            }

            public void setUrl_type_cn(String url_type_cn) {
                this.url_type_cn = url_type_cn;
            }

            public String getUrl_id() {
                return url_id;
            }

            public void setUrl_id(String url_id) {
                this.url_id = url_id;
            }

            public String getOrder_num() {
                return order_num;
            }

            public void setOrder_num(String order_num) {
                this.order_num = order_num;
            }

            public String getDisplay() {
                return display;
            }

            public void setDisplay(String display) {
                this.display = display;
            }

            public String getAddtime() {
                return addtime;
            }

            public void setAddtime(String addtime) {
                this.addtime = addtime;
            }
        }
    }

}
