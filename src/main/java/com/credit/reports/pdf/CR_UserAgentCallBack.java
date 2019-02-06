package com.credit.reports.pdf;

import org.xhtmlrenderer.pdf.ITextOutputDevice;
import org.xhtmlrenderer.pdf.ITextUserAgent;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CR_UserAgentCallBack extends ITextUserAgent {
    private final String authString = "flyingSaucer:flyingSaucerPassword";

    public CR_UserAgentCallBack(ITextOutputDevice outputDevice) {
        super(outputDevice);
    }

    protected InputStream resolveAndOpenStream(String reportURL) {
        InputStream inputStream = null;

        try {
            URL url = new URL(reportURL);
            String encoding = (new BASE64Encoder()).encode(authString.getBytes());
            URLConnection uc = url.openConnection();
            uc.setRequestProperty("Authorization", "Basic " + encoding);
            inputStream = uc.getInputStream();
        } catch (IOException var6) {
            Logger.getAnonymousLogger().log(Level.SEVERE, var6.getMessage());
        }

        return inputStream;
    }
}
