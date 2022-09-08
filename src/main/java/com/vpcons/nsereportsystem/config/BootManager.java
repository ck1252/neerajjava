package com.vpcons.nsereportsystem.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Component
public class BootManager {

    @Value("${server.port}")
    private String SERVER_PORT;

    @Value("${vpcons.system.shutdowm}")
    private boolean SHUTDOWN_FLAG;

    @Value("${vpcons.system.start}")
    private boolean START_FLAG;

    @Autowired
    private ApplicationContext appContext;

    /*
     * Invoke with `0` to indicate no error or different code to indicate
     * abnormal exit. es: shutdownManager.initiateShutdown(0);
     **/
    public void initiateShutdown(int returnCode){
        if(SHUTDOWN_FLAG)
            SpringApplication.exit(appContext, () -> returnCode);
    }

    @EventListener({ApplicationReadyEvent.class})
    void applicationReadyEvent() {
        // System.out.println("Application started ... launching browser now");
        if(START_FLAG)
            browse("http://localhost:"+SERVER_PORT+"/");
    }

    public static void browse(String url) {
        if(Desktop.isDesktopSupported()){
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(new URI(url));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }else{
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec("rundll32 url.dll,FileProtocolHandler " + url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
