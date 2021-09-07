package com.jerryio.spoon.desktop;

import java.net.InetSocketAddress;
import java.net.URI;
import java.security.interfaces.RSAPublicKey;
import java.util.Timer;
import java.util.TimerTask;

import javax.crypto.SecretKey;

import com.jerryio.spoon.desktop.enums.ConnectionMode;
import com.jerryio.spoon.desktop.enums.ConnectionStatus;
import com.jerryio.spoon.desktop.listeners.DesktopClientListener;
import com.jerryio.spoon.desktop.listeners.DesktopServerListener;
import com.jerryio.spoon.desktop.utils.Util;
import com.jerryio.spoon.desktop.view.MainFrame;
import com.jerryio.spoon.kernal.client.ClientDevice;
import com.jerryio.spoon.kernal.network.security.EncryptionManager;
import com.jerryio.spoon.kernal.server.RemoteDevice;
import com.jerryio.spoon.kernal.server.ServerDevice;

public class SpoonDesktop {

    private static SpoonDesktop instance; // singleton

    private ConnectionMode mode = ConnectionMode.CLIENT;
    private ConnectionStatus connectionStatus = ConnectionStatus.ClientReadyToConnect;
    private boolean isAllowNewConnection = true;
    private boolean isPrimarySelection;
    private boolean isClipboard;
    private boolean isTypingText = true; // IMPORTANT: init sync with MainFrame

    public ServerDevice server;
    public ClientDevice client;

    public static void build() {
        if (instance != null)
            return;

        instance = new SpoonDesktop();

        MainFrame.build();
    }

    public SpoonDesktop() {
        MainFrame.build();
        doStartConnectionWatchdog();
    }

    public ConnectionMode getMode() {
        return this.mode;
    }

    public void setMode(ConnectionMode mode) {
        this.mode = mode;

        doServerStop();
        doClientDisconnect();

        if (getMode() == ConnectionMode.CLIENT)
            connectionStatus = ConnectionStatus.ClientReadyToConnect;
        else
            connectionStatus = ConnectionStatus.ServerReadyToStart;

        MainFrame.getInstance().updateInterface();
    }

    public String[] getProvidedAddress() {
        if (getMode() == ConnectionMode.CLIENT)
            return new String[] { "spoon-router1.jerryio.com", "192.168.0.2" };
        else
            return new String[] { "0.0.0.0:7000" };

    }

    public ConnectionStatus getConnectionStatus() {
        return this.connectionStatus;
    }

    public void setConnectionStatus(ConnectionStatus connectionStatus) {
        this.connectionStatus = connectionStatus;
        MainFrame.getInstance().updateInterface();
    }

    public String getChannel() {
        if (client != null)
            return client.getChannel();
        return null;
    }

    public void setChannel(String channel) {
        try {
            if (client != null && !client.getChannel().equals(channel)) {
                client.setChannel(channel);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        MainFrame.getInstance().updateInterface();
    }

    public boolean isAllowNewConnection() {
        return this.isAllowNewConnection;
    }

    public void setIsAllowNewConnection(boolean isAllowNewConnection) {
        this.isAllowNewConnection = isAllowNewConnection;
        MainFrame.getInstance().updateInterface();
    }

    public int getClientsCount() {
        if (server != null)
            return server.getDevices().size();
        return 0;
    }

    public boolean isPrimarySelection() {
        return this.isPrimarySelection;
    }

    public void setIsPrimarySelection(boolean isPrimarySelection) {
        this.isPrimarySelection = isPrimarySelection;
        MainFrame.getInstance().updateInterface();
    }

    public boolean isClipboard() {
        return this.isClipboard;
    }

    public void setIsClipboard(boolean isClipboard) {
        this.isClipboard = isClipboard;
        MainFrame.getInstance().updateInterface();
    }

    public boolean isTypingText() {
        return this.isTypingText;
    }

    public void setIsTypingText(boolean isTypingText) {
        this.isTypingText = isTypingText;
        MainFrame.getInstance().updateInterface();
    }

    public void kickAllClients() {
        try {
            if (server != null) {
                for (RemoteDevice device : server.getDevices()) {
                    device.getConnection().getWebSocket().close();
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        MainFrame.getInstance().updateInterface();
    }

    public void sendMessage(String msg) {
        try {
            if (server != null)
                server.sendTextMessage(msg);
            else if (client != null)
                client.sendTextMessage(msg);
        } catch (Exception e) {
            // TODO: handle exception
        }
        MainFrame.getInstance().updateInterface();
    }

    public void executeAction() {
        switch (getConnectionStatus()) {
            case ClientReadyToConnect:
            case ClientConnectionFailed:
            case ClientDisconnected:
                doClientConnect();
                break;
            case ClientConnecting:
            case ClientHandshake:
            case ClientConnected:
                doClientDisconnect();
                break;
            case ServerReadyToStart:
            case ServerStartFailed:
            case ServerStopped:
                doServerStart();
                break;
            case ServerStarting:
            case ServerRunning:
                doServerStop();
                break;
            default:
                break;
        }

        MainFrame.getInstance().updateInterface();
    }

    public byte[] getSecurityCode() {
        RSAPublicKey a = null;
        SecretKey b = null;
        if (client != null) {
            EncryptionManager manager = client.getConnection().getEncryption();
            a = manager.getRSAPublicKey();
            b = manager.getAESSecretKey();
        } else if (server != null) {
            a = (RSAPublicKey) server.getKey().getPublic();

            RemoteDevice target = null;
            for (RemoteDevice device : server.getDevices())
                if (target == null || device.getId() > target.getId())
                    target = device;

            if (target == null)
                return null;
            b = target.getConnection().getEncryption().getAESSecretKey();
        }
        try {
            if (a != null && b != null)
                return Util.getMD5Checksum(a.getEncoded(), b.getEncoded());
        } catch (Exception e) {
        }
        return null;
    }

    private void doClientConnect() {
        try {
            String ip = MainFrame.getInstance().getInputAddress();
            if (!ip.contains(":"))
                ip += ":7000";

            ip = "ws://" + ip;

            client = new ClientDevice(new URI(ip), new DesktopClientListener());
            connectionStatus = ConnectionStatus.ClientConnecting;
        } catch (Exception e) {
            doClientDisconnect();
            connectionStatus = ConnectionStatus.ClientConnectionFailed;
        }
    }

    private void doClientDisconnect() {
        try {
            client.getConnection().close();
            connectionStatus = ConnectionStatus.ClientDisconnected;
        } catch (Exception e) {
            // TODO: maybe?
        }
        client = null;
    }

    private void doServerStart() {
        try {
            String ip = MainFrame.getInstance().getInputAddress();
            String address = "0.0.0.0";
            int port = 7000;

            if (ip.contains(":")) {
                String[] s = ip.split(":");
                address = s[0];
                port = Integer.parseInt(s[1]);
            } else if (!ip.contains(".")) {
                port = Integer.parseInt(ip);
            } else {
                address = ip;
            }

            server = new ServerDevice(new InetSocketAddress(address, port), new DesktopServerListener());
            server.setReuseAddr(true);
            server.start();
            connectionStatus = ConnectionStatus.ServerRunning; // XXX: should be ServerStarting
        } catch (Exception e) {
            doServerStop();
            connectionStatus = ConnectionStatus.ServerStartFailed;
        }
    }

    private void doServerStop() {
        try {
            server.stop(1000);
            connectionStatus = ConnectionStatus.ServerStopped;
        } catch (Exception e) {
            // TODO: maybe?
        }
        server = null;
    }

    private void doStartConnectionWatchdog() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SpoonDesktop desktop = SpoonDesktop.getInstance();

                if (desktop.client == null || desktop.getConnectionStatus() == ConnectionStatus.ClientConnecting
                        || desktop.client.getConnection().isConnected())
                    return;

                try {
                    System.out.println("Watchdog triggered");
                    desktop.client.getConnection().close();
                } catch (Exception e) {
                    System.out.println("Watchdog error");
                } finally {
                    desktop.client = null;
                    desktop.setConnectionStatus(ConnectionStatus.ClientDisconnected);
                }
            }
        }, 0, 500);
    }

    public static SpoonDesktop getInstance() {
        return instance;
    }

}
