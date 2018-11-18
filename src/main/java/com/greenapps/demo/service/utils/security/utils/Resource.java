/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.utils.security.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author danny
 */
public class Resource {

    public static String getDataSourceName() throws IOException {
        InputStream is = Resource.class.getResourceAsStream("/META-INF/persistence.xml");

        InputStreamReader isreader = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isreader);
        String linea = br.readLine();
        String file = "";
        while (linea != null) {
            linea = br.readLine();
            file = file + linea;
        }
        file = file.substring(file.indexOf("<jta-data-source>"), file.indexOf("</jta-data-source>"));
        file = file.replace("<jta-data-source>", "");
        file = file.trim();
        return file;
    }

}
