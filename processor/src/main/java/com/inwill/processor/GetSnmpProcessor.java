/**
 * 
 */
package com.inwill.processor;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import com.inwill.dci.snmp.Device;
import com.inwill.dci.snmp.SnmpMessage;

/**
 * @author G.S Prayoga of SWG
 *
 */
public class GetSnmpProcessor extends SnmpProcessor{

	private static final Logger log=Logger.getLogger(GetSnmpProcessor.class);
	@Override
	public void process(SnmpMessage snmpMessage, Device device) {
		configTarget(snmpMessage, device, PDU.GET);
		try {
			TransportMapping<?> transport=new DefaultUdpTransportMapping();
			Snmp snmp=new Snmp(transport);
			communityTarget.setRetries(RETRIES);
			communityTarget.setTimeout(RTO);
			transport.listen();
			ResponseEvent responseEvent=snmp.send(requestPDU, communityTarget);
			if(responseEvent!=null){
				PDU responePDU=responseEvent.getResponse();
				if(responePDU.getErrorStatusText().equalsIgnoreCase("Success")){
					String strResponse=responePDU.getVariableBindings().firstElement().toString();
					log.info(strResponse);
				}
			}else{
				log.info("RTO");
			}
			snmp.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
