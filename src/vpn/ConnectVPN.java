package vpn;

import java.awt.Component;
import java.io.IOException;
import javax.swing.JOptionPane;

public class ConnectVPN {

    DataConnect dataConnect;
    Component comp;

    public ConnectVPN(Component comp, DataConnect dc) {
        this.comp = comp;
        this.dataConnect = dc;
    }

    private String getOTPFromDialog() {
        return JOptionPane.showInputDialog(comp, "Введите ОТП-пароль:");
    }

    public void connect() throws IOException {
        String resultDialog = this.getOTPFromDialog();

        if (resultDialog.isEmpty() || resultDialog.length() < 6) {
            JOptionPane.showMessageDialog(comp, "Ошибка! Введен некорректный ОТП-пароль!");
        } else {
            try {
                
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec("sudo openvpn --suppress-timestamps --nobind --config " + dataConnect.getPathConfFile());
            } catch (Exception e) {
                System.out.println(e.toString());
                
            }
        }

    }

    public void disconnect() {

    }

}
