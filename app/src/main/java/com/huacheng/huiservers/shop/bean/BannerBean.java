package com.huacheng.huiservers.shop.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class BannerBean implements Serializable {
    public List<BannerBean> getImg_list() {
        return img_list;
    }

    public BannerBean reply;
    /**
     * 购物车商品数量
     */
    public static final String KEY_NUM = "num";

    /**
     * 购物车规格ID
     */
    public static final String KEY_PRODUCT_ID = "product_id";
    private String id;
    private String uptime;
    private String a_id;
    private String c_id;//广告位分类id
    private String c_name;//广告位标识
    private String c_img;//广告位标识
    private String ajbPass;
    private String ajbisQRorTP;
    private String img;//广告位图片路径
    private String url;//广告位图片连接
    private String show_id;
    private String tagid;
    private String picture_size;
    private String one_img;
    private String url_type_cn;
    private String img_size;
    private String show_id_name;
    private String type_name;
    private String state;
    private String title_img;//首页楼层商品图片路径
    private String text;//
    private String type_sign;//
    private String title;//
    private String area_id;//
    private String min_price;//价格
    private String original;//原价
    private String p_m_name;//发货商
    private String uid;
    private String type;
    private String p_id;
    private String name;
    private String start_time;
    private String end_time;
    private String p_title_img;
    private String p_title;
    private String price;
    private String number;
    private String addtime;
    private String oid;
    private String tagname;
    private String p_m_id;
    private String p_m_uid;
    private String top_img;
    private int total_Pages;
    private boolean isSelect;
    private String personal_num;
    private String address;
    private String phone;
    private String attention;
    private String enroll_start;
    private String enroll_end;
    private String send_shop_id;
    private String unit;
    private String avatars;
    private String count_reply;
    private String count_thumbs_up;
    private String count_thumbs_down;
    private int is_report;
    private int is_thumbs;
    private String tag_id;
    private String click;
    private String adv_url;
    private String adv_inside_url;
    private String category_title;
    private String admin_id, reply_num;

    private String community_name;
    private String community_id;
    private String new_reply_num;
    private String tempPass;
    private int is_pro;//是否是物业公告字段

    public int getIs_pro() {
        return is_pro;
    }

    public void setIs_pro(int is_pro) {
        this.is_pro = is_pro;
    }

    public String getUrl_type_cn() {
        return url_type_cn;
    }

    public void setUrl_type_cn(String url_type_cn) {
        this.url_type_cn = url_type_cn;
    }

    public String getCommunity_id() {
        return community_id;
    }

    public void setCommunity_id(String community_id) {
        this.community_id = community_id;
    }

    public String getTempPass() {
        return tempPass;
    }

    public String getC_img() {
        return c_img;
    }

    public void setC_img(String c_img) {
        this.c_img = c_img;
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public void setTempPass(String tempPass) {
        this.tempPass = tempPass;
    }

    public String getAjbisQRorTP() {
        return ajbisQRorTP;
    }

    public void setAjbisQRorTP(String ajbisQRorTP) {
        this.ajbisQRorTP = ajbisQRorTP;
    }

    public String getAjbPass() {
        return ajbPass;
    }

    public void setAjbPass(String ajbPass) {
        this.ajbPass = ajbPass;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCommunity_name() {
        return community_name;
    }

    public void setCommunity_name(String community_name) {
        this.community_name = community_name;
    }

    public BannerBean getReply() {
        return reply;
    }

    public void setReply(BannerBean reply) {
        this.reply = reply;
    }

    public String getNew_reply_num() {
        return new_reply_num;
    }

    public void setNew_reply_num(String new_reply_num) {
        this.new_reply_num = new_reply_num;
    }

    public String getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(String admin_id) {
        this.admin_id = admin_id;
    }

    public String getReply_num() {
        return reply_num;
    }

    public void setReply_num(String reply_num) {
        this.reply_num = reply_num;
    }

    public String getA_id() {
        return a_id;
    }

    public void setA_id(String a_id) {
        this.a_id = a_id;
    }

    public String getPicture_size() {
        return picture_size;
    }

    public void setPicture_size(String picture_size) {
        this.picture_size = picture_size;
    }

    public String getImg_size() {
        return img_size;
    }

    public void setImg_size(String img_size) {
        this.img_size = img_size;
    }

    public String getPersonal_score() {
        return personal_score;
    }

    public void setPersonal_score(String personal_score) {
        this.personal_score = personal_score;
    }

    private String personal_score;

    public String getCategory_title() {
        return category_title;
    }

    public void setCategory_title(String category_title) {
        this.category_title = category_title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public BannerBean getPop_up() {
        return pop_up;
    }

    public void setPop_up(BannerBean pop_up) {
        this.pop_up = pop_up;
    }

    private String link;

    public String getAdv_url() {
        return adv_url;
    }

    public void setAdv_url(String adv_url) {
        this.adv_url = adv_url;
    }

    public String getAdv_inside_url() {
        return adv_inside_url;
    }

    public void setAdv_inside_url(String adv_inside_url) {
        this.adv_inside_url = adv_inside_url;
    }

    public String getT_num() {
        return t_num;
    }

    public void setT_num(String t_num) {
        this.t_num = t_num;
    }

    private String content;
    private String t_num;

    public String getShop_more_s_c_id() {
        return shop_more_s_c_id;
    }

    public void setShop_more_s_c_id(String shop_more_s_c_id) {
        this.shop_more_s_c_id = shop_more_s_c_id;
    }

    private String shop_more_s_c_id;

    public String getAdvertorial() {
        return advertorial;
    }

    public void setAdvertorial(String advertorial) {
        this.advertorial = advertorial;
    }

    private String social_id;
    private String outside_url;
    private String portrait;
    private String telephone;
    private String info_id;
    private String advertorial;

    public String getInfo_id() {
        return info_id;
    }

    public void setInfo_id(String info_id) {
        this.info_id = info_id;
    }

    private String score;

    public String getActivity_more() {
        return activity_more;
    }

    public void setActivity_more(String activity_more) {
        this.activity_more = activity_more;
    }

    private String work_num;
    private String com_num;
    private String activity_more;

    public String getSecurity_tel() {
        return Security_tel;
    }

    public void setSecurity_tel(String security_tel) {
        Security_tel = security_tel;
    }

    private String ins_name;
    private String sponsor;
    private String Security_tel;
    private String order_num;

    public String getOrder_num() {
        return order_num;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getR_num() {
        return r_num;
    }

    public void setR_num(String r_num) {
        this.r_num = r_num;
    }

    private String r_num;

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    private String duty;

    public String getCost_cn() {
        return cost_cn;
    }

    public void setCost_cn(String cost_cn) {
        this.cost_cn = cost_cn;
    }

    private String cost_cn;

    public String getOutside_url() {
        return outside_url;
    }

    public void setOutside_url(String outside_url) {
        this.outside_url = outside_url;
    }

    private List<BannerBean> reply_list;
    private List<BannerBean> list;
    private List<BannerBean> work;
    private List<BannerBean> activity;
    private List<BannerBean> notice;

    private BannerBean pop_up;

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getWork_num() {
        return work_num;
    }

    public void setWork_num(String work_num) {
        this.work_num = work_num;
    }

    public String getCom_num() {
        return com_num;
    }

    public void setCom_num(String com_num) {
        this.com_num = com_num;
    }

    public String getIns_name() {
        return ins_name;
    }

    public void setIns_name(String ins_name) {
        this.ins_name = ins_name;
    }

    public List<BannerBean> getWork() {
        return work;
    }

    public void setWork(List<BannerBean> work) {
        this.work = work;
    }

    public List<BannerBean> getActivity() {
        return activity;
    }

    public void setActivity(List<BannerBean> activity) {
        this.activity = activity;
    }

    public List<BannerBean> getNotice() {
        return notice;
    }

    public void setNotice(List<BannerBean> notice) {
        this.notice = notice;
    }

    public List<BannerBean> getShop() {
        return shop;
    }

    public void setShop(List<BannerBean> shop) {
        this.shop = shop;
    }

    public List<BannerBean> getTeam() {
        return team;
    }

    public void setTeam(List<BannerBean> team) {
        this.team = team;
    }

    private List<BannerBean> shop;

    public List<BannerBean> getImgs() {
        return imgs;
    }

    public void setImgs(List<BannerBean> imgs) {
        this.imgs = imgs;
    }

    private List<BannerBean> imgs;

    public List<BannerBean> getCom_list() {
        return com_list;
    }

    public void setCom_list(List<BannerBean> com_list) {
        this.com_list = com_list;
    }

    private List<BannerBean> team;
    private List<BannerBean> com_list;

    public List<BannerBean> getList() {
        return list;
    }

    public void setList(List<BannerBean> list) {
        this.list = list;
    }

    public ArrayList<String> getImageUrls() {
        return imageUrls;
    }

    public String getTag_id() {
        return tag_id;
    }

    public void setTag_id(String tag_id) {
        this.tag_id = tag_id;
    }

    public void setImageUrls(ArrayList<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    private ArrayList<String> imageUrls;

    public String getSocial_id() {
        return social_id;
    }

    public void setSocial_id(String social_id) {
        this.social_id = social_id;
    }

    public List<BannerBean> getReply_list() {
        return reply_list;
    }

    public void setReply_list(List<BannerBean> reply_list) {
        this.reply_list = reply_list;
    }

    public String getTagid() {
        return tagid;
    }

    public void setTagid(String tagid) {
        this.tagid = tagid;
    }

    public String getOne_img() {
        return one_img;
    }

    public void setOne_img(String one_img) {
        this.one_img = one_img;
    }

    private String nickname;
    private String fullname;
    private String position;

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    private List<BannerBean> img_list;

    public String getTop_img() {
        return top_img;
    }

    public void setTop_img(String top_img) {
        this.top_img = top_img;
    }

    public String getSend_shop_id() {
        return send_shop_id;
    }

    public void setSend_shop_id(String send_shop_id) {
        this.send_shop_id = send_shop_id;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImg_list(List<BannerBean> img_list) {
        this.img_list = img_list;
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

    public String getAvatars() {
        return avatars;
    }

    public void setAvatars(String avatars) {
        this.avatars = avatars;
    }

    public String getCount_reply() {
        return count_reply;
    }

    public void setCount_reply(String count_reply) {
        this.count_reply = count_reply;
    }

    public String getCount_thumbs_up() {
        return count_thumbs_up;
    }

    public void setCount_thumbs_up(String count_thumbs_up) {
        this.count_thumbs_up = count_thumbs_up;
    }

    public String getCount_thumbs_down() {
        return count_thumbs_down;
    }

    public void setCount_thumbs_down(String count_thumbs_down) {
        this.count_thumbs_down = count_thumbs_down;
    }

    public int getIs_report() {
        return is_report;
    }

    public void setIs_report(int is_report) {
        this.is_report = is_report;
    }


    public int getIs_thumbs() {
        return is_thumbs;
    }

    public void setIs_thumbs(int is_thumbs) {
        this.is_thumbs = is_thumbs;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }


    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPersonal_num() {
        return personal_num;
    }

    public void setPersonal_num(String personal_num) {
        this.personal_num = personal_num;
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
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

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    private String status;
    private String inventory;
    private String exist_hours;
    private String menu_name;
    private String picture;
    private String personal_surplus;
    private String cost;
    private String p_addtime;

    public String getP_addtime() {
        return p_addtime;
    }

    public void setP_addtime(String p_addtime) {
        this.p_addtime = p_addtime;
    }

    public String getTitle_thumb_img() {
        return title_thumb_img;
    }

    public void setTitle_thumb_img(String title_thumb_img) {
        this.title_thumb_img = title_thumb_img;
    }

    private String introduction;
    private String title_thumb_img;

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

    public String getUrl_id() {
        return url_id;
    }

    public void setUrl_id(String url_id) {
        this.url_id = url_id;
    }

    private String menu_logo;
    private String url_type;
    private String url_id;
    private String pro_cn;
    private String utype;

    public String getPro_work_id() {
        return pro_work_id;
    }

    public void setPro_work_id(String pro_work_id) {
        this.pro_work_id = pro_work_id;
    }

    private String pro_work_id;

    public String getPro_cn() {
        return pro_cn;
    }

    public void setPro_cn(String pro_cn) {
        this.pro_cn = pro_cn;
    }

    public String getWork_com_num() {
        return work_com_num;
    }

    public void setWork_com_num(String work_com_num) {
        this.work_com_num = work_com_num;
    }

    private String work_com_num;


    public String getShow_id_name() {
        return show_id_name;
    }

    public void setShow_id_name(String show_id_name) {
        this.show_id_name = show_id_name;
    }

    public String getExist_hours() {
        return exist_hours;
    }

    public void setExist_hours(String exist_hours) {
        this.exist_hours = exist_hours;
    }

    public String getInventory() {
        return inventory;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getP_m_id() {
        return p_m_id;
    }

    public void setP_m_id(String p_m_id) {
        this.p_m_id = p_m_id;
    }

    public String getP_m_uid() {
        return p_m_uid;
    }

    public void setP_m_uid(String p_m_uid) {
        this.p_m_uid = p_m_uid;
    }

    public String getP_m_name() {
        return p_m_name;
    }

    public void setP_m_name(String p_m_name) {
        this.p_m_name = p_m_name;
    }

    public int getTotal_Pages() {
        return total_Pages;
    }

    public void setTotal_Pages(int total_Pages) {
        this.total_Pages = total_Pages;
    }

    public String getTitle() {
        return title;
    }

    public String getUtype() {
        return utype;
    }

    public void setUtype(String utype) {
        this.utype = utype;
    }

    public void setTitle(String title) {
        this.title = title;

    }

    public String getMin_price() {
        return min_price;
    }

    public void setMin_price(String min_price) {
        this.min_price = min_price;
    }

    /**
     * 是否被选中
     */
    private boolean isChildSelected;


    public String getTagname() {
        return tagname;
    }

    public void setTagname(String tagname) {
        this.tagname = tagname;
    }

    public static String getKeyNum() {
        return KEY_NUM;
    }

    public static String getKeyProductId() {
        return KEY_PRODUCT_ID;
    }

    public void setChildSelected(boolean isChildSelected) {
        this.isChildSelected = isChildSelected;
    }

    public String getType_sign() {
        return type_sign;
    }

    public void setType_sign(String type_sign) {
        this.type_sign = type_sign;
    }

    public String getShow_id() {
        return show_id;
    }

    public void setShow_id(String show_id) {
        this.show_id = show_id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public boolean isChildSelected() {
        return isChildSelected;
    }

    public void setIsChildSelected(boolean isChildSelected) {
        this.isChildSelected = isChildSelected;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public String getP_title_img() {
        return p_title_img;
    }

    public void setP_title_img(String p_title_img) {
        this.p_title_img = p_title_img;
    }

    public String getP_title() {
        return p_title;
    }

    public void setP_title(String p_title) {
        this.p_title = p_title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getTitle_img() {
        return title_img;
    }

    public void setTitle_img(String title_img) {
        this.title_img = title_img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
