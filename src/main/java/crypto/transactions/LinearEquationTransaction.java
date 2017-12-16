package crypto.transactions;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.Utils;
import org.bitcoinj.crypto.TransactionSignature;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;
import org.bitcoinj.script.ScriptChunk;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

import static org.bitcoinj.script.ScriptOpCodes.*;

public class LinearEquationTransaction extends ScriptTransaction {
    // TODO: Задание 2

    private int day = 14;
    private int month = 2 + 32;

    public LinearEquationTransaction(NetworkParameters parameters, File file, String password) {
        super(parameters, file, password);
    }

    @Override
    public Script createInputScript() {

        ScriptBuilder builder = new ScriptBuilder();

        builder.op(OP_2DUP);
        builder.op(OP_ADD);
        byte[] mas = {(byte) month};
        ScriptChunk chunk = new ScriptChunk(OP_PUSHDATA1, mas);
        builder.addChunk(chunk);

        builder.op(OP_NUMEQUALVERIFY);
        builder.op(OP_SUB);
        byte[] mas1 = {(byte) day};
        chunk = new ScriptChunk(OP_PUSHDATA1, mas1);
        builder.addChunk(chunk);
        builder.op(OP_NUMEQUAL);

        return builder.build();
    }

    @Override
    public Script createRedemptionScript(Transaction unsignedScript) {
//        TransactionSignature transactionSignature=si

        ScriptBuilder builder = new ScriptBuilder();

        byte[] mas = {(byte) ((month + day) / 2)};
        ScriptChunk chunk = new ScriptChunk(OP_PUSHDATA1, mas);
        builder.addChunk(chunk);


        byte[] mas1 = {(byte) ((month - day) / 2)};
        chunk = new ScriptChunk(OP_PUSHDATA1, mas1);
        builder.addChunk(chunk);


        return builder.build();
    }

    private byte[] encode(BigInteger bigInteger) {
        return Utils.reverseBytes(Utils.encodeMPI(bigInteger, false));
    }
}
