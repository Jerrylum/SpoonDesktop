package com.jerryio.spoon.desktop.listeners;

import java.util.Date;

import com.jerryio.spoon.kernal.event.EventHandler;
import com.jerryio.spoon.kernal.event.server.ClientConnectedEvent;
import com.jerryio.spoon.kernal.event.server.ClientDisconnectedEvent;
import com.jerryio.spoon.kernal.router.RouterListener;

public class DesktopRouterListener extends RouterListener {

    @Override
    @EventHandler
    public void onClientConnectedEvent(ClientConnectedEvent event) {
        System.out.println(new Date() + "> Client connected #" + event.getDevice().getId() + " total: "
                + this.getRouter().getDevices().size());
    }

    @Override
    @EventHandler
    public void onClientDisconnectedEvent(ClientDisconnectedEvent event) {
        System.out.println(new Date() + "> Client disconnected #" + event.getDevice().getId() + " total: "
                + this.getRouter().getDevices().size());
    }

}
