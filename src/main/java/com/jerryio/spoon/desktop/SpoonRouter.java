package com.jerryio.spoon.desktop;

import java.net.InetSocketAddress;
import java.security.GeneralSecurityException;

import com.jerryio.spoon.desktop.listeners.DesktopRouterListener;
import com.jerryio.spoon.kernal.router.RouterDevice;

public class SpoonRouter {
    private static SpoonRouter instance; // singleton

    public RouterDevice router;

    public static void build() throws GeneralSecurityException {
        if (instance != null)
            return;

        instance = new SpoonRouter();
    }

    public SpoonRouter() throws GeneralSecurityException {
        router = new RouterDevice(new InetSocketAddress("0.0.0.0", 7000), new DesktopRouterListener());
        router.start();
    }

    public static SpoonRouter getInstance() {
        return instance;
    }

}
