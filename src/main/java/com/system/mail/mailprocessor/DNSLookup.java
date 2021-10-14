package com.system.mail.mailprocessor;

import org.springframework.stereotype.Component;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Hashtable;

@Component
public class DNSLookup
{
    private InitialDirContext iDirC;
    private final static String record = "MX";
    public DNSLookup() throws NamingException
    {
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.dns.DnsContextFactory");
        iDirC = new InitialDirContext(env);
    }
    /**
     * 도메인으로 mx 주소 검색
     * @param host 도메인 정보
     * @return mx 주소를 반환
     */
    public String lookup (String host) throws NamingException, UnknownHostException
    {
        InetAddress inetAddress = InetAddress.getByName(host);
        // show the Internet Address as name/address
        System.out.println(inetAddress.getHostName() + " " + inetAddress.getHostAddress());

        // get the DNS records for inetAddress

        Attributes attributes = iDirC.getAttributes("dns:\\"+inetAddress.getHostName(), new String[] {record});
        Attribute mxRecord = attributes.get(record);
        for (int i=0; i<mxRecord.size();i++) {
            System.out.println(mxRecord.get(i));
        }
        return mxRecord.get(0).toString().split(" ")[1];
    }
}