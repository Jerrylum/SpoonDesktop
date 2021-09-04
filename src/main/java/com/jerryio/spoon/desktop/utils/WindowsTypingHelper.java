package com.jerryio.spoon.desktop.utils;

import com.sun.jna.platform.win32.BaseTSD;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;

public class WindowsTypingHelper {
    public static void handle(String target) {
        char[] targetCharArray = target.toCharArray();

        for (int i = 0; i < targetCharArray.length; i++) {
            char c = targetCharArray[i];

            doInput(c, false);

            doInput(c, true);
        }
    }

    private static void doInput(char c, boolean press) {
        // Prepare input reference
        WinUser.INPUT input = new WinUser.INPUT();

        input.type = new WinDef.DWORD(WinUser.INPUT.INPUT_KEYBOARD);
        input.input.setType("ki"); // Because setting INPUT_INPUT_KEYBOARD is not enough:
                                   // https://groups.google.com/d/msg/jna-users/NDBGwC1VZbU/cjYCQ1CjBwAJ
        input.input.ki.wVk = new WinDef.WORD(0);
        input.input.ki.time = new WinDef.DWORD(0);
        input.input.ki.dwExtraInfo = new BaseTSD.ULONG_PTR(0);

        input.input.ki.wScan = new WinDef.WORD(c);

        if (press)
            input.input.ki.dwFlags = new WinDef.DWORD(0x0004 | 0x0); // keydown
        else
            input.input.ki.dwFlags = new WinDef.DWORD(0x0004 | 0x0002); // keyup

        User32.INSTANCE.SendInput(new WinDef.DWORD(1), (WinUser.INPUT[]) input.toArray(1), input.size());
    }
}
