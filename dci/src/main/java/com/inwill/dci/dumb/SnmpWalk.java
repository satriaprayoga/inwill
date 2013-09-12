package com.inwill.dci.dumb;

import java.io.IOException;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.Null;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class SnmpWalk {
	
	public static void main(String[] args)
	  {
	    Address targetAddress = new UdpAddress("192.168.7.103/161");
	    OID targetOID         = new OID("1.3.6.1.2.1.1");
//	    OID targetOID         = new OID("1.3.6.1.4.1.517");


	    PDU requestPDU = new PDU();
	    requestPDU.add(new VariableBinding(targetOID));
	    //requestPDU.setType(PDU.GETNEXT);
	    requestPDU.setType(PDU.GETNEXT);
	    

	    CommunityTarget target = new CommunityTarget();
	    target.setCommunity(new OctetString("public"));
	    target.setAddress(targetAddress);
	    target.setVersion(SnmpConstants.version2c);

	    try
	    {
	      TransportMapping<?> transport = new DefaultUdpTransportMapping();
	      Snmp snmp = new Snmp(transport);
	      transport.listen();

	      boolean finished = false;

	      while (!finished)
	      {
	        VariableBinding vb = null;

	        PDU responsePDU = snmp.send(requestPDU, target).getResponse();
	        if (responsePDU != null)
	        {
	          vb = responsePDU.get(0);
	        }

	        if (responsePDU == null)
	        {
	          System.out.println("responsePDU == null");
	          finished = true;
	        }
	        else if (responsePDU.getErrorStatus() != 0)
	        {
	          System.out.println("responsePDU.getErrorStatus() != 0");
	          System.out.println(responsePDU.getErrorStatusText());
	          finished = true;
	        }
	        else if (vb.getOid() == null)
	        {
	          System.out.println("vb.getOid() == null");
	          finished = true;
	        }
	        else if (vb.getOid().size() < targetOID.size())
	        {
	          System.out.println("vb.getOid().size() < targetOID.size()");
	          finished = true;
	        }
	        else if (targetOID.leftMostCompare(targetOID.size(),
	                                           vb.getOid()) != 0)
	        {
	          System.out.println("targetOID.leftMostCompare() != 0)");
	          finished = true;
	        }
	        else if (Null.isExceptionSyntax(vb.getVariable().getSyntax()))
	        {
	          System.out.println(
	          "Null.isExceptionSyntax(vb.getVariable().getSyntax())");
	          finished = true;
	        }
	        else if (vb.getOid().compareTo(targetOID) <= 0)
	        {
	          System.out.println("Variable received is not "+
	                       "lexicographic successor of requested "+
	                       "one:");
	          System.out.println(vb.toString() + " <= "+targetOID);
	          finished = true;

	        }
	        else
	        {
	          // Dump response.
	          System.out.println(vb.toString());

	          // Set up the variable binding for the next entry.
	          requestPDU.setRequestID(new Integer32(0));
	          requestPDU.set(0, vb);
	        }
	      }

	      snmp.close();

	    }
	    catch (IOException e)
	    {
	      System.out.println("IOException: "+e);
	    }

	  }

}
