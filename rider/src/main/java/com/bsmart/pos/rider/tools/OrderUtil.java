package com.bsmart.pos.rider.tools;

/**
 * Author: yoda
 * DateTime: 2020/4/4 18:35
 */
public class OrderUtil {


    public static String formatReceiptNo(String receiptNo){
        StringBuffer sb = new StringBuffer();

        if (StringUtil.isNotEmpty(receiptNo)){

            String A = receiptNo.toUpperCase();

            int length = A.length();
            int section = length/8;
            int mode = length%8;

            for (int i = 0; i < section; i++) {
                sb.append(A.substring(i*8,(i+1)*8));
                if (mode==0 && i==section-1){

                }else{
                    sb.append("-");
                }

            }

            if (mode>0){
                sb.append(A.substring(section*8,section*8+mode));
            }
        }

        return sb.toString();
    }

    /**
     *
     * @param orderNo 202004011430531 => 2020 0401 1430 531
     * @return
     */
    public static String formatOrderNo(String orderNo){

        StringBuffer sb = new StringBuffer();

        if (StringUtil.isNotEmpty(orderNo)){

            int length = orderNo.length();
            int section = length/4;
            int mode = length%4;

            for (int i = 0; i < section; i++) {
                sb.append(orderNo.substring(i*4,(i+1)*4));
                sb.append(" ");
            }

            if (mode>0){
                sb.append(orderNo.substring(section*4,section*4+mode));
            }

        }
        return sb.toString();

    }

}
