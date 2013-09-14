/**
 * 
 */
package com.inwill.dci.snmp;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.transport.DefaultUdpTransportMapping;

/**
 * @author G.S Prayoga of SWG
 *
 */
public class SetSnmpProcessor extends SnmpProcessor {
	
	private static final Logger log=Logger.getLogger(SetSnmpProcessor.class);

	@Override
	public void process(SnmpMessage snmpMessage, Device device) {
		configTarget(snmpMessage, device, PDU.SET);
		try {
			TransportMapping<?> transport=new DefaultUdpTransportMapping();
			Snmp snmp=new Snmp(transport);
			communityTarget.setRetries(RETRIES);
			communityTarget.setTimeout(RTO);
			transport.listen();
			ResponseListener responseListener=new ResponseListener() {
				
				@Override
				public void onResponse(ResponseEvent event) {
					PDU responsePDU;
					String responseStr;
					((Snmp)event.getSource()).cancel(event.getRequest(), this);
					responsePDU=event.getResponse();
					if(responsePDU!=null){
						responseStr=responsePDU.getErrorStatusText();
						log.info("SET response: "+responseStr);
					}
				}
			};
			snmp.send(requestPDU, communityTarget, null, responseListener);
			snmp.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
