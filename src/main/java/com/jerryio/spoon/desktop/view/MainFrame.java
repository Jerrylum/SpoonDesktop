package com.jerryio.spoon.desktop.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import com.jerryio.spoon.desktop.enums.ConnectionMode;
import com.jerryio.spoon.desktop.enums.ConnectionStatus;
import com.jerryio.spoon.desktop.utils.Util;
import com.formdev.flatlaf.IntelliJTheme;
import com.jerryio.spoon.desktop.SpoonDesktop;

import java.awt.event.ActionListener;
import java.util.Arrays;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class MainFrame {

    private static MainFrame instance; // singleton
    private static boolean init = false;

    private JFrame frmSpoonDesktop;
    private JToggleButton tglbtnMode;
    private JLabel lblServerIP;
    private JComboBox<String> comboAddress;
    private JToggleButton tglbtnAction;
    private JLabel lblChannel;
    private JTextField textChannel;
    private JLabel lblClientControl;
    private JPanel panelClientControl;
    private JToggleButton tglbtnAllowConnect;
    private JButton btnKickClients;
    private JCheckBox chckbxPrimarySelection;
    private JCheckBox chckbxClipboard;
    private JCheckBox chckbxTyping;
    private JTextPane textUserInput;
    private JButton btnSend;
    private JLabel lblSecurity1;
    private JLabel lblSecurity2;

    private String[] cacheProvidedAddress;

    /**
     * Launch the application. Jerry: test only
     */
    public static void main(String[] args) {
        SpoonDesktop.build();
        build();
    }

    public static void build() {
        if (init || instance != null)
            return;
        init = true;

        IntelliJTheme.setup(MainFrame.class.getResourceAsStream("/DarkPurple.theme.json"));

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainFrame window = instance = new MainFrame();
                    window.updateInterface();
                    window.frmSpoonDesktop.setVisible(true);
                    window.tglbtnAction.grabFocus();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static MainFrame getInstance() {
        return instance;
    }

    /**
     * Create the application.
     * 
     * @throws IOException
     */
    public MainFrame() throws IOException {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     * 
     * @throws IOException
     */
    private void initialize() throws IOException {
        frmSpoonDesktop = new JFrame();
        frmSpoonDesktop.setIconImage(ImageIO.read(MainFrame.class.getResourceAsStream("/icon128.png")));
        frmSpoonDesktop.setTitle("Spoon Desktop");
        frmSpoonDesktop.setResizable(false);
        frmSpoonDesktop.setBounds(100, 100, 390, 610);
        frmSpoonDesktop.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        frmSpoonDesktop.getContentPane().add(panel, BorderLayout.CENTER);

        JLabel lblNewLabel = new JLabel("Connection");
        lblNewLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(lblNewLabel);

        Component verticalStrut = Box.createVerticalStrut(8);
        panel.add(verticalStrut);

        JLabel lblNewLabel_1 = new JLabel("Mode");
        lblNewLabel_1.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(lblNewLabel_1);
        
        JPanel panel_3_1 = new JPanel();
        panel_3_1.setMaximumSize(new Dimension(500, 24));
        panel.add(panel_3_1);
        panel_3_1.setLayout(new BoxLayout(panel_3_1, BoxLayout.X_AXIS));
        
        tglbtnMode = new JToggleButton("Client");
        tglbtnMode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SpoonDesktop desktop = SpoonDesktop.getInstance();
                desktop.setMode(
                        desktop.getMode() == ConnectionMode.CLIENT ? ConnectionMode.SERVER : ConnectionMode.CLIENT);
            }
        });
        tglbtnMode.setMaximumSize(new Dimension(80, 30));
        tglbtnMode.setFont(new Font("Arial", Font.PLAIN, 12));
        panel_3_1.add(tglbtnMode);

        Component horizontalStrut_1 = Box.createHorizontalStrut(8);
        panel_3_1.add(horizontalStrut_1);
        
        lblServerIP = new JLabel("(unknown)");
        lblServerIP.setFont(new Font("Arial", Font.PLAIN, 14));
        panel_3_1.add(lblServerIP);

        Component verticalStrut_1 = Box.createVerticalStrut(8);
        panel.add(verticalStrut_1);

        JLabel lblNewLabel_1_1 = new JLabel("IP Address");
        lblNewLabel_1_1.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(lblNewLabel_1_1);

        JPanel panel_3 = new JPanel();
        panel_3.setMaximumSize(new Dimension(500, 24));
        panel.add(panel_3);
        GridBagLayout gbl_panel_3 = new GridBagLayout();
        gbl_panel_3.columnWidths = new int[] { 120, 60 };
        gbl_panel_3.rowHeights = new int[] { 24 };
        gbl_panel_3.columnWeights = new double[] { Double.MIN_VALUE };
        gbl_panel_3.rowWeights = new double[] { Double.MIN_VALUE };
        panel_3.setLayout(gbl_panel_3);

        comboAddress = new JComboBox<String>();
        comboAddress.setFont(new Font("Arial", Font.PLAIN, 12));
        comboAddress.setEditable(true);
        comboAddress.setModel(new DefaultComboBoxModel<String>(new String[] { "loading" }));
        GridBagConstraints gbc_comboAddress = new GridBagConstraints();
        gbc_comboAddress.fill = GridBagConstraints.BOTH;
        gbc_comboAddress.gridx = 0;
        gbc_comboAddress.gridy = 0;
        panel_3.add(comboAddress, gbc_comboAddress);

        tglbtnAction = new JToggleButton("Loading");
        tglbtnAction.setFont(new Font("Arial", Font.PLAIN, 12));
        tglbtnAction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SpoonDesktop.getInstance().executeAction();
            }
        });
        GridBagConstraints gbc_tglbtnAction = new GridBagConstraints();
        gbc_tglbtnAction.insets = new Insets(0, 10, 0, 0);
        gbc_tglbtnAction.gridy = 0;
        gbc_tglbtnAction.gridx = 1;
        gbc_tglbtnAction.fill = GridBagConstraints.BOTH;
        panel_3.add(tglbtnAction, gbc_tglbtnAction);

        Component verticalStrut_2 = Box.createVerticalStrut(16);
        panel.add(verticalStrut_2);

        JLabel lblNewLabel_2 = new JLabel("Options");
        lblNewLabel_2.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(lblNewLabel_2);

        Component verticalStrut_3 = Box.createVerticalStrut(8);
        panel.add(verticalStrut_3);

        lblChannel = new JLabel("Channel");
        lblChannel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(lblChannel);

        textChannel = new JTextField();
        textChannel.setFont(new Font("Arial", Font.PLAIN, 12));
        textChannel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                SpoonDesktop.getInstance().setChannel(getInputChannel());
            }
        });
        panel.add(textChannel);
        textChannel.setColumns(10);
        textChannel.setMaximumSize(new Dimension(200, 30));

        lblClientControl = new JLabel("Clients Control");
        lblClientControl.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(lblClientControl);

        panelClientControl = new JPanel();
        panel.add(panelClientControl);
        panelClientControl.setMaximumSize(new Dimension(500, 30));
        panelClientControl.setLayout(new BoxLayout(panelClientControl, BoxLayout.X_AXIS));

        tglbtnAllowConnect = new JToggleButton("Accept Conn");
        tglbtnAllowConnect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SpoonDesktop.getInstance().setIsAllowNewConnection(tglbtnAllowConnect.isSelected());
            }
        });
        tglbtnAllowConnect.setSelected(true);
        tglbtnAllowConnect.setFont(new Font("Arial", Font.PLAIN, 12));
        panelClientControl.add(tglbtnAllowConnect);

        Component horizontalStrut = Box.createHorizontalStrut(8);
        panelClientControl.add(horizontalStrut);

        btnKickClients = new JButton("Kick 1 Clients");
        btnKickClients.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SpoonDesktop.getInstance().kickAllClients();
            }
        });
        btnKickClients.setFont(new Font("Arial", Font.PLAIN, 12));
        panelClientControl.add(btnKickClients);

        Component verticalStrut_1_1 = Box.createVerticalStrut(8);
        panel.add(verticalStrut_1_1);

        JLabel lblNewLabel_1_1_1_1 = new JLabel("Output");
        lblNewLabel_1_1_1_1.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(lblNewLabel_1_1_1_1);

        chckbxPrimarySelection = new JCheckBox("Set primary selection");
        chckbxPrimarySelection.setVisible(false); // XXX: not used
        chckbxPrimarySelection.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SpoonDesktop.getInstance().setIsPrimarySelection(chckbxPrimarySelection.isSelected());
            }
        });
        chckbxPrimarySelection.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(chckbxPrimarySelection);

        chckbxClipboard = new JCheckBox("Copy to clipboard");
        chckbxClipboard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SpoonDesktop.getInstance().setIsClipboard(chckbxClipboard.isSelected());
            }
        });
        chckbxClipboard.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(chckbxClipboard);

        chckbxTyping = new JCheckBox("Typing text");
        chckbxTyping.setSelected(true); // IMPORTANT: init sync with SpoonDesktop
        chckbxTyping.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SpoonDesktop.getInstance().setIsTypingText(chckbxTyping.isSelected());
            }
        });
        chckbxTyping.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(chckbxTyping);

        Component verticalStrut_2_2 = Box.createVerticalStrut(16);
        panel.add(verticalStrut_2_2);

        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(500, 20));
        panel.add(separator);

        Component verticalStrut_2_2_1 = Box.createVerticalStrut(16);
        panel.add(verticalStrut_2_2_1);

        textUserInput = new JTextPane();
        textUserInput.setFont(new Font("Arial", Font.PLAIN, 18));
        textUserInput.setMinimumSize(new Dimension(500, 200));

        JScrollPane pane = new JScrollPane(textUserInput);
        pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(pane);

        Component verticalStrut_2_2_2 = Box.createVerticalStrut(8);
        panel.add(verticalStrut_2_2_2);

        btnSend = new JButton("Send");
        btnSend.setFont(new Font("Arial", Font.PLAIN, 12));
        btnSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doSendUserInput();
            }
        });
        panel.add(btnSend);

        Component verticalStrut_2_2_1_2 = Box.createVerticalStrut(50);
        panel.add(verticalStrut_2_2_1_2);

        lblSecurity1 = new JLabel(" ");
        lblSecurity1.setToolTipText(
                "To ensure your messages are secured, please verify that the security code displayed on the screen is exactly the same as the one displayed on the remote device.");
        lblSecurity1.setForeground(Color.GRAY);
        lblSecurity1.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(lblSecurity1);

        lblSecurity2 = new JLabel("<html><br/></html>");
        lblSecurity2.setFont(new Font("Monospaced", Font.PLAIN, 14));
        panel.add(lblSecurity2);

        for (Component com : panel.getComponents())
            if (com instanceof JComponent)
                ((JComponent) com).setAlignmentX(Component.LEFT_ALIGNMENT);

        Action action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doSendUserInput();
            }
        };
        String keyStrokeAndKey = "control ENTER";
        KeyStroke keyStroke = KeyStroke.getKeyStroke(keyStrokeAndKey);
        textUserInput.getInputMap().put(keyStroke, keyStrokeAndKey);
        textUserInput.getActionMap().put(keyStrokeAndKey, action);
    }

    public void updateInterface() {
        SpoonDesktop desktop = SpoonDesktop.getInstance();
        boolean isClient = desktop.getMode() == ConnectionMode.CLIENT;

        lblServerIP.setText(isClient ? "" : Util.getMyIP());

        tglbtnMode.setText(isClient ? "Client" : "Server");
        tglbtnMode.setSelected(isClient);

        String[] newProvidedAddress = desktop.getProvidedAddress();
        if (!Arrays.equals(cacheProvidedAddress, newProvidedAddress)) {
            cacheProvidedAddress = newProvidedAddress;
            comboAddress.setModel(new DefaultComboBoxModel<String>(newProvidedAddress));
        }

        String actionBtnText = "Unknown";
        boolean actionBtnSelected = false;
        switch (desktop.getConnectionStatus()) {
            case ClientReadyToConnect:
                actionBtnText = "Connect";
                break;
            case ClientConnecting:
                actionBtnText = "Connecting";
                actionBtnSelected = true;
                break;
            case ClientConnectionFailed:
                actionBtnText = "Failed";
                break;
            case ClientHandshake:
                actionBtnText = "Handshake";
                actionBtnSelected = true;
                break;
            case ClientConnected:
                actionBtnText = "Connected";
                actionBtnSelected = true;
                break;
            case ClientDisconnected:
                actionBtnText = "Disconnected";
                break;
            case ServerReadyToStart:
                actionBtnText = "Start";
                break;
            case ServerStarting:
                actionBtnText = "Starting";
                actionBtnSelected = true;
                break;
            case ServerStartFailed:
                actionBtnText = "Failed";
                break;
            case ServerRunning:
                actionBtnText = "Running";
                actionBtnSelected = true;
                break;
            case ServerStopped:
                actionBtnText = "Stopped";
                break;
            default:
                break;
        }
        tglbtnAction.setText(actionBtnText);
        tglbtnAction.setSelected(actionBtnSelected);

        String channelReal = desktop.getChannel();
        if (channelReal != null && !channelReal.equals("") && !channelReal.equals(textChannel.getText()))
            textChannel.setText(channelReal);

        lblChannel.setVisible(isClient);
        textChannel.setVisible(isClient);

        lblClientControl.setVisible(!isClient);
        panelClientControl.setVisible(!isClient);

        tglbtnAllowConnect.setText(desktop.isAllowNewConnection() ? "Accept Conn" : "Locked");
        tglbtnAllowConnect.setSelected(desktop.isAllowNewConnection());

        btnKickClients
                .setText("Kick " + desktop.getClientsCount() + " Client" + (desktop.getClientsCount() > 1 ? "s" : ""));

        // chckbxPrimarySelection.setVisible(System.getProperty("os.name").startsWith("Linux"));
        // XXX: not used
        chckbxPrimarySelection.setSelected(desktop.isPrimarySelection());
        chckbxClipboard.setSelected(desktop.isClipboard());

        lblSecurity1.setText("   "); // IMPORTANT: no optimize
        lblSecurity2.setText("   ");

        byte[] codeInbytes = desktop.getSecurityCode();
        if (codeInbytes == null) {
            lblSecurity1.setText(" ");
            lblSecurity2.setText("<html><p>&nbsp;</p><p style=\"margin-top:-5;\">&nbsp;</p></html>");
        } else {
            String code = Util.bytesToHex(codeInbytes);

            String line0 = "";
            String line1 = "";
            for (int i = 0; i < 16; i++) {
                if (i % 4 == 0 && i != 0)
                    line0 += ' ';
                line0 += code.charAt(i);
            }
            for (int i = 16; i < 32; i++) {
                if (i % 4 == 0 && i != 0)
                    line1 += ' ';
                line1 += code.charAt(i);
            }

            lblSecurity1.setText("Security Code:");
            lblSecurity2.setText("<html><p>" + line0 + "</p><p style=\"margin-top:-5;\">" + line1 + "</p></html>");
        }
    }

    public String getInputAddress() {
        return (String) comboAddress.getSelectedItem();
    }

    public String getInputChannel() {
        return textChannel.getText();
    }

    public void setInputText(String msg) {
        textUserInput.setText("");
    }

    public void setInputAddress(String ip) {
        comboAddress.setSelectedItem(ip);
    }

    public void setInputChannel(String channel) {
        textChannel.setText(channel);
    }

    private void doSendUserInput() {
        SpoonDesktop desktop = SpoonDesktop.getInstance();
        desktop.sendMessage(textUserInput.getText());

        if (desktop.getConnectionStatus() == ConnectionStatus.ServerRunning && desktop.getClientsCount() != 0) {
            setInputText("");
        }

        textUserInput.grabFocus();
    }
}
