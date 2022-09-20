package org.octobot.bot.handler;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * TextHandler
 *
 * @author Pat-ji
 */
public class TextHandler {

    public static String encode(final String text) {
        try {
            final byte[] data = text.getBytes();
            for (int i = 0; i < data.length; i++)
                data[i] += 0xF1;

            final String replace = new String(data);
            final DESKeySpec keySpec = new DESKeySpec("1029384756".getBytes("UTF8"));
            final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            final SecretKey key = keyFactory.generateSecret(keySpec);
            final BASE64Encoder base64encoder = new BASE64Encoder();
            final Cipher cipher = Cipher.getInstance("DES");

            cipher.init(Cipher.ENCRYPT_MODE, key);
            return base64encoder.encode(cipher.doFinal(replace.getBytes("UTF8")));
        } catch (final Exception ignored) {}

        return "";
    }

    public static String decode(final String text) {
        try {
            final DESKeySpec keySpec = new DESKeySpec("1029384756".getBytes("UTF8"));
            final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            final SecretKey key = keyFactory.generateSecret(keySpec);
            final BASE64Decoder decoder = new BASE64Decoder();
            final Cipher cipher = Cipher.getInstance("DES");

            cipher.init(Cipher.DECRYPT_MODE, key);
            final byte[] data = cipher.doFinal(decoder.decodeBuffer(text));
            for (int i = 0; i < data.length; i++)
                data[i] -= 0xF1;

            return new String(data);
        } catch (final Exception ignored) {}

        return "";
    }

}
