package vpn;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Base64;

public class DataConnect implements Externalizable {

    private String pathConfFile;
    private String login;
    private transient String password;

    public DataConnect() {
    }

    public DataConnect(String path, String login, String password) {
        this.pathConfFile = path;
        this.login = login;
        this.password = password;
    }

    public String getPathConfFile() {
        return pathConfFile;
    }

    public void setPathConfFile(String pathConfFile) {
        this.pathConfFile = pathConfFile;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(this.getPathConfFile());
        out.writeObject(this.getLogin());
        out.writeObject(this.encryptString(this.getPassword()));
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        pathConfFile = (String) in.readObject();
        login = (String) in.readObject();
        password = this.decryptString((String) in.readObject());
    }

    private String encryptString(String data) {
        String encryptedData = Base64.getEncoder().encodeToString(data.getBytes());
        return encryptedData;
    }

    private String decryptString(String data) {
        String decrypted = new String(Base64.getDecoder().decode(data));
        return decrypted;
    }

}
