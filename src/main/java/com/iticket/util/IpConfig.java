package com.iticket.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

public abstract class IpConfig {
	private static final String serverIp;
	private static final String hostname;
	private static final String[] searchList = new String[]{"139.196.", "192.168.1.","114.215.107.90"};
	private static final String[] innerLocalIp = new String[]{"139.196.", "114.215.107.90"};
	private static final List<String> preEnvIp = Arrays.asList("114.215.107.90","10.165.20.179");
	static{
		String[] host = IpConfig.getServerAddr();
		serverIp = host[0];
		//hostname = host[1];
		String h = "";
		try {
			h = java.net.InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		if(StringUtils.isBlank(h)){
			h = host[1];
		}
		hostname = h;
	}

	/**
	 * @param ip
	 * @return
	 */
	public static boolean isInnerIp(String ip){
		for(String inner:innerLocalIp){
			if(StringUtils.startsWith(ip, inner)){
				return true;
			}
		}
		return false;
	}
	/**
	 * @param ip
	 * @return
	 */
	public static boolean isPreEnvIp(String ip){
		return preEnvIp.contains(ip);
	}

	public static final boolean isLocalIp(String ip) {
		return ip.contains("192.168.") || ip.equals("127.0.0.1"); 
	}

	public static void filterIp(String ip){
		if(isInnerIp(ip) || isLocalIp(ip)){
			return;
		}
		throw new IllegalArgumentException("invalid ip");
	}
	public static boolean isServerIp(String ip){
		for(String search: searchList){
			if(StringUtils.startsWith(ip, search)) return true;
		}
		return false;
	}
	public static String[] getServerAddr(){
		Map<String, String> hostMap = getServerAddrMap();
		for(String search: searchList){
			for(String addr: hostMap.keySet()){
				if(addr.startsWith(search)){
					return new String[]{addr, hostMap.get(addr)};
				}
			}
		}
		return new String[]{"127.0.0.1", "localhost"};
	}
	private static Map<String, String> getServerAddrMap(){
		Map<String, String> hostMap = new TreeMap<String, String>();
		try{
			Enumeration<NetworkInterface> niList = NetworkInterface.getNetworkInterfaces();
			while(niList.hasMoreElements()){
				NetworkInterface ni = niList.nextElement();
				Enumeration<InetAddress> addrList = ni.getInetAddresses();
				while(addrList.hasMoreElements()){
					InetAddress addr = addrList.nextElement();
					if(addr instanceof Inet4Address) {
						hostMap.put(addr.getHostAddress(), addr.getHostName());
					}
				}
			}
		}catch(Exception e){
		}
		return hostMap;
	}
	public static String getServerip() {
		return serverIp;
	}
	public static String getHostname() {
		return hostname;
	}
}
