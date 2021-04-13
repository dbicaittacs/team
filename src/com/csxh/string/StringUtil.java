package com.csxh.string;

/*
 * ����ַ���������
 */
public class StringUtil {

	/*
	 * ��Ŀ�괮��ָ������λ���ַ�����Сдת��������ת������´�
	 * �����������:indexs�������ַ�ת��Сд
	 */
	public static String toLowerCaseAt(String target, int... indexs){
		char[] chs=target.toCharArray();
		if(indexs.length==0){
		   //����һ����ĸת��Сд
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
	/*
	 * ��Ŀ�괮��ָ������λ���ַ����д�дת��������ת������´�
	 * �����������:indexs�������ַ�ת�ɴ�д
	 */
	public static String toUpperCaseAt(String target, int... indexs){
		char[] chs=target.toCharArray();
		if(indexs.length==0){
			//����һ����ĸת��Сд
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
