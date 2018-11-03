package com.huacheng.huiservers.geren.bean;

public class ActivityDetailsModel {
	
    /**
     * status : 1
     * msg : 成功获取活动详情！
     * data : {"id":"5","display":"1","index_hot":"1","title":"钢琴开课啦","menu_pid":"1","menu_id":"4","menu_name":"家政服务","com_id_arr":"128,127","picture":"data/upload/activity/17/05/08/590fd397edcde.jpg","personal_num":"100","personal_surplus":"100","cost":"100","sponsor":"榆次天河娱乐城","address":"锦纶路23号3","phone":"1351354120","introduction":"培训钢琴课程","attention":"自带钢琴","enroll_start":"1495555200","enroll_end":"1495728000","start_time":"1494432000","end_time":"1495036800","order_num":"0","addtime":"1494209431","uptime":"1494300678"}
     * dialog :
     */

    private int status;
    private String msg;
    private DataBean data;
    private String dialog;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getDialog() {
        return dialog;
    }

    public void setDialog(String dialog) {
        this.dialog = dialog;
    }

    public static class DataBean {
        /**
         * id : 5
         * display : 1
         * index_hot : 1
         * title : 钢琴开课啦
         * menu_pid : 1
         * menu_id : 4
         * menu_name : 家政服务
         * com_id_arr : 128,127
         * picture : data/upload/activity/17/05/08/590fd397edcde.jpg
         * personal_num : 100
         * personal_surplus : 100
         * cost : 100
         * sponsor : 榆次天河娱乐城
         * address : 锦纶路23号3
         * phone : 1351354120
         * introduction : 培训钢琴课程
         * attention : 自带钢琴
         * enroll_start : 1495555200
         * enroll_end : 1495728000
         * start_time : 1494432000
         * end_time : 1495036800
         * order_num : 0
         * addtime : 1494209431
         * uptime : 1494300678
         */

        private String id;
        private String display;
        private String index_hot;
        private String title;
        private String menu_pid;
        private String menu_id;
        private String menu_name;
        private String com_id_arr;
        private String picture;
        private String personal_num;
        private String personal_surplus;
        private String cost;
        private String sponsor;
        private String address;
        private String phone;
        private String introduction;
        private String attention;
        private String enroll_start;
        private String enroll_end;
        private String start_time;
        private String end_time;
        private String order_num;
        private String addtime;
        private String uptime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDisplay() {
            return display;
        }

        public void setDisplay(String display) {
            this.display = display;
        }

        public String getIndex_hot() {
            return index_hot;
        }

        public void setIndex_hot(String index_hot) {
            this.index_hot = index_hot;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMenu_pid() {
            return menu_pid;
        }

        public void setMenu_pid(String menu_pid) {
            this.menu_pid = menu_pid;
        }

        public String getMenu_id() {
            return menu_id;
        }

        public void setMenu_id(String menu_id) {
            this.menu_id = menu_id;
        }

        public String getMenu_name() {
            return menu_name;
        }

        public void setMenu_name(String menu_name) {
            this.menu_name = menu_name;
        }

        public String getCom_id_arr() {
            return com_id_arr;
        }

        public void setCom_id_arr(String com_id_arr) {
            this.com_id_arr = com_id_arr;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getPersonal_num() {
            return personal_num;
        }

        public void setPersonal_num(String personal_num) {
            this.personal_num = personal_num;
        }

        public String getPersonal_surplus() {
            return personal_surplus;
        }

        public void setPersonal_surplus(String personal_surplus) {
            this.personal_surplus = personal_surplus;
        }

        public String getCost() {
            return cost;
        }

        public void setCost(String cost) {
            this.cost = cost;
        }

        public String getSponsor() {
            return sponsor;
        }

        public void setSponsor(String sponsor) {
            this.sponsor = sponsor;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public String getAttention() {
            return attention;
        }

        public void setAttention(String attention) {
            this.attention = attention;
        }

        public String getEnroll_start() {
            return enroll_start;
        }

        public void setEnroll_start(String enroll_start) {
            this.enroll_start = enroll_start;
        }

        public String getEnroll_end() {
            return enroll_end;
        }

        public void setEnroll_end(String enroll_end) {
            this.enroll_end = enroll_end;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
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

        public String getUptime() {
            return uptime;
        }

        public void setUptime(String uptime) {
            this.uptime = uptime;
        }
}

}
