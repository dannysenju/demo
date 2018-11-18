package com.greenapps.demo.service.utils.security.login;

import com.greenapps.demo.service.utils.security.utils.Constant;
import java.util.HashMap;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;

/**
 *
 * @author danny
 */
public class JaasLoginConfig extends Configuration {

    @Override
    public AppConfigurationEntry[] getAppConfigurationEntry(String name) {

        if (name.equals(Constant.getInstance().getJAAS_DB())) {
            AppConfigurationEntry[] configArr = new AppConfigurationEntry[1];
            configArr[0] = new AppConfigurationEntry(Constant.getInstance().getDB_LOGIN_MODULE_CLASS(),
                    AppConfigurationEntry.LoginModuleControlFlag.REQUIRED, new HashMap<String, Object>());
            return configArr;
        }

        throw new RuntimeException("Se esperaba app: [" + Constant.getInstance().getJAAS_DB() + " ] pero llego: " + name);
    }

}
