package com.example.backend.scan;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.backend.providerConfig.ProviderConfig;
import com.example.backend

    port com.example.backend.providerConfig.ProviderCo
    

    
        rt com.example.backend.provid
        
    p

    ervice
        ic class ScanService {
                

        ivate final WebClient webClient;

        blic ScanService(Provi
                .configRe
                .webClient = builder.build();
                
                 
                 String s
     

    
        String url = buildLogsUrl(cfg, 
        

            .uri(url)
            .headers(h -> apply
            uthHeaders(h, cfg))
            .retrieve()
            

            .block();
        
    

      String base = cfg.getBaseUrl();
        String path = cfg.getLogsPath();

        // clean slashes so you don’t end up w
             (base.endsWith("/")) base = base.substring(0, base.leng
        if (!path.startsWith("/")) path = "/" + path;
            
        // build query string
            turn base + path + "
        
            
        i
      String type = cfg.getAuthType();

    if ("BEARER".equalsIgnoreCase(type)) {
      h.add("Authorization", "Bearer " + cfg.getAuthSecret());
    } else if ("API_KEY".equalsIgnoreCase(type)) {
      h.add("X-API-KEY", cfg.getAuthSecret());
    } else if ("NONE".equalsIgnoreCase(type)) {
      // no headers needed
    } else {
      throw new RuntimeException("Unsupported authType: " + type);
    }
  }
}
