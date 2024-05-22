package id.ac.ui.cs.advprog.beauthuserstaff.controller.staff_dashboard;
import id.ac.ui.cs.advprog.beauthuserstaff.service.StaffDashboardService.ConfirmTopUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}


@RestController
@RequestMapping(path="/staff", produces="application/json")
@CrossOrigin(origins="*")
public class ConfirmTopUpController {

    @Autowired
    ConfirmTopUpService confirmTopUpService;

    @Autowired
    RestTemplate restTemplate;

    @PostMapping("/confirm-topup")
    public ResponseEntity<String> confirmTopUp(@RequestBody String jsonContent) throws JSONException {

        JSONObject jsonObject = new JSONObject(jsonContent);
        String id = jsonObject.getString("id");
        //String confirmUrl = "http://localhost:8081/topup/" + id + "/confirm";
        String confirmUrl = "34.142.213.219/topup/" + id + "/confirm";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                confirmUrl, HttpMethod.PUT, requestEntity, String.class);
    }

    @GetMapping("/view-waiting-top-ups")
    public ResponseEntity<String> getAllWaitingTopUps(){
        //String url = "http://localhost:8081/topup/waiting";
        String url = "34.142.213.219/topup/waiting";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                url, HttpMethod.GET, requestEntity, String.class);
    }

}