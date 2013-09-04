package com.inwill.dci.dumb;

import java.io.IOException;

import org.snmp4j.CommandResponder;
import org.snmp4j.CommandResponderEvent;
import org.snmp4j.CommunityTarget;
import org.snmp4j.MessageDispatcher;
import org.snmp4j.MessageDispatcherImpl;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.mp.MPv3;
import org.snmp4j.security.Priv3DES;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TcpAddress;
import org.snmp4j.smi.TransportIpAddress;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.transport.AbstractTransportMapping;
import org.snmp4j.transport.DefaultTcpTransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.MultiThreadedMessageDispatcher;
import org.snmp4j.util.ThreadPool;

public class TrapReceiver implements CommandResponder {
	
	public static void main(String[] args) {
		TrapReceiver receiver=new TrapReceiver();
		try {
			receiver.listen(new UdpAddress("localhost/162"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public synchronized void listen(TransportIpAddress address)throws IOException{
		AbstractTransportMapping<?> transport=null;
		if(address instanceof TcpAddress){
			transport=new DefaultTcpTransportMapping((TcpAddress)address);
		}else{
			transport=new DefaultUdpTransportMapping((UdpAddress)address);
		}
		
		ThreadPool threadPool=ThreadPool.create("DispatcherPool", 10);
		MessageDispatcher mDispatcher=new MultiThreadedMessageDispatcher(threadPool, new MessageDispatcherImpl());
		mDispatcher.addMessageProcessingModel(new MPv1());
		mDispatcher.addMessageProcessingModel(new MPv2c());
		mDispatcher.addMessageProcessingModel(new MPv3());
		
		SecurityProtocols.getInstance().addDefaultProtocols();
		SecurityProtocols.getInstance().addPrivacyProtocol(new Priv3DES());
		
		CommunityTarget target=new CommunityTarget();
		target.setCommunity(new OctetString("public"));
		
		Snmp snmp=new Snmp(mDispatcher, transport);
		snmp.addCommandResponder(this);
		
		transport.listen();
		System.out.println("listening on : "+address);
		
		try {
			wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public  synchronized void processPdu(CommandResponderEvent event) {
		System.out.println("receiving PDU: ");
		PDU pdu=event.getPDU();
		if(pdu!=null){
			System.out.println("Trap types: "+pdu.getType());
			System.out.println("Variables: "+pdu.getVariableBindings());
		}
	}
}
