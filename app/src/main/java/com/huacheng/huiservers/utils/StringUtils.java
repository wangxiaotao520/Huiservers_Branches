package com.huacheng.huiservers.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringUtils {
    /**
     * 判断字符串是否有值，如果为null或者是空字符串或者只有空格或者为"null"字符串，则返回true，否则则返回false
     */
    public static boolean isEmpty(String value) {
        if (value != null && !"".equalsIgnoreCase(value.trim())
                && !"null".equalsIgnoreCase(value.trim())) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * dip转换px
     */
    public static int dip2px(int dip) {
        final float scale = UIUtils.getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    //将时间转成时分秒
    public static String changeTime(int second) {
        int h = 0;
        int d = 0;
        int s = 0;
        int temp = second % 3600;
        if (second > 3600) {
            h = second / 3600;
            if (temp != 0) {
                if (temp > 60) {
                    d = temp / 60;
                    if (temp % 60 != 0) {
                        s = temp % 60;
                    }
                } else {
                    s = temp;
                }
            }
        } else {
            d = second / 60;
            if (second % 60 != 0) {
                s = second % 60;
            }
        }
        if (h > 0) {
            return h + "时" + d + "分" + s + "秒";
        } else if (d > 0) {
            return d + "分" + s + "秒";
        } else {
            return s + "秒";
        }
    }

    /**
     * px转换dip
     */
    public static int px2dip(Context context, int px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * 根据手机分辨率从DP转成PX
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 根据手机的分辨率PX(像素)转成DP
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @return
     */
    // sp
    public static int px2sp(Context context, float pxValue) {
        // 获取
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    //判读结束时间是否小于开始时间
    public static int isDateBefore(String date1, String date2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        int flg = 0;
        try {
            Date dt1 = df.parse(date1);
            Date dt2 = df.parse(date2);
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1 在dt2前");
                flg = 1;
            } else if (dt1.getTime() < dt2.getTime() && ((dt1.getTime() - dt2.getTime()) / (1000 * 60 * 60) <= -2)) {
                System.out.println("dt1在dt2后");
                flg = 2;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return flg;
    }

    /**
     * 判断当前日期是星期几
     *
     * @param pTime 设置的需要判断的时间  //格式如2012-09-08
     * @return dayForWeek 判断结果
     * @Exception 发生异常
     */

    //  String pTime = "2012-03-12";
    public static String getWeek(String pTime) {
        String Week = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            Week += "日";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 2) {
            Week += "一";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 3) {
            Week += "二";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 4) {
            Week += "三";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 5) {
            Week += "四";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 6) {
            Week += "五";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 7) {
            Week += "六";
        }
        return Week;
    }

    private static SimpleDateFormat sdf = null;

    /*时间戳转换成字符窜*/
    public static String getDateToString(String time, String type) {
        long mTime;
        if (time == null) {
            return "无";
        } else {
            Long lon = Long.parseLong(time);
            mTime = lon * 1000;
            if (time.equals("0000") || time == "0000") {
                return "无数据";
            } else if (type.equals("1")) {
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            } else if (type.equals("2")) {
                sdf = new SimpleDateFormat("yyyy-MM-dd");
            } else if (type.equals("3")) {
                sdf = new SimpleDateFormat("MM-dd HH:mm");
            } else if (type.equals("4")) {
                sdf = new SimpleDateFormat("HH:mm");
            } else if (type.equals("5")) {
                sdf = new SimpleDateFormat("HH:mm ss''");
            } else if (type.equals("6")) {
                sdf = new SimpleDateFormat("MM-dd HH:mm");
            } else if (type.equals("7")) {
                sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm");
            } else if (type.equals("8")) {
                sdf = new SimpleDateFormat("yyyy.MM.dd");
            }
            String date = sdf.format(mTime);
            return date;
        }


    }

    /*截取字符串,只要时间 例将2015-09-28截取为28(从最后一个"-"处截取)*/
    public static String getSubLastString(String time, String tag) {
        String timt = time.substring(time.lastIndexOf(tag) + 1, time.length());
        return timt;
    }

    /*截取字符串,从开始截取到某个字符*/
    public static String getSubLastStrings(String time, String tag) {
        String timt = time.substring(0, time.lastIndexOf(tag));
        return timt;
    }

    /**
     * 判断多个字符串是否相等，如果其中有一个为空字符串或者null，则返回false，只有全相等才返回true
     */
    public static boolean isEquals(String... agrs) {
        String last = null;
        for (int i = 0; i < agrs.length; i++) {
            String str = agrs[i];
            if (isEmpty(str)) {
                return false;
            }
            if (last != null && !str.equalsIgnoreCase(last)) {
                return false;
            }
            last = str;
        }
        return true;
    }

    /**
     * 返回一个高亮spannable
     *
     * @param content 文本内容
     * @param color   高亮颜色
     * @param start   起始位置
     * @param end     结束位置
     * @return 高亮spannable
     */
    public static CharSequence getHighLightText(String content, int color,
                                                int start, int end) {
        if (TextUtils.isEmpty(content)) {
            return "";
        }
        start = start >= 0 ? start : 0;
        end = end <= content.length() ? end : content.length();
        SpannableString spannable = new SpannableString(content);
        CharacterStyle span = new ForegroundColorSpan(color);
        spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    /**
     * 获取链接样式的字符串，即字符串下面有下划线
     *
     * @param resId 文字资源
     * @return 返回链接样式的字符串
     */
    public static Spanned getHtmlStyleString(int resId) {
        StringBuilder sb = new StringBuilder();
        sb.append("<a href=\"\"><u><b>").append(UIUtils.getString(resId))
                .append(" </b></u></a>");
        return Html.fromHtml(sb.toString());
    }

    /**
     * 格式化文件大小，不保留末尾的0
     */
    public static String formatFileSize(long len) {
        return formatFileSize(len, false);
    }

    /**
     * 格式化文件大小，保留末尾的0，达到长度一致
     */
    public static String formatFileSize(long len, boolean keepZero) {
        String size;
        DecimalFormat formatKeepTwoZero = new DecimalFormat("#.00");
        DecimalFormat formatKeepOneZero = new DecimalFormat("#.0");
        if (len < 1024) {
            size = String.valueOf(len + "B");
        } else if (len < 10 * 1024) {
            // [0, 10KB)，保留两位小数
            size = String.valueOf(len * 100 / 1024 / (float) 100) + "KB";
        } else if (len < 100 * 1024) {
            // [10KB, 100KB)，保留一位小数
            size = String.valueOf(len * 10 / 1024 / (float) 10) + "KB";
        } else if (len < 1024 * 1024) {
            // [100KB, 1MB)，个位四舍五入
            size = String.valueOf(len / 1024) + "KB";
        } else if (len < 10 * 1024 * 1024) {
            // [1MB, 10MB)，保留两位小数
            if (keepZero) {
                size = String.valueOf(formatKeepTwoZero.format(len * 100 / 1024
                        / 1024 / (float) 100))
                        + "MB";
            } else {
                size = String.valueOf(len * 100 / 1024 / 1024 / (float) 100)
                        + "MB";
            }
        } else if (len < 100 * 1024 * 1024) {
            // [10MB, 100MB)，保留一位小数
            if (keepZero) {
                size = String.valueOf(formatKeepOneZero.format(len * 10 / 1024
                        / 1024 / (float) 10))
                        + "MB";
            } else {
                size = String.valueOf(len * 10 / 1024 / 1024 / (float) 10)
                        + "MB";
            }
        } else if (len < 1024 * 1024 * 1024) {
            // [100MB, 1GB)，个位四舍五入
            size = String.valueOf(len / 1024 / 1024) + "MB";
        } else {
            // [1GB, ...)，保留两位小数
            size = String.valueOf(len * 100 / 1024 / 1024 / 1024 / (float) 100)
                    + "GB";
        }
        return size;
    }

    public static void saveInfo(Context context, String key, List<Map<String, String>> datas) {
        JSONArray mJsonArray = new JSONArray();
        for (int i = 0; i < datas.size(); i++) {
            Map<String, String> itemMap = datas.get(i);
            Iterator<Entry<String, String>> iterator = itemMap.entrySet().iterator();

            JSONObject object = new JSONObject();

            while (iterator.hasNext()) {
                Entry<String, String> entry = iterator.next();
                try {
                    object.put(entry.getKey(), entry.getValue());
                } catch (JSONException e) {

                }
            }
            mJsonArray.put(object);
        }

        SharedPreferences sp = context.getSharedPreferences("finals", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString(key, mJsonArray.toString());
        editor.commit();
    }

    public static String getNum(String num) {
        Pattern p = Pattern.compile("[0-9]*");
        Matcher m = p.matcher(num);
        if (m.matches()) {
        }
        p = Pattern.compile("[a-zA-Z]");
        m = p.matcher(num);
        if (m.matches()) {
        }
        p = Pattern.compile("[\u4e00-\u9fa5]");
        m = p.matcher(num);
        if (m.matches()) {
        }
        return num;
    }
    /*public static List<FileInfo> getInfo(Context context, String key) {
    //    List<Map<String, String>> datas = new ArrayList<Map<String, String>>();
	    List<FileInfo> fileInfo=new ArrayList<FileInfo>();
	    SharedPreferences sp = context.getSharedPreferences("finals", Context.MODE_PRIVATE);
	    String result = sp.getString(key, "");
	    try {
	        JSONArray array = new JSONArray(result);
	        for (int i = 0; i < array.length(); i++) {
	            JSONObject itemObject = array.getJSONObject(i);
	            Map<String, String> itemMap = new HashMap<String, String>();
	            JSONArray names = itemObject.names();
	            if (names != null) {  
	                for (int j = 0; j < names.length(); j++) {
	                    String name = names.getString(j);
	                    String value = itemObject.getString(name);
	                    itemMap.put(name, value);
	                }
	            }
	            fileInfo.add(FileInfo.setMapToInfo(itemMap));
	        }
	    } catch (JSONException e) {

	    }

	    return fileInfo;
	}
	 */

    public static String convertUnicode(String ori) {
        char aChar;
        int len = ori.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len; ) {
            aChar = ori.charAt(x++);
            if (aChar == '\\') {
                aChar = ori.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = ori.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed   \\uxxxx   encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);

        }
        return outBuffer.toString();
    }

    //String test = "100,120,166,1555,120,150,100";
    public static String subLimit(String str) {
        StringBuilder strBuilder = new StringBuilder();
        String[] test1 = str.split(",");
        ArrayList list = new ArrayList();
        for (int i = 0; i < test1.length; i++) {
            if (!list.contains(test1[i]))
                list.add(test1[i]);
        }
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) {
                strBuilder.append(list.get(i));
            } else {
                strBuilder.append(list.get(i) + ",");
            }
        }
        return strBuilder.toString();
    }

    public static String delHTMLTag(String htmlStr) {
        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式
        String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式

        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); //过滤script标签

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); //过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); //过滤html标签

        return htmlStr.trim(); //返回文本字符串
    }

    /**
     * 判断服务器返回来的img路径
     *
     * @param imgUrl
     * @return
     */
    public static String getImgUrl(String imgUrl) {
        String url = null;
        if (imgUrl.indexOf("http") != -1) {
            url = imgUrl;
            //System.out.println("存在包含关系，因为返回的值不等于-1");
        } else {
            url = MyCookieStore.URL + imgUrl;
            //System.out.println("不存在包含关系，因为返回的值等于-1");
        }
        return url;
    }

    /**
     * 判断是否为数字(正负数都行)
     *
     * @param str 需要验证的字符串
     * @return
     */
    public boolean isNumericZF(String str) {
        int num = Integer.parseInt(str);
        if (num > 0) {
//            Toast.makeText(this, "Is 正 Number", Toast.LENGTH_SHORT).show();
            return true;
        } else {
//            Toast.makeText(this, "Is 负 Number", Toast.LENGTH_SHORT).show();
            return false;

        }
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    /**
     * 判断是否是json格式
     * @param content
     * @return
     */
    public static boolean isJsonValid(String content){

        try {
              new Gson(). fromJson(content, Object.class);
            return  true;
        } catch (Exception e) {
            return false;
        }
    }
}
