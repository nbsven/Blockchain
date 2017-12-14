package crypto.transactions;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.script.Script;

import java.io.File;

public class MultiSigTransaction extends ScriptTransaction {
    // TODO: Задание 3

    public MultiSigTransaction(NetworkParameters parameters, File file, String password) {
        super(parameters, file, password);
    }

    @Override
    public Script createInputScript() {
        // TODO: Создайте скрипт, который требует подпись банка и одного из трех клиентов
        return null;
    }

    @Override
    public Script createRedemptionScript(Transaction unsignedTransaction) {
        // Не забудьте про баг в CHECK_MULTISIG!
        // TODO: Создайте скрипт для расходования денег
        return null;
    }
}
