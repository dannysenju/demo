package com.greenapps.demo.web.utils.emails;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * singleton
 *
 * @author danny
 */
public class JsfUtilities {

    private static JsfUtilities instance = new JsfUtilities();

    private JsfUtilities() {

    }

    public static JsfUtilities getInstance() {
        return instance;
    }

    public String getCode() {
        return RandomStringUtils.randomAlphanumeric(17);
    }

}
