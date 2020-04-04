package template.base.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class ClientRestApiUtil implements CommandLineRunner{

    public Object getRestfulService(String finalUrl, Map<String, String> requestParams, Map<String, String> pathVariable, HttpHeaders httpHeaders){
        Set rParamsSet = requestParams.entrySet();
        Iterator rParamsItr = rParamsSet.iterator();
        if (requestParams.size() > 0){
            String finalRequestParams = "?";
            while (rParamsItr.hasNext()){
                Map.Entry entry=(Map.Entry)rParamsItr.next();
                finalRequestParams = finalRequestParams + entry.getKey()+"="+entry.getValue()+"&";
            }
            finalUrl = finalUrl + finalRequestParams.substring(0, finalRequestParams.length() - 1);
        }
        RestTemplate restTemplate= new RestTemplate();
        if (httpHeaders.size() > 0){
            HttpEntity<String> entity = new HttpEntity<String>("parameters", httpHeaders);
            Object response = restTemplate.exchange(finalUrl, HttpMethod.GET, entity, Object.class, pathVariable);
            log.info(response.toString());
            return response;
        }else {
            Object response = restTemplate.getForObject(finalUrl, Object.class, pathVariable);
            log.info(response.toString());
            return response;
        }
    }

    public void postRestfulService(String finalUri, Object payloadRequest){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject( finalUri, payloadRequest, Object.class);
    }

    public void putRestfulService(String finalUrl, Object payload, Map<String, String> params){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.put ( finalUrl, payload, params);
    }

    public void deleteRestfulService(String finalUrl, Map<String, String> params){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete ( finalUrl,  params );
    }



    @Override
    public void run(String... args) throws Exception {

    }
}
