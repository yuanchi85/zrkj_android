package zrkj.project.util;

import java.util.HashMap;
import java.util.Map;

public class Code {
	public final static String traceUrl = "http://zs.fugesen.com.cn:6060/fgstm/trace.do?info&code="; // 开始符号
	public final static String RS = "s" + ""; // 开始符号
	public final static String GS = ";" + ""; // 内容分隔符
	public final static String EOR = "e" + ""; // 结束符
	public final static String CURZD="E#";//到货单
	public final static String CURZF="E*";//到货单材料条码
	public final static String PURZD="D#";//到货单
	public final static String MOCTJ="C*";//箱条码
	public final static String MOCTP="P*";//包盒条码
	public final static String PURZF="B*";//原材料条码
	public final static String SDAZJ1="J";//载具条码
	public final static String SDAZJ2="W*";//钢网条码
	public final static String CMSZI="K*";//库位条码
	public final static String QMCHB="Q*";//不良类型条码
	public final static String MOCZA="F#";//领料单
	public final static String COPTG="C#";//产品销货单单号
	public final static String COPTGRK="R#";//产品入库单单号
	public final static String MOCZE="T#";//退料单
	public static char byteAsciiToChar(int ascii) {
		char ch = (char) ascii;
		return ch;
	}

	public static Map<String, Object> jx(String tm) {
		Map<String, Object> m = new HashMap<String, Object>(keys.length + 1);
		if (tm.startsWith(RS) && tm.endsWith(EOR)) {
			try {
				tm = tm.substring(RS.length(), tm.length() - EOR.length());
				String[] strs = tm.split(GS);
				m.put("type", strs[0]);// 第0个表示条码类别
				for (int i = 1; i < strs.length; i++) {
					for (String key : keys) {
						if (strs[i].startsWith(key)) {
							m.put(key,
									strs[i].substring(key.length(),
											strs[i].length()));
							break;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null; // 表示无效条码
			}
		} else {
			return m;
			// return null; // 表示无效条码
		}
		return m;
	}

	// key可以是任意位数不为空的字符串 注意写备注 具体key值待讨论
	public final static String[] keys = { "0"// 0 品号
			, "1"// 1 品名
			, "2"// 2 批次
			, "3"// 3 流水码
			, "4"// 4 数量
			, "5"// 5 物料类别
			, "6"// 6 剁位号
			, "7"// 7 单号
			, "8"// 8 单号
			, };

}
