package com.bsmart.pos.rider.base.api.enums;

import java.util.HashMap;

/**
 * Author: yoda
 * DateTime: 2020/3/9 17:03
 */
public class PostTypeConstant {

    private static  PostTypeConstant typeConstant = null;

    public static PostTypeConstant getInstance(){
        if (null == typeConstant){
            typeConstant = new PostTypeConstant();
        }
        return typeConstant;
    }

    public HashMap<Integer,String> TYPE_ENUM;

    private PostTypeConstant(){

        TYPE_ENUM = new HashMap<>();
        TYPE_ENUM.put(0,"Merchandise");
        TYPE_ENUM.put(1,"Document");
        TYPE_ENUM.put(2,"Electronic product");
        TYPE_ENUM.put(3,"Daily necessities");
        TYPE_ENUM.put(4,"Cake");
        TYPE_ENUM.put(5,"Fast food fruit");
        TYPE_ENUM.put(6,"Flowers");
        TYPE_ENUM.put(7,"Seafood");
        TYPE_ENUM.put(8,"Auto parts");
        TYPE_ENUM.put(9,"Other");

    }
}
