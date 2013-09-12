package com.inwill.emulator;

public abstract class SnmpRequestProcessor {

	private String address;
	
	private int version;
	
	public SnmpRequestProcessor(String address,int version) {
		this.address=address;
		this.version=version;
	}
}
