package vpn;

import java.awt.Component;
import java.io.File;
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
    private File tmpFile;

    public ConnectVPN(Component comp, DataConnect dc) {
        this.comp = comp;
        this.dataConnect = dc;
    }

    public boolean getStatusConnect() {
        return statusConnect;
    }

    public void setStatusConnect(boolean statusConnect) {
        this.statusConnect = statusConnect;
    }

    public DataConnect getDataConnect() {
        return dataConnect;
    }

    public void setDataConnect(DataConnect dataConnect) {
        this.dataConnect = dataConnect;
    }

    public void connect(String OTP) {

        tmpFile = FileUtils.genereteTempFile();
        System.out.println(tmpFile);
        
        if (FileUtils.fileWriter(tmpFile, this.genereteConnectString(this.getDataConnect(), OTP))) {

            String[] cmd = new String[]{"/bin/bash", "-c", "/usr/bin/sudo -S openvpn "
                + "--auth-user-pass " + tmpFile
                + "--config " + dataConnect.getPathConfFile()
                + " 2>&1"};

            try {
                process = new ProcessBuilder(cmd).start();
                if (inputStream(process)) {

                    this.setStatusConnect(true);
                } else {
                    this.setStatusConnect(false);
                }

            } catch (IOException ex) {
                Logger.getLogger(ConnectVPN.class.getName()).log(Level.SEVERE, null, ex);
            }

//            finally {
//                if (FileUtils.deleteTempFile(tmpFile)) {
//                    System.out.println("Временный файл удален");
//                } else {
//                    System.out.println("Ошибка удаления файла");
//                }
//            }
        } else {
            System.out.println("Ошибка, данные в временный файл не записаны ");
        }

    }

    public void disconnect() {

    }

    private boolean inputStream(Process process) {

        System.out.println("input");

        InputStreamReader input = new InputStreamReader(process.getInputStream());

        try {

            int bytes = 0;
            char buffer[] = new char[1024];

            boolean check = false;

            do {
                String data = String.valueOf(buffer, 0, input.read(buffer, 0, 1024));

                if (data.contains("[sudo] пароль")) {
                    if (!outputStream(data)) {
                        check = true;
                    } else {
                        check = false;
                    }
                }
//                System.out.println("Проверка после подключения буфера" + String.valueOf(buffer, 0, input.read(buffer, 0, 1024)));
            } while (check);

            return true;
        } catch (IOException ex) {
            System.out.println(ex);
        } 
        
//        finally {
//            try {
//                input.close();
//            } catch (IOException ex) {
//                Logger.getLogger(ConnectVPN.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }

        return false;
    }

    private boolean outputStream(String data) {
        OutputStreamWriter output = new OutputStreamWriter(process.getOutputStream());

        try {
            char password[] = this.getPasswordDialog();
            output.write(password);
            output.write('\n');
            output.flush();
            output.close();

            System.out.println("Пройдено");
            return true;
        } catch (IOException ex) {
            Logger.getLogger(ConnectVPN.class.getName()).log(Level.SEVERE, null, ex);

        }

        return false;

    }

    public char[] getPasswordDialog() {
        JPasswordField pf = new JPasswordField();
        int okCxl = JOptionPane.showConfirmDialog(null, pf, "Enter Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        char[] cp = null;
        if (okCxl == JOptionPane.OK_OPTION) {
            cp = pf.getPassword();
            System.out.println(cp);
        }
        return cp;
    }

    private String genereteConnectString(DataConnect dc, String OTP) {
        return dc.getLogin() + "\n" + dc.getPassword() + OTP;
    }
}
