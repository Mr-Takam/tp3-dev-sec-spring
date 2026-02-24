package fr.efrei.tp3_dev_sec_spring.api.web;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public Object handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Integer statusCode = (status != null) ? Integer.valueOf(status.toString()) : 500;

        // Détection du type de client (Navigateur vs API/Postman)
        String acceptHeader = request.getHeader("Accept");

        if (acceptHeader != null && acceptHeader.contains(MediaType.APPLICATION_JSON_VALUE)) {
            // RÉPONSE POUR POSTMAN / API (JSON)
            return buildJsonResponse(statusCode);
        }

        // RÉPONSE POUR NAVIGATEUR (HTML)
        if (statusCode == 403) {
            return "error/403"; // Renvoie vers templates/error/403.html
        }
        return "error"; 
    }

    /**
     * Construit une réponse JSON propre pour les outils de terminal ou Postman.
     */
    @ResponseBody
    private ResponseEntity<Map<String, Object>> buildJsonResponse(Integer statusCode) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("status", statusCode);
        
        if (statusCode == 403) {
            errorDetails.put("error", "Forbidden");
            errorDetails.put("message", "Accès refusé : vous n'avez pas les privilèges suffisants ou le plafond est dépassé.");
        } else {
            errorDetails.put("error", "Internal Server Error");
            errorDetails.put("message", "Une erreur inattendue est survenue.");
        }

        return new ResponseEntity<>(errorDetails, HttpStatus.valueOf(statusCode));
    }
}