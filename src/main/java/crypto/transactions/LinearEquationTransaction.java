package crypto.transactions;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.Utils;
import org.bitcoinj.crypto.TransactionSignature;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;

import java.io.File;
import java.math.BigInteger;

import static org.bitcoinj.script.ScriptOpCodes.*;

public class LinearEquationTransaction extends ScriptTransaction {
    // TODO: Задание 2

    private long day=14;
    private long month=2;

    public LinearEquationTransaction(NetworkParameters parameters, File file, String password) {
        super(parameters, file, password);
    }

    @Override
    public Script createInputScript() {
        ScriptBuilder builder = new ScriptBuilder();
        builder.op(OP_2DUP);

        builder.op(OP_ADD);
        builder.data(encode(BigInteger.valueOf(month)));
        builder.op(OP_NUMEQUALVERIFY);

        builder.op(OP_SUB);
        builder.data(encode(BigInteger.valueOf(day)));
        builder.op(OP_NUMEQUAL);

        return builder.build();
    }

    @Override
    public Script createRedemptionScript(Transaction unsignedScript) {

        ScriptBuilder builder = new ScriptBuilder();
        builder.data(encode(BigInteger.valueOf((day+month)/2)));
        builder.data(encode(BigInteger.valueOf((month-day)/2)));

        return builder.build();
    }

    private byte[] encode(BigInteger bigInteger) {
        return Utils.reverseBytes(Utils.encodeMPI(bigInteger, false));
    }
}
