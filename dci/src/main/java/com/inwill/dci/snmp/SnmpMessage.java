package com.inwill.dci.snmp;

import java.io.Serializable;

import org.snmp4j.PDU;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;

/**
 * 
 * @author G.S Prayoga of SWG
 *
 */
public class SnmpMessage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5319225113801033041L;
	
	private String oid;
	private String mib;
	
	public SnmpMessage() {
	}
	
	public SnmpMessage(String oid,String mib){
		this.oid=oid;
		this.mib=mib;
	}
	
	public String getMib() {
		return mib;
	}
	
	public void setMib(String mib) {
		this.mib = mib;
	}
	
	public String getOid() {
		return oid;
	}
	
	public void setOid(String oid) {
		this.oid = oid;
	}
	
	@Override
	public String toString() {
		return oid+"="+mib;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mib == null) ? 0 : mib.hashCode());
		result = prime * result + ((oid == null) ? 0 : oid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SnmpMessage other = (SnmpMessage) obj;
		if (mib == null) {
			if (other.mib != null)
				return false;
		} else if (!mib.equals(other.mib))
			return false;
		if (oid == null) {
			if (other.oid != null)
				return false;
		} else if (!oid.equals(other.oid))
			return false;
		return true;
	}
	
	public PDU toRequestPDU(int pduType){
		PDU pdu=new PDU();
		if(oid!=null && !(oid.isEmpty())){
			VariableBinding vb=new VariableBinding(new OID(oid));
			if(mib!=null && !(mib.isEmpty())){
				vb.setVariable(new OctetString(mib));
			}
			pdu.add(vb);
			pdu.setType(pduType);
			return pdu;
		}else{
			return null;
		}
		
	}
	
	

}
