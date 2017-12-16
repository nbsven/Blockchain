package crypto.transactions;

import org.bitcoinj.core.*;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.script.Script;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongycastle.util.encoders.Hex;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

public class ScriptTests {
    public static final String PASSWORD = "eshkere";
    // TODO: поставьте в true для использования mainnet.
    private boolean useMainNet = false;
    // TODO: Измените этот адрес на адрес тестового сервиса для получения биткоинов, который вы использовали
    private static final String faucetAddress = "mxBY3BbSD7xL35EHFNEWDDAwJdLbhFJBb1";

    private String wallet_name;
    private NetworkParameters networkParameters;
    private WalletAppKit kit;
    private Coin coins;

    private static final Logger LOGGER = LoggerFactory.getLogger(ScriptTests.class);

    public ScriptTests() {
        if (useMainNet) {
            networkParameters = new MainNetParams();
            wallet_name = "main-wallet";
            LOGGER.info("Running on mainnet.");
        } else {
            networkParameters = new TestNet3Params();
            wallet_name = "test-wallet";
            LOGGER.info("Running on testnet.");
        }
        kit = new WalletAppKit(networkParameters, new File(wallet_name), PASSWORD);
    }

    public void downloadBlockchain() {
        LOGGER.info("Starting to sync blockchain. This might take a few minutes");
        kit.setAutoSave(true);
        kit.startAsync();
        kit.awaitRunning();
        kit.wallet().allowSpendingUnconfirmedTransactions();
        LOGGER.info("Synced blockchain.");
        LOGGER.info("You've got " + kit.wallet().getBalance() + " in your pocket");
    }



    @Test
    public void printAddress() {
        downloadBlockchain();
//        kit.wallet().importKey(generateKeyFromString("Plak"));
//        System.out.println(kit.wallet().getPendingTransactions());
        System.out.println(kit.wallet());
        LOGGER.info("Your address is {}", kit.wallet().currentReceiveAddress());
        try (BufferedWriter writer=new BufferedWriter(new FileWriter(new File("Raw_transactions.txt")))) {
            for(Transaction t:kit.wallet().getPendingTransactions()){
                System.out.println("Raw Hex transaction: "+new String(Hex.encode(t.bitcoinSerialize())));
                writer.write(String.format("%S%n%S%n%n",t.getHashAsString(),new String(Hex.encode(t.bitcoinSerialize()))));
                System.out.println(t);
            }
        }catch (IOException e){
            e.printStackTrace();
        }

//        System.out.println(kit.wallet().getTransactions(false).iterator().next().bitcoinSerialize());
        kit.stopAsync();
        kit.awaitTerminated();
    }

    private void testTransaction(ScriptTransaction scriptTransaction) throws InsufficientMoneyException {
        final Script inputScript = scriptTransaction.createInputScript();
        Transaction transaction = scriptTransaction.createOutgoingTransaction(inputScript, coins);
        TransactionOutput relevantOutput = transaction.getOutputs().stream().filter(to -> to.getScriptPubKey().equals(inputScript)).findAny().get();
        Transaction redemptionTransaction = scriptTransaction.createUnsignedRedemptionTransaction(relevantOutput, scriptTransaction.getReceiveAddress());
        Script redeemScript = scriptTransaction.createRedemptionScript(redemptionTransaction);
        scriptTransaction.testScript(inputScript, redeemScript, redemptionTransaction);
        redemptionTransaction.getInput(0).setScriptSig(redeemScript);
        scriptTransaction.sendTransaction(transaction);
        scriptTransaction.sendTransaction(redemptionTransaction);
    }

    // TODO: раскомментируйте этот тест, когда у вас будут биткоины в testnet или mainnet, чтобы проверить, что транзакции работают как ожидается
    @Test
    public void testPayToPubKey() throws InsufficientMoneyException {
        try (ScriptTransaction payToPubKey = new PayToPubKey(networkParameters, new File(wallet_name), PASSWORD)) {
            testTransaction(payToPubKey);

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    // TODO: Раскомментируйте этот тест, когда будете готовы с PayToPubKeyHash
    @Test
    public void testPayToPubKeyHash() throws InsufficientMoneyException {
        try (ScriptTransaction payToPubKeyHash = new PayToPubKeyHash(networkParameters, new File(wallet_name), PASSWORD)) {
            coins = Coin.valueOf(1_000_000);
            testTransaction(payToPubKeyHash);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    // TODO: Раскомментируйте этот тест для проверки LinearEquationTransaction
    @Test
    public void testLinearEquation() throws InsufficientMoneyException {
        try (LinearEquationTransaction linEq = new LinearEquationTransaction(networkParameters, new File(wallet_name), PASSWORD)) {
            coins = Coin.valueOf(2_000_000);
            testTransaction(linEq);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

//     TODO: Раскомментируйте, когда будет готова MultiSigTransaction
    @Test
    public void testMultiSig() throws InsufficientMoneyException {
        try (ScriptTransaction multiSig = new MultiSigTransaction(networkParameters, new File(wallet_name), PASSWORD)) {
            coins = Coin.valueOf(3_000_000);
            testTransaction(multiSig);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    // TODO: Раскомментируйте, когда будете готовы вернуть тестовые биткоины сервису, от которого вы их получили
//    @Test
//    public void sendMoneyBackToFaucet() throws AddressFormatException, InsufficientMoneyException {
//        if (useMainNet) {
//            return;
//        }
//        downloadBlockchain();
//        Transaction transaction = kit.wallet().createSend(new Address(networkParameters, faucetAddress), kit.wallet().getBalance().subtract(Coin.MILLICOIN));
//        kit.wallet().commitTx(transaction);
//        kit.peerGroup().broadcastTransaction(transaction);
//        kit.stopAsync();
//        kit.awaitTerminated();
//    }
}
