package com.group.sampleproject.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.group.sampleproject.entity.Token;
import com.group.sampleproject.repository.TokenRepository;
import com.group.sampleproject.repository.UserRepository;

@SpringBootTest
public class TokenServiceTests {

    @SpyBean
    TokenService tokenService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private TokenRepository tokenRepository;

    @Test
    void generateTokenWithFutureDate() throws  ParseException {
        
        // 未来の日付でテスト
        String inpDateStr = "2099/12/31 00:00:00";
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        Date testDate = sdformat.parse(inpDateStr);

        String token = tokenService.generateToken("testClaim", testDate);

        DecodedJWT decodedJwt = JWT.decode(token);
        Date expirationDate = decodedJwt.getExpiresAt();
        String claim = decodedJwt.getClaim("username").asString();

        assertThat(expirationDate).isEqualTo(testDate);
        assertThat(claim).isEqualTo("testClaim");
            
    }

    @Test
    void generateTokenWithPastDate() throws  ParseException {
        
        // 過去の日付でテスト
        String inpDateStr = "2000/12/31 00:00:00";
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        Date testDate = sdformat.parse(inpDateStr);

        String token = tokenService.generateToken("testClaim", testDate);

        assertThrows(JWTVerificationException.class, () -> JWT.require(Algorithm.HMAC256("secret")).build().verify(token));
            
    }

    @Test
    void createNewTokens() throws ParseException{

        // 未来の日付でテスト
        String tokenDateStr = "2099/12/31 00:00:00";
        String refreshDateStr = "2099/12/31 01:00:00";
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        Date testDateForToken = sdformat.parse(tokenDateStr);
        Date testDateForRefreshToken = sdformat.parse(refreshDateStr);
        
        // tokenService.getDatePlusMinメソッドをモック化しておく。テスト対象と同じクラスなので@SpyBeanを使う
        // doReturn(返却するオブジェクト).when(spyしたオブジェクト).method()
        doReturn(testDateForToken).when(tokenService).getDatePlusMin(1);
        doReturn(testDateForRefreshToken).when(tokenService).getDatePlusMin(60);

        // tokenRepository.createToken は戻り値が無いのでモック化不要
        // when(tokenRepository.createToken(new Token("",""))).thenReturn(null);

        // テスト対象のメソッド実行
        Map<String,String> result = tokenService.createNewTokens("testUsername");

        // 各トークンを取得
        String token = result.get("token");
        String refreshToken = result.get("refreshToken");
        
        DecodedJWT decodedToken = JWT.decode(token);
        DecodedJWT decodedRefreshToken = JWT.decode(refreshToken);

        // tokenの検証
        Date expirationDate = decodedToken.getExpiresAt();
        String claim = decodedToken.getClaim("username").asString();

        assertThat(expirationDate).isEqualTo(testDateForToken);
        assertThat(claim).isEqualTo("testUsername");

        // refreshTokenの検証
        expirationDate = decodedRefreshToken.getExpiresAt();
        claim = decodedRefreshToken.getClaim("username").asString();

        assertThat(expirationDate).isEqualTo(testDateForRefreshToken);
        assertThat(claim).isEqualTo("testUsername");
    }
    
}
