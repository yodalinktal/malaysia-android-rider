package com.bsmart.pos.rider.base.api.enums;

import java.util.HashMap;

/**
 * Author: yoda
 * DateTime: 2020/3/5 15:32
 */
public class TypeConstant {

    public final static Integer CreditCard = 0;
    public final static Integer COD = 1;
    public final static Integer CreditTerm = 2;
    public final static Integer OnlineBank = 3;
    public final static Integer EWallet = 4;

    private static  TypeConstant typeConstant = null;

    public HashMap<String,Integer> TYPE_ENUM;

    private TypeConstant(){

        TYPE_ENUM = new HashMap<>();
        TYPE_ENUM.put("Credit Card",CreditCard);
        TYPE_ENUM.put("Cash On Delivery (COD)",COD);
        TYPE_ENUM.put("Credit Term",CreditTerm);
        TYPE_ENUM.put("Online Bank",OnlineBank);
        TYPE_ENUM.put("E-Wallet",EWallet);

        TYPE_ENUM.put("Merchandise",0);
        TYPE_ENUM.put("Document",1);
        TYPE_ENUM.put("Electronic product",2);
        TYPE_ENUM.put("Daily necessities",3);
        TYPE_ENUM.put("Cake",4);
        TYPE_ENUM.put("Fast food fruit",5);
        TYPE_ENUM.put("Flowers",6);
        TYPE_ENUM.put("Seafood",7);
        TYPE_ENUM.put("Auto parts",8);
        TYPE_ENUM.put("Other",9);


        TYPE_ENUM.put("0.1~5kg",0);
        TYPE_ENUM.put("5~20kg",1);
        TYPE_ENUM.put("20kg+",2);

    }

    public static TypeConstant getInstance(){
        if (null == typeConstant){
            typeConstant = new TypeConstant();
        }
        return typeConstant;
    }

}
