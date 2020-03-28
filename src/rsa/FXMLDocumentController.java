package rsa;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.io.DataOutputStream;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;

public class FXMLDocumentController implements Initializable {

    @FXML
    private TextField mtext;
    @FXML
    private TextArea mta;

    String cipherText, message;
    KeyPair pair;

    public void sendToAndroid() {

        try {
            
            String message = mtext.getText();
            cipherText = encrypt(message, pair.getPrivate());
            byte[] encodedPK = pair.getPublic().getEncoded();
            
            Socket socket = new Socket("192.168.1.3", 7301);
            DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
            dOut.writeUTF(cipherText);
            dOut.writeInt(encodedPK.length);
            dOut.write(encodedPK);
            dOut.flush();
            dOut.close();
            socket.close();

        } catch (Exception ex) {

            System.out.println(ex.getMessage());
        }

    }

    public void showEncryptedM() {

        try {

            mta.setWrapText(true);
            message = mtext.getText();
            pair = generateKeyPair();
            cipherText = encrypt(message, pair.getPrivate());
            mta.setText("Encrypted Message By RSA :\n\n" + cipherText);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    public static KeyPair generateKeyPair() throws Exception {

        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(512, new SecureRandom());
        KeyPair pair = generator.generateKeyPair();

        return pair;
    }

    public static String encrypt(String plainText, PrivateKey privatekey) throws Exception {

        Cipher encryptCipher = Cipher.getInstance("RSA");

        encryptCipher.init(Cipher.ENCRYPT_MODE, privatekey);

        byte[] cipherText = encryptCipher.doFinal(plainText.getBytes());

        return Base64.getEncoder().encodeToString(cipherText);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}



