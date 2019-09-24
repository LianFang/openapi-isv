package org.springblade.common.utils;

import java.security.SecureRandom;

/**
 * @version 1.0
 * @author：xy
 * @createTime：2019-09-23
 */

public class SmsCodeUtil {

	private SmsCodeUtil() {
	}

	/**
	 * 生成指定长度的数字验证码
	 *
	 * @param length
	 * @return String
	 */
	public static String generateNumCode(int length) {
		StringBuilder sb = new StringBuilder();
		SecureRandom random = new SecureRandom();
		for (int i = 0; i < length; i++) {
			sb.append(random.nextInt(10));
		}
		return sb.toString();
	}
}
