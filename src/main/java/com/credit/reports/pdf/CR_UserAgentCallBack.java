//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.credit.reports.pdf;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xhtmlrenderer.pdf.ITextOutputDevice;
import org.xhtmlrenderer.pdf.ITextUserAgent;
import sun.misc.BASE64Encoder;

public class CR_UserAgentCallBack extends ITextUserAgent {
    private final String authString = "flyingSaucer:flyingSaucerPassword";

    public CR_UserAgentCallBack(ITextOutputDevice outputDevice) {
        super(outputDevice);
    }

    protected InputStream resolveAndOpenStream(String reportURL) {
        InputStream inputStream = null;

        try {
            URL url = new URL(reportURL);
            String encoding = (new BASE64Encoder()).encode("flyingSaucer:flyingSaucerPassword".getBytes());
            URLConnection uc = url.openConnection();
            uc.setRequestProperty("Authorization", "Basic " + encoding);
            inputStream = uc.getInputStream();
        } catch (MalformedURLException var6) {
            Logger.getAnonymousLogger().log(Level.SEVERE, var6.getMessage());
        } catch (FileNotFoundException var7) {
            Logger.getAnonymousLogger().log(Level.SEVERE, var7.getMessage());
        } catch (IOException var8) {
            Logger.getAnonymousLogger().log(Level.SEVERE, var8.getMessage());
        }

        return inputStream;
    }
}
