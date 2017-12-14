package crypto.transactions;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.Utils;
import org.bitcoinj.script.Script;

import java.io.File;
import java.math.BigInteger;

public class LinearEquationTransaction extends ScriptTransaction {
    // TODO: Задание 2
    public LinearEquationTransaction(NetworkParameters parameters, File file, String password) {
        super(parameters, file, password);
    }

    @Override
    public Script createInputScript() {
        // TODO: Создайте скрипт, требующий нахождения двух числе x и y, таких что x+y=ваш месяц рождения, |x-y|=ваш день рождения (если требуется, то +1)
        return null;
    }

    @Override
    public Script createRedemptionScript(Transaction unsignedScript) {
        // TODO: Создайте скрипт для расходования этих денег
        return null;
    }

    private byte[] encode(BigInteger bigInteger) {
        return Utils.reverseBytes(Utils.encodeMPI(bigInteger, false));
    }
}
