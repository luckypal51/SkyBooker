package com.app.api_gateway.security;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.web.client.RestTemplate;

import com.app.api_gateway.util.JwtUtil;

import io.jsonwebtoken.Claims;

public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config>{
	
	@Autowired
	private RouteValidator routevalidator;
	
	@Autowired
	private RestTemplate restTemplate;
    public static class Config{}
    
    public AuthenticationFilter(){
    	super(Config.class);
    }
    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {

            if (routevalidator.isSecured.test(exchange.getRequest())) {

                if (!exchange.getRequest().getHeaders()
                        .containsKey(jakarta.ws.rs.core.HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("Header missing");
                }

                String authHeader = exchange.getRequest()
                        .getHeaders()
                        .get(jakarta.ws.rs.core.HttpHeaders.AUTHORIZATION)
                        .get(0);

                if (authHeader != null && authHeader.startsWith("Bearer ")) {

                    String token = authHeader.substring(7);

                    try {
                        // ✅ Get claims
                        Claims claims = JwtUtil.getClaims(token);

                        String role = claims.get("role", String.class);

                        String path = exchange.getRequest().getURI().getPath();

                        // 🔥 Role check
                        for (Map.Entry<String, List<String>> entry :routevalidator.roleAccessMap.entrySet()) {

                            if (path.startsWith(entry.getKey())) {

                                if (!entry.getValue().contains(role)) {
                                    throw new RuntimeException("Access Denied");
                                }
                            }
                        }

                    } catch (Exception e) {
                        throw new RuntimeException("Invalid Token");
                    }
                }
            }

            return chain.filter(exchange);
        });
    }
}
