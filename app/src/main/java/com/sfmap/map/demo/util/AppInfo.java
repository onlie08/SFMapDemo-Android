package com.sfmap.map.demo.util;

import android.content.Context;

public class AppInfo {
    //配置的地图中心点
    private static String mapCenterLocation = "";
    private static String mapCenterDefLocation = "39.913151,116.398219";//北京天安门位置

    private static String getCustomOrDefaultURL(Context context, String curValue, String defaultValue, String metaKey) {
        try {
            if ((curValue == null) || (curValue.equals(""))) {
                curValue = ConfigerHelper.getInstance(context).getKeyValue(metaKey);
                if (curValue == null || curValue.equals("")) {
                    curValue = defaultValue;
                }
            }
        }catch (Exception e){
            curValue = defaultValue;
        }
        return curValue;
    }

    /*
     * 配置的地图中心点
     * @param context
     * @return
     */
    public static String getMapCenterLocation(Context context) {
        return getCustomOrDefaultURL(context, mapCenterLocation, mapCenterDefLocation, ConfigerHelper.MAP_CENTER_URL);
    }

}

