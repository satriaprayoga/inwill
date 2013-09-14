/**
 * 
 */
package com.inwill.dci.snmp;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;

/**
 * @author G.S Prayoga of SWG
 *
 */
public abstract class SnmpProcessor {
	
	protected PDU requestPDU;
	protected Address targetAddress;
	protected CommunityTarget communityTarget;
	protected OID targetOID;
	
	protected void configTarget(SnmpMessage snmpMessage, Device device,int type){
		targetAddress = new UdpAddress(device.getAddress());
		targetOID = new OID(snmpMessage.getOid());

		requestPDU = snmpMessage.toRequestPDU(type);

		communityTarget = new CommunityTarget();
		communityTarget.setAddress(targetAddress);
		communityTarget.setCommunity(new OctetString(device.getCommunity()));
		communityTarget.setVersion(device.getVersion());
	}

	public abstract void process(SnmpMessage snmpMessage,Device device);
}
