package ca.bmskarate.util;

import java.security.MessageDigest;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class SecurityUtils {
    private static final Logger logger = Logger.getLogger(SecurityUtils.class.getName());

    public static String getMD5Hash(String input) {
        String md5Out = null;

        if(input==null)
            return null;

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] out = md.digest(input.getBytes("ISO-8859-1"));

            md5Out = bytesToHex(out);
        }
        catch(Exception e){
            //log the errors nothing else can be done here
            LogRecord lr = new LogRecord(Level.SEVERE, e.getMessage());
            logger.log(lr);
        }

        return md5Out;
    }

    private static String bytesToHex(byte[] data){

        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < data.length; i++){
            buf.append(byteToHex(data[i]));
        }
        return buf.toString();
    }



    private static String byteToHex(byte data){

        StringBuffer buf = new StringBuffer();
        buf.append(toHexChar((data>>>4)&0x0F));
        buf.append(toHexChar(data&0x0F));
        return buf.toString();

    }



    /**
     * Convenience method to convert an int to a hex char.
     *
     * @param i the int to convert
     * @return char the converted char
     */

    private static char toHexChar(int i){

        if ((0 <= i) && (i <= 9 ))
            return (char)('0' + i);

        else
            return (char)('a' + (i-10));
    }
}
