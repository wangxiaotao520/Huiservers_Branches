package com.huacheng.huiservers.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ToolUtils {

    Context mContext;
    View mView;

    public ToolUtils(View v, Context context) {
        this.mView = v;
        this.mContext = context;
    }

    private static TelephonyManager telephonyManager;

    /**
     * 过滤emoji表情（因为emoji表情版本太多，所以只可以过滤大部分，或者说常用的表情）
     *
     * @param source
     * @return
     */
    public static String filterEmoji(String source) {
        StringBuilder afterFilterString = new StringBuilder();
        if (source != null && source != "") {
            int len = source.length();
            for (int i = 0; i < len; i++) {
                char codePoint = source.charAt(i);
                if (!isEmojiCharacter(codePoint)) { // 如果匹配,则该字符是Emoji表情
                    afterFilterString.append(codePoint);
                }
            }
        }
        return afterFilterString.toString();
    }

    /**
     * 判断字符串是否是Emoji表情（因为emoji表情版本太多，所以只可以判断大部分，或者说常用的表情）
     *
     * @param source
     * @return true 表示包含emoji表情
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (isEmojiCharacter(codePoint)) { // 如果匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /**
     * 判断单个字符是否是Emoji表情
     *
     * @param codePoint 比较的单个字符
     * @return true 表示该字符是Emoji表情
     */
    private static boolean isEmojiCharacter(char codePoint) {
        boolean temp = (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
                || (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
                || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
                || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));

        return !temp;
    }

    /**
     * 判断是否是身份证
     *
     * @param identifyCard
     */
    public static boolean isIdentifyCard(String identifyCard) {
        // 15位和18位身份证号码的正则表达式
        Pattern p = Pattern
                .compile("(^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$)|(^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[Xx])$)");
        // 18位身份证校验,比较严格校验
        /*"^(\\d{6})(19|20)(\\d{2})(1[0-2]|0[1-9])(0[1-9]|[1-2][0-9]|3[0-1])(\\d{3})(\\d|X|x)?$"*/
        /*Pattern p = Pattern.compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");*/
        Matcher m = p.matcher(identifyCard);
        return m.matches();
    }

    /**
     * 判断格式是否为数字
     */
    public static boolean isNum(String isnum) {
        Pattern p = Pattern.compile("[0-9]*");
        Matcher m = p.matcher(isnum);
        return m.matches();
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobile(String number) {
        /*
		移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		联通：130、131、132、152、155、156、185、186
		电信：133、153、180、189、（1349卫通）
		总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
        String num = "[1][3458]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(number)) {
            return false;
        } else {
            // matches():字符串是否在给定的正则表达式匹配
            return number.matches(num);
        }
    }
    /**
     * 正则：手机号（精确）
     * <p>移动：134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188、198</p>
     * <p>联通：130、131、132、145、155、156、175、176、185、186、166</p>
     * <p>电信：133、153、173、177、180、181、189、199</p>
     * <p>全球星：1349</p>
     * <p>虚拟运营商：170</p>
     */
    /**
     * 判断电话号码是否有效
     * 移动：134、135、136、137、138、139、147、150、151、152、157、158、159、182、183、187、188
     * 联通：130、131、132、145、155、156、185、186 电信：133、153、180、181、189 虚拟运营商：17x
     */
    public static boolean isMobileNO(String number) {
        if (number.startsWith("+86")) {
            number = number.substring(3);
        }

        if (number.startsWith("+") || number.startsWith("0")) {
            number = number.substring(1);
        }

        number = number.replace(" ", "").replace("-", "");
        System.out.print("号码：" + number + "///////");

       /* Pattern p = Pattern
                .compile("^((13[0-9])|(14[5,7])|(15[^4,\\D])|(18[0-9])|(17[0-9]))\\d{8}$");*/
        Pattern p = Pattern
                .compile("^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0-9])|(18[0-9])|166|198|199|(147))\\d{8}$");

        Matcher m = p.matcher(number);

        return m.matches();
    }

    /**
     * 号码的运营商类型
     *
     * @param number
     * @return
     */
    public static String getMobileType(String number) {
        String type = "未知用户";
        Pattern p = Pattern.compile("^(([4,8]00))\\d{7}$");
        if (p.matcher(number).matches())
            return "企业电话";

        if (number.startsWith("+86")) {
            number = number.substring(3);
        }

        if (number.startsWith("+") || number.startsWith("0")) {
            number = number.substring(1);
        }

        number = number.replace(" ", "").replace("-", "");
        System.out.print("号码：" + number);

        p = Pattern.compile("^((13[4-9])|(147)|(15[0-2,7-9])|(18[2,3,7,8]))\\d{8}$");
        if (p.matcher(number).matches())
            return "移动用户";

        p = Pattern.compile("^((13[0-2])|(145)|(15[5,6])|(18[5,6]))\\d{8}$");
        if (p.matcher(number).matches())
            return "联通用户";

        p = Pattern.compile("^((1[3,5]3)|(18[0,1,9]))\\d{8}$");
        if (p.matcher(number).matches())
            return "电信用户";

        p = Pattern.compile("^((17[0-9]))\\d{8}$");
        if (p.matcher(number).matches())
            return "虚拟运营端";

        if (number.length() >= 7 && number.length() <= 12)
            return "固话用户";

        return type;
    }

    /**
     * 获取随机数
     *
     * @param iRdLength
     * @return
     */
    public static String getRandom(int iRdLength) {
        Random rd = new Random();
        int iRd = rd.nextInt();
        if (iRd < 0) { // 负数时转换为正数
            iRd *= -1;
        }
        String sRd = String.valueOf(iRd);
        int iLgth = sRd.length();
        if (iRdLength > iLgth) { // 获取数长度超过随机数长度
            return digitToString(iRd, iRdLength);
        } else {
            return sRd.substring(iLgth - iRdLength, iLgth);
        }
    }

    /**
     * 把一个整数转化为一个n位的字符串
     *
     * @param digit
     * @param n
     * @return
     */
    public static String digitToString(int digit, int n) {
        String result = "";
        for (int i = 0; i < n - String.valueOf(digit).length(); i++) {
            result = result + "0";
        }
        result = result + String.valueOf(digit);
        return result;
    }

    /**
     * 计算MD5
     *
     * @param str
     * @return
     */
    public static String MD5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes("utf-8"));
            byte[] result = md.digest();
            StringBuffer sb = new StringBuffer(32);
            for (int i = 0; i < result.length; i++) {
                int val = result[i] & 0xff;
                if (val <= 0xf) {
                    sb.append("0");
                }
                sb.append(Integer.toHexString(val));
            }
            return sb.toString();// .toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 四舍五入
     */
    public static String double_convert(double value) {
        long l1 = Math.round(value * 100);// 四舍五入
        double ret = l1 / 100.0;// 注意：使用100.0,而不是 100
        if (ret - (int) ret == 0) {
            return (int) ret + "";
        } else {
            return ret + "";
        }
    }

    /**
     * 根据字符串，计算出其占用的宽度
     *
     * @param str
     * @param textsize
     * @return
     */
    public static float getTextWidthFontPx(String str, float textsize) {
        Paint mPaint = new Paint();
        mPaint.setTextSize(textsize);
        return str.length() * mPaint.getFontSpacing();
    }

    /**
     * 根据经纬度计算距离
     *
     * @param longitude1
     * @param latitude1
     * @return
     */
    private static final double EARTH_RADIUS = 6378137.0;

    // 返回单位是米
    public static double getDistance(double longitude1, double latitude1, double longitude2,
                                     double latitude2) {
        double Lat1 = rad(latitude1);
        double Lat2 = rad(latitude2);
        double a = Lat1 - Lat2;
        double b = rad(longitude1) - rad(longitude2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(Lat1)
                * Math.cos(Lat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 是否含有指定字符
     *
     * @param expression
     * @param text
     * @return
     */
    private static boolean matchingText(String expression, String text) {
        Pattern p = Pattern.compile(expression);
        Matcher m = p.matcher(text);
        boolean b = m.matches();
        return b;
    }

    /**
     * 邮政编码
     *
     * @param zipcode
     * @return
     */
    public static boolean isZipcode(String zipcode) {
        Pattern p = Pattern.compile("[0-9]\\d{5}");
        Matcher m = p.matcher(zipcode);
        System.out.println(m.matches() + "-zipcode-");
        return m.matches();
    }

    /**
     * 邮件格式
     *
     * @param email
     * @return
     */
    public static boolean isValidEmail(String email) {

        Pattern p = Pattern
                .compile("^(\\w+(\\.\\w)*@\\w+(\\.\\w{2,3}){1,3})$");
//		Pattern p = Pattern
//				.compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
        Matcher m = p.matcher(email);
        System.out.println(m.matches() + "-email-");
        return m.matches();
    }

    /**
     * 固话号码格式
     *
     * @param telfix
     * @return
     */
    public static boolean isTelfix(String telfix) {
        Pattern p = Pattern.compile("d{3}-d{8}|d{4}-d{7}");
        Matcher m = p.matcher(telfix);
        System.out.println(m.matches() + "-telfix-");
        return m.matches();
    }

    /**
     * 用户名匹配
     *
     * @param name
     * @return
     */
    public static boolean isCorrectUserName(String name) {
        Pattern p = Pattern.compile("([A-Za-z0-9]){2,10}");
        Matcher m = p.matcher(name);
        System.out.println(m.matches() + "-name-");
        return m.matches();
    }

    /**
     * 密码匹配，以字母开头，长度 在6-18之间，只能包含字符、数字和下划线。
     *
     * @param pwd
     * @return
     */
    public static boolean isCorrectUserPwd(String pwd) {
        Pattern p = Pattern.compile("\\w{6,18}");
        Matcher m = p.matcher(pwd);
        System.out.println(m.matches() + "-pwd-");
        return m.matches();
    }

    /**
     * 密码必须至少要8位，且包含数字和字母
     *
     * @param pwd
     * @return
     */
    public static boolean isReguPwd(String pwd) {
        //分开来注释一下：
        //^ 匹配一行的开头位置
        //(?![0-9]+$) 预测该位置后面不全是数字
        //(?![a-zA-Z]+$) 预测该位置后面不全是字母
        //[0-9A-Za-z] {6,10} 由6-10位数字或这字母组成
        //$ 匹配行结尾位置
        Pattern p = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,}$");
        Matcher m = p.matcher(pwd);
        return m.matches();
    }

    /**
     * 计算剩余日期
     *
     * @param remainTime
     * @return
     */
    public static String calculationRemainTime(String endTime, long countDown) {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date now = new Date(System.currentTimeMillis());// 获取当前时间
            Date endData = df.parse(endTime);
            long l = endData.getTime() - countDown - now.getTime();
            long day = l / (24 * 60 * 60 * 1000);
            long hour = (l / (60 * 60 * 1000) - day * 24);
            long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
            long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
            return "剩余" + day + "天" + hour + "小时" + min + "分" + s + "秒";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取手机Imei号
     *
     * @param context
     * @return
     */
    public static String getImeiCode(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    // 获取屏幕的宽度
    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getWidth();
    }

    // 获取屏幕的高度
    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getHeight();
    }

    // 上传图片压缩

    /**
     * @param listView
     * @author sunglasses
     * @category 计算listview的高度
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static void compressBmpToFile(Bitmap bmp, File file) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 80;// 个人喜欢从80开始,
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        while (baos.toByteArray().length / 1024 > 100) {
            baos.reset();
            options -= 10;
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 字数随着输入的长度二改变
    public static void geteditNum(final Context context, final EditText edit, final TextView txt,
                                  final int num) {
        edit.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int selectStart;
            private int selectEnd;

            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
                temp = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = edit.getText().toString();
                txt.setText(String.valueOf(str.length()));
                selectStart = edit.getSelectionStart();
                selectEnd = edit.getSelectionEnd();
                if (temp.length() > num) {
                    XToast.makeText(context, "最大字数不超过50字", XToast.LENGTH_SHORT).show();
                    s.delete(selectStart - 1, selectEnd);
                    int tempSelection = selectStart;
                    edit.setText(s);
                    edit.setSelection(tempSelection);
                }
            }
        });
    }

    /**
     * 加密
     *
     * @param plaintext 明文
     * @return ciphertext 密文
     */
    public final static String encrypt(String plaintext) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] btInput = plaintext.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取电话号码
     */
    public static String getNativePhoneNumber() {
        String NativePhoneNumber = null;
        NativePhoneNumber = telephonyManager.getLine1Number();
        return NativePhoneNumber;
    }

    /**
     * 检查当前网络是否可用
     *
     * @param context
     * @return
     */

    public static boolean isNetworkAvailable(Activity activity) {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //判断移动数据是否打开
    public static boolean isMobileConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return true;
        }
        return false;
    }

    //判断WiFi是否打开
    public static boolean isWiFiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    //判断网络连接是否可用
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
        } else {
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 返回手机移动数据的状态
     *
     * @param pContext
     * @param arg      默认填null
     * @return true 连接 false 未连接
     */
    public static boolean getMobileDataState(Context pContext, Object[] arg) {

        try {

            ConnectivityManager mConnectivityManager = (ConnectivityManager) pContext.getSystemService(Context.CONNECTIVITY_SERVICE);

            Class ownerClass = mConnectivityManager.getClass();

            Class[] argsClass = null;
            if (arg != null) {
                argsClass = new Class[1];
                argsClass[0] = arg.getClass();
            }

            Method method = ownerClass.getMethod("getMobileDataEnabled", argsClass);

            Boolean isOpen = (Boolean) method.invoke(mConnectivityManager, arg);

            return isOpen;

        } catch (Exception e) {
            // TODO: handle exception

            System.out.println("得到移动数据状态出错");
            return false;
        }

    }

    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }


    /**
     * 关闭软键盘
     */
    public void closeInputMethod() {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();
        if (isOpen) {
            // imm.toggleSoftInput(0,
            // InputMethodManager.HIDE_NOT_ALWAYS);//没有显示则显示

            imm.hideSoftInputFromWindow(mView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    /**
     * 输入价格
     * @param editText
     */
    public static void setPriceInput(final EditText editText) {
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    editText.setText(s);
                    editText.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        editText.setText(s.subSequence(0, 1));
                        editText.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }

        });
    }
}
