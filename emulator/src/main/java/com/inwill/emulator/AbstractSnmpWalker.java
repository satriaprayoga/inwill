package com.inwill.emulator;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public abstract class AbstractSnmpWalker implements SnmpWalker<String>{
	
	
	public AbstractSnmpWalker() {
		
	}

	@Override
	public Collection<String> processWalk(String address,
			String communityString, OID rootOid, int version) {
//		if(version==SnmpConstants.version1){
//			return processWalkV1(address, communityString, rootOid);
//		}else if(version==SnmpConstants.version2c){
//			return processWalkV2(address, communityString, rootOid);
//		}
		return null;
	}
	
	protected final List<String> processWalkV1(String address,
			String communityString, OID rootOid){
		Target target=createSnmpTarget(address, communityString, SnmpUtils.DEFAULT_RTO, SnmpUtils.DEFAULT_RETRIES, SnmpConstants.version1);
		PDU requestPdu=new PDU();
		requestPdu.add(new VariableBinding(rootOid));
		requestPdu.setType(PDU.GETNEXT);
		
		try {
			TransportMapping<?> transportMapping=new DefaultUdpTransportMapping();
			Snmp snmp=new Snmp(transportMapping);
			transportMapping.listen();
			
			boolean finished=false;
			while(!finished){
				VariableBinding vb=null;
				PDU responsePdu=snmp.send(requestPdu, target).getResponse();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	protected final List<String> processWalkV2(String address,String communityString, OID rootOid){
		return null;
	}
	
	protected Target createSnmpTarget(String address, String communityString,long rto,int retries,int version){
		Address targetAddress=GenericAddress.parse(address);
		CommunityTarget target=new CommunityTarget();
		target.setAddress(targetAddress);
		target.setCommunity(new OctetString(communityString));
		target.setRetries(retries);
		target.setTimeout(rto);
		target.setVersion(version);
		return target;
	}
			
}
