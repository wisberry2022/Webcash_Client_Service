package ETC;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

public class FileHandler {

    public static final int READ = 0;
    public static final int WRITE = 1;

    private File readFile;
    private File writeFile;
    private InputStream is = null;
    private OutputStream os = null;
    private BufferedInputStream bis = null;
    private BufferedOutputStream bos = null;
    private Reader fr = null;
    private BufferedReader br = null;
    private Writer fw = null;
    private BufferedWriter bw = null;
    private ByteArrayOutputStream baos = null;

    public FileHandler() {}

    public FileHandler(String filename, int num) {
        if(num == 0) {
            this.readFile = new File(filename);
            this.createReadFile();
        }else if(num == 1) {
            this.writeFile = new File(filename);
            this.createWriteFile();
        }else {
            System.out.println("wrong input!");
        }

    }

    public FileHandler(String readFile, String writeFile) {
        this.readFile = new File(readFile);
        this.writeFile = new File(writeFile);
        this.createReadFile();
        this.createWriteFile();
    }

    // readFile, writeFile 설정
    public FileHandler setRead(String readFile) {
        this.readFile = new File(readFile);
        this.createReadFile();
        return this;
    }

    public FileHandler setWrite(String writeFile) {
        this.writeFile = new File(writeFile);
        this.createWriteFile();
        return this;
    }

    public void setRW(String readFile, String writeFile) {
        this.readFile = new File(readFile);
        this.writeFile = new File(writeFile);
    }

    // InputStream 설정 메소드
    public void settingInput() {
        try {
            is = new FileInputStream(readFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void settingBis() {
        bis = new BufferedInputStream(is);
    }

    public void settingReader() {
        try {
            fr = new FileReader(readFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void settingBufferedReader() {
        br = new BufferedReader(fr);
    }

    // OutputStream 설정 메소드
    public void settingBos() {
        bos = new BufferedOutputStream(os);
    }

    public void settingBaos() {
        baos = new ByteArrayOutputStream();
    }

    public void settingOutput() {
        try {
            os = new FileOutputStream(writeFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void settingWriter() {
        try {
            fw = new FileWriter(writeFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void settingBufferedWriter() {
        bw = new BufferedWriter(fw);
    }

    // read 메소드
    public String read() {
        byte[] temp = new byte[(int)this.readFile.length()];
        try {
            for(int i = 0; i<temp.length; i++) {
                temp[i] = (byte)is.read();
            }
            return new String(temp);
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }

    public String Bufferead() {
        byte[] bArr = new byte[(int)this.readFile.length()];
        try {
            int su;
            String msg = "";
            for(int i = 0; i<bArr.length; i++) {
                bArr[i] = (byte)bis.read();
            }
            return new String(bArr);
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }

    public String FileRead() {
        try {
            String msg = "";
            char[] buf = new char[8];
            int su = -1;
            while(true) {
                su = fr.read(buf);
                if(su == -1) break;
                msg += new String(buf, 0, su);
            }
            return msg;
        } catch (IOException e) {
            e.printStackTrace();
            return "IOException";
        }
    }

    public String BufferedFileRead() {
        try {
            String msg = "";
            String temp;
            while(true) {
                temp = br.readLine();
                if(temp == null) break;
                msg += temp + "\n";
            }
            return msg;
        } catch (IOException e) {
            e.printStackTrace();
            return "IOException";
        }
    }

    // write 메소드
    public void write(String msg) {
        try {
            byte[] bArr = msg.getBytes();
            os.write(bArr);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void bufferWrite(String msg) {
        try {
            byte[] bArr = msg.getBytes();
            bos.write(bArr);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void FileWrite(String msg) {
        try {
            fw.write(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void BufferedFileWrite(String msg) {
        try {
            bw.write(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // io close 메소드
    public void iClose() {
        try {
            if(is != null) is.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void bisClose() {
        if(bis != null)
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public void oClose() {
        try {
            if(os != null) os.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void bosClose() {
        try {
            if(bos != null) bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void ioClose() {
        try {
            if(is != null) this.is.close();
            if(os != null) this.os.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void baosClose() {
        try {
            if(baos != null) this.baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Reader close 메소드
    public void frClose() {
        try {
            if(fr != null) fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void brClose() {
        try {
            if(br != null) br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Writer close 메소드
    public void fwClose() {
        try {
            if(fw != null) fw.close();
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void bwClose() {
        try {
            if(bw != null) bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // private method
    private void createReadFile() {
        try {
            if(!readFile.exists()) readFile.createNewFile();
            else return;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createWriteFile() {
        try {
            if(!writeFile.exists()) writeFile.createNewFile();
            else return;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}