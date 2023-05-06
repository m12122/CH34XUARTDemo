package cn.wch.ch34xuartdemo.utils;

import cn.wch.ch34xuartdemo.LogUtil;

//作用：将接收缓冲区的内容解析出来：空的和重复读到的标签过滤

public class Trans {
    public static String invalidstr = "AA01FF00011516DD";
    public static byte[] getEpc(byte[] readdata,int length) {
        byte[] text = new byte[8];
        byte[] epc = new byte[12];
        System.arraycopy(readdata, 0, text, 0, 8); //缓冲区首8字节数据
        if (length <= 16) {//1‘一个或两个空； 2’16字节标签（后半截）
            if (FormatUtil.bytesToHexString(text).equals(invalidstr)) {
                return null;
            } else {
                if(length==8){
                    System.arraycopy(readdata, 0, epc, 8, 4);
                    return epc;
                }else{
                    System.arraycopy(readdata, 0, epc, 0, 12);
                    return epc;
                }
            }
        } else if (length == 24) {//length = 24 有三种情况：1‘3X空 ；2’一个完整标签（24） ；3‘16字节标签+1X空
            if (FormatUtil.bytesToHexString(text).equals(invalidstr)) {//1‘
                return null;
            } else {
                System.arraycopy(readdata, 16, text, 0, 8);//缓冲区后8个字节的数据
                if (FormatUtil.bytesToHexString(text).equals(invalidstr)) {//3’
                    System.arraycopy(readdata, 0, epc, 0, 12);
                    return epc;
                } else {//2‘
                    System.arraycopy(readdata, 8, epc, 0, 12);
                    return epc;
                }
            }
        } else {//length=32有7种情况：1‘1X空+24字节标签 ；2’ 2X空+16字节标签 ;3'3X空+8字节标签 ;4'4X空
            //5’ 8字节标签+3X空 ; 6'16字节标签+2X空 ; 7'一个完整标签（24字节）+1X空
            //8’8字节标签+一整个标签 ; 9‘16字节标签+16字节标签
            if (FormatUtil.bytesToHexString(text).equals(invalidstr)) {
                System.arraycopy(readdata, 24, text, 0, 8);//最后8个字节的数据
                if (FormatUtil.bytesToHexString(text).equals(invalidstr)) {//5'(首尾皆空)
                    return null;
                } else {//首空尾不空 1’/2‘/3’
                    System.arraycopy(readdata, 8, text, 0, 8);//第二个8字节
                    if (FormatUtil.bytesToHexString(text).equals(invalidstr)) {//2'/3‘
                        System.arraycopy(readdata,16,text,0,8);//第3个8字节
                        if(FormatUtil.bytesToHexString(text).equals(invalidstr)){//3'
                            return null;
                        }else{//2'
                            System.arraycopy(readdata,8,epc,0,8);
                            return epc;
                        }
                    } else {//1'
                        System.arraycopy(readdata, 16, epc, 0, 12);
                        return epc;
                    }
                }

            } else {//5'/6'/7'
                System.arraycopy(readdata, 8, text, 0, 8);//第二个8字节
                if (FormatUtil.bytesToHexString(text).equals(invalidstr)) {//5’
                    System.arraycopy(readdata, 0, epc, 8, 4);
                    return epc;
                } else {//4‘
                    System.arraycopy(readdata, 16, epc, 0, 8);//第三个8字节
                    if(FormatUtil.bytesToHexString(text).equals(invalidstr)){//6'
                       System.arraycopy(readdata,0,epc,0,12);
                       return epc;
                    }else{//7'
                        System.arraycopy(readdata,8,epc,0,12);
                        return epc;
                    }
                }
            }
        }
    }
    public String GetEpc(byte[] readdata,int length){
        byte[] epc = new byte[length];
        String EPC = "";
        String data = FormatUtil.bytesToHexString(readdata);
        int startindex = data.indexOf("DD");
        int endindex = data.indexOf("AA");
        if (startindex<endindex){//前面有半截标签数据，可能是8位，可能是16位
            if (endindex == 15){
                EPC= data.substring(0,7);

            }else{
                EPC = data.substring(0,23);
            }

        }else{
            if(endindex-startindex>15){
                data=data.substring(startindex,endindex);
            }
        }

        return EPC;
    }
}
