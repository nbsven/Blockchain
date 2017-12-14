package crypto.transactions;

import org.bitcoinj.core.*;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.TransactionSignature;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;

import java.io.File;

import static org.bitcoinj.script.ScriptOpCodes.*;

public class PayToPubKey extends ScriptTransaction {
    private ECKey key;

    public PayToPubKey(NetworkParameters parameters, File file, String password) {
        super(parameters, file, password);
        key = getWallet().getImportedKeys().get(0);
    }

    @Override
    public Script createInputScript() {
        ScriptBuilder builder = new ScriptBuilder();
        builder.data(key.getPubKey());
        builder.op(OP_CHECKSIG);
        return builder.build();
    }

    @Override
    public Script createRedemptionScript(Transaction unsignedTransaction) {
        TransactionSignature txSig = sign(unsignedTransaction, key);

        ScriptBuilder builder = new ScriptBuilder();
        builder.data(txSig.encodeToBitcoin());
        return builder.build();
    }
}
