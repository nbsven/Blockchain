package crypto.transactions;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.TransactionSignature;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;

import java.io.File;

import static org.bitcoinj.script.ScriptOpCodes.*;

public class PayToPubKeyHash extends ScriptTransaction {
    private ECKey key;


    public PayToPubKeyHash(NetworkParameters parameters, File file, String password) {
        super(parameters, file, password);
        if(getWallet().getImportedKeys().size()==0){
            key = generateKeyFromString("plak",getParameters());
            getWallet().importKey(key);
        }else {
            key = getWallet().getImportedKeys().get(0);
        }


    }

    @Override
    public Script createInputScript() {
        ScriptBuilder builder = new ScriptBuilder();
        builder.op(OP_DUP);
        builder.op(OP_HASH160);
        builder.data(key.getPubKeyHash());
        builder.op(OP_EQUALVERIFY);
        builder.op(OP_CHECKSIG);

        return builder.build();
    }

    @Override
    public Script createRedemptionScript(Transaction unsignedTransaction) {
        TransactionSignature txSig = sign(unsignedTransaction, key);

        ScriptBuilder builder = new ScriptBuilder();
        builder.data(txSig.encodeToBitcoin());
        builder.data(key.getPubKey());
        return builder.build();
    }

    public ECKey generateKeyFromString(String name, NetworkParameters networkParameters) {
        ECKey key;
        boolean flag = false;
        name = name.toUpperCase();
        while (true) {
            key = new ECKey();
            String s = key.toAddress(networkParameters).toString();
            if (s.charAt(0) == 'm' || s.charAt(0) == 'n' || s.charAt(0)=='1' || s.charAt(0)=='3') {
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
