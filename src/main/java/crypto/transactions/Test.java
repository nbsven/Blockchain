package crypto.transactions;


import org.bitcoinj.core.*;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet2Params;
import org.bitcoinj.params.TestNet3Params;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Test {
    public static void main(String[] args) throws AddressFormatException {
        final NetworkParameters networkParameters = MainNetParams.get();

        Wallet wallet = null;
        final File walletFile = new File("test.wallet");
        try {
            wallet = new Wallet(networkParameters);

            for (int i = 0; i < 5; i++) {

                wallet.importKey(new ECKey());
            }

            wallet.saveToFile(walletFile);

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("key="+generateKeyFromString("Plak"));


//        wallet.addWatchedAddress()
        System.out.println(wallet.currentReceiveAddress());
        System.out.println(wallet.freshReceiveKey());
        System.out.println(wallet.freshReceiveKey());
        System.out.println(wallet.freshReceiveKey());
        System.out.println(wallet.getImportedKeys().size());
        String s = wallet.isPubKeyHashMine(wallet.getImportedKeys().get(0).getPubKeyHash()) ? "mine" : "not mine";
        System.out.println(wallet);
        System.out.println(s);

    }

    public static ECKey generateKeyFromString(String name) {
        ECKey key;
        boolean flag = false;
        name = name.toUpperCase();
        while (true) {
            key = new ECKey();
            String s = key.toAddress(TestNet3Params.get()).toString();
            if (s.charAt(0) == '1' || s.charAt(0) == 'n') {
//                System.out.println("0");
//                System.out.println(s.substring(0,2));
                if (s.charAt(1) == name.charAt(0) || s.charAt(1) == (char) ((int) name.charAt(0) + 32)) {
//                    System.out.println("1");
                    if (s.charAt(2) == name.charAt(1) || s.charAt(2) == (char) ((int) name.charAt(1) + 32)) {
//                        System.out.println("2");
                        if (s.charAt(3) == name.charAt(2) || s.charAt(3) == (char) ((int) name.charAt(2) + 32)) {
                            System.out.println("3");
                            if (s.charAt(4) == name.charAt(3) || s.charAt(4) == (char) ((int) name.charAt(3) + 32)) {
                                System.out.println(s);
                                System.out.println("4");
                                break;
                            }
                        }
                    }
                }
            }
//            System.out.println(s);
        }
        return key;
    }
}
