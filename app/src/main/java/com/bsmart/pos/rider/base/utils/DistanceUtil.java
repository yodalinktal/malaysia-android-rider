package com.bsmart.pos.rider.base.utils;

import com.bsmart.pos.rider.base.api.bean.AddressBean;

import static java.lang.Math.PI;
import static java.lang.Math.asin;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

/**
 * Author: yoda
 * DateTime: 2020/3/5 16:29
 */
public class DistanceUtil {

    private final static double EARTH_RADIUS = 6378137.0;

    public static double gps2m(AddressBean from,AddressBean to){
        return gps2m(from.getLat(),from.getLon(),to.getLat(),to.getLon());
    }

    public static double gps2m(double lat_a, double lng_a, double lat_b, double lng_b) {

        double radLat1 = (lat_a * PI / 180.0);

        double radLat2 = (lat_b * PI / 180.0);

        double a = radLat1 - radLat2;

        double b = (lng_a - lng_b) * PI / 180.0;

        double s = 2 * asin(sqrt(pow(sin(a / 2), 2)

                + cos(radLat1) * cos(radLat2)

                * pow(sin(b / 2), 2)));

        s = s * EARTH_RADIUS;

        s = Math.round(s * 10000) / 10000;

        return s;

    }

    public static void main(String[] args){
        double d = gps2m(36.894154,121.534362,39.509805,116.4105069);
        System.out.println(d);
    }

}
