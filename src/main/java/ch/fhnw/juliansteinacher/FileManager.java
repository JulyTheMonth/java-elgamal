package ch.fhnw.juliansteinacher;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileManager {


    final Path directory;

    final String SECRETKEY = "sk.txt";
    final String PUBLICKEY = "pk.txt";
    final String TEXT = "text.txt";
    final String CHIFFRE = "chiffre.txt";
    final String TEXT_D = "text-d.txt";

    public FileManager(Path directory) {
        this.directory = directory;
    }


    public void saveSecretKey(BigInteger secretKey) {
        try {
            Path path = this.directory.resolve(SECRETKEY);
            FileWriter fw = new FileWriter(path.toFile());
            PrintWriter pw = new PrintWriter(fw);
            pw.print(secretKey);
            pw.close();
            System.out.println("File saved at: " + path.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void savePublicKey(BigInteger publicKey) {
        try {
            Path path = this.directory.resolve(PUBLICKEY);
            FileWriter fw = new FileWriter(path.toFile());
            PrintWriter pw = new PrintWriter(fw);
            pw.print(publicKey);
            pw.close();
            System.out.println("File saved at: " + path.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public BigInteger getPublicKey() {
        try {
            return new BigInteger(Files.readString(this.directory.resolve(PUBLICKEY)));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public BigInteger getSecretKey() {
        try {
            return new BigInteger(Files.readString(this.directory.resolve(SECRETKEY)));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getText(){
        try {
            return Files.readString(this.directory.resolve(TEXT));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveChiffre(String chiffre) {
        try {
            Path path = this.directory.resolve(CHIFFRE);
            FileWriter fw = new FileWriter(path.toFile());
            PrintWriter pw = new PrintWriter(fw);
            pw.print(chiffre);
            pw.close();
            System.out.println("File saved at: " + path.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String getChiffreText() {
        try {
            return Files.readString(this.directory.resolve(CHIFFRE));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveDecrypted(String decrypted) {
        try {
            Path path = this.directory.resolve(TEXT_D);
            FileWriter fw = new FileWriter(path.toFile());
            PrintWriter pw = new PrintWriter(fw);
            pw.print(decrypted);
            pw.close();
            System.out.println("File saved at: " + path.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
