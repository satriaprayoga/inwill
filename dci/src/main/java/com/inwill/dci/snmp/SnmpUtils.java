/**
 * 
 */
package com.inwill.dci.snmp;

import java.util.HashMap;
import java.util.Map;

import org.snmp4j.smi.OID;
import org.snmp4j.smi.Variable;

/**
 * @author G.S Prayoga of SWG
 *
 */
public class SnmpUtils {
	
	static final Map<OID, Variable> oidMaps=new HashMap<OID,Variable>();
	
	public SnmpUtils() {
		// TODO Auto-generated constructor stub
	}
	
	protected static void loadMIB(String filename){
		
	}

}
