package vpn;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class FileUtils {

    public static void addFileFilter(JFileChooser jfch, FileFilter ff) {
        jfch.removeChoosableFileFilter(jfch.getFileFilter());
        jfch.setFileFilter(ff);
        jfch.setSelectedFile(new File(""));
    }

    public static File genereteTempFile(String authFileName) {
        File file = new File(FileUtils.getPathToFile() + "/" + authFileName);
        file.setWritable(true);
        file.setReadable(true);
        return file;
    }

    public static String getPathToFile() {

        File f = new File(System.getProperty("java.class.path"));
        File dir = f.getAbsoluteFile().getParentFile();
        return dir.toString();

    }

    public static boolean fileWriter(File file, String data) {
        FileWriter writer;
        try {
            writer = new FileWriter(file);

            writer.append(data);
            writer.flush();
            writer.close();
            return true;

        } catch (IOException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;

    }

    public static boolean deleteTempFile(File file) {
        if (file != null) {
            return file.delete();
        }
        return false;
    }

    public static void serializable(Object obj, String fileParh) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(fileParh);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(obj);
            objectOutputStream.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public static Object deserialize(String fileName) {
        try {
            FileInputStream fis = new FileInputStream(fileName);
            ObjectInputStream oin = new ObjectInputStream(fis);
            Object ts = (Object) oin.readObject();
            fis.close();
            return ts;

        } catch (ClassNotFoundException | IOException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static File[] finder(String dirName, String fileName) {
        File dir = new File(dirName);
        return dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String filename) {
                return filename.matches(fileName);
            }
        });

    }

}
