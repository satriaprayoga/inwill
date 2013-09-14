/**
 * 
 */
package com.inwill.dci.snmp;

import org.snmp4j.PDU;

/**
 * @author G.S Prayoga of SWG
 *
 */
public class GetSnmpProcessor extends SnmpProcessor{

	@Override
	public void process(SnmpMessage snmpMessage, Device device) {
		configTarget(snmpMessage, device, PDU.GET);
	}

}
