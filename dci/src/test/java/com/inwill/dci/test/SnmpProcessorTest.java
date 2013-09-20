/**
 * 
 */
package com.inwill.dci.test;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.inwill.dci.snmp.Device;
import com.inwill.dci.snmp.GetSnmpProcessor;
import com.inwill.dci.snmp.SetSnmpProcessor;
import com.inwill.dci.snmp.SnmpDevice;
import com.inwill.dci.snmp.SnmpMessage;
import com.inwill.dci.snmp.SnmpProcessor;
import com.inwill.dci.snmp.WalkSnmpProcessor;

/**
 * @author G.S Prayoga of SWG
 *
 */
public class SnmpProcessorTest {
	
	protected final Logger log=Logger.getLogger("TEST");

	
	
	@Test
	public void testWalkSnmp(){
		log.info("Performing GETNEXT (walk) Test");
		Device device=new SnmpDevice("127.0.0.1/2010", "public");
		SnmpMessage snmpMessage=new SnmpMessage("1.3.6.1.2.1.1", "");
		SnmpProcessor get=new WalkSnmpProcessor();
		get.process(snmpMessage, device);
	}
	
//	@Test
//	public void testGetSnmp(){
//		log.info("Performing GET Test");
//		Device device=new SnmpDevice("192.168.1.1/161", "public");
//		SnmpMessage snmpMessage=new SnmpMessage("1.3.6.1.2.1.1.1.0", "");
//		SnmpProcessor get=new GetSnmpProcessor();
//		get.process(snmpMessage, device);
//	}
	
//	@Test
//	public void testSetSnmp(){
//		log.info("Performing SET Test");
//		Device device=new SnmpDevice("192.168.1.1/161", "public");
//		SnmpMessage snmpMessage=new SnmpMessage("1.3.6.1.2.1.1.4.0", "modem");
//		SnmpProcessor get=new SetSnmpProcessor();
//		get.process(snmpMessage, device);
//	}
}
