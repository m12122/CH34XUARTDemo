package cn.wch.ch34xuartdemo.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.wch.ch34xuartdemo.LogUtil;

//作用：将接收缓冲区的内容解析出来：空的和重复读到的标签过滤

public class Trans {
    public static String invalidstr = "AA01FF00011516DD";

    public static List<byte[]> getEpc(byte[] readdata, int length) {
        List<byte[]> result = new ArrayList<>();
        byte[] text = new byte[8];
        byte[] epc_full = new byte[12];
        byte[] epc_4 = new byte[4];
        byte[] epc_8 = new byte[8];
        int DDindex;
        String data = FormatUtil.bytesToHexString(readdata);
        System.arraycopy(readdata, 0, text, 0, 8); //缓冲区首8字节数据
        if (length <= 16) {//1‘一个或两个空； 2’16字节标签（后半截）;3'8字节标签+1X空；4’8字节标签
            if (FormatUtil.bytesToHexString(text).equals(invalidstr)) {//1‘

            } else {
                if (length == 8) {//4‘
                    System.arraycopy(readdata, 0, epc_4, 0, 4);
                    result.add(epc_4);

                } else {
                    System.arraycopy(readdata,8,text,0,8);
                    if (FormatUtil.bytesToHexString(text).equals(invalidstr)){//3'
                        System.arraycopy(readdata,0,epc_4,0,4);
                        result.add(epc_4);
                    }else{//2'
                        System.arraycopy(readdata, 0, epc_full, 0, 12);
                        result.add(epc_full);
                    }
                }
            }
        } else if (length == 24) {//length = 24 有4种情况：1‘3X空 ；2’一个完整标签（24） ；3‘16字节标签+1X空
            // 4'8字节标签+2X空
            if (FormatUtil.bytesToHexString(text).equals(invalidstr)) {//1‘
                //无epc
            } else {
                System.arraycopy(readdata, 16, text, 0, 8);//缓冲区后8个字节的数据
                if (FormatUtil.bytesToHexString(text).equals(invalidstr)) {//3’/4‘
                    System.arraycopy(readdata,8,text,0,8);//第2个8字节
                    if (FormatUtil.bytesToHexString(text).equals(invalidstr)){//4’
                        System.arraycopy(readdata,0,epc_4,0,4);
                        result.add(epc_4);
                    }else {//3'
                        System.arraycopy(readdata, 0, epc_full, 0, 12);
                        result.add(epc_full);
                    }
                } else {//2‘
                    System.arraycopy(readdata, 8, epc_full, 0, 12);
                    result.add(epc_full);
                }
            }
        } else {//length=32有10种情况：1‘1X空+24字节标签 ；2’ 2X空+16字节标签 ;3'3X空+8字节标签 ;4'4X空
            //5’ 8字节标签+3X空 ; 6'16字节标签+2X空 ; 7'一个完整标签（24字节）+1X空
            //8’8字节标签+一整个标签 ; 9‘16字节标签+16字节标签; 10'一个完整标签（24）+8字节标签
            //11'8字节标签+1X空+16字节标签； 12’8字节标签+2X空+8字节标签  ；13‘16字节标签+1X空+8字节标签=6'
            if (FormatUtil.bytesToHexString(text).equals(invalidstr)) {
                System.arraycopy(readdata, 24, text, 0, 8);//最后8个字节的数据
                if (FormatUtil.bytesToHexString(text).equals(invalidstr)) {//4'(首尾皆空)
                    //无epc
                } else {//首空尾不空 1’/2‘/3’
                    System.arraycopy(readdata, 8, text, 0, 8);//第二个8字节
                    if (FormatUtil.bytesToHexString(text).equals(invalidstr)) {//2'/3‘
                        System.arraycopy(readdata, 16, text, 0, 8);//第3个8字节
                        if (FormatUtil.bytesToHexString(text).equals(invalidstr)) {//3'
                            //无epc
                        } else {//2'
                            System.arraycopy(readdata, 24, epc_8, 0, 8);
                            result.add(epc_8);
                        }
                    } else {//1'
                        System.arraycopy(readdata, 16, epc_full, 0, 12);
                        result.add(epc_full);
                    }
                }

            } else {//5'/6'/7'/8‘/9’(首不空)
                System.arraycopy(readdata, 8, text, 0, 8);//第二个8字节
                if (FormatUtil.bytesToHexString(text).equals(invalidstr)) {//5’//11’/12‘
                    System.arraycopy(readdata,24,text,0,8);//最后一个8字节
                    if(FormatUtil.bytesToHexString(text).equals(invalidstr)){//5’
                        System.arraycopy(readdata,0,epc_4,0,4);
                        result.add(epc_4);
                    }else {
                        System.arraycopy(readdata,16,text,0,8);
                        if (FormatUtil.bytesToHexString(text).equals(invalidstr)){//12'
                            System.arraycopy(readdata,0,epc_4,0,4);
                            result.add(epc_4);
                        }else{//11'
                            System.arraycopy(readdata,0,epc_4,0,4);
                            System.arraycopy(readdata,24,epc_8,0,8);
                            result.add(epc_4);
                            result.add(epc_8);
                            LogUtil.d("两段前后顺序");
                        }
                    }
                } else {//6‘/7’/8‘/9’/13'
                    System.arraycopy(readdata, 16, text, 0, 8);//第三个8字节
                    if (FormatUtil.bytesToHexString(text).equals(invalidstr)) {//6'/13'
                        System.arraycopy(readdata, 0, epc_full, 0, 12);
                        result.add(epc_full);
                    } else {//7'/8'/9'/10'
                        System.arraycopy(readdata, 24, text, 0, 8);//第四个8字节
                        if (FormatUtil.bytesToHexString(text).equals(invalidstr)) {//7'
                            System.arraycopy(readdata, 8, epc_full, 0, 12);
                            result.add(epc_full);
                        } else {//四个8字节皆不为空：8‘/9’/10'
                            DDindex = data.indexOf("DD");
                            LogUtil.d("DDindex:"+DDindex);
                            if (DDindex == 14 || DDindex == 13) {//8'
                                System.arraycopy(readdata, 0, epc_4, 0, 4);
                                System.arraycopy(readdata, 16, epc_full, 0, 12);
                                result.add(epc_4);
                                result.add(epc_full);
                            } else if(DDindex == 30 || DDindex == 29){//9'有两种情况：
                                System.arraycopy(readdata, 0, epc_full, 0, 12);
                                System.arraycopy(readdata, 24, epc_8, 0, 8);
                                result.add(epc_full);
                                result.add(epc_8);
                            }else {//10'
                                System.arraycopy(readdata, 8, epc_full, 0, 12);
                                result.add(epc_full);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }
}