/**
 *    Copyright 2009-2021 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.cc.mybatis.common;

/**
 * @author chenchong
 * @create 2021/2/3 5:25 下午
 * @description
 */

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.security.InvalidParameterException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.util.BeanUtil;
import org.apache.commons.lang3.StringUtils;

public class ValidateUtil {
  private static final int[] POWER_LIST = new int[]{7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
  private static final int[] PARITY_BIT = new int[]{49, 48, 88, 57, 56, 55, 54, 53, 52, 51, 50};
  private static final String YEAR_PREFIX = "19";
  private static final Map<Integer, String> ZONE_NUM = new HashMap(100);
  private static final Pattern VIN_PATTERN = Pattern.compile("[A-HJ-NPR-Z0-9]{17}");
  private static final Pattern PLATE_NO_PATTERN = Pattern.compile("^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z][A-Z][A-Z0-9]{4,5}[A-Z0-9挂学警港澳]$");
  private static final Pattern OWNER_NAME = Pattern.compile("^[\\u4e00-\\u9fa5]{2,5}$");
  private static final Pattern COMPANY_NAME = Pattern.compile("^[\\u4e00-\\u9fa5]{3,}$");
  private static final Pattern COMPANY_PHONE = Pattern.compile("^0[0-9]{2,3}[1-9][0-9]{6,7}$");
  private static final Pattern BANK_CARD = Pattern.compile("^[0-9]{13,19}$");
  private static final Pattern MOBILE = Pattern.compile("^1[0-9]{10}$");
  private static final Map<Integer, Integer> VIN_MAP_WEIGHTING = new HashMap(20);
  private static final Map<Character, Integer> VIN_MAP_VALUE = new HashMap(20);
  private static final int[] POWER = new int[]{1, 3, 9, 27, 19, 26, 16, 17, 20, 29, 25, 13, 8, 24, 10, 30, 28};
  private static final char[] SOCIAL_CREDIT_CODE = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'T', 'U', 'W', 'X', 'Y'};

  private ValidateUtil() {
  }

  public static boolean isValidPlateNo(String plateNo) {
    return StringUtils.isNotBlank(plateNo) && PLATE_NO_PATTERN.matcher(plateNo).matches();
  }

  public static boolean isValidCertificateNo(String certNo) {
    boolean isInvalid = certNo == null || certNo.length() != 15 && certNo.length() != 18;
    if (isInvalid) {
      return false;
    } else {
      char[] cs = certNo.toUpperCase().toCharArray();
      int power = checksum(cs);
      if (certNo.length() == 18 && cs[cs.length - 1] != PARITY_BIT[power % 11]) {
        return false;
      } else if (!ZONE_NUM.containsKey(Integer.valueOf(certNo.substring(0, 2)))) {
        return false;
      } else {
        String year = certNo.length() == 15 ? "19" + certNo.substring(6, 8) : certNo.substring(6, 10);
        int iYear = Integer.parseInt(year);
        if (iYear >= 1900 && iYear <= Calendar.getInstance().get(1)) {
          String month = certNo.length() == 15 ? certNo.substring(8, 10) : certNo.substring(10, 12);
          int iMonth = Integer.parseInt(month);
          if (iMonth >= 1 && iMonth <= 12) {
            String day = certNo.length() == 15 ? certNo.substring(10, 12) : certNo.substring(12, 14);
            int iDay = Integer.parseInt(day);
            return iDay >= 1 && iDay <= 31;
          } else {
            return false;
          }
        } else {
          return false;
        }
      }
    }
  }

  private static int checksum(char[] cs) {
    int power = 0;

    for(int i = 0; i < cs.length - 1; ++i) {
      if (i < cs.length - 1) {
        power += (cs[i] - 48) * POWER_LIST[i];
      }
    }

    return power;
  }

  public static boolean isValidEngineNo(String engineNo) {
    return StringUtils.isNotBlank(engineNo) && engineNo.length() > 1 && engineNo.length() < 50 && !engineNo.contains("*");
  }

  public static boolean isValidVin(String vin) {
    if (StringUtils.isBlank(vin)) {
      return false;
    } else if (!VIN_PATTERN.matcher(vin).matches()) {
      return false;
    } else {
      char[] vinArr = vin.toCharArray();
      int amount = 0;

      int result;
      for(result = 0; result < vinArr.length; ++result) {
        amount += (Integer)VIN_MAP_VALUE.get(vinArr[result]) * (Integer)VIN_MAP_WEIGHTING.get(result + 1);
      }

      result = amount % 11;
      if (result == 10) {
        return vinArr[8] == 'X';
      } else {
        return result == (Integer)VIN_MAP_VALUE.get(vinArr[8]);
      }
    }
  }

  public static boolean isValidRegisterDate(Date registerDate) {
    return registerDate != null && registerDate.getTime() < System.currentTimeMillis();
  }

  public static void notNull(Object object, String description) {
    if (object == null) {
      description = StringUtils.isBlank(description) ? "参数" : description;
      throw new InvalidParameterException(description + "为空");
    }
  }

  public static void notBlank(CharSequence text, String description) {
    if (StringUtils.isBlank(text)) {
      description = StringUtils.isBlank(description) ? "参数" : description;
      throw new InvalidParameterException(description + "为空");
    }
  }

  public static boolean isValidCompanyCreditCode(String creditCode) {
    if (StringUtils.length(creditCode) != 18) {
      return false;
    } else {
      Map<String, Integer> datas = new HashMap(20);

      for(int i = 0; i < SOCIAL_CREDIT_CODE.length; ++i) {
        datas.put(SOCIAL_CREDIT_CODE[i] + "", i);
      }

      char[] chars = creditCode.substring(0, 17).toCharArray();
      int sum = 0;

      int i;
      for(i = 0; i < chars.length; ++i) {
        Integer code = (Integer)datas.get(chars[i] + "");
        if (code == null) {
          return false;
        }

        sum += POWER[i] * code;
      }

      i = sum % 31;
      i = i == 0 ? 31 : i;
      return creditCode.substring(17, 18).equals(SOCIAL_CREDIT_CODE[31 - i] + "");
    }
  }

  public static boolean isValidVehicleOwnerName(String name) {
    return StringUtils.isNotBlank(name) && OWNER_NAME.matcher(name).matches();
  }

  public static boolean isValidCompanyName(String companyName) {
    return StringUtils.isNotBlank(companyName) && COMPANY_NAME.matcher(companyName).matches();
  }

  public static boolean isValidBankCard(String bankCard) {
    return StringUtils.isNotBlank(bankCard) && BANK_CARD.matcher(bankCard).matches();
  }

  public static boolean isValidCompanyPhone(String companyPhone) {
    return StringUtils.isNotBlank(companyPhone) && COMPANY_PHONE.matcher(companyPhone).matches();
  }

  public static boolean isValidMobile(String mobile) {
    return StringUtils.isNotBlank(mobile) && MOBILE.matcher(mobile).matches();
  }

  static {
    ZONE_NUM.put(11, "北京");
    ZONE_NUM.put(12, "天津");
    ZONE_NUM.put(13, "河北");
    ZONE_NUM.put(14, "山西");
    ZONE_NUM.put(15, "内蒙古");
    ZONE_NUM.put(21, "辽宁");
    ZONE_NUM.put(22, "吉林");
    ZONE_NUM.put(23, "黑龙江");
    ZONE_NUM.put(31, "上海");
    ZONE_NUM.put(32, "江苏");
    ZONE_NUM.put(33, "浙江");
    ZONE_NUM.put(34, "安徽");
    ZONE_NUM.put(35, "福建");
    ZONE_NUM.put(36, "江西");
    ZONE_NUM.put(37, "山东");
    ZONE_NUM.put(41, "河南");
    ZONE_NUM.put(42, "湖北");
    ZONE_NUM.put(43, "湖南");
    ZONE_NUM.put(44, "广东");
    ZONE_NUM.put(45, "广西");
    ZONE_NUM.put(46, "海南");
    ZONE_NUM.put(50, "重庆");
    ZONE_NUM.put(51, "四川");
    ZONE_NUM.put(52, "贵州");
    ZONE_NUM.put(53, "云南");
    ZONE_NUM.put(54, "西藏");
    ZONE_NUM.put(61, "陕西");
    ZONE_NUM.put(62, "甘肃");
    ZONE_NUM.put(63, "青海");
    ZONE_NUM.put(64, "宁夏");
    ZONE_NUM.put(65, "新疆");
    ZONE_NUM.put(71, "台湾");
    ZONE_NUM.put(81, "香港");
    ZONE_NUM.put(82, "澳门");
    ZONE_NUM.put(91, "外国");
    VIN_MAP_WEIGHTING.put(1, 8);
    VIN_MAP_WEIGHTING.put(2, 7);
    VIN_MAP_WEIGHTING.put(3, 6);
    VIN_MAP_WEIGHTING.put(4, 5);
    VIN_MAP_WEIGHTING.put(5, 4);
    VIN_MAP_WEIGHTING.put(6, 3);
    VIN_MAP_WEIGHTING.put(7, 2);
    VIN_MAP_WEIGHTING.put(8, 10);
    VIN_MAP_WEIGHTING.put(9, 0);
    VIN_MAP_WEIGHTING.put(10, 9);
    VIN_MAP_WEIGHTING.put(11, 8);
    VIN_MAP_WEIGHTING.put(12, 7);
    VIN_MAP_WEIGHTING.put(13, 6);
    VIN_MAP_WEIGHTING.put(14, 5);
    VIN_MAP_WEIGHTING.put(15, 4);
    VIN_MAP_WEIGHTING.put(16, 3);
    VIN_MAP_WEIGHTING.put(17, 2);
    VIN_MAP_VALUE.put('0', 0);
    VIN_MAP_VALUE.put('1', 1);
    VIN_MAP_VALUE.put('2', 2);
    VIN_MAP_VALUE.put('3', 3);
    VIN_MAP_VALUE.put('4', 4);
    VIN_MAP_VALUE.put('5', 5);
    VIN_MAP_VALUE.put('6', 6);
    VIN_MAP_VALUE.put('7', 7);
    VIN_MAP_VALUE.put('8', 8);
    VIN_MAP_VALUE.put('9', 9);
    VIN_MAP_VALUE.put('A', 1);
    VIN_MAP_VALUE.put('B', 2);
    VIN_MAP_VALUE.put('C', 3);
    VIN_MAP_VALUE.put('D', 4);
    VIN_MAP_VALUE.put('E', 5);
    VIN_MAP_VALUE.put('F', 6);
    VIN_MAP_VALUE.put('G', 7);
    VIN_MAP_VALUE.put('H', 8);
    VIN_MAP_VALUE.put('J', 1);
    VIN_MAP_VALUE.put('K', 2);
    VIN_MAP_VALUE.put('M', 4);
    VIN_MAP_VALUE.put('L', 3);
    VIN_MAP_VALUE.put('N', 5);
    VIN_MAP_VALUE.put('P', 7);
    VIN_MAP_VALUE.put('R', 9);
    VIN_MAP_VALUE.put('S', 2);
    VIN_MAP_VALUE.put('T', 3);
    VIN_MAP_VALUE.put('U', 4);
    VIN_MAP_VALUE.put('V', 5);
    VIN_MAP_VALUE.put('W', 6);
    VIN_MAP_VALUE.put('X', 7);
    VIN_MAP_VALUE.put('Y', 8);
    VIN_MAP_VALUE.put('Z', 9);
  }
}
