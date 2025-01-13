package com.tenpo.interceptor;

import com.tenpo.errors.RateLimitException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    // Mapa para almacenar las solicitudes por tipo de petición y cliente (IP)
    private final Map<String, Long> requestCountMap = new ConcurrentHashMap<>();
    private final Map<String, Long> lastRequestTimestampMap = new ConcurrentHashMap<>();
    private static final int MAX_REQUESTS_PER_MINUTE = 3; // 3 solicitudes por tipo por minuto
    private static final long TIME_WINDOW = 60 * 1000; // Tiempo de 1 minuto en milisegundos

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Excluir el endpoint GET /allTransactions, swagger del rate limiting
    	 if ((request.getMethod().equals("GET") && 
    	         (request.getRequestURI().equals("/api/transaction/allTransactions") || 
    	          request.getRequestURI().startsWith("/swagger-ui/")))) {
    	        return true; 
    	    }

        // Obtener la dirección IP del cliente y la ruta de la solicitud
        String clientIp = request.getRemoteAddr();
        String requestURI = request.getRequestURI();
        long currentTime = System.currentTimeMillis();
        
        // Crear una clave única para cada combinación de cliente y tipo de solicitud
        String key = clientIp + ":" + requestURI;

        if (lastRequestTimestampMap.containsKey(key)) {
            long lastRequestTime = lastRequestTimestampMap.get(key);
            long timeElapsed = currentTime - lastRequestTime;

            if (timeElapsed < TIME_WINDOW) {
                long requestCount = requestCountMap.getOrDefault(key, 0L);
                if (requestCount >= MAX_REQUESTS_PER_MINUTE) {
                    // Si el límite de solicitudes ha sido alcanzado, lanzar excepción
                    throw new RateLimitException("Rate limit exceeded. Try again later.");
                }
                // Incrementar el contador de solicitudes para esta combinación de cliente y ruta
                requestCountMap.put(key, requestCount + 1);
            } else {
                // Si ha pasado el tiempo de ventana, reiniciar el contador
                requestCountMap.put(key, 1L);
                lastRequestTimestampMap.put(key, currentTime);
            }
        } else {
            // Si es la primera solicitud, inicializar el contador
            requestCountMap.put(key, 1L);
            lastRequestTimestampMap.put(key, currentTime);
        }

        return true;
    }
}
