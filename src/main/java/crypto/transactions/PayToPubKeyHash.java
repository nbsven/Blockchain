package crypto.transactions;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.script.Script;

import java.io.File;

public class PayToPubKeyHash extends ScriptTransaction {
    // TODO: Задание 1

    public PayToPubKeyHash(NetworkParameters parameters, File file, String password) {
        super(parameters, file, password);
    }

    @Override
    public Script createInputScript() {
        // TODO: Создайте P2PKH-скрипт
        return null;
    }

    @Override
    public Script createRedemptionScript(Transaction unsignedTransaction) {
        // TODO: Потратьте деньги с этой P2PKH-транзакции
        return null;
    }
}
