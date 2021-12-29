package org.hu.rpc.zk.util;

import org.springframework.util.StringUtils;

import java.net.*;
import java.util.Enumeration;

/**
 * @Author: hu.chen
 * @Description: IP获取类
 * @DateTime: 2021/12/29 1:12 PM
 **/
public class IpUtils {

    /**
     * 获取本机内网ip地址
     **/
    public static String getLocalIpAddr() {
        if (isLinux()) {
            return getLinuxLocalIp();
        }
        InetAddress ip = null;
        if (isWindows()) {
            try {
                ip = InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            if (ip != null) {
                return ip.getHostAddress();
            }
        }
        String ipAddress = getLocalIp(true);
        if (StringUtils.isEmpty(ipAddress)) {//如果eth0网卡为空
            ipAddress = getLocalIp(false);
        }
        return ipAddress;
    }


    /**
     * 获取Linux下的IP地址
     *
     * @return IP地址
     * @throws SocketException
     */
    private static String getLinuxLocalIp() {
        String ip = "";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                String name = intf.getName();
                if (!name.contains("docker") && !name.contains("lo")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            String ipaddress = inetAddress.getHostAddress().toString();
                            if (!ipaddress.contains("::") && !ipaddress.contains("0:0:") && !ipaddress.contains("fe80")) {
                                ip = ipaddress;
                                System.out.println(ipaddress);
                            }
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            System.out.println("获取ip地址异常");
            ip = "127.0.0.1";
            ex.printStackTrace();
        }
        System.out.println("IP:" + ip);
        return ip;
    }


    /**
     * 获取本地地址
     *
     * @param justEth0 true - 只看eth0  false  所有
     * @return
     * @Date:2014-4-24
     * @Author:Guibin Zhang
     * @Description:
     */
    private static String getLocalIp(boolean justEth0) {
        InetAddress ip = null;
        Enumeration<NetworkInterface> allNetInterfaces = null;
        try {
            allNetInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        while (allNetInterfaces.hasMoreElements()) {
            NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
            if (!justEth0 || "eth0".equalsIgnoreCase(netInterface.getName())) {
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    ip = (InetAddress) addresses.nextElement();
                    if (ip != null && ip.getHostAddress() != null && ip instanceof Inet4Address && ip.getHostAddress().indexOf(".") != -1
                            && !ip.getHostAddress().startsWith("192.168.") && !"127.0.0.1".equals(ip.getHostAddress()) && !"localhost".equals(ip.getHostAddress())) {
                        return ip.getHostAddress();
                    }
                }
            }
        }
        return null;
    }

    /**
     * 判断系统是不是windows
     **/
    public static boolean isWindows() {
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().indexOf("windows") > -1) {
            return true;
        }
        return false;
    }

    /**
     * 判断系统是不是mac
     **/
    public static boolean isMac() {
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().indexOf("mac") > -1) {
            return true;
        }
        return false;
    }

    /**
     * 判断系统是不是linux
     **/
    public static boolean isLinux() {
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().indexOf("linux") > -1) {
            return true;
        }
        return false;
    }
}
