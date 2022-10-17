package com.learn.springsecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class controller {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailService userDetailService;
    @Autowired
    private jwthelper jwthelper;
    @GetMapping(path = "/hello")
    public String hello(){
        return "hellow";
    }
    @PostMapping(path = "/auth")
    public ResponseEntity<?> auth(@RequestBody authreq authreq)throws Exception{
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authreq.getUsername(), authreq.getPassword()));
        }catch( BadCredentialsException e){
            throw new Exception("Bad Credetials");
        }
        final UserDetails userDetails = userDetailService.loadUserByUsername(authreq.getUsername());
        final String jwt =this.jwthelper.generateToken(userDetails);
        return ResponseEntity.ok(new authRes(jwt));
    }
}
