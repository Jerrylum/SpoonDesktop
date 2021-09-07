package com.jerryio.spoon.desktop.listeners;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.jerryio.spoon.desktop.SpoonDesktop;
import com.jerryio.spoon.desktop.enums.ConnectionStatus;
import com.jerryio.spoon.desktop.utils.InputHelper;
import com.jerryio.spoon.desktop.view.MainFrame;
import com.jerryio.spoon.kernal.client.ClientListener;
import com.jerryio.spoon.kernal.event.EventHandler;
import com.jerryio.spoon.kernal.event.client.ClientErrorEvent;
import com.jerryio.spoon.kernal.event.client.ConnectionCloseEvent;
import com.jerryio.spoon.kernal.event.client.ConnectionOpenEvent;
import com.jerryio.spoon.kernal.network.protocol.general.SendTextPacket;
import com.jerryio.spoon.kernal.network.protocol.handshake.RequireEncryptionPacket;

public class DesktopClientListener extends ClientListener {

    @Override
    @EventHandler
    public void handleTextReceived(SendTextPacket packet) throws IOException {
        InputHelper.handle(packet.getMessage());
    }

    @Override
    @EventHandler
    public void lastMessageReceivedEvent() {
        MainFrame.getInstance().setInputText("");
    }

    @Override
    @EventHandler
    public void handleRequireEncryption(RequireEncryptionPacket packet) throws GeneralSecurityException, IOException {
        super.handleRequireEncryption(packet);
        SpoonDesktop.getInstance().setChannel(MainFrame.getInstance().getInputChannel());
        SpoonDesktop.getInstance().setConnectionStatus(ConnectionStatus.ClientConnected);
    }

    @Override
    @EventHandler
    public void onOpenEvent(ConnectionOpenEvent event) {
        SpoonDesktop.getInstance().setConnectionStatus(ConnectionStatus.ClientHandshake);
    }

    @Override
    @EventHandler
    public void onCloseEvent(ConnectionCloseEvent event) {
        SpoonDesktop.getInstance().setConnectionStatus(ConnectionStatus.ClientDisconnected);
    }

    @EventHandler
    public void onError(ClientErrorEvent event) {
        event.getException().printStackTrace();
    }

}
