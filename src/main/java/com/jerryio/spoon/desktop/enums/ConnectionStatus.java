package com.jerryio.spoon.desktop.enums;

public enum ConnectionStatus {
    ClientReadyToConnect, // Connect
    ClientConnecting, // Connecting
    ClientConnectionFailed, // Failed
    ClientHandshake, // Handshake...
    ClientConnected, // Connected
    ClientDisconnected, // Disconnected
    ServerReadyToStart, // Start
    ServerStarting, // Starting
    ServerStartFailed, // Failed
    ServerRunning, // Running
    ServerStopped // Stopped
}
