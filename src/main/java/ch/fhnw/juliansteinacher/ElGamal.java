package ch.fhnw.juliansteinacher;

import java.math.BigInteger;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ElGamal {

    //Constant N. Must be a Primenumber
    final BigInteger N = new BigInteger("FFFFFFFFFFFFFFFFC90FDAA22168C234C4C6628B80DC1CD1" +
            "29024E088A67CC74020BBEA63B139B22514A08798E3404DD" +
            "EF9519B3CD3A431B302B0A6DF25F14374FE1356D6D51C245" +
            "E485B576625E7EC6F44C42E9A637ED6B0BFF5CB6F406B7ED" +
            "EE386BFB5A899FA5AE9F24117C4B1FE649286651ECE45B3D" +
            "C2007CB8A163BF0598DA48361C55D39A69163FA8FD24CF5F" +
            "83655D23DCA3AD961C62F356208552BB9ED529077096966D" +
            "670C354E4ABC9804F1746C08CA18217C32905E462E36CE3B" +
            "E39E772C180E86039B2783A2EC07A28FB5C55DF06F4C52C9" +
            "DE2BCBF6955817183995497CEA956AE515D2261898FA0510" +
            "15728E5A8AACAA68FFFFFFFFFFFFFFFF", 16);

    final BigInteger erzeuger = BigInteger.valueOf(2);

    FileManager fm;

    public static void main(String[] args) {

        //second Parameter Directoy of files.
        String fileDirectory = args[1];
        FileManager fm = new FileManager(Path.of(fileDirectory));

        ElGamal el = new ElGamal(fm);

        //first Parameter executionmode.
        switch (args[0]) {
            case "genkey":
                el.generateKeyPair();
                break;
            case "encrypt":
                el.encryptTextFile();
                break;
            case "decrypt":
                el.decryptFile();
                break;
            default:
                System.out.println("No suitable Argument: genkey, encrypt, decrypt");
                break;
        }
    }


    public ElGamal(FileManager fm) {
        this.fm = fm;
    }

    /**
     * generates a keypair.
     */
    private void generateKeyPair() {

        //Random Integer for b / secretKey
        BigInteger secretKey = getRandomBigInteger();

        // g^b
        BigInteger publicKey = erzeuger.modPow(secretKey, N);

        //save p
        this.fm.savePublicKey(publicKey);

        this.fm.saveSecretKey(secretKey);

    }

    /**
     * encrypts the textfile
     */
    private void encryptTextFile() {

        //Get text and publicKey
        String text = this.fm.getText();
        BigInteger publicKey = this.fm.getPublicKey();


        ArrayList<String> chiffres = new ArrayList<>();

        //random BigInteger for encryption.
        BigInteger a = getRandomBigInteger();

        //Iterate through each character as their Ascii-Code
        text.chars().forEach(i -> {
            //encrypt the Character and add it to the pairs.
            BigInteger bi = BigInteger.valueOf(i);
            BigInteger y1 = erzeuger.modPow(a, N);
            BigInteger ghba = publicKey.modPow(a, N);
            BigInteger y2 = ghba.multiply(bi).mod(N);
            chiffres.add("(" + y1 + "," + y2 + ")");
        });

        //join all pairs and save it.
        String chiffre = String.join(";", chiffres);

        this.fm.saveChiffre(chiffre);
    }

    /**
     * decrypts the chiffrefile
     */
    private void decryptFile() {

        //get chiffre String and split it into the character pais.
        String chiffre = this.fm.getChiffreText();
        String[] chiffrePair = chiffre.split(";");

        StringBuilder text = new StringBuilder();

        BigInteger secretKey = this.fm.getSecretKey();


        //Streams the pairs and decrypts each pair and writes the text.
        Arrays.stream(chiffrePair).forEach(pair -> {
            String[] pairs = pair.replace("(", "").replace(")", "").split(",");

            if (pairs.length != 2){
                throw new RuntimeException("Error in chiffre.txt Formating");
            }

            BigInteger y1 = new BigInteger(pairs[0]);
            BigInteger y2 = new BigInteger(pairs[1]);

            BigInteger inverseY1HB = y1.modPow(secretKey, N).modInverse(N);

            BigInteger charBigInteger = y2.multiply(inverseY1HB).mod(N);

            text.append((char) charBigInteger.intValue());
        });

        System.out.println(text);
        //saves the File.
        this.fm.saveDecrypted(text.toString());
    }


    /**
     * @return random BigInteger between 0 and ord(n).
     */
    private BigInteger getRandomBigInteger() {
        BigInteger upperLimit = N.subtract(BigInteger.ONE);

        Random randomSource = new Random();

        BigInteger randomNumber;
        do {
            randomNumber = new BigInteger(upperLimit.bitLength(), randomSource);
        } while (randomNumber.compareTo(upperLimit) >= 0);
        return randomNumber;
    }


}
