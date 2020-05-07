import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class TrayIconApp {
    private static TrayIcon trayIcon;
//    private static Logger LOGGER = LoggerFactory
//            .getLogger(TrayIconApp.class);

    public static void registerTrayIcon(Image image, String toolTip, ActionListener action) {
        if (SystemTray.isSupported()) {
            if (trayIcon != null) {
                trayIcon = null;
            }
            trayIcon = new TrayIcon(image);
            trayIcon.setImageAutoSize(true);

            if (toolTip != null) {
                trayIcon.setToolTip(toolTip);
            }

            if (action != null) {
                trayIcon.addActionListener(action);
            }

            try {
                for (TrayIcon registeredTrayIcon : SystemTray.getSystemTray()
                        .getTrayIcons()) {
                    SystemTray.getSystemTray().remove(registeredTrayIcon);
                }

                SystemTray.getSystemTray().add(trayIcon);
            } catch (AWTException e) {
//                LOGGER.error("I got catch an error during add system tray !", e);
                e.printStackTrace();
            }
        } else {
//            LOGGER.error("System tray is not supported !");
            System.out.println("System tray is not supported !");
        }
    }

    public TrayIconApp(){
        TrayIconHandler.registerTrayIcon(
            Toolkit.getDefaultToolkit().getImage("img/trayIcon.gif"),
            "JobTime",
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Client_App.frame.setVisible(true);
                }
            }
        );
        TrayIconHandler.addItem("Exit", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        TrayIconHandler.displayMessage("JobTime", "Start", TrayIcon.MessageType.INFO);
    }
    /*public static void main(String[] args) {
        TrayIconHandler.registerTrayIcon(
                Toolkit.getDefaultToolkit().getImage("img/trayIcon.gif"),
                "JobTime",
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Open your application here.
                    }
                }
        );
        TrayIconHandler.addItem("Exit", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        TrayIconHandler.displayMessage("JobTime", "Start", TrayIcon.MessageType.INFO);
    }*/
}
