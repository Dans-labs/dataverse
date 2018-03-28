/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.harvard.iq.dataverse.pidprovider;

import edu.harvard.iq.dataverse.engine.command.CommandContext;
import edu.harvard.iq.dataverse.pidprovider.contract.IPIDProvider;
import edu.harvard.iq.dataverse.settings.SettingsServiceBean;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author devel
 */
public class PIDService {

    private static final Map<String, IPIDProvider> map = new java.util.concurrent.ConcurrentHashMap<>();
    private static final Logger logger = Logger.getLogger(PIDService.class.getCanonicalName());

    private PIDService() {
    }

    /**
     * Returns the PID provider as configured by the application. Always returns
     * the same object;
     *
     * @param protocol Is ambiguous, the plugin to use is defined by its name.
     * @param ctxt Contains the provider name.
     * @return
     * @throws Exception
     */
    public static IPIDProvider getProvider(String protocol, CommandContext ctxt) throws Exception {
        logger.log(Level.FINE, "getting PID provider, protocol=" + protocol);
        String nonNullDefaultIfKeyNotFound = "";
        
        if (protocol == null) {
            protocol = ctxt.settings().getValueForKey(SettingsServiceBean.Key.Protocol, nonNullDefaultIfKeyNotFound);
        }
        
        String providerName = ctxt.settings().getValueForKey(SettingsServiceBean.Key.DoiProvider, nonNullDefaultIfKeyNotFound);

        // Exception for compatibility reasons
        // In case of handles there is no provider name given, but Handle is the only plugin for handles so...
        if (protocol.equals("hdl")) {
            providerName = "Handle";
        }

        IPIDProvider pidProvider = map.get(providerName);

        if (pidProvider == null) {
            synchronized (map) {
                pidProvider = map.get(providerName);
                if (pidProvider == null) {
                    loadPlugins();
                }
            }
            pidProvider = map.get(providerName);
        }
        return pidProvider;
    }

    private static void loadPlugins() throws Exception {
        map.clear();
        Iterator<IPIDProvider> iter = ServiceLoader.load(IPIDProvider.class).iterator();
        while (iter.hasNext()) {
            IPIDProvider p = iter.next();
            if (!map.containsKey(p.getPluginName())) {
                try {
                    p.start();
                } catch (Exception ex) {
                    throw new Exception("Loading plugin: " + p.getPluginName() + " caused a problem.", ex);
                }
                map.put(p.getPluginName(), p);
            } else {
                logger.log(Level.SEVERE, "A plugin with name \"" + p.getPluginName() + "\" was tried to add, but is already present.");
            }
        }
        // Log which plugins could be loaded.
        String pluginList = "";
        for (Entry e : map.entrySet()) {
            pluginList += e.getValue().toString() + "\n";
        }
        logger.log(Level.INFO, "Plugins loaded: \n" + pluginList);
    }

}
