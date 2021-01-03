package DailyCodeBuffer.JWT.controller;

import DailyCodeBuffer.JWT.model.JwtRequest;
import DailyCodeBuffer.JWT.model.JwtResponse;
import DailyCodeBuffer.JWT.service.UserService;
import DailyCodeBuffer.JWT.utility.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {


    private JwtUtility jwtUtility;
    private AuthenticationManager authenticateManager;
    private UserService userService;

    @Autowired
    public HomeController(JwtUtility jwtUtility, AuthenticationManager authenticateManager, UserService userService) {
        this.jwtUtility = jwtUtility;
        this.authenticateManager = authenticateManager;
        this.userService = userService;
    }




    @GetMapping("/")
    public String  home()
    {
        return "Welcome to Home";
    }

    @PostMapping("/authenticate")
    public JwtResponse authenticate(@RequestBody  JwtRequest jwtRequest) throws Exception
    {
        try {
            authenticateManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getUsername(),
                            jwtRequest.getPassword()
                    )

            );
        }
        catch(BadCredentialsException e)
        {
            throw new Exception("Invalid Credentials",e);
        }

        final UserDetails userDetails =
                userService.loadUserByUsername(jwtRequest.getUsername());

        final String token = jwtUtility.generateToken(userDetails);

        return new JwtResponse(token);
    }

}
