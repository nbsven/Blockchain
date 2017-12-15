package crypto.transactions;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.Wallet;
import org.bitcoinj.crypto.TransactionSignature;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;
import org.omg.CORBA.TRANSACTION_MODE;

import java.awt.image.AreaAveragingScaleFilter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.bitcoinj.script.ScriptOpCodes.*;

public class MultiSigTransaction extends ScriptTransaction {

    ECKey client1;
    ECKey client2;
    ECKey client3;
    ECKey bank;


    public MultiSigTransaction(NetworkParameters parameters, File file, String password) {
        super(parameters, file, password);
        Wallet wallet = getWallet();
        if (getWallet().getImportedKeys().size() < 4) {
            client1 = new ECKey();
            client2 = new ECKey();
            client3 = new ECKey();
            bank = new ECKey();
            wallet.importKey(client1);
            wallet.importKey(client2);
            wallet.importKey(client3);
            wallet.importKey(bank);
        } else {
            List<ECKey> list = wallet.getImportedKeys();
            client1 = list.get(0);
            client2 = list.get(1);
            client3 = list.get(2);
            bank = list.get(3);
        }
    }

    @Override
    public Script createInputScript() {
        ScriptBuilder builder = new ScriptBuilder();
        builder.op(OP_IF);
        builder.op(OP_2);
        builder.data(client1.getPubKey());
        builder.op(OP_ELSE);
        builder.op(OP_IF);
        builder.op(OP_2);
        builder.data(client2.getPubKey());
        builder.op(OP_ELSE);
        builder.op(OP_IF);
        builder.op(OP_2);
        builder.data(client3.getPubKey());
        builder.op(OP_ELSE);
        builder.op(OP_ENDIF);
        builder.op(OP_ENDIF);
        builder.op(OP_ENDIF);

        builder.data(bank.getPubKey());
        builder.op(OP_2);
        builder.op(OP_CHECKMULTISIG);
        return builder.build();

    }

    @Override
    public Script createRedemptionScript(Transaction unsignedTransaction) {
        TransactionSignature txSig2 = sign(unsignedTransaction, client2);
        TransactionSignature txSigB = sign(unsignedTransaction, bank);

        ScriptBuilder builder = new ScriptBuilder();
        builder.smallNum(0);
        builder.data(txSig2.encodeToBitcoin());
        builder.data(txSigB.encodeToBitcoin());
        builder.smallNum(1);
        builder.smallNum(0);
        return builder.build();
    }
}
