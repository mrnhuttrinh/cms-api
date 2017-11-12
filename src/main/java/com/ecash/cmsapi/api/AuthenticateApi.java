package com.ecash.cmsapi.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ecash.ecashcore.model.User;
import com.ecash.ecashcore.service.UserService;

@RestController
public class AuthenticateApi extends BaseApi {
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public ResponseEntity<?> authenticateUser(@RequestBody Map<String, Object> body) {
		String email = body.get("email").toString();
		String password = body.get("password").toString();
		User user = userService.addNewUser(email);
        return ResponseEntity.ok(user);
    }
}
