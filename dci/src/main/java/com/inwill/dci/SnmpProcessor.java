/**
 * 
 */
package com.inwill.dci;

/**
 * @author G.S Prayoga of SWG
 *
 */
public interface SnmpProcessor {

	public void process(SnmpMessage snmpMessage,Device device);
}
