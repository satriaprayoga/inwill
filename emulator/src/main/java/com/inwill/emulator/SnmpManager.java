package com.inwill.emulator;

import org.snmp4j.CommunityTarget;
import org.snmp4j.Target;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OctetString;

public abstract class SnmpManager {
	
	
	protected Target createSnmpTarget(String address, String communityString,long rto,int retries,int version){
		Address targetAddress=GenericAddress.parse(address);
		CommunityTarget target=new CommunityTarget();
		target.setAddress(targetAddress);
		target.setCommunity(new OctetString(communityString));
		target.setRetries(retries);
		target.setTimeout(rto);
		target.setVersion(version);
		return target;
	}
	
}
