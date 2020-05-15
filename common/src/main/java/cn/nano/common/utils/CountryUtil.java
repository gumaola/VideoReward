package cn.nano.common.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

import java.util.Locale;

public class CountryUtil {

    public static String getCountryFromNetwork(Context context) {
        try {
            TelephonyManager manager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            if (manager != null && manager.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) {
                String country = manager.getNetworkCountryIso();
                if (country != null && country.length() == 2) {
                    return country.toLowerCase(Locale.US);
                }
            }
        } catch (Exception e) {
            // Failed to get country from network
        }
        return null;
    }

    public static String getCountryFromLocale() {
        return Locale.getDefault().getCountry();
    }
}
