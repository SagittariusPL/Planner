package pl.straszewski.service;

import org.springframework.stereotype.Component;
import pl.straszewski.exceptions.TokenExpireException;
import pl.straszewski.exceptions.TokenNotFoundException;
import pl.straszewski.model.Token;
import pl.straszewski.model.UserEntity;
import pl.straszewski.repository.TokenRepository;


import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Component
public class TokenService {

    private TokenRepository tokenRepository;
    private MailService mailService;

    public TokenService(TokenRepository tokenRepository, MailService mailService) {
        this.tokenRepository = tokenRepository;
        this.mailService = mailService;
    }

    public void sendToken(UserEntity user, HttpServletRequest request ) {
        String tokenvalue = UUID.randomUUID().toString();
        Token token = new Token();
        token.setValue(tokenvalue);
        token.setUserEntity(user);
        token.setUtilTimestamp(new Date());
        tokenRepository.save(token);
        String domainUrl = request.getRequestURL().toString();
        String message ="This link will expire in 24hours";
        String url = String.format("%s \n %s/token?value=%s",message,domainUrl,tokenvalue);


        mailService.sendMail(user.getEmail(), "rejestracja", url, false);
    }

    public Token findToken(String value) {
        Date date = new Date();
        final long ONE_DAY_IN_MILLISECOND =86400000L;
        long result;

        Optional<Token> token = tokenRepository.findByValue(value);
        if (token.isPresent()) {
            Date timestamp = token.get().getUtilTimestamp();
            result = date.getTime() - timestamp.getTime();
            if (result>ONE_DAY_IN_MILLISECOND)
            {
                throw new TokenExpireException("Your time to confirm account expired");
            }
            return token.get();
        }
        throw new TokenNotFoundException("This token not exist");
    }
}
