package vpn;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectVPN extends Thread{

    private DataConnect dataConnect;
    private Component comp;
    private Process process;
    private File tmpFile;
    private boolean statusConnect = false;

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

    
    @Override
    public void run(){
         tmpFile = FileUtils.genereteTempFile();
        if (FileUtils.fileWriter(tmpFile, (dataConnect.getLogin() + "\n" + dataConnect.getPassword()))) {

            String[] cmd = new String[]{"/bin/bash", "-c", "openvpn --auth-user-pass " + tmpFile + " --config " + dataConnect.getPathConfFile()};
            
            try {
                ProcessBuilder processBuilder = new ProcessBuilder(cmd);
                process = processBuilder.start();
                
                FileUtils.deleteTempFile(tmpFile);
                this.setStatusConnect(true);
                process.waitFor();
                this.setStatusConnect(false);
            } catch (IOException ex) {
                System.out.println("Error in method connect " + ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(ConnectVPN.class.getName()).log(Level.SEVERE, null, ex);
            }

        } 
    }

    public boolean disconnect() {
        this.process.destroy();
        return statusConnect = false;
    }
}
