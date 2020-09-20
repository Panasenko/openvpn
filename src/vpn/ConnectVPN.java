package vpn;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import static java.util.Optional.empty;
import java.util.logging.Level;
import java.util.logging.Logger;

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

//        environment.put("GREETING", "Hola Mundo");
//
//        processBuilder.command("/bin/bash", "-c", "echo $GREETING");
        tmpFile = FileUtils.genereteTempFile();

        if (FileUtils.fileWriter(tmpFile, (dataConnect.getLogin() + "\n" + dataConnect.getPassword()))) {

//            String[] cmd = new String[]{"/bin/bash", "-c", "openvpn "
//                + "--auth-user-pass " + "/etc/openvpn/client/login.txt"
//                + "--config " + dataConnect.getPathConfFile()};
//            String[] cmd = new String[]{"java", "-version"};
            try {
                ProcessBuilder processBuilder = new ProcessBuilder("java", "-version");

                Process process = processBuilder.start();

//                List<String> results = readOutput(process.getInputStream());

                int exitCode = process.waitFor();
                System.out.println(exitCode);

            } catch (IOException ex) {
                System.out.println("Выпало в кетч " + ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(ConnectVPN.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            System.out.println("Ошибка, данные в временный файл не записаны ");
        }

    }

    public void disconnect() {

    }


}
