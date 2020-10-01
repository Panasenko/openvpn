package vpn;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;

public class ConnectVPN extends Thread {

    private DataConnect dataConnect;
    private final String tmpFileName;
    private final String otp;
    private Process process;
    private File tmpFile;
    private final JButton btnConnect;

    public ConnectVPN(String tmpFileName, DataConnect dc, String otp, JButton btnConnect) {
        this.tmpFileName = tmpFileName;
        this.dataConnect = dc;
        this.otp = otp;
        this.btnConnect = btnConnect;
    }

    public DataConnect getDataConnect() {
        return dataConnect;
    }

    public void setDataConnect(DataConnect dataConnect) {
        this.dataConnect = dataConnect;
    }

    @Override
    public void run() {
        tmpFile = FileUtils.genereteTempFile(tmpFileName);

        if (FileUtils.fileWriter(tmpFile, (dataConnect.getLogin() + "\n" + dataConnect.getPassword() + otp))) {

            String[] cmd = new String[]{"/bin/bash", "-c", "openvpn --auth-user-pass " + tmpFile + " --config " + dataConnect.getPathConfFile()};

            try {
                ProcessBuilder processBuilder = new ProcessBuilder(cmd);
                process = processBuilder.start();

//                FileUtils.deleteTempFile(tmpFile);
                btnConnect.setText("Disconnect");
                process.waitFor();
                btnConnect.setText("Connect");

            } catch (IOException ex) {
                System.out.println("Error in method connect " + ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(ConnectVPN.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public boolean disconnect() {
        this.process.destroy();
        btnConnect.setText("Connect");
        return true;
    }
}
