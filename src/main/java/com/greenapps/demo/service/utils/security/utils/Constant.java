/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.greenapps.demo.service.utils.security.utils;

/**
 *
 * @author danny
 */
public class Constant {

    public static Constant instance = new Constant();

    private Constant() {

    }

    public static Constant getInstance() {
        return instance;
    }

    private static final String SEED = "ChU13T4!d3CeRD0!"; // ESTE SEED ES POSIBLE MODIFICARLO
    private static final String JNDI = "java:/";
    private static final String QUERY_USERNAME = "SELECT idusuario, activo FROM usuario WHERE username = ?";
    private static final String QUERY_PASS = "SELECT password FROM usuario WHERE username = ? AND activo = 1";
    private static final String JAAS_DB = "DataBase";
    private static final String USER_ADMIN = "sigre";
    private static final String DB_LOGIN_MODULE_CLASS = "com.greenapps.demo.service.utils.security.login.DataBaseLoginModule";
    private static final String AUTHENTICATION_TYPE = "BD";
    private static final String[] LOGIN_COM_EXCEPTIONS = {"FALLO DE AUNTENTICACION", "ERRORLOGINADMIN", "ATTEMPTSEXCEEDED", "INACTIVEUSER", "EXPIREDUSER", "NOTALLOWEDDATE", "NOTALLOWEDSCHEDULE"};
    private static final String EXPIRED_PASSWORD = "EXPIREDPASSWORD";
    public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    public static final String MSG_ERROR_EMAIL = "Formato de email incorrecto. Ej:(demo@mail.com)";
    public static final String MSG_ERROR_USERNAME = "Ya existe el usuario en el sistema";
    public static final String MSG_ERROR_USERNAME_EXIST = "No existe el usuario en el sistema";
    public static final String MGS_ERROR_PASS = "No coincide la contraseña de confirmación";
    public static final String MSG_ERROR_VAL_EMAIL = "Verifique su email";
    public static final String MSG_ERROR_VAL_USERNAME = "Cambie el nombre de usuario";
    public static final String MSG_ERROR_VAL_PASS = "Deben coincidir los dos campos";
    public static final String HOST_NAME = "smtp.googlemail.com";
    public static final int SMT_PORT = 465;
    public static final String USERNAME_EMAIL = "demosofttrons@gmail.com";
    public static final String NAME_MAIL = "demosofttrons";
    private static final String PASSWORD_EMAIL = "demosoft123!";
    public static final String FIELD_PASS = "password";
    public static final String FIELD_ACTIVATE = "activo";
    public static final String VALUE_FIELD_INACTIVATE = "0";
    public static final String TYPE_LOGIN_BD = "BD";
    public static final String ICON_DEFAULT = "fe fe-external-link";
    public static final String NOT_LOGGED_USER = "NologgedUser";
    public static final String LOCKED_USER = "Usuario Bloqueado";
    

    
    public String getSEED() {
        return SEED;
    }

    public String getJNDI() {
        return JNDI;
    }

    public String getQUERY_USERNAME() {
        return QUERY_USERNAME;
    }

    public String getQUERY_PASS() {
        return QUERY_PASS;
    }

    public String getJAAS_DB() {
        return JAAS_DB;
    }

    public String getDB_LOGIN_MODULE_CLASS() {
        return DB_LOGIN_MODULE_CLASS;
    }

    public String getUSER_ADMIN() {
        return USER_ADMIN;
    }

    public String getAUTHENTICATION_TYPE() {
        return AUTHENTICATION_TYPE;
    }

    public String[] getLOGIN_COM_EXCEPTIONS() {
        return LOGIN_COM_EXCEPTIONS;
    }

    public String getEXPIRED_PASSWORD() {
        return EXPIRED_PASSWORD;
    }

    public String getPASSWORD_EMAIL() {
        return PASSWORD_EMAIL;
    }

}
