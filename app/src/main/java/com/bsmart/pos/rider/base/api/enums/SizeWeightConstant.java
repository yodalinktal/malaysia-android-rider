package com.bsmart.pos.rider.base.api.enums;

import java.util.HashMap;

/**
 * Author: yoda
 * DateTime: 2020/3/9 17:03
 */
public class SizeWeightConstant {

    private static  SizeWeightConstant typeConstant = null;

    public static SizeWeightConstant getInstance(){
        if (null == typeConstant){
            typeConstant = new SizeWeightConstant();
        }
        return typeConstant;
    }

    public HashMap<Integer,String> TYPE_ENUM;

    private SizeWeightConstant(){

        TYPE_ENUM = new HashMap<>();
        TYPE_ENUM.put(0,"0.1~5kg");
        TYPE_ENUM.put(1,"5~20kg");
        TYPE_ENUM.put(2,"20kg+");

    }
}
