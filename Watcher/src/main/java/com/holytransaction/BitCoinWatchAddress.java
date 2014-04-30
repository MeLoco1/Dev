package com.holytransaction;

import com.google.bitcoin.core.*;
import com.google.bitcoin.net.discovery.DnsDiscovery;
import com.google.bitcoin.params.MainNetParams;
import com.google.bitcoin.params.TestNet3Params;
import com.google.bitcoin.store.BlockStore;
import com.google.bitcoin.store.BlockStoreException;
import com.google.bitcoin.store.SPVBlockStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;

public class BitCoinWatchAddress {
    final Logger LOG = LoggerFactory.getLogger(BitCoinWatchAddress.class);


    public void watchBTC(ArrayList<String> addresses) throws Exception {

        if (addresses == null) {
            throw new Exception("no address");
        }
        try {
//            NetworkParameters params = MainNetParams.get();
            NetworkParameters params = TestNet3Params.get();
            Wallet wallet = new Wallet(params);

            for (String strAddress : addresses) {
                Address address = new Address(params, strAddress);
                wallet.addWatchedAddress(address);
            }

            BlockStore blockStore = new SPVBlockStore(params, new File(Options.BITCOIN_BLOCKSTORE_PATH));
            BlockChain chain = new BlockChain(params, wallet, blockStore);
            PeerGroup peerGroup = new PeerGroup(params, chain);
            peerGroup.addWallet(wallet);
            peerGroup.addPeerDiscovery(new DnsDiscovery(params));
            peerGroup.startAndWait();
            peerGroup.downloadBlockChain();

            wallet.addEventListener(new AbstractWalletEventListener() {
                @Override
                public void onCoinsReceived(Wallet w, Transaction tx, BigInteger prevBalance, BigInteger newBalance) {
                    // The transaction "tx" can either be pending, or included into a block (we didn't see the broadcast).
                    BigInteger value = tx.getValueSentToMe(w);
                    LOG.info("Received tx for " + Utils.bitcoinValueToFriendlyString(value) + ": " + tx);
                }

                @Override
                public void onCoinsSent(Wallet wallet, Transaction tx, BigInteger prevBalance, BigInteger newBalance) {
                    BigInteger value = tx.getValueSentToMe(wallet);
                    LOG.info("Sent tx for " + Utils.bitcoinValueToFriendlyString(value) + ": " + tx);
                }

                @Override
                public void onTransactionConfidenceChanged(Wallet wallet, Transaction tx) {
                    LOG.info("transaction confidence changed for " + tx.toString());
                }
            });
        } catch (BlockStoreException e) {
            System.out.println(e);
        } catch (AddressFormatException e) {
            e.printStackTrace();
        }
    }
}






