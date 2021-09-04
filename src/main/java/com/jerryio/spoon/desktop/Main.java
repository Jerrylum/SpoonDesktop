package com.jerryio.spoon.desktop;

import com.jerryio.spoon.desktop.enums.ConnectionMode;
import com.jerryio.spoon.desktop.view.MainFrame;

public class Main {

    public static void main(String[] args) throws Exception {
        ConnectionMode mode = ConnectionMode.CLIENT;

        if (args.length > 0) {
            mode = ConnectionMode.valueOf(args[0].toUpperCase());
        }

        if (mode == ConnectionMode.ROUTER) {
            SpoonRouter.build();
        } else {
            SpoonDesktop.build();

            Thread.sleep(300);

            SpoonDesktop desktop = SpoonDesktop.getInstance();
            MainFrame frame = MainFrame.getInstace();

            if (args.length < 1)
                return;
            desktop.setMode(mode);
            if (args.length < 2)
                return;
            frame.setInputAddress(args[1]);
            if (args.length < 3)
                return;
            frame.setInputChannel(args[2]);
            if (args.length < 4 || !args[3].equalsIgnoreCase("run"))
                return;
            desktop.executeAction();
        }
    }

}
