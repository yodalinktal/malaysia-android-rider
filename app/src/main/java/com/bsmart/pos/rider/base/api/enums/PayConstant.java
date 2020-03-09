package com.bsmart.pos.rider.base.api.enums;

import java.util.HashMap;

/**
 * Author: yoda
 * DateTime: 2020/3/9 17:03
 */
public class PayConstant {

    private static  PayConstant typeConstant = null;

    public static PayConstant getInstance(){
        if (null == typeConstant){
            typeConstant = new PayConstant();
        }
        return typeConstant;
    }

    public HashMap<Integer,String> TYPE_ENUM;

    private PayConstant(){

        TYPE_ENUM = new HashMap<>();
        TYPE_ENUM.put(0,"Credit Card");
        TYPE_ENUM.put(1,"Online Banking");
        TYPE_ENUM.put(2,"E-Wallet");

    }


}
