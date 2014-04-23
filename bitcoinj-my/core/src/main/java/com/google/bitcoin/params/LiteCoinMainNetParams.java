package com.google.bitcoin.params;

import com.google.bitcoin.core.NetworkParameters;
import com.google.bitcoin.core.Sha256Hash;
import com.google.bitcoin.core.Utils;

import static com.google.common.base.Preconditions.checkState;

/**
 * Parameters for the main production network on which people trade goods and services.
 */
public class LiteCoinMainNetParams extends NetworkParameters {
    public LiteCoinMainNetParams() {
        super();
        interval = INTERVAL;
        targetTimespan = TARGET_TIMESPAN;
        proofOfWorkLimit = Utils.decodeCompactBits(0x1d00ffffL);
        acceptableAddressCodes = new int[] { 48 };
        dumpedPrivateKeyHeader = 128;
        addressHeader = 48;
        port = 9333;
        packetMagic = 0xfbc0b6dbL;
        genesisBlock.setDifficultyTarget(0x1d00ffffL);
        genesisBlock.setTime(1317972665L);
        genesisBlock.setNonce(2084524493L);
        genesisBlock.setMerkleRoot(new Sha256Hash("97ddfbbae6be97fd6cdf3e7ca13232a3afff2353e29badfab7f73011edd4ced9"));
        id = ID_MAINNET;
        subsidyDecreaseBlockCount = 840000;
        spendableCoinbaseDepth = 100;
        String genesisHash = genesisBlock.getScryptHashAsString();

        // This contains (at a minimum) the blocks which are not BIP30 compliant. BIP30 changed how duplicate
        // transactions are handled. Duplicated transactions could occur in the case where a coinbase had the same
        // extraNonce and the same outputs but appeared at different heights, and greatly complicated re-org handling.
        // Having these here simplifies block connection logic considerably.
        /*
        checkpoints.put(91722, new Sha256Hash("00000000000271a2dc26e7667f8419f2e15416dc6955e5a6c6cdf3f2574dd08e"));
        checkpoints.put(91812, new Sha256Hash("00000000000af0aed4792b1acee3d966af36cf5def14935db8de83d6f9306f2f"));
        checkpoints.put(91842, new Sha256Hash("00000000000a4d0a398161ffc163c503763b1f4360639393e0e4c8e300e0caec"));
        checkpoints.put(91880, new Sha256Hash("00000000000743f190a18c5577a3c2d2a1f610ae9601ac046a38084ccb7cd721"));
        checkpoints.put(200000, new Sha256Hash("000000000000034a7dedef4a161fa058a2d67a173a90155f3a2fe6fc132e0ebf"));
        */

        dnsSeeds = new String[] {
                "dnsseed.litecointools.com",
                "dnsseed.litecoinpool.org",
                "dnsseed.ltc.xurious.com",
                "dnsseed.koin-project.com",
                "dnsseed.weminemnc.com"
        };
    }

    @Override
    public String getPaymentProtocolId() {
        return PAYMENT_PROTOCOL_ID_MAINNET;
    }

    private static LiteCoinMainNetParams instance;
    public static synchronized LiteCoinMainNetParams get() {
        if (instance == null) {
            instance = new LiteCoinMainNetParams();
        }
        return instance;
    }
}

