package vpn;

import java.awt.Component;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

public class ConnectVPN {

    private DataConnect dataConnect;
    private Component comp;
    private Process process;
    private boolean statusConnect = false;

    public ConnectVPN(Component comp, DataConnect dc) {
        this.comp = comp;
        this.dataConnect = dc;
    }

    public boolean getStatusConnect() {
        return statusConnect;
    }

    public DataConnect getDataConnect() {
        return dataConnect;
    }

    public void setDataConnect(DataConnect dataConnect) {
        this.dataConnect = dataConnect;
    }

    public void connect(String OTP) {

        String[] cmd = new String[]{"/bin/bash", "-c", "/usr/bin/sudo -S openvpn "
            + "--auth-user-pass /etc/openvpn/client/login.txt "
            + "--config " + dataConnect.getPathConfFile()
            + " 2>&1"};

        try {
            process = new ProcessBuilder(cmd).start();

            inputStream(process);

        } catch (IOException ex) {
            Logger.getLogger(ConnectVPN.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void disconnect() {

    }

    private boolean inputStream(Process process) {

        try {
            InputStreamReader input = new InputStreamReader(process.getInputStream());

            int bytes, tryies = 0;
            char buffer[] = new char[1024];
            while ((bytes = input.read(buffer, 0, 1024)) != -1) {
                if (bytes == 0) {
                    continue;
                }
                outputStream(String.valueOf(buffer, 0, bytes));
            }

            return tryies < 3;
        } catch (IOException ex) {
            System.out.println(ex);
        }

        return false;
    }
//    private boolean inputStream(Process process) {
//
//        try {
//            InputStreamReader input = new InputStreamReader(process.getInputStream());
//
//            int bytes, tryies = 0;
//            char buffer[] = new char[1024];
//            while ((bytes = input.read(buffer, 0, 1024)) != -1) {
//                if (bytes == 0) {
//                    continue;
//                }
//                outputStream(String.valueOf(buffer, 0, bytes));
//            }
//
//            return tryies < 3;
//        } catch (IOException ex) {
//            System.out.println(ex);
//        }
//
//        return false;
//    }
//
//    private void outputStream(String data) {
//        OutputStreamWriter output = new OutputStreamWriter(process.getOutputStream());
//
//        if (data.contains("[sudo] пароль")) {
//            try {
//                char password[] = new char[]{'5', '4', '8', '9', '8', '7'};
//                output.write(password);
//                output.write('\n');
//                output.flush();
//                Arrays.fill(password, '\0');
//            } catch (IOException ex) {
//                Logger.getLogger(ConnectVPN.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//
//    }
    
    private void outputStream(String data) {
        OutputStreamWriter output = new OutputStreamWriter(process.getOutputStream());

        if (data.contains("[sudo] пароль")) {
            try {
                char password[] = passwordDialog();
                output.write(password);
                output.write('\n');
                output.flush();
            } catch (IOException ex) {
                Logger.getLogger(ConnectVPN.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public char[] passwordDialog() {
        JPasswordField pwd = new JPasswordField(10);

        int option = JOptionPane.showConfirmDialog(null, new JPasswordField(10), "Confirm root password", JOptionPane.OK_CANCEL_OPTION);
        return pwd.getPassword();

    }

}
