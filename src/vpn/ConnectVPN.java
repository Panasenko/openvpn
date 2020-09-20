package vpn;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.Map;

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

    public void connect() {

        tmpFile = FileUtils.genereteTempFile();
        System.out.println(tmpFile);
        if (FileUtils.fileWriter(tmpFile, (dataConnect.getLogin() + "\n" + dataConnect.getPassword()))) {

            String[] cmd = new String[]{"/bin/bash", "-c", "openvpn --auth-user-pass " + tmpFile + " --config " + dataConnect.getPathConfFile() + " &"};
            try {
                ProcessBuilder processBuilder = new ProcessBuilder();

                Map<String, String> environment = processBuilder.environment();
                processBuilder.command(cmd);

                Process process = processBuilder.start();

                if (process.exitValue() == 0) {
                    this.setStatusConnect(true);
                } else {
                    this.setStatusConnect(false);
                }
                
            } catch (IOException ex) {
                System.out.println("Выпало в кетч " + ex);
            } 

        } else {
            System.out.println("Ошибка, данные в временный файл не записаны ");
        }
    }

    public void disconnect() {
        this.process.destroy();
    }

}
