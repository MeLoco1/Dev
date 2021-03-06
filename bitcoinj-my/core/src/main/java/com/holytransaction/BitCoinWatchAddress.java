package com.holytransaction;

import com.google.bitcoin.core.*;
import com.google.bitcoin.net.discovery.DnsDiscovery;
import com.google.bitcoin.params.TestNet3Params;
import com.google.bitcoin.store.BlockStore;
import com.google.bitcoin.store.BlockStoreException;
import com.google.bitcoin.store.SPVBlockStore;

import java.io.File;
import java.math.BigInteger;

public class BitCoinWatchAddress {
    public static void main(String[] args) {
        try {
            NetworkParameters params = TestNet3Params.get();
            Wallet wallet = new Wallet(params);
            Address address = new Address(params, "mimoZNLcP2rrMRgdeX5PSnR7AjCqQveZZ4");
            wallet.addWatchedAddress(address);
            BlockStore blockStore = new SPVBlockStore(params, new File("blockchain.spv"));
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
                    System.out.println("Received tx for " + Utils.bitcoinValueToFriendlyString(value) + ": " + tx);
                }

                @Override
                public void onTransactionConfidenceChanged(Wallet wallet, Transaction tx) {
                    System.out.println("transaction confidence changed for " + tx.toString());
                }
            });
        } catch (BlockStoreException e) {
            System.out.println(e);
        } catch (AddressFormatException e) {
            e.printStackTrace();
        }
    }
}
