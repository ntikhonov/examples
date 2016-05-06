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
import org.apache.ignite.plugin.security.SecurityException;

/**
 * This example demonstrates use of Access control list with GridGain cache.
 * <p>
 * To execute this example you should start an instance of {@code GridAclExampleServerNodeStartup}
 * class which will start up a GridGain remote server node with proper configuration.
 * <p>
 * Alternatively a remote server node can be started by execution the following command in a command line:
 * {IGNITE_HOME}/bin/ignite.{bat|sh} {IGNITE_HOME}/examples/config/server-auth.xml
 * <p>
 * After the remote node has been started, set IGNITE_HOME environment variable referring to GridGain module location
 * and run this example which will create and put entry to cache and then read it.
 */
public class GridAclReadonlyExample {
    /**
     * Executes example.
     *
     * @param args Command line arguments, none required.
     */
    public static void main(String[] args) {
        System.out.println("Node has permissions to get only.");

        // Starting client node with readonly permissions.
        try (Ignite ignite = Ignition.start("examples/config/client-auth-readonly.xml")) {
            CacheConfiguration<Integer, Integer> cfg = new CacheConfiguration<>();

            cfg.setAtomicityMode(CacheAtomicityMode.ATOMIC);
            cfg.setBackups(1);
            cfg.setName("cache");

            // Creating cache.
            IgniteCache<Integer, Integer> cache = ignite.getOrCreateCache(cfg);

            try {
                System.out.println("Try to put entry [key=2, value=2].");

                // Unsuccessful put. SecurityException will be thrown.
                cache.put(2, 2);

                // Should not happen.
                System.out.println("Successful put [key=2, value=2].");

                assert false : "ACL failed";
            }
            catch (SecurityException ex) {
                System.out.println("Put failed due security permission.");
            }

            System.out.println("Got entry added by server node [key=3, value=" + Integer.toString(cache.get(3))+ "].");

            // Successful read of data pregenerated by server node(s).
            assert cache.get(3) == 3;
        }
    }
}
