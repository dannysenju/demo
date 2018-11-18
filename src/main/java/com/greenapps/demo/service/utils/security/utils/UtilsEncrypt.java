package com.greenapps.demo.service.utils.security.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * SINGLETON QUE PERMITE UTILIZAR UN METODO PARA ENCRIPTACIÓN
 *
 * @author danny
 */
public class UtilsEncrypt {

    private static UtilsEncrypt instance = new UtilsEncrypt();

    public boolean comparePassword(String oldPass, String password) {

        char[] passwordBD = password.toCharArray();
        char[] passwordView = oldPass.toCharArray();

        boolean isEqual = true;

        if (passwordBD.length == passwordView.length) {
            for (int i = 0; i < passwordBD.length; i++) {
                if (passwordBD[i] != passwordView[i]) {
                    isEqual = false;
                    break;
                }
            }
        } else {
            isEqual = false;
        }
        return isEqual;

    }

    private UtilsEncrypt() {

    }

    public static UtilsEncrypt getInstance() {
        return instance;
    }

    public String encryptPassword(String pwd) {

        String seed = DigestUtils.sha512Hex(Constant.getInstance().getSEED());

        return DigestUtils.sha512Hex(seed.concat(pwd)); // encriptación con semilla y sha512
    }

    public static void main(String[] args) {

        UtilsEncrypt ue = UtilsEncrypt.getInstance();

        System.out.println(ue.encryptPassword("123"));

    }

}
