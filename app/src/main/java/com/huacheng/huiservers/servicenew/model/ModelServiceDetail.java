package com.huacheng.huiservers.servicenew.model;

import java.util.List;

/**
 * 类描述：
 * 时间：2018/9/6 10:44
 * created by DFF
 */
public class ModelServiceDetail {

    /**
     * id : 30
     * title : 太极擦玻璃
     * i_id : 115
     * title_img : public/upload/service/title_img/18/08/09/5b6b9ae8d7df8.png
     * title_thumb_img : public/upload/service/title_img/18/08/09/5b6b9ae8d7df8_thumb.png
     * tag_list : [{"id":"82","s_id":"30","tagname":"快速","original":"12.00","price":"12.00"},{"id":"83","s_id":"30","tagname":"慢速","original":"14.00","price":"14.00"}]
     * score_info : {"id":"42","s_id":"30","uid":"105","score":"2","evaluate":"阿达嘎嘎嘎灌灌灌灌灌嘎嘎嘎嘎嘎嘎嘎嘎嘎灌灌灌灌灌个","anonymous":"2","evaluatime":"1536131452","avatars":"data/upload/center_avatars/18/01/10/5a55e1075dfca.jpg","nickname":"天涯倦客","score_num":"3"}
     * img_list : [{"id":"245","img":"public/upload/service/20180905/5b8fb56121fd9.png"},{"id":"246","img":"public/upload/service/20180905/5b8fb561511cc.jpg"},{"id":"247","img":"public/upload/service/20180905/5b8fb56180b8f.png"}]
     * ins_info : {"id":"115","name":"武当派","logo":"public/upload/ins/20180905/5b8fdee71851f.jpg","telphone":"13513541301","coupon_num":0,"service_num":"11","cate_list":[{"category_cn":"清洗玻璃"},{"category_cn":"保养车"},{"category_cn":"吃药"},{"category_cn":"包养机械"},{"category_cn":"输液"},{"category_cn":"包养车"}],"service_list":[{"id":"31","title":"太极保养车","title_img":"public/upload/service/title_img/18/08/09/5b6b9ae8d7df8.png","title_thumb_img":"public/upload/service/title_img/18/08/09/5b6b9ae8d7df8_thumb.png","price":"23.00"},{"id":"32","title":"太极保养车11","title_img":"public/upload/service/title_img/18/08/09/5b6b9ae8d7df8.png","title_thumb_img":"public/upload/service/title_img/18/08/09/5b6b9ae8d7df8_thumb.png","price":"0.00"},{"id":"34","title":"测试","title_img":"","title_thumb_img":"","price":"0.00"},{"id":"36","title":"测试5","title_img":"public/upload/service/20180904/5b8e56d4330e1.png","title_thumb_img":"","price":"0.00"},{"id":"37","title":"测试7","title_img":"public/upload/service/20180904/5b8e5cb400085.png","title_thumb_img":"public/upload/service/20180904/thumb_5b8e5cb400085.png","price":"0.00"},{"id":"38","title":"测试8","title_img":"public/upload/service/20180904/5b8e5dcf204e7.png","title_thumb_img":"public/upload/service/20180904/thumb_5b8e5dcf204e7.png","price":"0.00"},{"id":"39","title":"测试8","title_img":"public/upload/service/20180904/5b8e5e030c479.png","title_thumb_img":"public/upload/service/20180904/thumb_5b8e5e030c479.png","price":"0.00"},{"id":"40","title":"测试8","title_img":"","title_thumb_img":"","price":"0.00"},{"id":"41","title":"测试8","title_img":"","title_thumb_img":"","price":"0.00"},{"id":"43","title":"测试888","title_img":"public/upload/service/20180904/5b8e62419c25f.png","title_thumb_img":"public/upload/service/20180904/thumb_5b8e62419c25f.png","price":"12.33"},{"id":"44","title":"测试9","title_img":"public/upload/service/20180904/5b8e7d9cc28c9.png","title_thumb_img":"public/upload/service/20180904/thumb_5b8e7d9cc28c9.png","price":"23.00"},{"id":"45","title":"测试1111","title_img":"public/upload/service/20180905/5b8f233a6327b.jpg","title_thumb_img":"public/upload/service/20180905/thumb_5b8f233a6327b.jpg","price":"0.00"}]}
     */

    private String id;
    private String title;
    private String i_id;
    private String title_img;
    private String title_thumb_img,title_img_size;

    private ScoreInfoBean score_info;
    private InsInfoBean ins_info;
    private List<TagListBean> tag_list;
    private List<ImgListBean> img_list;

    public String getTitle_img_size() {
        return title_img_size;
    }

    public void setTitle_img_size(String title_img_size) {
        this.title_img_size = title_img_size;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getI_id() {
        return i_id;
    }

    public void setI_id(String i_id) {
        this.i_id = i_id;
    }

    public String getTitle_img() {
        return title_img;
    }

    public void setTitle_img(String title_img) {
        this.title_img = title_img;
    }

    public String getTitle_thumb_img() {
        return title_thumb_img;
    }

    public void setTitle_thumb_img(String title_thumb_img) {
        this.title_thumb_img = title_thumb_img;
    }

    public ScoreInfoBean getScore_info() {
        return score_info;
    }

    public void setScore_info(ScoreInfoBean score_info) {
        this.score_info = score_info;
    }

    public InsInfoBean getIns_info() {
        return ins_info;
    }

    public void setIns_info(InsInfoBean ins_info) {
        this.ins_info = ins_info;
    }

    public List<TagListBean> getTag_list() {
        return tag_list;
    }

    public void setTag_list(List<TagListBean> tag_list) {
        this.tag_list = tag_list;
    }

    public List<ImgListBean> getImg_list() {
        return img_list;
    }

    public void setImg_list(List<ImgListBean> img_list) {
        this.img_list = img_list;
    }

    public static class ScoreInfoBean {
        /**
         * id : 42
         * s_id : 30
         * uid : 105
         * score : 2
         * evaluate : 阿达嘎嘎嘎灌灌灌灌灌嘎嘎嘎嘎嘎嘎嘎嘎嘎灌灌灌灌灌个
         * anonymous : 2
         * evaluatime : 1536131452
         * avatars : data/upload/center_avatars/18/01/10/5a55e1075dfca.jpg
         * nickname : 天涯倦客
         * score_num : 3
         */

        private String id;
        private String s_id;
        private String uid;
        private String score;
        private String evaluate;
        private String anonymous;
        private String evaluatime;
        private String avatars;
        private String nickname;
        private String score_num;
        private int total_Pages;

        public int getTotal_Pages() {
            return total_Pages;
        }

        public void setTotal_Pages(int total_Pages) {
            this.total_Pages = total_Pages;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getS_id() {
            return s_id;
        }

        public void setS_id(String s_id) {
            this.s_id = s_id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getEvaluate() {
            return evaluate;
        }

        public void setEvaluate(String evaluate) {
            this.evaluate = evaluate;
        }

        public String getAnonymous() {
            return anonymous;
        }

        public void setAnonymous(String anonymous) {
            this.anonymous = anonymous;
        }

        public String getEvaluatime() {
            return evaluatime;
        }

        public void setEvaluatime(String evaluatime) {
            this.evaluatime = evaluatime;
        }

        public String getAvatars() {
            return avatars;
        }

        public void setAvatars(String avatars) {
            this.avatars = avatars;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getScore_num() {
            return score_num;
        }

        public void setScore_num(String score_num) {
            this.score_num = score_num;
        }
    }

    public static class InsInfoBean {
        /**
         * id : 115
         * name : 武当派
         * logo : public/upload/ins/20180905/5b8fdee71851f.jpg
         * telphone : 13513541301
         * coupon_num : 0
         * service_num : 11
         * cate_list : [{"category_cn":"清洗玻璃"},{"category_cn":"保养车"},{"category_cn":"吃药"},{"category_cn":"包养机械"},{"category_cn":"输液"},{"category_cn":"包养车"}]
         * service_list : [{"id":"31","title":"太极保养车","title_img":"public/upload/service/title_img/18/08/09/5b6b9ae8d7df8.png","title_thumb_img":"public/upload/service/title_img/18/08/09/5b6b9ae8d7df8_thumb.png","price":"23.00"},{"id":"32","title":"太极保养车11","title_img":"public/upload/service/title_img/18/08/09/5b6b9ae8d7df8.png","title_thumb_img":"public/upload/service/title_img/18/08/09/5b6b9ae8d7df8_thumb.png","price":"0.00"},{"id":"34","title":"测试","title_img":"","title_thumb_img":"","price":"0.00"},{"id":"36","title":"测试5","title_img":"public/upload/service/20180904/5b8e56d4330e1.png","title_thumb_img":"","price":"0.00"},{"id":"37","title":"测试7","title_img":"public/upload/service/20180904/5b8e5cb400085.png","title_thumb_img":"public/upload/service/20180904/thumb_5b8e5cb400085.png","price":"0.00"},{"id":"38","title":"测试8","title_img":"public/upload/service/20180904/5b8e5dcf204e7.png","title_thumb_img":"public/upload/service/20180904/thumb_5b8e5dcf204e7.png","price":"0.00"},{"id":"39","title":"测试8","title_img":"public/upload/service/20180904/5b8e5e030c479.png","title_thumb_img":"public/upload/service/20180904/thumb_5b8e5e030c479.png","price":"0.00"},{"id":"40","title":"测试8","title_img":"","title_thumb_img":"","price":"0.00"},{"id":"41","title":"测试8","title_img":"","title_thumb_img":"","price":"0.00"},{"id":"43","title":"测试888","title_img":"public/upload/service/20180904/5b8e62419c25f.png","title_thumb_img":"public/upload/service/20180904/thumb_5b8e62419c25f.png","price":"12.33"},{"id":"44","title":"测试9","title_img":"public/upload/service/20180904/5b8e7d9cc28c9.png","title_thumb_img":"public/upload/service/20180904/thumb_5b8e7d9cc28c9.png","price":"23.00"},{"id":"45","title":"测试1111","title_img":"public/upload/service/20180905/5b8f233a6327b.jpg","title_thumb_img":"public/upload/service/20180905/thumb_5b8f233a6327b.jpg","price":"0.00"}]
         */

        private String id;
        private String name;
        private String logo;
        private String telphone;
        private int coupon_num;
        private String service_num;
        private List<CateListBean> cate_list;
        private List<ServiceListBean> service_list;

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

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getTelphone() {
            return telphone;
        }

        public void setTelphone(String telphone) {
            this.telphone = telphone;
        }

        public int getCoupon_num() {
            return coupon_num;
        }

        public void setCoupon_num(int coupon_num) {
            this.coupon_num = coupon_num;
        }

        public String getService_num() {
            return service_num;
        }

        public void setService_num(String service_num) {
            this.service_num = service_num;
        }

        public List<CateListBean> getCate_list() {
            return cate_list;
        }

        public void setCate_list(List<CateListBean> cate_list) {
            this.cate_list = cate_list;
        }

        public List<ServiceListBean> getService_list() {
            return service_list;
        }

        public void setService_list(List<ServiceListBean> service_list) {
            this.service_list = service_list;
        }

        public static class CateListBean {
            /**
             * category_cn : 清洗玻璃
             */

            private String category_cn;

            public String getCategory_cn() {
                return category_cn;
            }

            public void setCategory_cn(String category_cn) {
                this.category_cn = category_cn;
            }
        }

        public static class ServiceListBean {
            /**
             * id : 31
             * title : 太极保养车
             * title_img : public/upload/service/title_img/18/08/09/5b6b9ae8d7df8.png
             * title_thumb_img : public/upload/service/title_img/18/08/09/5b6b9ae8d7df8_thumb.png
             * price : 23.00
             */

            private String id;
            private String title;
            private String title_img;
            private String title_thumb_img;
            private String price;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getTitle_img() {
                return title_img;
            }

            public void setTitle_img(String title_img) {
                this.title_img = title_img;
            }

            public String getTitle_thumb_img() {
                return title_thumb_img;
            }

            public void setTitle_thumb_img(String title_thumb_img) {
                this.title_thumb_img = title_thumb_img;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }
        }
    }

    public static class TagListBean {
        /**
         * id : 82
         * s_id : 30
         * tagname : 快速
         * original : 12.00
         * price : 12.00
         */

        private String id;
        private String s_id;
        private String tagname;
        private String original;
        private String price;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getS_id() {
            return s_id;
        }

        public void setS_id(String s_id) {
            this.s_id = s_id;
        }

        public String getTagname() {
            return tagname;
        }

        public void setTagname(String tagname) {
            this.tagname = tagname;
        }

        public String getOriginal() {
            return original;
        }

        public void setOriginal(String original) {
            this.original = original;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }

    public static class ImgListBean {
        /**
         * id : 245
         * img : public/upload/service/20180905/5b8fb56121fd9.png
         */

        private String id;
        private String img;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }
    }
}