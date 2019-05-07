package tong.ziwei.phonecommon;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberToCarrierMapper;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.google.i18n.phonenumbers.geocoding.PhoneNumberOfflineGeocoder;

import java.util.Locale;

/**
 * Created by clara.tong on 2019\5\7 0007
 */
public class PhoneUtil {
    private static final String TAG = "PhoneUtil";

    private static PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    private static PhoneNumberToCarrierMapper carrierMapper = PhoneNumberToCarrierMapper.getInstance();

    private static PhoneNumberOfflineGeocoder geocoder = PhoneNumberOfflineGeocoder.getInstance();
    private static String LANGUAGE ="CN";


    /**根据号码获取归属地
     * return a geographical description string for the specified number.
     * see com.android.i18n.phonenumbers.PhoneNumberOfflineGeocoder
     */
    public static String getGeoDescription(Context context, String number) {
        android.util.Log.d(TAG, "getGeoDescription('" + number + "')...");

        if (TextUtils.isEmpty(number)) {
            return null;
        }

     /*   PhoneNumberUtil util = PhoneNumberUtil.getInstance();
        PhoneNumberOfflineGeocoder geocoder = PhoneNumberOfflineGeocoder.getInstance();
*/
        Locale locale = context.getResources().getConfiguration().locale;

        final TelephonyManager telephonyManager =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String countryIso = telephonyManager.getNetworkCountryIso().toUpperCase();

        if (countryIso == null) {
            countryIso = locale.getCountry();
            Log.w(TAG, "No CountryDetector; falling back to countryIso based on locale: "
                    + countryIso);
        }

        Phonenumber.PhoneNumber pn = null;
        try {
            android.util.Log.d(TAG, "parsing '" + number
                    + "' for countryIso '" + countryIso + "'...");
            pn = phoneNumberUtil.parse(number, countryIso);
            android.util.Log.d(TAG, "- parsed number: " + pn);
        } catch (NumberParseException e) {
            android.util.Log.d(TAG, "getGeoDescription: NumberParseException for incoming number '" +
                    number + "'");
        }

        if (pn != null) {
            String description = geocoder.getDescriptionForNumber(pn, locale);
            android.util.Log.d(TAG, "- got description: '" + description + "'");
            return description;
        }

        return null;
    }
/*
    作者：渔人爱编程
    来源：CSDN
    原文：https://blog.csdn.net/u013122625/article/details/52886440
    版权声明：本文为博主原创文章，转载请附上博文链接！*/


    /**
     * 根据国家代码和手机号  判断手机运营商,这个号码可以不用加+86
     * @param phoneNumber
     * @return
     */
    public static String GetCarrier(String phoneNumber) throws NumberParseException {

        Phonenumber.PhoneNumber referencePhonenumber = new Phonenumber.PhoneNumber();
        referencePhonenumber = phoneNumberUtil.parse(phoneNumber,LANGUAGE );

        //返回结果只有英文，自己转成成中文
        String carrierEn = carrierMapper.getNameForNumber(referencePhonenumber, Locale.ENGLISH);
        String carrierZh = "";
        carrierZh += geocoder.getDescriptionForNumber(referencePhonenumber, Locale.CHINESE);
        switch (carrierEn) {
            case "China Mobile":
                carrierZh += "移动";
                break;
            case "China Unicom":
                carrierZh += "联通";
                break;
            case "China Telecom":
                carrierZh += "电信";
                break;
            default:
                break;
        }
        return carrierZh;
    }


}
