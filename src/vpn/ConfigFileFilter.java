package vpn;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class ConfigFileFilter extends FileFilter{
    
    private String fileExtension;
    private String fileDescription;

    public ConfigFileFilter(String fileExtension, String fileDescription) {
        this.fileExtension = fileExtension;
        this.fileDescription = fileDescription;
    }
    
    @Override
    public boolean accept(File f) {
        return f.isDirectory() || f.getAbsolutePath().endsWith(fileExtension);
    }

    @Override
    public String getDescription() {
        return fileDescription + "(." + fileExtension + ")";
    }
    
}
