/*
 *  Copyright (C) GridGain Systems. All Rights Reserved.
 *  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.examples.auth;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.configuration.CacheConfiguration;

/**
 * Starts up an server node with access control list example configuration.
 */
public class GridAclExampleServerNodeStartup {
    /**
     * Executes example.
     *
     * @param args Command line arguments, none required.
     */
    public static void main(String[] args) {
        // Starting server node.
        Ignite ignite = Ignition.start("examples/config/server-auth.xml");

        generateData(ignite);
    }

    /**
     * Generates data.
     *
     * @param ignite Ignite.
     */
    public static void generateData(Ignite ignite){
        CacheConfiguration<Integer, Integer> cfg = new CacheConfiguration<>();

        cfg.setAtomicityMode(CacheAtomicityMode.ATOMIC);
        cfg.setBackups(1);
        cfg.setName("cache");

        // Creating cache.
        IgniteCache<Integer, Integer> cache = ignite.getOrCreateCache(cfg);

        System.out.println("Put entry [key=3, value=3].");

        // Successful put.
        cache.put(3, 3);

        System.out.println("Got entry [key=3, value=" + Integer.toString(cache.get(3))+ "].");

        // Successful read.
        assert cache.get(3) == 3;
    }
}
