package com.holytransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Watcher {
    private final static Logger LOG = LoggerFactory.getLogger(Watcher.class);

    private final static int BTC = 0;

    public static void main(String[] args) {

        if (args.length != 2) {
            LOG.warn("Wrong parameters");
            System.exit(0);
        }

        String keyFilePath = null;
        String currency = null;
        for (String arg : args) {
            if (arg.contains("-currency=")) {
                currency = (arg.split("="))[1];
            }
            if (arg.contains("-keys=")) {
                keyFilePath = (arg.split("="))[1];

            }
        }

        ArrayList<String> keys = new ArrayList<>();
        readKeys(keyFilePath, keys);


        switch (currency) {
            case "BTC": {
                try {
                    new BitCoinWatchAddress().watchBTC(keys);
                } catch (Exception e) {
                    LOG.error(e.getMessage(), e);
                    e.printStackTrace();
                }
            }
            default: {
                LOG.warn("No such currency\n Available parameters : BTC, LTC, DOGE, PPC, XPR");
            }
        }
    }

    private static void readKeys(String keyFilePath, ArrayList<String> keys) {
        try (BufferedReader br = new BufferedReader(new FileReader(keyFilePath))) {
            String fileLine;
            while ((fileLine = br.readLine()) != null) {
                keys.add(fileLine);
            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
