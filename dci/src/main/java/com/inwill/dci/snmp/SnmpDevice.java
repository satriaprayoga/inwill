/**
 * 
 */
package com.inwill.dci.snmp;

import org.snmp4j.mp.SnmpConstants;

/**
 * @author G.S Prayoga of SWG
 *
 */
public class SnmpDevice implements Device{

	private static final long serialVersionUID = 8200556102311778903L;
	
	private String address;
	private String community;
	private int version;
	
	public SnmpDevice(String address,String community,int version) {
		this.address=address;
		this.community=community;
		this.version=version;
	}
	
	public SnmpDevice(String address,String community) {
		this(address, community, SnmpConstants.version2c);
	}

	public String getAddress() {
		return address;
	}

	public String getCommunity() {
		return community;
	}

	public int getVersion() {
		return version;
	}

	

}
