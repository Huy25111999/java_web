package vn.m2m.utils;

import me.prettyprint.cassandra.utils.TimeUUIDUtils;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    protected static final char[] HEX_CHARS = { '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    protected static final char[] SECURE_CHARS = { 'a', 'b', 'c', 'd', 'e',
            'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
            's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E',
            'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
            'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4',
            '5', '6', '7', '8', '9' };

    public static String blank ="empty";
    public static UUID getTimeUUID() {
        return TimeUUIDUtils.getUniqueTimeUUIDinMillis();
    }

    public static String getTimeUUIDString() {
        return new String(bytesToHex(getTimeUUIDByteArray()));
    }

    public static byte[] getTimeUUIDByteArray() {
        return TimeUUIDUtils.asByteArray(TimeUUIDUtils
                .getUniqueTimeUUIDinMillis());
    }

    public static String phoneInternalRegex = "(\\+[1-9]\\d{0,2}|0)\\d{9,11}"; // (+countrycode|0)(phone)
    public static String phoneVNRegex = "(\\+84|0)\\d{9,10}"; // (+countrycode|0)(phone)

    public static String byteArrayIdToStringId(byte[] bytes) {
        return new String(bytesToHex(bytes));
    }

    public static byte[] stringIdToByteArrayId(String id) {
        if (!StringUtils.isEmpty(id)) {
            return hexToBytes(id);
        }
        return null;
    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHexString(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static char[] bytesToHex(byte[] raw) {
        if (raw != null) {
            int length = raw.length;
            char[] hex = new char[length * 2];
            for (int i = 0; i < length; i++) {
                int value = (raw[i] + 256) % 256;
                int highIndex = value >> 4;
                int lowIndex = value & 0x0f;
                hex[i * 2 + 0] = HEX_CHARS[highIndex];
                hex[i * 2 + 1] = HEX_CHARS[lowIndex];
            }
            return hex;
        } else
            return "".toCharArray();
    }

    public static byte[] hexToBytes(char[] hex) {
        int length = hex.length / 2;
        byte[] raw = new byte[length];
        for (int i = 0; i < length; i++) {
            int high = Character.digit(hex[i * 2], 16);
            int low = Character.digit(hex[i * 2 + 1], 16);
            int value = (high << 4) | low;
            if (value > 127)
                value -= 256;
            raw[i] = (byte) value;
        }
        return raw;
    }

    public static byte[] hexToBytes(String hex) {
        return hexToBytes(hex.toCharArray());
    }

    public static String toMD5(String original) {
        String destination = "";
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(original.getBytes());
            BigInteger dis = new BigInteger(1, md5.digest());
            destination = dis.toString(16);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return destination;
    }

    public static boolean validateEmail(String email) {
        // String regex =
        // "^[_A-Za-z0-9-\\.]+[_A-Za-z0-9-\\.]+@[A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        String regex = "^[^\\s\\t@]+@[A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return email.matches(regex);
    }

    public static boolean validatePhone(String phone) {
        String regex = "[1-9][0-9]{10,14}";
        return phone.matches(regex);
    }

    public static boolean validatePhoneVn(String phone) {
        String regex = "^0\\d{9,10}$";
        return phone.matches(regex);
    }

    public static boolean validatePhoneVnNew(String phone) {
        String regex = "[0-9]{9,11}";
        return phone.matches(regex);
    }

    public static boolean validateStrongPass(String password) {
        // String regex =
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        return password.matches(regex);
    }

    public static int length(String str){
        if(str==null|| "".equals(str)){
            return 0;
        }
        return str.length();
    }
    public static long generateRandom(int length) {
        Random random = new Random();
        char[] digits = new char[length];
        digits[0] = (char) (random.nextInt(9) + '1');
        for (int i = 1; i < length; i++) {
            digits[i] = (char) (random.nextInt(10) + '0');
        }
        return Long.parseLong(new String(digits));
    }

    public static List sortMapByValue(final Map m, boolean asc) {
        List keys = new ArrayList();
        keys.addAll(m.keySet());
        if (asc == true) {
            Collections.sort(keys, new Comparator() {
                public int compare(Object o1, Object o2) {
                    Object v1 = m.get(o1);
                    Object v2 = m.get(o2);
                    if (v1 == null) {
                        return (v2 == null) ? 0 : 1;
                    } else if (v1 instanceof Comparable) {
                        return ((Comparable) v1).compareTo(v2);
                    } else {
                        return 0;
                    }
                }
            });
        } else {
            Collections.sort(keys, new Comparator() {
                public int compare(Object o1, Object o2) {
                    Object v1 = m.get(o1);
                    Object v2 = m.get(o2);
                    if (v2 == null) {
                        return (v1 == null) ? 0 : 1;
                    } else if (v2 instanceof Comparable) {
                        return ((Comparable) v2).compareTo(v1);
                    } else {
                        return 0;
                    }
                }
            });
        }
        return keys;
    }

    public static List sortMapByValue(final Map m) {
        List keys = new ArrayList();
        keys.addAll(m.keySet());

        Collections.sort(keys, new Comparator() {
            public int compare(Object o1, Object o2) {
                Object v1 = m.get(o1);
                Object v2 = m.get(o2);
                if (v1 == null) {
                    return (v2 == null) ? 0 : 1;
                } else if (v1 instanceof Comparable) {
                    return ((Comparable) v1).compareTo(v2);
                } else {
                    return 0;
                }
            }
        });

        return keys;
    }

    //return km
    public static double distance(double lat1, double lon1, double lat2,
                                  double lon2) {
        if (lat1==lat2&&lon1==lon2) return 0;
        double theta = lon1 - lon2;

        double radlat1=deg2rad(lat1);
        double radlat2=deg2rad(lat2);
        double radtheta=deg2rad(theta);


        double dist = Math.sin(radlat1) * Math.sin(radlat2)
                + Math.cos(radlat1) * Math.cos(radlat2)
                * Math.cos(radtheta);

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;
        return (dist);
    }

    public static boolean nearbyPlace(String placeLat, String placeLon,
                                      String pointlat, String pointLon, String radius) {

        try {
            double placeLatDouble = Double.parseDouble(placeLat);
            double placeLonDouble = Double.parseDouble(placeLon);
            double pointLatDouble = Double.parseDouble(pointlat);
            double pointLonDouble = Double.parseDouble(pointLon);
            double radiusDouble = Double.parseDouble(radius);

            double distance = distance(placeLatDouble, placeLonDouble,
                    pointLatDouble, pointLonDouble);

            if (distance > radiusDouble) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean nearbyPlace(String pointlat, String pointLon,
                                      String placeLat, String placeLon, String deltaLat, String deltaLon) {

        try {
            double placeLatDouble = Double.parseDouble(placeLat);
            double placeLonDouble = Double.parseDouble(placeLon);
            double pointLatDouble = Double.parseDouble(pointlat);
            double pointLonDouble = Double.parseDouble(pointLon);
            double deltaLatDouble = Double.parseDouble(deltaLat);
            double deltaLonDouble = Double.parseDouble(deltaLon);

            double distanceLat = distance(placeLatDouble, pointLonDouble,
                    pointLatDouble, pointLonDouble);
            if (distanceLat > deltaLatDouble) {
                return false;
            }

            double distanceLon = distance(pointLatDouble, placeLonDouble,
                    pointLatDouble, pointLonDouble);
            if (distanceLon > deltaLonDouble) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static String getDiagonal(String strSide1, String strSide2) {
        try {
            double side1 = Double.parseDouble(strSide1);
            double side2 = Double.parseDouble(strSide2);

            double radiusDouble = Math.sqrt(side1 * side1 + side2 * side2);

            return String.valueOf(radiusDouble);

        } catch (Exception e) {
            return "";
        }
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


    public static Integer versionFullCompare(String fullversion1, String fullversion2){
        String version1 = splitVersionNumber(fullversion1);
        String version2 = splitVersionNumber(fullversion2);
        return versionCompare(version1, version2);
    }

    public static String splitVersionNumber(String fullversion){
        // fullversion: MKID_01_V1.7.0.150909_Unlock
        // return: 1.7.0.150909
        String version ="";
        Pattern pattern = Pattern.compile("([V|v][\\d|\\.]+)");
        Matcher matcher = pattern.matcher(fullversion);

        if (matcher.find())
        {
            version = matcher.group(0);
            version = version.replaceFirst("[V|v]", "");
        }
        return version;
    }

    public static Integer versionCompare(String version1, String version2) {
        //version example: 1.7.0.150909
        String[] vals1 = version1.split("\\.");
        String[] vals2 = version2.split("\\.");
        int i = 0;
        // set index to first non-equal ordinal or length of shortest version string
        while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i]))
        {
            i++;
        }
        // compare first non-equal ordinal number
        if (i < vals1.length && i < vals2.length)
        {
            int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
            return Integer.signum(diff);
        }
        // the strings are equal or one string is a substring of the other
        // e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
        else
        {
            return Integer.signum(vals1.length - vals2.length);
        }
    }


    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        } catch (Exception e){
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    public static boolean isExistComma(String s){
        if(s==null){
            return true;
        }
        int i = s.indexOf(",");
        if(i<0){
            return false;
        }

        return true;
    }

    public static int randomNumber(){
        Random random = new Random();
        int rand = 0;
        while(rand==0){
            rand=random.nextInt(Integer.MAX_VALUE);
        }

        return rand;
    }

    public static int randomNumber(int min, int max){
        Random random = new Random();
        int rand = random.nextInt(max-min+1) + min;
        return rand;
    }

    public static List<String> splitCommand(String s)
    {
        List<String> cmds=new ArrayList<String>();
        int i=0;
        while (i<s.length()){
            if (s.charAt(i)=='['){
                i++;
                String temp="";
                while ((i<s.length())&&(s.charAt(i)!=']')){
                    temp+=s.charAt(i);
                    i++;
                }

                if ((i<s.length())&&(s.charAt(i)==']')){
                    cmds.add(temp);
                }
            }
            i++;
        }
        return cmds;
    }

    public static List<String> parserPatternDoubleQuota(String s, String quotaStart, String quotaEnd){
        List<String> list = new ArrayList<String>();

        String sPatterNotSelfie  = String.format("[^\\%s|\\%s]+",quotaStart, quotaEnd);
        String sPatternSplit = String.format("(\\%s%s\\%s)", quotaStart,sPatterNotSelfie, quotaEnd);

        Pattern pattern = Pattern.compile(sPatternSplit);
        Matcher matcher = pattern.matcher(s);
        while (matcher.find()) {
            list.add(matcher.group(1));
        }
        return list;
    }

    public static String hashPassword(String password){
        String passwordHash = "";
        try {
            passwordHash = PasswordHash.createHash(password);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return passwordHash;
    }

    public static boolean isValidatePassword(String password,String encryptPassword){
        boolean isValidatePassword = false;
        try {
            isValidatePassword = PasswordHash.validatePassword(password, encryptPassword);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return isValidatePassword;
    }

    public static String removeLastChar(String str) {
        if(str!=null && !"".equals(str) && str.length()>1) {
            return str.substring(0, str.length() - 1);
        }
        else {
            return str;
        }
    }

    //input: cc 07 2b 02 01 dc 6d 9b 5d 99 fa 41 5f 11 88 1d 00 64 3b 58 be 39 2e 45 03 4a 56 7f 05 ef 77 76
    public static byte[] hexStringToByteArray(String s) {
        String input = s.replaceAll("\\s","");
        int len = input.length();
        if (len % 2 == 0) {
            byte[] data = new byte[len / 2];
            for (int i = 0; i < len; i += 2) {
                data[i / 2] = (byte) ((Character.digit(input.charAt(i), 16) << 4)
                        + Character.digit(input.charAt(i+1), 16));
            }
            return data;
        } else {
            return null;
        }
    }

    public static String leadingZeros(String s, int length) {
        if (s.length() >= length) return s;
        else return String.format("%0" + (length-s.length()) + "d%s", 0, s);
    }

    public static byte[] byte64Encoded(String str){
        return byte64Encoded(str.getBytes());
    }

    public static byte[] byte64Decoded(String str){
        return byte64Decoded(str.getBytes());
    }

    public static byte[] byte64Encoded(byte[] data){
        byte[] bytesEncoded = Base64.getEncoder().encode(data);
        return bytesEncoded;
    }

    public static byte[] byte64Decoded(byte[] data){
        byte[] valueDecoded = Base64.getDecoder().decode(data);
        return valueDecoded;
    }
}
