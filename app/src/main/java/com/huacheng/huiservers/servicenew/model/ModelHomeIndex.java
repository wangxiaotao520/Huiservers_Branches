package com.huacheng.huiservers.servicenew.model;

import java.util.List;

/**
 * Description:
 * created by wangxiaotao
 * 2018/7/27 0027 下午 5:31
 */
public class ModelHomeIndex {

    private String bg_img;
    private String article_list;
    private List<MenuListBean> menu_list;
    private List<SocialListBean> social_list;
    private List<ProListBean> pro_list;

    public String getBg_img() {
        return bg_img;
    }

    public void setBg_img(String bg_img) {
        this.bg_img = bg_img;
    }

    public String getArticle_list() {
        return article_list;
    }

    public void setArticle_list(String article_list) {
        this.article_list = article_list;
    }

    public List<MenuListBean> getMenu_list() {
        return menu_list;
    }

    public void setMenu_list(List<MenuListBean> menu_list) {
        this.menu_list = menu_list;
    }

    public List<SocialListBean> getSocial_list() {
        return social_list;
    }

    public void setSocial_list(List<SocialListBean> social_list) {
        this.social_list = social_list;
    }

    public List<ProListBean> getPro_list() {
        return pro_list;
    }

    public void setPro_list(List<ProListBean> pro_list) {
        this.pro_list = pro_list;
    }

    public static class MenuListBean {
        /**
         * id : 22
         * menu_name : 当面付
         * menu_logo : data/upload/menu_logo/5b138a496246d.png
         * url_type : 18
         * url_type_cn : 当面付
         * url_id :
         * addtime : 1527775571
         */

        private String id;
        private String menu_name;
        private String menu_logo;
        private String url_type;
        private String url_type_cn;
        private String url_id;
        private String addtime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }
    }

    public static class SocialListBean {
        /**
         * id : 289
         * uid : 0
         * admin_id : 19
         * community_id : -1
         * c_id : 82
         * c_name : 本地头条
         * title : 54Ot54OI5bqG56Wd5Y2O5pmfwrfmgqblupxFMeWMuuiNo+iAgOWwgemhtg==
         * content : MjAxN+W5tDfmnIgxMuaXpeWNjuaZn8K35oKm5bqcRTHljLrllpzov47lsIHpobbph5HnjKrotLrllpwyOC0tNTblubPnsbPlsI/miLflnovmlrDlk4HliqDmjqjpppbku5g25LiH6LW35ZKo6K+i54Ot57q/MDM1NC0tMjQxOTg4
         * click : 13
         * reply_num : 0
         * addtime : 07-13
         * community_name : 全城
         * avatars : data/upload/admin_avatar/8.png
         * nickname : 小慧
         * img_list : [{"id":"369","img":"data/upload/social/18/07/13/5b4814f32497e.jpg","img_size":"1.5"}]
         * total_Pages : 1
         */

        private String id;
        private String uid;
        private String admin_id;
        private String community_id;
        private String c_id;
        private String c_name;
        private String title;
        private String content;
        private String click;
        private String reply_num;
        private String addtime;
        private String community_name;
        private String avatars;
        private String nickname;
        private int total_Pages;
        private List<ImgListBean> img_list;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getAdmin_id() {
            return admin_id;
        }

        public void setAdmin_id(String admin_id) {
            this.admin_id = admin_id;
        }

        public String getCommunity_id() {
            return community_id;
        }

        public void setCommunity_id(String community_id) {
            this.community_id = community_id;
        }

        public String getC_id() {
            return c_id;
        }

        public void setC_id(String c_id) {
            this.c_id = c_id;
        }

        public String getC_name() {
            return c_name;
        }

        public void setC_name(String c_name) {
            this.c_name = c_name;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getClick() {
            return click;
        }

        public void setClick(String click) {
            this.click = click;
        }

        public String getReply_num() {
            return reply_num;
        }

        public void setReply_num(String reply_num) {
            this.reply_num = reply_num;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getCommunity_name() {
            return community_name;
        }

        public void setCommunity_name(String community_name) {
            this.community_name = community_name;
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

        public int getTotal_Pages() {
            return total_Pages;
        }

        public void setTotal_Pages(int total_Pages) {
            this.total_Pages = total_Pages;
        }

        public List<ImgListBean> getImg_list() {
            return img_list;
        }

        public void setImg_list(List<ImgListBean> img_list) {
            this.img_list = img_list;
        }

        public static class ImgListBean {
            /**
             * id : 369
             * img : data/upload/social/18/07/13/5b4814f32497e.jpg
             * img_size : 1.5
             */

            private String id;
            private String img;
            private String img_size;

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

            public String getImg_size() {
                return img_size;
            }

            public void setImg_size(String img_size) {
                this.img_size = img_size;
            }
        }
    }

    public static class ProListBean {
        /**
         * id : 2796
         * topclass : 446
         * title : 羊奶精华龙脑皂
         * title_img : data/upload/product/18/07/07/5b408dc673a09_thumb.jpg
         * title_thumb_img : data/upload/product/18/07/07/5b408dc673a09_thumb.jpg
         * cate_tag_id : 52,66,149
         * send_shop_id : 75
         * shop_cate_stime : 1530979200
         * shop_cate_etime : 1531584000
         * addtime : 1530957254
         * goods_tag : [{"c_name":"货真价实","c_img":"data/upload/category_icon/c_img/17/12/27/5a436640b5255.png"},{"c_name":"免费配送","c_img":"data/upload/category_icon/c_img/17/12/27/5a43693c107d6.png"},{"c_name":"特惠产品","c_img":"data/upload/category_icon/c_img/18/07/07/5b4074b30ea75.png"}]
         * tagid : 3886
         * tagname : 95g/盒
         * price : 6
         * original : 28
         * inventory : 50
         * unit : 盒
         * exist_hours : 1
         * is_hot : 0
         * is_new : 0
         * is_time : 1
         * order_num : 55
         * total_Pages : 2
         */

        private String id;
        private String topclass;
        private String title;
        private String title_img;
        private String title_thumb_img;
        private String cate_tag_id;
        private String send_shop_id;
        private String shop_cate_stime;
        private String shop_cate_etime;
        private String addtime;
        private String tagid;
        private String tagname;
        private double price;
        private int original;
        private int inventory;
        private String unit;
        private int exist_hours;
        private int is_hot;
        private int is_new;
        private int order_num;
        private int total_Pages;
        private List<GoodsTagBean> goods_tag;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTopclass() {
            return topclass;
        }

        public void setTopclass(String topclass) {
            this.topclass = topclass;
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

        public String getCate_tag_id() {
            return cate_tag_id;
        }

        public void setCate_tag_id(String cate_tag_id) {
            this.cate_tag_id = cate_tag_id;
        }

        public String getSend_shop_id() {
            return send_shop_id;
        }

        public void setSend_shop_id(String send_shop_id) {
            this.send_shop_id = send_shop_id;
        }

        public String getShop_cate_stime() {
            return shop_cate_stime;
        }

        public void setShop_cate_stime(String shop_cate_stime) {
            this.shop_cate_stime = shop_cate_stime;
        }

        public String getShop_cate_etime() {
            return shop_cate_etime;
        }

        public void setShop_cate_etime(String shop_cate_etime) {
            this.shop_cate_etime = shop_cate_etime;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getTagid() {
            return tagid;
        }

        public void setTagid(String tagid) {
            this.tagid = tagid;
        }

        public String getTagname() {
            return tagname;
        }

        public void setTagname(String tagname) {
            this.tagname = tagname;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getOriginal() {
            return original;
        }

        public void setOriginal(int original) {
            this.original = original;
        }

        public int getInventory() {
            return inventory;
        }

        public void setInventory(int inventory) {
            this.inventory = inventory;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public int getExist_hours() {
            return exist_hours;
        }

        public void setExist_hours(int exist_hours) {
            this.exist_hours = exist_hours;
        }

        public int getIs_hot() {
            return is_hot;
        }

        public void setIs_hot(int is_hot) {
            this.is_hot = is_hot;
        }

        public int getIs_new() {
            return is_new;
        }

        public void setIs_new(int is_new) {
            this.is_new = is_new;
        }

        public int getOrder_num() {
            return order_num;
        }

        public void setOrder_num(int order_num) {
            this.order_num = order_num;
        }

        public int getTotal_Pages() {
            return total_Pages;
        }

        public void setTotal_Pages(int total_Pages) {
            this.total_Pages = total_Pages;
        }

        public List<GoodsTagBean> getGoods_tag() {
            return goods_tag;
        }

        public void setGoods_tag(List<GoodsTagBean> goods_tag) {
            this.goods_tag = goods_tag;
        }

        public static class GoodsTagBean {
            /**
             * c_name : 货真价实
             * c_img : data/upload/category_icon/c_img/17/12/27/5a436640b5255.png
             */

            private String c_name;
            private String c_img;

            public String getC_name() {
                return c_name;
            }

            public void setC_name(String c_name) {
                this.c_name = c_name;
            }

            public String getC_img() {
                return c_img;
            }

            public void setC_img(String c_img) {
                this.c_img = c_img;
            }
        }
    }
}
