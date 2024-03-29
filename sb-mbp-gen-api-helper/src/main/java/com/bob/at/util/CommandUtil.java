package com.bob.at.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

public class CommandUtil {

    public static String exeCmd(String[] commandArr) throws Exception {
        BufferedReader br = null;
        try {
            Process p = Runtime.getRuntime().exec(commandArr);
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            return sb.toString();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void exeCmd(String commandStr) throws Exception {
        Process process = null;
        String osCmd = getOsCmd();
        try {
            if (osCmd.startsWith("cmd ")) {
                process = Runtime.getRuntime().exec(osCmd + commandStr);
            } else {
                String[] commandArr = { "/bin/sh", "-c", commandStr};
                process = Runtime.getRuntime().exec(commandArr);
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(new String(line.getBytes(),"UTF-8"));
            }
        } catch (Exception e) {
            throw e;
        } finally {
            process.destroy();
        }
    }

    public static String getOsCmd(){
        Properties props=System.getProperties(); //获得系统属性集
        String osName = props.getProperty("os.name"); //操作系统名称
        if (osName.toLowerCase().contains("linux")) {
            return "/bin/sh -c ";
        } else if(osName.toLowerCase().contains("windows")) {
            return "cmd /c ";
        }else{
            throw new RuntimeException("服务器不是linux|windows操作系统");
        }
    }

}
