package com.inwill.emulator;

import java.util.Collection;

import org.snmp4j.smi.OID;

public interface SnmpWalker<E> {

	public Collection<E> processWalk(String address, String communityString, OID rootOid,int version); 
}
