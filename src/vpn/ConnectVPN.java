package vpn;

import java.awt.Component;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectVPN {

    private DataConnect dataConnect;
    private Component comp;

    private InputStreamReader input;
    private OutputStreamWriter output;
    private Process process;

    public ConnectVPN(Component comp, DataConnect dc) {
        this.comp = comp;
        this.dataConnect = dc;
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

            runWithPrivileges(process);

        } catch (IOException ex) {
            Logger.getLogger(ConnectVPN.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void disconnect() {

    }

    public boolean runWithPrivileges(Process process) {

        try {

            output = new OutputStreamWriter(process.getOutputStream());
            input = new InputStreamReader(process.getInputStream());
            

            int bytes, tryies = 0;
            char buffer[] = new char[1024];
            while ((bytes = input.read(buffer, 0, 1024)) != -1) {
                System.out.println("23");
                if (bytes == 0) {
                    continue;
                }
                String data = String.valueOf(buffer, 0, bytes);
                System.out.println(data);

                if (data.contains("[sudo] пароль")) {
                    char password[] = new char[]{'5', '4', '8', '9', '8', '7'};
                    output.write(password);
                    output.write('\n');
                    output.flush();
                    Arrays.fill(password, '\0');
                    tryies++;
                }
            }

            return tryies < 3;
        } catch (IOException ex) {
            System.out.println(ex);
        }

        return false;
    }

}
