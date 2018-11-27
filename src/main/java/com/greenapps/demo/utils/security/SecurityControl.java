/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.greenapps.demo.utils.security;

import com.greenapps.demo.service.utils.security.utils.Constant;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Singleton for security control
 *
 * @author danny
 */
public class SecurityControl {

    private static SecurityControl instance = new SecurityControl();

    private SecurityControl() {

    }

    public static SecurityControl getInstance() {
        return instance;
    }

    private String encriptKey(String key) {
        return DigestUtils.sha512Hex(DigestUtils.sha512Hex(Constant.getInstance().getSEED()) + key);
    }

}
