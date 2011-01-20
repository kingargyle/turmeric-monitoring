/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.model;

import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.ebayopensource.turmeric.utils.jpa.model.Persistent;

@Entity
public class Machine extends Persistent {
    private String hostAddress;
    private String canonicalHostName;
    @ManyToOne
    private MachineGroup machineGroup;

    protected Machine() {
    }

    public Machine(String hostAddress, String canonicalHostName, MachineGroup machineGroup) {
        this.hostAddress = hostAddress;
        this.canonicalHostName = canonicalHostName;
        this.machineGroup = machineGroup;
    }

    public String getHostAddress() {
        return hostAddress;
    }

    public String getCanonicalHostName() {
        return canonicalHostName;
    }

    public MachineGroup getMachineGroup() {
        return machineGroup;
    }

    public static Machine newMachine() {
        try {
            InetAddress address = InetAddress.getLocalHost();
            return new Machine(address.getHostAddress(), address.getCanonicalHostName(), null);
        } catch (UnknownHostException x) {
            throw new RuntimeException(x);
        }
    }
}
