package id.ac.ui.cs.advprog.beauthuserstaff.controller.staff_dashboard;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.config.JwtAuthFilter;
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

    private static final String AUTHENTICATED_KEY = "authenticated";
    private static final String NOT_AUTHENTICATED_KEY = "not authenticated";
    private static final String FORBIDDEN_MESSAGE = "You are not authorized to make this request";


    @Autowired
    ConfirmTopUpService confirmTopUpService;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    JwtAuthFilter jwtAuthFilter;

    @PostMapping("/confirm-topup")
    public ResponseEntity<String> confirmTopUp(@RequestHeader(value = "Authorization") String token,
                                               @RequestBody String jsonContent) throws JSONException {
        if (authenticate(token).equals(NOT_AUTHENTICATED_KEY)){
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN) // Set the status to 403 Forbidden
                    .body(FORBIDDEN_MESSAGE);
        }
        JSONObject jsonObject = new JSONObject(jsonContent);
        String id = jsonObject.getString("id");
        String confirmUrl = "http" + "://34.142.213.219/topup/" + id + "/confirm";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);


        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(
                confirmUrl, HttpMethod.PUT, requestEntity, String.class);
    }

    @GetMapping("/view-waiting-top-ups")
    public ResponseEntity<String> getAllWaitingTopUps(@RequestHeader(value = "Authorization") String token){
        if (authenticate(token).equals(NOT_AUTHENTICATED_KEY)){
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN) // Set the status to 403 Forbidden
                    .body(FORBIDDEN_MESSAGE);
        }

        String url = "http://" + "34.142.213.219/topup/waiting";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(
                url, HttpMethod.GET, requestEntity, String.class);
    }


    private String authenticate(String token){
        String role = null;

        try {
            role = jwtAuthFilter.filterToken(token);
        }catch (Exception ignored){}


        if (role == null || !(role.equals("STAFF"))){
            return NOT_AUTHENTICATED_KEY;
        }
        return AUTHENTICATED_KEY;
    }

}