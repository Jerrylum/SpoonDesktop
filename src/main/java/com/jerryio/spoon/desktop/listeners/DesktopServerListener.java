package com.jerryio.spoon.desktop.listeners;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.jerryio.spoon.desktop.SpoonDesktop;
import com.jerryio.spoon.desktop.enums.ConnectionStatus;
import com.jerryio.spoon.desktop.utils.InputHelper;
import com.jerryio.spoon.desktop.view.MainFrame;
import com.jerryio.spoon.kernal.event.EventHandler;
import com.jerryio.spoon.kernal.event.server.ClientConnectedEvent;
import com.jerryio.spoon.kernal.event.server.ClientDisconnectedEvent;
import com.jerryio.spoon.kernal.event.server.ServerErrorEvent;
import com.jerryio.spoon.kernal.network.protocol.general.SendTextPacket;
import com.jerryio.spoon.kernal.network.protocol.handshake.EncryptionBeginPacket;
import com.jerryio.spoon.kernal.server.RemoteDevice;
import com.jerryio.spoon.kernal.server.ServerListener;

public class DesktopServerListener extends ServerListener {

    @Override
    @EventHandler
    public void handleTextReceived(RemoteDevice device, SendTextPacket packet) throws IOException {
        InputHelper.handle(packet.getMessage());
    }

    @Override
    @EventHandler
    public void handleClientAESkey(RemoteDevice device, EncryptionBeginPacket packet) throws GeneralSecurityException {
        super.handleClientAESkey(device, packet);
        MainFrame.getInstace().updateInterface();
    }

    @Override
    @EventHandler
    public void onClientConnectedEvent(ClientConnectedEvent event) {
        if (!SpoonDesktop.getInstance().isAllowNewConnection())
            event.getDevice().getConnection().getWebSocket().close();

        MainFrame.getInstace().updateInterface();
    }

    @Override
    @EventHandler
    public void onClientDisconnectedEvent(ClientDisconnectedEvent event) {
        MainFrame.getInstace().updateInterface();
    }

    @EventHandler
    public void onError(ServerErrorEvent event) {
        if (!event.isSocketProblem()) {
            SpoonDesktop.getInstance().server = null;
            SpoonDesktop.getInstance().setConnectionStatus(ConnectionStatus.ServerStopped);
        }
    }

}
