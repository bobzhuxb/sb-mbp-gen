package com.bob.sm.util;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class NetworkUtil {

    public static void main(String[] args) {
        NetworkUtil.getComputerName();
    }

    public static void getLocalIpOld() {
        try {
            InetAddress address = InetAddress.getLocalHost();//获取的是本地的IP地址 //PC-20140317PXKX/192.168.0.121
            String hostAddress = address.getHostAddress();//192.168.0.121
            System.out.println(hostAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取本机所有IP
     */
    private static String[] getAllLocalHostIP() {
        List<String> res = new ArrayList<String>();
        Enumeration netInterfaces;
        try {
            netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) netInterfaces
                    .nextElement();
                System.out.println("---Name---:" + ni.getName());
                Enumeration nii = ni.getInetAddresses();
                while (nii.hasMoreElements()) {
                    ip = (InetAddress) nii.nextElement();
                    if (ip.getHostAddress().indexOf(":") == -1) {
                        res.add(ip.getHostAddress());
                        System.out.println("本机的ip=" + ip.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return (String[]) res.toArray(new String[0]);
    }

    public static String getLocalIPNew() {
        String ip = "";
        try {
            Enumeration<?> e1 = (Enumeration<?>) NetworkInterface.getNetworkInterfaces();
            while (e1.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) e1.nextElement();
                System.out.println ("getLocalIP--nic.getDisplayName ():" + ni.getDisplayName ());
                System.out.println ("getLocalIP--nic.getName ():" + ni.getName ());
                if (!ni.getName().equals("eth0")) {
                    continue;
                } else {
                    Enumeration<?> e2 = ni.getInetAddresses();
                    while (e2.hasMoreElements()) {
                        InetAddress ia = (InetAddress) e2.nextElement();
                        if (ia instanceof Inet6Address)
                            continue;
                        ip = ia.getHostAddress();
                    }
                    break;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return ip;
    }

    /**
     * 获取服务器IP地址
     * @return
     */
    public static String getLocalIPNew2(){
        String SERVER_IP = null;
        try {
            Enumeration netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
                ip = (InetAddress) ni.getInetAddresses().nextElement();
                SERVER_IP = ip.getHostAddress();
                if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
                    && ip.getHostAddress().indexOf(":") == -1) {
                    SERVER_IP = ip.getHostAddress();
                    break;
                } else {
                    ip = null;
                }
            }
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return SERVER_IP;
    }

    public static String getWinLocalIP ()
    {
        String ip = "";
        try
        {
            Enumeration <?> e1 = (Enumeration <?>) NetworkInterface.getNetworkInterfaces ();
            while (e1.hasMoreElements ())
            {
                NetworkInterface ni = (NetworkInterface) e1.nextElement ();
                System.out.println ("getWinLocalIP--nic.getDisplayName ():" + ni.getDisplayName ());
                System.out.println ("getWinLocalIP--nic.getName ():" + ni.getName ());
                Enumeration <?> e2 = ni.getInetAddresses ();
                while (e2.hasMoreElements ())
                {
                    InetAddress ia = (InetAddress) e2.nextElement ();
                    ip = ia.getHostAddress ();
                }
            }
        }
        catch (SocketException e)
        {
            e.printStackTrace ();
            System.exit (-1);
        }
        return ip;
    }

    /**
     * 获取本机所有物理地址
     *
     * @return
     */
    public static String getMacAddress() {
        String mac = "";
        String line = "";

        String os = System.getProperty("os.name");

        if (os != null && os.startsWith("Windows")) {
            try {
                String command = "cmd.exe /c ipconfig /all";
                Process p = Runtime.getRuntime().exec(command);

                BufferedReader br = new BufferedReader(new InputStreamReader(p
                    .getInputStream()));

                while ((line = br.readLine()) != null) {
                    if (line.indexOf("Physical Address") > 0) {
                        int index = line.indexOf(":") + 2;

                        mac = line.substring(index);

                        break;
                    }
                }

                br.close();

            } catch (IOException e) {
            }
        }

        return mac;
    }

    public String getMacAddress(String host) {
        String mac = "";
        StringBuffer sb = new StringBuffer();

        try {
            NetworkInterface ni = NetworkInterface.getByInetAddress(InetAddress
                .getByName(host));

            // byte[] macs = ni.getHardwareAddress();

            // for (int i = 0; i < macs.length; i++) {
            // mac = Integer.toHexString(macs[i] & 0xFF);
            //
            // if (mac.length() == 1) {
            // mac = '0' + mac;
            // }
            //
            // sb.append(mac + "-");
            // }

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        mac = sb.toString();
        mac = mac.substring(0, mac.length() - 1);

        return mac;
    }

    public static String getComputerName() {
        try {
            // 获取计算机名
            String name = InetAddress.getLocalHost().getHostName();
            return name;
        } catch (Exception e) {
            System.out.println("异常：" + e);
        }
        return "";
    }

    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr()的原因是有可能用户使用了代理软件方式避免真实IP地址,
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值
     *
     * @return ip
     */
    public static String getRemoteIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
//        System.out.println("x-forwarded-for ip: " + ip);
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if( ip.indexOf(",")!=-1 ){
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
//            System.out.println("Proxy-Client-IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
//            System.out.println("WL-Proxy-Client-IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
//            System.out.println("HTTP_CLIENT_IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
//            System.out.println("HTTP_X_FORWARDED_FOR ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
//            System.out.println("X-Real-IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
//            System.out.println("getRemoteAddr ip: " + ip);
        }
//        System.out.println("获取客户端ip: " + ip);
        return ip;
    }

}
