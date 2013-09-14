/**
 * 
 */
package com.inwill.dci;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.Null;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

/**
 * @author G.S Prayoga of SWG
 * 
 */
public class WalkSnmpProcessor implements SnmpProcessor {

	private final static Logger log = Logger.getLogger(WalkSnmpProcessor.class);

	@Override
	public void process(SnmpMessage snmpMessage, Device device) {
		Address targetAddress = new UdpAddress(device.getAddress());
		OID targetOID = new OID(snmpMessage.getOid());

		PDU requestPDU = snmpMessage.toRequestPDU(PDU.GETNEXT);

		CommunityTarget communityTarget = new CommunityTarget();
		communityTarget.setAddress(targetAddress);
		communityTarget.setCommunity(new OctetString(device.getCommunity()));
		communityTarget.setVersion(device.getVersion());

		try {
			TransportMapping<?> transport = new DefaultUdpTransportMapping();
			Snmp snmp = new Snmp(transport);
			transport.listen();
			boolean finished = false;

			while (!finished) {
				VariableBinding vb = null;
				PDU responsePDU = snmp.send(requestPDU, communityTarget)
						.getResponse();
				if (responsePDU != null) {
					vb = responsePDU.get(0);
				}
				if (responsePDU == null) {
					log.info("responsePDU == null");
					finished = true;
				} else if (responsePDU.getErrorStatus() != 0) {
					finished = true;
				} else if (vb.getOid() == null) {
					finished = true;
				} else if (vb.getOid().size() < targetOID.size()) {
					finished = true;
				} else if (targetOID.leftMostCompare(targetOID.size(),
						vb.getOid()) != 0) {

					log.info("targetOID.leftMostCompare() != 0)");
					finished = true;
				} else if (Null.isExceptionSyntax(vb.getVariable().getSyntax())) {
					log.info("Null.isExceptionSyntax(vb.getVariable().getSyntax())");
					finished = true;
				} else if (vb.getOid().compareTo(targetOID) <= 0) {
					log.info("Variable received is not "
							+ "lexicographic successor of requested " + "one:");
					log.info(vb.toString() + " <= " + targetOID);
					finished = true;

				} else {
					// Dump response.
					// System.out.println(vb.toString());
					log.info(vb.toString());
					// Set up the variable binding for the next entry.
					requestPDU.setRequestID(new Integer32(0));
					requestPDU.set(0, vb);
				}
			}
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

}
