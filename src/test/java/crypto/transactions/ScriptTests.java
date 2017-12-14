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

import java.io.File;

public class ScriptTests {
    // TODO: поставьте в true для использования mainnet.
    private boolean useMainNet = false;
    // TODO: Измените этот адрес на адрес тестового сервиса для получения биткоинов, который вы использовали
    private static final String faucetAddress = "mxBY3BbSD7xL35EHFNEWDDAwJdLbhFJBb1";

    private String wallet_name;
    private NetworkParameters networkParameters;
    private WalletAppKit kit;

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
        kit = new WalletAppKit(networkParameters, new File(wallet_name), "oxygen");
    }

    public void downloadBlockchain() {
        LOGGER.info("Starting to sync blockchain. This might take a few minutes");
        kit.setAutoSave(true);
        kit.startAsync();
        kit.awaitRunning();
//        kit.wallet().allowSpendingUnconfirmedTransactions();
        LOGGER.info("Synced blockchain.");
        LOGGER.info("You've got " + kit.wallet().getBalance() + " in your pocket");
    }

    public ECKey generateKeyFromString(String name) {
        ECKey key;
        boolean flag = false;
        name = name.toUpperCase();
        while (true) {
            key = new ECKey();
            String s = key.toAddress(TestNet3Params.get()).toString();
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

    @Test
    public void printAddress() {
        downloadBlockchain();

        System.out.println(kit.wallet());
        LOGGER.info("Your address is {}", kit.wallet().currentReceiveAddress());
        kit.stopAsync();
        kit.awaitTerminated();
    }

    private void testTransaction(ScriptTransaction scriptTransaction) throws InsufficientMoneyException {
        final Script inputScript = scriptTransaction.createInputScript();
        Transaction transaction = scriptTransaction.createOutgoingTransaction(inputScript, Coin.CENT);
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
        try (ScriptTransaction payToPubKey = new PayToPubKey(networkParameters, new File(wallet_name), "oxygen")) {
            testTransaction(payToPubKey);

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    // TODO: Раскомментируйте этот тест, когда будете готовы с PayToPubKeyHash
//    @Test
//    public void testPayToPubKeyHash() throws InsufficientMoneyException {
//        try (ScriptTransaction payToPubKeyHash = new PayToPubKeyHash(networkParameters, new File(wallet_name), "password")) {
//            testTransaction(payToPubKeyHash);
//        } catch (Exception e) {
//            e.printStackTrace();
//            Assert.fail(e.getMessage());
//        }
//    }

    // TODO: Раскомментируйте этот тест для проверки LinearEquationTransaction
//    @Test
//    public void testLinearEquation() throws InsufficientMoneyException {
//        try (LinearEquationTransaction linEq = new LinearEquationTransaction(networkParameters, new File(wallet_name), "password")) {
//            testTransaction(linEq);
//        } catch (Exception e) {
//            e.printStackTrace();
//            Assert.fail(e.getMessage());
//        }
//    }

    // TODO: Раскомментируйте, когда будет готова MultiSigTransaction
//    @Test
//    public void testMultiSig() throws InsufficientMoneyException {
//        try (ScriptTransaction multiSig = new MultiSigTransaction(networkParameters, new File(wallet_name), "password")) {
//            testTransaction(multiSig);
//        } catch (Exception e) {
//            e.printStackTrace();
//            Assert.fail(e.getMessage());
//        }
//    }

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
