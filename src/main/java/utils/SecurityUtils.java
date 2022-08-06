package utils;
import java.util.Base64;

public class SecurityUtils {

    public static String encPass64(String pass){
        return Base64.getEncoder().encodeToString(pass.getBytes());
    }

    public static String decPass64(String pass){
        return new String(Base64.getDecoder().decode(pass));
    }

    public static String encPass228(String pass){
        return Encode228(pass);
    }

    public static String decPass228(String pass){
        return Decode228(pass);
    }


    /*private static String convertStringToHex(String str) {

        StringBuffer hex = new StringBuffer();

        // loop chars one by one
        for (char temp : str.toCharArray()) {

            // convert char to int, for char `a` decimal 97
            int decimal = (int) temp;

            // convert int to hex, for decimal 97 hex 61
            hex.append(Integer.toHexString(decimal));
        }
        hex.reverse();
        return hex.toString();

    }

    // Hex -> Decimal -> Char
    private static String convertHexToString(String hex) {

        StringBuilder result = new StringBuilder();

        // split into two chars per loop, hex, 0A, 0B, 0C...
        for (int i = 0; i < hex.length() - 1; i += 2) {

            String tempInHex = hex.substring(i, (i + 2));

            //convert hex to decimal
            int decimal = Integer.parseInt(tempInHex, 16);

            // convert the decimal to char
            result.append((char) decimal);

        }
        result.reverse();
        return result.toString();

    }

    private static String RotateChar(String arg) {
        StringBuilder result = new StringBuilder();
        StringBuilder tmpsB = new StringBuilder();
        for (int i = 0; i < arg.length() - 1; i += 2) {
            tmpsB.append(arg.substring(i, (i + 2))).reverse();
            result.append(tmpsB.toString());
            tmpsB.setLength(0);
        }
        return result.toString();
    }
    */
    private static String Encode228(String str) {
        StringBuilder result = new StringBuilder(str);
        StringBuilder tmpsB = new StringBuilder();

        int i, j;
        for (i = 1; i < result.length(); i++){

            for(j = 0;j < result.length();j=j+i+1){
                if(result.length() > j+i+1) {
                    tmpsB.append(new StringBuilder(result.substring(j, i + j + 1)).reverse());
                }else {
                    tmpsB.append(new StringBuilder(result.substring(j, j + (result.length() - j))).reverse());
                }
            }
            result.delete(0,result.length());
            result.append(tmpsB);
            tmpsB.delete(0,tmpsB.length());
        }
        return result.toString();
    }

    private static String Decode228(String str) {
        StringBuilder result = new StringBuilder(str);
        StringBuilder tmpsB = new StringBuilder();

        int i, j;
        for (i=result.length()-1;i > 0;i--){

            for(j = 0;j < result.length();j=j+i+1){
                if(result.length() > j+i+1) {
                    tmpsB.append(new StringBuilder(result.substring(j, i + j + 1)).reverse());
                }else {
                    tmpsB.append(new StringBuilder(result.substring(j, j + (result.length() - j))).reverse());
                }
            }
            result.delete(0,result.length());
            result.append(tmpsB);
            tmpsB.delete(0,tmpsB.length());
        }
        return result.toString();
    }

}
