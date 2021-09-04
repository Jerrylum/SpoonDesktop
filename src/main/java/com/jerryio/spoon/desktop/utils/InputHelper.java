package com.jerryio.spoon.desktop.utils;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;

import com.jerryio.spoon.desktop.SpoonDesktop;

public class InputHelper {

    private static Robot robot;
    private static Transferable originalContents;

    public static void handle(String text) {
        SpoonDesktop desktop = SpoonDesktop.getInstance();

        if (desktop.isClipboard()) {
            doClipboard(text);
            if (desktop.isTypingText()) {
                doPasteClipboard();
            }
        } else if (desktop.isTypingText()) {
            if (Util.IsWindows) {
                WindowsTypingHelper.handle(text);
            } else {
                doClipboardWithBackup(text);
                doPasteClipboard();
                robot.delay(50);
                doRestoreClipboard();
            }
        }
    }

    private static void doClipboard(String text) {
        StringSelection stringSelection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    private static void doClipboardWithBackup(String text) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        originalContents = clipboard.getContents(null);
        doClipboard(text);
    }

    private static void doRestoreClipboard() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(originalContents, null);
        originalContents = null;
    }

    private static void doPasteClipboard() {
        try {
            if (robot == null) {
                robot = new Robot();
                robot.delay(300);
            }

            if (Util.IsMac) {
                robot.keyPress(KeyEvent.VK_META);
                robot.keyPress(KeyEvent.VK_V);
                robot.keyRelease(KeyEvent.VK_V);
                robot.keyRelease(KeyEvent.VK_META);
            } else {
                robot.keyPress(KeyEvent.VK_SHIFT);
                robot.keyPress(KeyEvent.VK_INSERT);
                robot.keyRelease(KeyEvent.VK_INSERT);
                robot.keyRelease(KeyEvent.VK_SHIFT);
            }
        } catch (Exception e) {
        }
    }
}
