package com.csxh.string;

/*
 * 设计字符串工具类
 */
public class StringUtil {

	/*
	 * 对目标串中指定索引位的字符进行小写转换，返回转换后的新串
	 */
	public static String toLowerCaseAt(String target, int... indexs){
		char[] chs=target.toCharArray();
		if(indexs.length==0){
		   //将第一个字母转成小写
		   chs[0]=Character.toLowerCase(chs[0]);	
		}else{
		   for(int i=0;i<indexs.length;i++){
			   int index=indexs[i];
			   if(index>-1 && index<chs.length){
				   chs[index]=Character.toLowerCase(chs[index]);
			   }
		   }	
		}
		return new String(chs);
	}
	
	public static String toUpperCaseAt(String target, int... indexs){
		char[] chs=target.toCharArray();
		if(indexs.length==0){
			//将第一个字母转成小写
			chs[0]=Character.toUpperCase(chs[0]);	
		}else{
			for(int i=0;i<indexs.length;i++){
				int index=indexs[i];
				if(index>-1 && index<chs.length){
					chs[index]=Character.toUpperCase(chs[index]);
				}
			}	
		}
		return new String(chs);
	}
	
}
