package ca.bmskarate.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionTokenManager {
    private static final Map<String, SessionCache> sessionCache = new ConcurrentHashMap<>();

    private static long sessionTimeOut=4*60*60*1000;

    public static Authentication getToken(String token, String addr){
        Date dte = new Date();
        if(token==null)
            return null;

        SessionCache session = sessionCache.get(token);

        if(session!=null && session.getAuth().equals(addr)) {
            if (session.getTs() > dte.getTime() + sessionTimeOut) {
                removeToken(token);
                return null;
            }
            session.setTs(dte.getTime());
            return session.getAuth();
        }

        return null;
    }

    public static void removeToken(String token){
        sessionCache.remove(token);
    }

    public static String setToken(Authentication auth, String addr){
        String token = generateSessionToken();
        Date dte = new Date();

        SessionCache session = new SessionCache(auth, dte.getTime(), addr);
        sessionCache.put(token, session);

        //clean up old sessions to free memory
        for(String tempToken:sessionCache.keySet()){
            if(sessionCache.get(tempToken).getTs()>dte.getTime()+sessionTimeOut)
                removeToken(token);
        }

        return token;
    }

    private static String generateSessionToken(){
        java.security.SecureRandom rng = new java.security.SecureRandom();

        String token = Long.toHexString(rng.nextLong());

        return token;
    }

    @Getter
    @Setter
    private static class SessionCache{
        Authentication auth;
        long ts;
        String addr;

        public SessionCache(Authentication auth, long ts, String addr) {
            this.auth = auth;
            this.ts = ts;
            this.addr = addr;
        }
    }
}
