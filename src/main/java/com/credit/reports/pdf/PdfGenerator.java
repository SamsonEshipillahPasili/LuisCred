package com.credit.reports.pdf;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.net.URLConnection;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.resource.XMLResource;
import org.xml.sax.InputSource;
import sun.misc.BASE64Encoder;

public class PdfGenerator {

    public static byte[] generatePDF(String url) throws Exception {
        ByteArrayOutputStream pdfByteArrayOutputStream = new ByteArrayOutputStream();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(pdfByteArrayOutputStream);
        ITextRenderer renderer = new ITextRenderer();

        CR_UserAgentCallBack userAgentCallBack = new CR_UserAgentCallBack(renderer.getOutputDevice());
        userAgentCallBack.setSharedContext(renderer.getSharedContext());
        renderer.getSharedContext().setUserAgentCallback(userAgentCallBack);
        URL inputSourceURL = new URL(url);
        String encoding = (new BASE64Encoder()).encode("flyingSaucer:flyingSaucerPassword".getBytes());
        URLConnection uc = inputSourceURL.openConnection();
        uc.setRequestProperty("Authorization", "Basic " + encoding);
        InputSource inputSource = new InputSource(new BufferedInputStream(uc.getInputStream()));

        Document document = XMLResource.load(inputSource).getDocument();
        renderer.setDocument(document, url);
        renderer.layout();
        renderer.createPDF(bufferedOutputStream, true, 1);
        return pdfByteArrayOutputStream.toByteArray();
    }
}
