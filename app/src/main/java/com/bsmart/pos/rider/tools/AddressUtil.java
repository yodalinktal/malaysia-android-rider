package com.bsmart.pos.rider.tools;

import com.bsmart.pos.rider.base.api.bean.AddressBean;

/**
 * Author: yoda
 * DateTime: 2020/3/11 17:16
 */
public class AddressUtil {

    public static boolean CheckAddressValid(AddressBean addressBean){

        if (null != addressBean){

            if (StringUtil.isNotEmpty(addressBean.getZone())
                 && StringUtil.isNotEmpty(addressBean.getDetail())
                 && StringUtil.isNotEmpty(addressBean.getName())
                 && StringUtil.isNotEmpty(addressBean.getTelephone())
                 && null != addressBean.getLat()
                 && null != addressBean.getLon()
               ){
                return true;
            }

        }

        return false;

    }

}
